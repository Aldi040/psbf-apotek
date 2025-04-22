package models

import java.time.LocalDate
import play.api.libs.json._

case class Transaksi(
  idTransaksi: Option[Int],
  idPelanggan: Option[Int],
  tanggalTransaksi: LocalDate,
  totalHarga: BigDecimal,
  metodePembayaran: String
)

object Transaksi {
  implicit val transaksiFormat: Format[Transaksi] = Json.format[Transaksi]
}
