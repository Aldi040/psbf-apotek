package controllers

import javax.inject._
import play.api.mvc._
import repositories.{DetailRepo, ObatRepo, PelangganRepo}
import models.{DetailPembelian, Obat, Pelanggan}

@Singleton
class DetailController @Inject()(
  cc: ControllerComponents,
  detailRepo: DetailRepo,
  pelangganRepo: PelangganRepo,
  obatRepo: ObatRepo
) extends AbstractController(cc) {

  def index = Action {
    val details = detailRepo.getAllDetail()
    Ok(views.html.detail.index(details))
  }

  def createForm = Action {
    val pelanggans = pelangganRepo.getAllPelanggan()
    val obats = obatRepo.getAllObat()
    Ok(views.html.detail.create(pelanggans, obats))
  }

  def save = Action(parse.formUrlEncoded) { request =>
    val data = request.body
    detailRepo.insert(
      data("idPelanggan").head.toInt,
      data("idObat").head.toInt,
      data("qty").head.toInt,
      data("idTransaksi").head.toInt
    )
    Redirect(routes.DetailController.index)
  }

  def editForm(id: Int) = Action {
    val detail = detailRepo.findRawById(id)
    val pelanggans = pelangganRepo.getAllPelanggan()
    val obats = obatRepo.getAllObat()
    detail match {
      case Some((idPelanggan, idObat, qty, idTransaksi)) =>
        Ok(views.html.detail.edit(id, idPelanggan, idObat, qty, idTransaksi, pelanggans, obats))
      case None => NotFound("Data tidak ditemukan")
    }
  }

  def update(id: Int) = Action(parse.formUrlEncoded) { request =>
    val data = request.body
    detailRepo.update(
      id,
      data("idPelanggan").head.toInt,
      data("idObat").head.toInt,
      data("qty").head.toInt,
      data("idTransaksi").head.toInt
    )
    Redirect(routes.DetailController.index)
  }

  def delete(id: Int) = Action {
    detailRepo.delete(id)
    Redirect(routes.DetailController.index)
  }
}
