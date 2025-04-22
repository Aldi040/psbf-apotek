package models

import java.time.LocalDate

case class TransaksiWithPelanggan(
  idTransaksi: Option[Int],
  namaPelanggan: Option[String],
  tanggalTransaksi: LocalDate,
  totalHarga: BigDecimal,
  metodePembayaran: String
)
