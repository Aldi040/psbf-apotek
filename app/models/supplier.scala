package models

case class Supplier(
  id: Int,
  nama: String,
  alamat: Option[String],
  telepon: Option[String],
  email: Option[String]
)