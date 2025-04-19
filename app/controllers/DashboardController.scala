package controllers

import javax.inject._
import play.api.mvc._
import repositories.PendapatanRepository
import scala.concurrent.{ExecutionContext, Future}
import java.math.BigDecimal

@Singleton
class DashboardController @Inject()(
  cc: ControllerComponents,
  pendapatanRepo: PendapatanRepository
)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  def index = Action.async {
    // Membungkus setiap panggilan repository dalam Future untuk memastikan eksekusi asinkron
    val hariFuture = Future(pendapatanRepo.getPendapatanHariIni())
    val bulanFuture = Future(pendapatanRepo.getPendapatanBulanIni())
    val tahunFuture = Future(pendapatanRepo.getPendapatanTahunIni())
    val grafikFuture = Future(pendapatanRepo.getPendapatanPerBulan())

    // Menggunakan for-comprehension untuk menggabungkan semua Future
    for {
      hari <- hariFuture
      bulan <- bulanFuture
      tahun <- tahunFuture
      grafik <- grafikFuture
    } yield {
      // Mengonversi Double ke BigDecimal untuk tampilan
      val hariBigDecimal = new BigDecimal(hari)
      val bulanBigDecimal = new BigDecimal(bulan)
      val tahunBigDecimal = new BigDecimal(tahun)

      // Render tampilan dengan data yang didapat
      Ok(views.html.dashboard(hariBigDecimal, bulanBigDecimal, tahunBigDecimal, grafik))
    }
  }
}
