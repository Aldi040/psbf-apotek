package models

case class Obat(
  id: Int,
  nama_obat: String,
  kategori: Option[String],
  keterangan: Option[String],
  jumlah_stock: Option[Int],
  harga: Option[Double],
  exp: Option[java.sql.Date],
  id_supplier: Option[Int]
)
