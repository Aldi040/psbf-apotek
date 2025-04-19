package controllers

import javax.inject._
import play.api.mvc._
import models.Supplier
import repositories.SupplierRepo

@Singleton
class SupplierController @Inject()(cc: ControllerComponents, repo: SupplierRepo) extends AbstractController(cc) {

  // Menampilkan daftar supplier
  def index = Action {
    val list = repo.getAllSupplier.toList
    Ok(views.html.supplier.index(list))
  }

  // Menampilkan form untuk membuat supplier baru
  def createForm = Action {
    Ok(views.html.supplier.create())
  }

  // Menyimpan supplier baru
  def save = Action { implicit request =>
    val data = request.body.asFormUrlEncoded.get
    val nama = data("nama").head
    val alamat = data.get("alamat").flatMap(_.headOption)
    val telepon = data.get("telepon").flatMap(_.headOption)
    val email = data.get("email").flatMap(_.headOption)

    repo.insert(Supplier(0, nama, alamat, telepon, email))
    Redirect(routes.SupplierController.index)
  }

  // Menampilkan form untuk mengedit supplier
  def edit(id: Int) = Action {
    repo.findById(id) match {
      case Some(supplier) =>
        Ok(views.html.supplier.edit(supplier))
      case None =>
        NotFound("Supplier tidak ditemukan")
    }
  }

  // Memperbarui supplier
  def updateSupplier(id: Int) = Action { implicit request =>
    val data = request.body.asFormUrlEncoded.get
    val nama = data("nama").head
    val alamat = data.get("alamat").flatMap(_.headOption)
    val telepon = data.get("telepon").flatMap(_.headOption)
    val email = data.get("email").flatMap(_.headOption)

    val updated = Supplier(id, nama, alamat, telepon, email)
    repo.update(updated)
    Redirect(routes.SupplierController.index)
  }

  // Menghapus supplier
  def delete(id: Int) = Action {
    repo.delete(id)
    Redirect(routes.SupplierController.index)
  }
}
