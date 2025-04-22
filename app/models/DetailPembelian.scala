package models

case class DetailPembelian(
  idPembelian: Int,
  namaPelanggan: String,
  namaObat: String,
  qty: Int,
  idTransaksi: Int
)
