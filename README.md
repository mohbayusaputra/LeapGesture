# 🖐️ LeapGesture - Aplikasi Deteksi Bentuk Tangan Berbasis Deep Learning

**LeapGesture** adalah aplikasi Android berbasis teknologi **Deep Learning (TensorFlow Lite)** yang dirancang untuk mendeteksi dan mengenali **10 jenis pose/gesture tangan (Leap Motion Hand Gesture)** melalui tangkapan kamera langsung maupun unggahan gambar dari galeri. Aplikasi ini menerjemahkan pose tangan menjadi informasi klasifikasi beserta tingkat akurasi (*confidence*) dan skenario penerapannya, serta terintegrasi dengan **Firebase Realtime Database** untuk menyimpan riwayat deteksi setiap pengguna.

---

## ✨ Fitur Utama

1. **🔍 Deteksi Bentuk Tangan (Deep Learning - TensorFlow Lite)**
   - Mengambil foto pose tangan secara langsung menggunakan **Kamera** atau mengunggah gambar dari **Galeri**.
   - Memproses citra berukuran input `244x244` piksel secara *on-device* menggunakan model **[LeapGesture_Model_244.tflite](app/src/main/ml/LeapGesture_Model_244.tflite)**.
   - Menampilkan **Hasil Prediksi** (nama kelas gesture), nilai **Confidence (%)**, serta penjelasan **Penerapan** dari pose tangan tersebut.
   - Menyimpan hasil prediksi beserta gambar (terenkode *Base64*) dan waktu deteksi ke **Firebase Realtime Database**.

2. **🕒 Riwayat Deteksi (History)**
   - Menampilkan daftar riwayat hasil prediksi gesture yang pernah disimpan oleh pengguna secara *real-time* dari Firebase.
   - Menyajikan pratinjau gambar tangan, label hasil prediksi, nilai *confidence*, serta deskripsi penerapannya.

3. **🔐 Autentikasi & Manajemen Profil Pengguna**
   - **Splash Screen** dan **Welcome Screen (Onboarding 3 Slide)** yang memperkenalkan cara kerja deteksi *Leap Gesture*.
   - Fitur **Registrasi** dan **Login** pengguna yang tersimpan pada node `Users` di Firebase Realtime Database.
   - Halaman **Profil Pengguna** yang mencakup fitur **Edit Profil** (memperbarui *username*, *email*, dan *password*), halaman **About (Tentang Aplikasi)**, serta **Logout**.

4. **🏠 Halaman Beranda (Home)**
   - Menampilkan sapaan pengguna yang sedang aktif beserta konten informasi edukatif dan *pop-up dialog* interaktif.

---

## 🏷️ 10 Kelas Gesture Tangan yang Dideteksi

Model Deep Learning pada aplikasi ini dilatih untuk mengenali **10 kelas pose tangan** sesuai daftar pada [`app/src/main/assets/label.txt`](app/src/main/assets/label.txt):

| No | Label Gesture | Deskripsi & Penerapan pada Aplikasi |
|----|---------------|-------------------------------------|
| 1 | `palm` | Gesture tangan terbuka, digunakan untuk menyapa atau menghentikan sesuatu. |
| 2 | `L` | Gesture membentuk huruf L, sering digunakan untuk navigasi kiri atau penunjuk arah. |
| 3 | `fist` | Tangan mengepal, digunakan untuk aksi konfirmasi atau penegasan. |
| 4 | `fist_moved` | Tangan mengepal dan bergerak, menunjukkan penekanan atau perintah. |
| 5 | `thumb` | Jempol tegak, biasanya berarti bagus atau menyetujui. |
| 6 | `index` | Telunjuk menunjuk, digunakan untuk memilih atau menandai sesuatu. |
| 7 | `ok` | Membentuk lingkaran dengan jempol dan telunjuk, menandakan "oke" atau setuju. |
| 8 | `palm_moved` | Tangan terbuka dan bergerak, menunjukkan arah atau gerakan menggeser. |
| 9 | `C` | Membentuk huruf C, sering digunakan untuk memilih objek berbentuk melingkar atau menggenggam. |
| 10 | `down` | Gerakan tangan ke bawah, digunakan untuk *scroll* atau pilihan turun. |

---

## 🛠️ Teknologi & Pustaka (Tech Stack)

- **Bahasa Pemrograman:** Kotlin
- **Komponen UI & Arsitektur:** Android SDK (Min SDK 27, Target SDK 35), ViewBinding, Material Design Components, ConstraintLayout, Navigation Component, ViewPager2 (`dotsindicator`)
- **Deep Learning / Machine Learning:** TensorFlow Lite (`tensorflow-lite-support`, `tensorflow-lite-metadata`, ML Model Binding)
- **Database & Penyimpanan:** Firebase Realtime Database, SharedPreferences (sesi pengguna lokal), Room Persistence Library
- **Jaringan & Pemrosesan Gambar:** Retrofit2, Gson, Glide, Picasso

---

## 📂 Struktur Proyek

```text
LeapGesture/
├── app/
│   ├── src/main/
│   │   ├── assets/
│   │   │   └── label.txt                     # Daftar 10 label kelas Leap Gesture
│   │   ├── ml/
│   │   │   └── LeapGesture_Model_244.tflite  # Model Deep Learning TFLite (Input 244x244)
│   │   ├── java/com/example/leapgesture/
│   │   │   ├── Data/                         # Modul API (Retrofit), Model, dan Repository
│   │   │   ├── SplashScreen/                 # SplashScreenActivity
│   │   │   ├── WelcomeScreen/                # Onboarding 3 Slide (Pengenalan Leap Gesture)
│   │   │   └── UI/
│   │   │       ├── View/
│   │   │       │   ├── Home/                 # HomeFragment & InfoDialogFragment
│   │   │       │   ├── UjiDL/                # DlFragment (Deteksi 10 Gesture via Kamera/Galeri & TFLite)
│   │   │       │   ├── Riwayat/              # RiwayatFragment & RiwayatAdapter (Data Firebase)
│   │   │       │   ├── Profil/               # ProfilFragment, EditProfileActivity, AboutActivity
│   │   │       │   ├── LoginActivity.kt
│   │   │       │   ├── RegisterActivity.kt
│   │   │       │   └── MainActivity.kt
│   │   │       └── ViewModel/                # NewsViewModel & NewsViewModelFactory
│   │   └── res/                              # Layout XML, Drawable, Mipmap, Values, Navigation
│   └── build.gradle.kts
├── build.gradle.kts
└── settings.gradle.kts
```

---

## 🚀 Cara Menjalankan Proyek

1. **Clone repositori ini:**
   ```bash
   git clone https://github.com/mohbayusaputra/LeapGesture.git
   ```
2. **Buka di Android Studio:**
   - Pilih **Open** pada Android Studio dan arahkan ke folder `LeapGesture`.
   - Tunggu hingga proses *Gradle Sync* selesai.
3. **Konfigurasi Firebase:**
   - Pastikan berkas `app/google-services.json` sudah terhubung dengan proyek Firebase Realtime Database Anda (memiliki node `Users` dan `prediksi`).
4. **Jalankan Aplikasi:**
   - Hubungkan perangkat Android fisik (disarankan untuk menguji pengambilan foto melalui kamera) atau gunakan Emulator Android (API 27+).
   - Klik tombol **Run (`Shift + F10`)** di Android Studio.

---

## 📌 Informasi Proyek

Aplikasi ini dikembangkan untuk memenuhi proyek **Ujian Akhir Semester (UAS)** mata kuliah **Deep Learning**.

