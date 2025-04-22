package controllers

import javax.inject._
import play.api.mvc._
import models.{Transaksi, TransaksiWithPelanggan}
import repositories.{TransaksiRepo, PelangganRepo}
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Singleton
class TransaksiController @Inject()(
  cc: ControllerComponents,
  transaksiRepo: TransaksiRepo,
  pelangganRepo: PelangganRepo
) extends AbstractController(cc) {

  // Menampilkan daftar transaksi
def index(keyword: Option[String]) = Action { implicit request =>
  println(s"Received keyword: ${keyword.getOrElse("No keyword")}")  // Log the keyword received

  val transaksiList = keyword match {
    case Some(k) =>
      println(s"Searching transactions with keyword: $k")  // Log the search keyword
      transaksiRepo.searchByName(k)
    case None =>
      println("No keyword, showing all transactions")  // Log if no keyword
      transaksiRepo.getAllTransaksi()
  }

  val pelangganList = pelangganRepo.getAllPelanggan()
  val mapPelanggan = pelangganList.map(p => p.id -> p.nama).toMap

  val transaksiGabung = transaksiList.map { t =>
    val nama = t.idPelanggan.flatMap(mapPelanggan.get)
    TransaksiWithPelanggan(t.idTransaksi, nama, t.tanggalTransaksi, t.totalHarga, t.metodePembayaran)
  }

  Ok(views.html.transaksi.index(transaksiGabung, keyword))
}






  // Menampilkan form untuk membuat transaksi baru
def createForm = Action { implicit request =>
  val pelangganList = pelangganRepo.getAllPelanggan().toList  // Mengubah Seq ke List
  Ok(views.html.transaksi.create(pelangganList))  // Pastikan pelangganList dikirim ke view
}


def save = Action { implicit request =>
  val data = request.body.asFormUrlEncoded.get

  // Mendapatkan data dari form
  val idPelanggan = data("idPelanggan").headOption.flatMap(_.toIntOption)  // Mengambil ID Pelanggan dari form
  val tanggalTransaksiStr = data("tanggalTransaksi").head
  val totalHargaStr = data("totalHarga").head
  val metodePembayaran = data("metodePembayaran").head

  // Convert totalHarga menjadi BigDecimal
  val totalHarga = BigDecimal(totalHargaStr)

  // Convert tanggalTransaksi menjadi LocalDate
  val tanggalTransaksi = LocalDate.parse(tanggalTransaksiStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"))

  // Pastikan idPelanggan tidak kosong
  idPelanggan match {
    case Some(id) =>
      val transaksi = Transaksi(
        idTransaksi = None, // Auto-generated
        idPelanggan = idPelanggan, 
        tanggalTransaksi = tanggalTransaksi, 
        totalHarga = totalHarga, 
        metodePembayaran = metodePembayaran
      )

      // Menyimpan transaksi ke database
      transaksiRepo.insert(transaksi)

      // Redirect ke halaman transaksi
    Redirect(routes.TransaksiController.index(None))

    case None =>
      // Handle jika idPelanggan kosong
      BadRequest("Pelanggan tidak dipilih!")
  }
}



  // Menampilkan form untuk mengedit transaksi
  def edit(id: Int) = Action {
    transaksiRepo.findById(id) match {
      case Some(transaksi) => Ok(views.html.transaksi.edit(transaksi))
      case None => NotFound("Transaksi tidak ditemukan")
    }
  }

  // Memperbarui transaksi yang sudah ada
  def update(id: Int) = Action { implicit request =>
    val data = request.body.asFormUrlEncoded.get

    // Mendapatkan nilai dari form dan memastikan idPelanggan dalam bentuk Option[Int]
    val idPelanggan = data("idPelanggan").headOption.flatMap(_.toIntOption)  // Mengubah ke Option[Int]
    val tanggalTransaksi = data("tanggalTransaksi").head
    val totalHarga = data("totalHarga").head.toDouble
    val metodePembayaran = data("metodePembayaran").head

    // Mengubah tanggalTransaksi menjadi LocalDate (pastikan format yang sesuai)
    val tanggalTransaksiFormatted = LocalDate.parse(tanggalTransaksi, DateTimeFormatter.ofPattern("yyyy-MM-dd"))

    // Membuat objek Transaksi dengan idPelanggan dalam bentuk Option[Int]
    val transaksi = Transaksi(
      idTransaksi = Some(id),  // idTransaksi sudah pasti ada, jadi menggunakan Some(id)
      idPelanggan = idPelanggan,
      tanggalTransaksi = tanggalTransaksiFormatted,
      totalHarga = BigDecimal(totalHarga),  // Menggunakan BigDecimal untuk totalHarga
      metodePembayaran = metodePembayaran
    )

    // Memperbarui transaksi di database
    transaksiRepo.update(transaksi)

    // Redirect ke halaman transaksi
    Redirect(routes.TransaksiController.index(None))
  }

  // Menghapus transaksi berdasarkan ID
  def delete(id: Int) = Action {
    transaksiRepo.delete(id)
    Redirect(routes.TransaksiController.index(None))
  }
}
