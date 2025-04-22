package models

import play.api.data.Form
import play.api.data.Forms._
import java.time.LocalDate

case class TransaksiFormData(idPelanggan: Int, tanggal: LocalDate, totalHarga: BigDecimal, metodePembayaran: String)

object TransaksiFormData {
  val form: Form[TransaksiFormData] = Form(
    mapping(
      "idPelanggan" -> number,
      "tanggal" -> localDate,
      "totalHarga" -> bigDecimal,
      "metodePembayaran" -> nonEmptyText
    )(TransaksiFormData.apply)(TransaksiFormData.unapply)
  )
}
