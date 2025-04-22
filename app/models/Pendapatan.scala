package models

import java.sql.Date

case class Pendapatan(
  id: Int,
  idPelanggan: Int,
  tanggal: Date,
  totalHarga: BigDecimal,
  metodePembayaran: String
)
