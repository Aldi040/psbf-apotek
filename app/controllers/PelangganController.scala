package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.db.Database
import models.Pelanggan

@Singleton
class PelangganController @Inject()(db: Database, cc: ControllerComponents) 
  extends AbstractController(cc) with play.api.i18n.I18nSupport {

  val pelangganForm = Form(
    mapping(
      "nama" -> nonEmptyText,
      "jenis_kelamin" -> nonEmptyText
    )( (nama, jk) => (nama, jk) )(
      data => Some( (data._1, data._2) )
    )
  )

  def index = Action { implicit request =>
    Ok(views.html.pelanggan.pelanggan(Pelanggan.getAll(db)))
  }

  def createForm = Action { implicit request =>
    Ok(views.html.pelanggan.form(pelangganForm))
  }

  def save = Action { implicit request =>
    pelangganForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.pelanggan.form(formWithErrors)),
      data => {
        Pelanggan.create(data._1, data._2)(db)
        Redirect(routes.PelangganController.index).flashing("success" -> "Pelanggan berhasil dibuat")
      }
    )
  }

  def editForm(id: Int) = Action { implicit request =>
    Pelanggan.getById(id)(db) match {
      case Some(p) => 
        val formData = (p.nama, p.jenisKelamin)
        Ok(views.html.pelanggan.form(
          pelangganForm.fill(formData), 
          Some(id)
        ))
      case None => NotFound("Pelanggan tidak ditemukan")
    }
  }

  def update(id: Int) = Action { implicit request =>
    pelangganForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.pelanggan.form(formWithErrors, Some(id))),
      data => {
        Pelanggan.update(id, data._1, data._2)(db)
        Redirect(routes.PelangganController.index).flashing("success" -> "Pelanggan berhasil diupdate")
      }
    )
  }

  def delete(id: Int) = Action {
    Pelanggan.delete(id)(db)
    Redirect(routes.PelangganController.index).flashing("success" -> "Pelanggan berhasil dihapus")
  }
}