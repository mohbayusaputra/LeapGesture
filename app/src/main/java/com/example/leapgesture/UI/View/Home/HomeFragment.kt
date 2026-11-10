package com.example.leapgesture.UI.View.Home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.example.leapgesture.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadProfileData()
        setupClickListeners()
    }

    private fun loadProfileData() {
        val sharedPreferences = requireActivity().getSharedPreferences("user_preferences", AppCompatActivity.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "Pengguna")
        binding.welcomeText.text = "Halo, $username"
    }

    private fun setupClickListeners() {
        binding.cardSejarah.setOnClickListener {
            val title = "📚 Sejarah BISINDO"
            val content = "Bahasa Isyarat Indonesia (BISINDO) berkembang secara alami dalam komunitas Tuli Indonesia. Berbeda dengan SIBI yang dibuat pemerintah, BISINDO adalah bahasa yang berkembang organik dan diakui sebagai bahasa ibu kaum Tuli."
            showInfoDialog(title, content)
        }

        binding.cardManfaat.setOnClickListener {
            val title = "💪 Manfaat Mempelajari"
            val content = "Meningkatkan kemampuan komunikasi non-verbal, membuka peluang karir, membangun empati, menciptakan lingkungan yang lebih inklusif, dan meningkatkan kesadaran akan diversitas linguistik."
            showInfoDialog(title, content)
        }

        binding.cardKomunitas.setOnClickListener {
            val title = "👥 Komunitas Tuli"
            val content = "Komunitas Tuli Indonesia sangat beragam dan kaya akan budaya. Mereka memiliki organisasi seperti Gerkatin yang memperjuangkan hak-hak penyandang disabilitas pendengaran dan menggunakan BISINDO sebagai bahasa utama."
            showInfoDialog(title, content)
        }

        binding.gridAlfabet.children.forEach { view ->
            if (view is TextView) {
                view.setOnClickListener {
                    val clickedLetter = (it as TextView).text.toString()
                    val title = "Huruf $clickedLetter"
                    val content = getAlphabetDescription(clickedLetter)
                    showInfoDialog(title, content)
                }
            }
        }
    }

    // FUNGSI UNTUK MENAMPILKAN DIALOG
    private fun showInfoDialog(title: String, content: String) {
        val dialog = InfoDialogFragment.newInstance(title, content)
        // PASTIKAN BARIS INI SUDAH BENAR
        dialog.show(childFragmentManager, "InfoDialogFragment")
    }

    // FUNGSI UNTUK MENDAPATKAN DESKRIPSI ALFABET
    private fun getAlphabetDescription(letter: String): String {
        return when (letter) {
            "A" -> "Kepalkan tangan dengan ibu jari di samping."
            "B" -> "Keempat jari lurus ke atas, ibu jari ditekuk ke dalam telapak tangan."
            "C" -> "Bentuk huruf C dengan tangan."
            "D" -> "Jari telunjuk lurus, jari lain ditekuk, ibu jari menyentuh jari tengah."
            "E" -> "Semua jari ditekuk ke dalam."
            "F" -> "Ibu jari dan telunjuk membentuk lingkaran, jari lain lurus."
            "G" -> "Ibu jari dan telunjuk sejajar horizontal."
            "H" -> "Telunjuk dan jari tengah sejajar horizontal."
            "I" -> "Kelingking lurus ke atas."
            "J" -> "Seperti huruf I, kemudian gerakkan membentuk huruf J."
            "K" -> "Telunjuk lurus ke atas, jari tengah menyentuh ibu jari."
            "L" -> "Ibu jari dan telunjuk membentuk sudut 90 derajat."
            "M" -> "Tiga jari pertama di atas ibu jari."
            "N" -> "Dua jari pertama di atas ibu jari."
            "O" -> "Semua ujung jari bertemu membentuk lingkaran."
            "P" -> "Seperti huruf K tetapi mengarah ke bawah."
            "Q" -> "Ibu jari dan telunjuk mengarah ke bawah."
            "R" -> "Telunjuk dan jari tengah bersilang."
            "S" -> "Kepalkan tangan dengan ibu jari di depan."
            "T" -> "Ibu jari di antara telunjuk dan jari tengah."
            "U" -> "Telunjuk dan jari tengah lurus sejajar ke atas."
            "V" -> "Telunjuk dan jari tengah membentuk huruf V."
            "W" -> "Tiga jari pertama lurus ke atas membentuk W."
            "X" -> "Telunjuk ditekuk seperti kait."
            "Y" -> "Ibu jari dan kelingking lurus, jari lain tertutup."
            "Z" -> "Telunjuk lurus, gerakkan membentuk huruf Z di udara."
            else -> "Deskripsi tidak ditemukan."
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}