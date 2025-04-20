package controllers

import javax.inject._
import play.api.mvc._
import models.Obat
import repositories.ObatRepo
import repositories.SupplierRepo
import models.Supplier

@Singleton
class ObatController @Inject()(cc: ControllerComponents, repo: ObatRepo,  supplierRepo: SupplierRepo ) extends AbstractController(cc) {


  def index = Action {
    val list = repo.getAllObat.toList
    Ok(views.html.obat.index(list))
  }


  def createForm = Action {
    val allSuppliers: Seq[models.Supplier] = supplierRepo.getAllSupplier()
    Ok(views.html.obat.create(allSuppliers))

  }

def save = Action { implicit request =>
  val data       = request.body.asFormUrlEncoded.get
  val nama       = data("nama_obat").head
  val kategori   = data.get("kategori").flatMap(_.headOption)            // Option[String]
  val keterangan = data.get("keterangan").flatMap(_.headOption)         // Option[String]
  val stok       = data.get("jumlah_stock").flatMap(_.headOption).map(_.toInt)            // Option[Int]
  val harga      = data.get("harga").flatMap(_.headOption).map(_.toDouble)               // Option[Double]
  val exp        = data.get("exp").flatMap(_.headOption).map(java.sql.Date.valueOf)               // Option[java.sql.Date]
  val supplier   = data.get("id_supplier").flatMap(_.headOption).map(_.toInt)            // Option[Int]

  repo.insert(Obat(id = 0, nama_obat = nama, kategori = kategori, keterangan = keterangan, jumlah_stock = stok, harga = harga, exp = exp, id_supplier = supplier))

  Redirect(routes.ObatController.index)
}


  def edit(id: Int) = Action {
    repo.findById(id) match {
      case Some(obat) =>
        Ok(views.html.obat.edit(obat))
      case None =>
        NotFound("obat tidak ditemukan")
    }
  }

def updateObat(id: Int) = Action { implicit request =>
  val data = request.body.asFormUrlEncoded.get
  val nama_obat  = data("nama_obat").head
  val kategori   = data.get("kategori").flatMap(_.headOption)
  val keterangan = data.get("keterangan").flatMap(_.headOption)
  val stok = data.get("jumlah_stock").flatMap(_.headOption).map(_.toInt)
  val harga      = data.get("harga").flatMap(_.headOption).map(_.toDouble)
  val exp        = data.get("exp").flatMap(_.headOption).map(java.sql.Date.valueOf)
  val supplier   = data.get("id_supplier").flatMap(_.headOption).map(_.toInt)

  val updated = Obat(id, nama_obat, kategori, keterangan, stok, harga, exp, supplier)

  repo.update(updated)
  Redirect(routes.ObatController.index)
}

  def delete(id: Int) = Action {
    repo.delete(id)
    Redirect(routes.ObatController.index)
  }
}
