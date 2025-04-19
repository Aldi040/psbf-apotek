package repositories

import javax.inject._
import play.api.db.DBApi
import java.sql.Connection
import java.math.BigDecimal

@Singleton
class PendapatanRepository @Inject()(dbapi: DBApi) {

  private val db = dbapi.database("default")

  // Fungsi untuk mendapatkan pendapatan hari ini
  def getPendapatanHariIni(): Double = {
    db.withConnection { implicit connection =>
      val stmt = connection.prepareStatement(
        """SELECT SUM(TOTAL_HARGA) 
          |FROM transaksi 
          |WHERE DATE(TANGGAL_TRANSAKSI) = CURDATE()""".stripMargin)
      val rs = stmt.executeQuery()
      if (rs.next()) rs.getDouble(1) else 0.0
    }
  }

  // Fungsi untuk mendapatkan pendapatan bulan ini
  def getPendapatanBulanIni(): Double = {
    db.withConnection { implicit connection =>
      val stmt = connection.prepareStatement(
        """SELECT SUM(TOTAL_HARGA) 
          |FROM transaksi 
          |WHERE MONTH(TANGGAL_TRANSAKSI) = MONTH(CURDATE()) 
          |AND YEAR(TANGGAL_TRANSAKSI) = YEAR(CURDATE())""".stripMargin)
      val rs = stmt.executeQuery()
      if (rs.next()) rs.getDouble(1) else 0.0
    }
  }

  // Fungsi untuk mendapatkan pendapatan tahun ini
  def getPendapatanTahunIni(): Double = {
    db.withConnection { implicit connection =>
      val stmt = connection.prepareStatement(
        """SELECT SUM(TOTAL_HARGA) 
          |FROM transaksi 
          |WHERE YEAR(TANGGAL_TRANSAKSI) = YEAR(CURDATE())""".stripMargin)
      val rs = stmt.executeQuery()
      if (rs.next()) rs.getDouble(1) else 0.0
    }
  }

  // Fungsi untuk mendapatkan pendapatan per bulan
  def getPendapatanPerBulan(): Seq[(String, BigDecimal)] = {
    db.withConnection { implicit connection =>
      val stmt = connection.prepareStatement(
        """SELECT MONTH(TANGGAL_TRANSAKSI), SUM(TOTAL_HARGA) 
          |FROM transaksi 
          |GROUP BY MONTH(TANGGAL_TRANSAKSI) 
          |ORDER BY MONTH(TANGGAL_TRANSAKSI)""".stripMargin)
      val rs = stmt.executeQuery()
      var result = Seq.empty[(String, BigDecimal)]
      while (rs.next()) {
        result :+= (rs.getString(1), rs.getBigDecimal(2))
      }
      result
    }
  }
}
