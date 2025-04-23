package controllers

import javax.inject._
import play.api.mvc._
import models.Supplier
import repositories.SupplierRepo

@Singleton
class SupplierController @Inject()(cc: ControllerComponents, supplierRepo: SupplierRepo) extends AbstractController(cc) {

  // Menampilkan daftar supplier, dengan opsi pencarian
  def index(keyword: Option[String]) = Action { implicit request =>
    val list = keyword match {
      case Some(k) => supplierRepo.searchByName(k).toList
      case None    => supplierRepo.getAllSupplier().toList
    }
    Ok(views.html.supplier.index(list, keyword))
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

    supplierRepo.insert(Supplier(0, nama, alamat, telepon, email))
    Redirect(routes.SupplierController.index(None))
  }

  // Menampilkan form untuk mengedit supplier
  def edit(id: Int) = Action {
    supplierRepo.findById(id) match {
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
    supplierRepo.update(updated)
    Redirect(routes.SupplierController.index(None))
  }

  // Menghapus supplier
  def delete(id: Int) = Action {
    supplierRepo.delete(id)
    Redirect(routes.SupplierController.index(None))
  }
}
