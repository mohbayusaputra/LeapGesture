package com.example.leapgesture.UI.View.UjiDL

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.renderscript.ScriptGroup.Binding
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.example.leapgesture.databinding.FragmentDlBinding
import com.example.leapgesture.ml.LeapgestureModel244
import com.google.firebase.database.FirebaseDatabase
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.ByteArrayOutputStream
import java.io.File

class DlFragment : Fragment() {

    private var _binding: FragmentDlBinding? = null
    private val binding get() = _binding!!

    private lateinit var bitmap: Bitmap
    private var imageUri: Uri? = null
    private var lastTakenImageUri: Uri? = null

    private lateinit var currentPhotoFile: File

    private lateinit var getImageFromGalleryLauncher: ActivityResultLauncher<String>
    private lateinit var takePictureLauncher: ActivityResultLauncher<Uri>
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    private lateinit var labels: List<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDlBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set toolbar title
        binding.toolbar.title = "Deteksi"

        loadLabels()
        setupPermissionLauncher()
        setupGalleryLauncher()
        setupCameraLauncher()

        binding.btnUploadImage.setOnClickListener {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
                    requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                } else {
                    getImageFromGalleryLauncher.launch("image/*")
                }
            } else {
                getImageFromGalleryLauncher.launch("image/*")
            }
        }

        binding.btnOpenCamera.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            } else {
                openCamera()
            }
        }

        binding.btnPredict.setOnClickListener {
            if (!::bitmap.isInitialized) {
                Toast.makeText(requireContext(), "Pilih gambar dulu dari kamera atau galeri.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            predictGesture()
        }

        binding.btnSaveResult.setOnClickListener {
            savePredictionResult()
        }
    }

    private fun loadLabels() {
        try {
            val labelText = requireContext().assets.open("label.txt").bufferedReader().use { it.readText() }
            labels = labelText.split("\n").map { it.trim() }.filter { it.isNotEmpty() }
        } catch (e: Exception) {
            labels = emptyList()
            Toast.makeText(requireContext(), "Gagal memuat label.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupPermissionLauncher() {
        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            val message = if (granted) "Izin diberikan" else "Izin ditolak"
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupGalleryLauncher() {
        getImageFromGalleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                try {
                    // Ubah stream menjadi bitmap, tapi resize agar tidak terlalu besar
                    val inputStream = requireContext().contentResolver.openInputStream(uri)
                    val options = BitmapFactory.Options().apply {
                        // Mengurangi ukuran gambar agar tidak melebihi memori
                        inJustDecodeBounds = true
                    }
                    BitmapFactory.decodeStream(inputStream, null, options)
                    inputStream?.close()

                    val imageWidth = options.outWidth
                    val imageHeight = options.outHeight

                    val maxDimension = 1000  // ukuran maksimum untuk tinggi/lebar
                    var scaleFactor = 1

                    while (imageWidth / scaleFactor > maxDimension || imageHeight / scaleFactor > maxDimension) {
                        scaleFactor *= 2
                    }

                    val inputStream2 = requireContext().contentResolver.openInputStream(uri)
                    val options2 = BitmapFactory.Options().apply {
                        inSampleSize = scaleFactor
                    }
                    bitmap = BitmapFactory.decodeStream(inputStream2, null, options2)!!
                    inputStream2?.close()
                    binding.imageView.setImageBitmap(bitmap)
                    resetResult()

                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(requireContext(), "Gagal membuka gambar", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupCameraLauncher() {
        takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                lastTakenImageUri?.let { uri ->
                    try {
                        requireContext().contentResolver.openInputStream(uri).use { inputStream ->
                            val bitmapFromStream = BitmapFactory.decodeStream(inputStream)
                                ?: throw Exception("Bitmap null")
                            bitmap = bitmapFromStream
                            binding.imageView.setImageBitmap(bitmap)
                            resetResult()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(requireContext(), "Gagal membuka foto", Toast.LENGTH_SHORT).show()
                    }
                } ?: Toast.makeText(requireContext(), "imageUri tidak tersedia", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openCamera() {
        val photoFile = createImageFile()
        currentPhotoFile = photoFile
        val uriForCamera = FileProvider.getUriForFile(requireContext(), "${requireContext().packageName}.fileprovider", photoFile)
        imageUri = uriForCamera
        lastTakenImageUri = uriForCamera
        takePictureLauncher.launch(uriForCamera)
    }

    private fun createImageFile(): File {
        val storageDir = requireContext().cacheDir
        return File.createTempFile("JPEG_${System.currentTimeMillis()}", ".jpg", storageDir)
    }

    private fun predictGesture() {
        val resized = Bitmap.createScaledBitmap(bitmap, 244, 244, true)
        val model = LeapgestureModel244.newInstance(requireContext())

        try {
            val tensorImage = TensorImage(DataType.FLOAT32).apply { load(resized) }
            val inputBuffer = TensorBuffer.createFixedSize(intArrayOf(1, 244, 244, 3), DataType.FLOAT32)
            inputBuffer.loadBuffer(tensorImage.buffer)

            val outputs = model.process(inputBuffer)
            val confidences = outputs.outputFeature0AsTensorBuffer.floatArray

            val maxIndex = confidences.indices.maxByOrNull { confidences[it] } ?: -1
            if (maxIndex == -1 || maxIndex >= labels.size) {
                binding.txthasil.text = "Tidak dapat memprediksi gesture"
                binding.deskripsi.text = "Label tidak dikenali."
                return
            }

            val label = labels[maxIndex]
            val confidence = confidences[maxIndex] * 100

            binding.txthasil.text = "Prediksi: $label\nConfidence: ${"%.2f".format(confidence)}%"

            val descriptions = mapOf(
                "palm" to "Gesture ini digunakan untuk menyapa atau menghentikan sesuatu.",
                "L" to "Gesture ini membentuk huruf L, sering digunakan untuk navigasi kiri atau arah.",
                "fist" to "Tangan mengepal, digunakan untuk aksi konfirmasi atau penegasan.",
                "fist_moved" to "Tangan mengepal dan bergerak, bisa menunjukkan penekanan atau perintah.",
                "thumb" to "Jempol tegak, biasanya berarti bagus atau menyetujui.",
                "index" to "Telunjuk menunjuk, bisa digunakan untuk memilih atau menandai sesuatu.",
                "ok" to "Membentuk lingkaran dengan jempol dan telunjuk, menandakan 'oke' atau setuju.",
                "palm_moved" to "Tangan terbuka dan bergerak, bisa menunjukkan arah atau gerakan menggeser.",
                "C" to "Membentuk huruf C, sering digunakan untuk memilih objek berbentuk melingkar atau menggenggam.",
                "down" to "Gerakan ke bawah, bisa digunakan untuk scroll atau pilihan turun."
            )

            binding.deskripsi.text = descriptions[label] ?: "Deskripsi tidak tersedia."
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Terjadi kesalahan saat prediksi", Toast.LENGTH_SHORT).show()
        } finally {
            model.close()
        }
    }


    private fun savePredictionResult() {
        if (!::bitmap.isInitialized) {
            Toast.makeText(requireContext(), "Tidak ada gambar untuk disimpan.", Toast.LENGTH_SHORT).show()
            return
        }

        val result = binding.txthasil.text.toString()
        if (result.isBlank() || result == "-") {
            Toast.makeText(requireContext(), "Belum ada hasil prediksi.", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = requireContext().getSharedPreferences("user_preferences", Context.MODE_PRIVATE)
            .getString("user_id", null)

        if (userId.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "User belum login atau user_id tidak ditemukan.", Toast.LENGTH_SHORT).show()
            return
        }

        val baos = ByteArrayOutputStream().apply {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, this)
        }
        val imageBase64 = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT)

        val dataMap = mapOf(
            "gambar" to imageBase64,
            "hasil" to result,
            "deskripsi" to binding.deskripsi.text.toString(),
            "timestamp" to System.currentTimeMillis()
        )

        val ref = FirebaseDatabase.getInstance().getReference("prediksi").child(userId)
        val key = ref.push().key
        if (key == null) {
            Toast.makeText(requireContext(), "Gagal membuat key unik untuk prediksi.", Toast.LENGTH_SHORT).show()
            return
        }

        ref.child(key).setValue(dataMap)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Prediksi berhasil disimpan", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Gagal menyimpan ke Firebase", Toast.LENGTH_SHORT).show()
            }
    }

    private fun resetResult() {
        binding.txthasil.text = "-"
        binding.deskripsi.text = "-"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
