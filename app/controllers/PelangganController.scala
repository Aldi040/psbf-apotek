package controllers

import javax.inject._
import play.api.mvc._
import models.Pelanggan
import repositories.PelangganRepo

@Singleton
class PelangganController @Inject()(cc: ControllerComponents, pelangganRepo: PelangganRepo) extends AbstractController(cc) {

  // Menampilkan daftar pelanggan
  def index(keyword: Option[String]) = Action { implicit request =>
    val list = keyword match {
      case Some(k) => pelangganRepo.searchByName(k).toList
      case None    => pelangganRepo.getAllPelanggan().toList
    }
    Ok(views.html.pelanggan.index(list, keyword))
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
    pelangganRepo.insert(Pelanggan(0, nama, jk))  // Menyimpan data pelanggan baru ke database
    Redirect(routes.PelangganController.index(None))  // Perbaiki agar parameter opsional sesuai
  }

  // Menampilkan form untuk mengedit data pelanggan
  def edit(id: Int) = Action {
    pelangganRepo.findById(id) match {
      case Some(pelanggan) =>
        Ok(views.html.pelanggan.edit(pelanggan))
      case None =>
        NotFound("Pelanggan tidak ditemukan")
    }
  }

  // Memperbarui data pelanggan
  def updatePelanggan(id: Int) = Action { implicit request =>
    val data = request.body.asFormUrlEncoded.get
    val nama = data("nama").head
    val jk = data("jenisKelamin").head

    pelangganRepo.update(Pelanggan(id, nama, jk))
    Redirect(routes.PelangganController.index(None))
  }

  // Fungsi delete untuk menghapus pelanggan
  def delete(id: Int) = Action {
    pelangganRepo.delete(id)
    Redirect(routes.PelangganController.index(None))
  }
}
