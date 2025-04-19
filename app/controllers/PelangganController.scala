package controllers

import javax.inject._
import play.api.mvc._
import models.Pelanggan
import repositories.PelangganRepo

@Singleton
class PelangganController @Inject()(cc: ControllerComponents, repo: PelangganRepo) extends AbstractController(cc) {

  // Menampilkan daftar pelanggan
  def index = Action {
    val list = repo.getAllPelanggan().toList  // Mengonversi Seq ke List
    Ok(views.html.pelanggan.index(list))  // Mengirimkan data dalam format List
  }

  // Menampilkan form untuk membuat pelanggan baru
  def createForm = Action {
    Ok(views.html.pelanggan.create())
  }

  // Menyimpan pelanggan baru
  def save = Action { implicit request =>
    val data = request.body.asFormUrlEncoded.get
    val nama = data("nama").head
    val jk = data("jenisKelamin").head
    repo.insert(Pelanggan(0, nama, jk))  // Menyimpan data pelanggan baru ke database
    Redirect(routes.PelangganController.index)  // Mengarahkan ke halaman daftar pelanggan
  }

  // Menampilkan form untuk mengedit data pelanggan
  def edit(id: Int) = Action {
    repo.findById(id) match {
      case Some(pelanggan) =>
        // Jika pelanggan ditemukan, tampilkan form edit dengan data pelanggan
        Ok(views.html.pelanggan.edit(pelanggan))
      case None =>
        // Jika pelanggan tidak ditemukan, tampilkan error
        NotFound("Pelanggan tidak ditemukan")
    }
  }

  // Memperbarui data pelanggan
  def updatePelanggan(id: Int) = Action { implicit request =>
    val data = request.body.asFormUrlEncoded.get
    val nama = data("nama").head
    val jk = data("jenisKelamin").head

    // Memperbarui data pelanggan di database
    repo.update(Pelanggan(id, nama, jk))
    Redirect(routes.PelangganController.index)  // Mengarahkan kembali ke halaman daftar pelanggan
  }

  // Fungsi delete untuk menghapus pelanggan (dapat ditambahkan sesuai kebutuhan)
  def delete(id: Int) = Action {
    repo.delete(id)
    Redirect(routes.PelangganController.index)  // Setelah menghapus, kembali ke halaman daftar pelanggan
  }
}
