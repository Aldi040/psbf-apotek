package controllers

import javax.inject._
import play.api.mvc._

@Singleton
class TransaksiController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  def index = Action {
    Ok(views.html.transaksi.index())
  }
  
}
