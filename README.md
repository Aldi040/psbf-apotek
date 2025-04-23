# 💊 PSBF Apotek Management System

Selamat datang di **PSBF Apotek**! 🎉  
Project ini adalah aplikasi manajemen apotek berbasis web yang dibangun menggunakan **Play Framework (Scala)** dengan pendekatan **MVC**, **JDBC**, dan **Repository Pattern**.

---

## 🚀 Fitur Utama

- 👥 CRUD Data Pelanggan
- 🏭 CRUD Data Supplier
- 💊 CRUD Data Obat
- 🧾 Transaksi Pembelian / Penjualan
- 🔍 Fitur Pencarian Nama
- 🎨 Tampilan UI berbasis Bootstrap & FontAwesome

---

## 🛠️ Teknologi yang Digunakan

- ⚙️ **Play Framework (Scala)**
- 🧱 **MVC Architecture**
- 🔌 **JDBC** (tanpa ORM)
- 🗃️ **Repository Pattern** untuk pemisahan logika akses data
- 🎨 **HTML + Bootstrap** untuk tampilan
- 🛢️ **MySQL** (bisa disesuaikan dengan kebutuhan)

---

## 🧑‍💻 Cara Menjalankan Project

### 📦 Prasyarat

Pastikan kamu sudah menginstal:

- ✅ Java JDK 8 atau 11+
- ✅ SBT (Scala Build Tool)
- ✅ MySQL (atau database lainnya)

---

### ▶️ Menjalankan Aplikasi

1. **Clone project ini:**

    ```bash
    https://github.com/Aldi040/psbf-apotek.git
    cd psbf-apotek
    ```

2. **Jalankan aplikasi dengan SBT:**

    ```bash
    sbt run
    ```

3. **Akses dari browser:**

    ```
    http://localhost:9000/
    ```

---

## 🧪 Struktur Kode Utama

- `controllers/` – Berisi class controller (misal: `PelangganController.scala`)
- `models/` – Berisi model data (`Pelanggan.scala`, `Supplier.scala`, dst.)
- `repositories/` – Berisi logika akses database dengan JDBC (`PelangganRepo.scala`, `SupplierRepo.scala`, dst.)
- `views/` – Berisi file HTML template untuk halaman web

---

## 🤝 Kontribusi

Kontribusi sangat terbuka! Fork dan pull request sangat dipersilakan 💡

---

Terima kasih sudah mampir! Selamat coding dan semoga bermanfaat 💚
