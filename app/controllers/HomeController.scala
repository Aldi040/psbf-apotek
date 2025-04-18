package controllers

import javax.inject._
import play.api.mvc._
import play.api.db.Database
import repositories.DatabaseTest

class HomeController @Inject()(
  val controllerComponents: ControllerComponents,
  db: Database
) extends BaseController {

  // Tambahkan method index untuk handle route "/"
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok("Selamat datang di Sistem Apotek")
  }

  def testDb = Action { implicit request: Request[AnyContent] =>
    val dbTest = new DatabaseTest(db)
    if(dbTest.testConnection()) {
      Ok("Koneksi database berhasil!")
    } else {
      InternalServerError("Gagal terkoneksi ke database")
    }
  }
}