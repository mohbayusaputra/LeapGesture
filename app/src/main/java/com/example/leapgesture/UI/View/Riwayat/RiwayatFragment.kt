package com.example.leapgesture.UI.View.Riwayat

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.leapgesture.databinding.FragmentRiwayatBinding
import com.google.firebase.database.*

class RiwayatFragment : Fragment() {

    private var _binding: FragmentRiwayatBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: RiwayatAdapter
    private val listPrediksi = mutableListOf<Prediksi>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRiwayatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.title = "Riwayat"

        adapter = RiwayatAdapter(listPrediksi)
        binding.riwayatRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.riwayatRecyclerView.adapter = adapter

        loadData()
    }

    private fun loadData() {
        val sharedPreferences = requireContext().getSharedPreferences("user_preferences", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("user_id", null)

        if (userId == null) {
            Toast.makeText(requireContext(), "User ID tidak ditemukan. Silakan login ulang.", Toast.LENGTH_SHORT).show()
            Log.e("RiwayatFragment", "User ID null")
            return
        }

        val database = FirebaseDatabase.getInstance().getReference("prediksi").child(userId)
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listPrediksi.clear()
                if (snapshot.exists()) {
                    for (childSnapshot in snapshot.children) {
                        val prediksi = childSnapshot.getValue(Prediksi::class.java)
                        if (prediksi != null) {
                            listPrediksi.add(prediksi)
                        }
                    }
                    adapter.notifyDataSetChanged()
                    Log.d("RiwayatFragment", "Data loaded: ${listPrediksi.size} items")
                } else {
                    Log.d("RiwayatFragment", "Data snapshot kosong")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Gagal memuat data: ${error.message}", Toast.LENGTH_SHORT).show()
                Log.e("RiwayatFragment", "Database error: ${error.message}")
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
