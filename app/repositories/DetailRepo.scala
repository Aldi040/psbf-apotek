package repositories

import javax.inject._
import play.api.db.DBApi
import models.DetailPembelian

@Singleton
class DetailRepo @Inject()(dbapi: DBApi) {
  private val db = dbapi.database("default")

  def getAllDetail(): Seq[DetailPembelian] = {
    db.withConnection { implicit connection =>
      val stmt = connection.createStatement()
      val rs = stmt.executeQuery(
        """
          SELECT dp.id_pembelian, p.nama_pelanggan, o.nama_obat, dp.qty, dp.id_transaksi
          FROM pembelian_obat dp
          JOIN pelanggan p ON dp.id_pelanggan = p.id_pelanggan
          JOIN obat o ON dp.id_obat = o.id_obat
        """
      )
      val buffer = scala.collection.mutable.ListBuffer[DetailPembelian]()
      while (rs.next()) {
        buffer += DetailPembelian(
          rs.getInt("id_pembelian"),
          rs.getString("nama_pelanggan"),
          rs.getString("nama_obat"),
          rs.getInt("qty"),
          rs.getInt("id_transaksi")
        )
      }
      buffer.toList
    }
  }

  def insert(idPelanggan: Int, idObat: Int, qty: Int, idTransaksi: Int): Unit = {
    db.withConnection { implicit connection =>
      val stmt = connection.prepareStatement(
        "INSERT INTO pembelian_obat (id_pelanggan, id_obat, qty, id_transaksi) VALUES (?, ?, ?, ?)"
      )
      stmt.setInt(1, idPelanggan)
      stmt.setInt(2, idObat)
      stmt.setInt(3, qty)
      stmt.setInt(4, idTransaksi)
      stmt.executeUpdate()
    }
  }

  def delete(idPembelian: Int): Unit = {
    db.withConnection { implicit connection =>
      val stmt = connection.prepareStatement("DELETE FROM pembelian_obat WHERE id_pembelian = ?")
      stmt.setInt(1, idPembelian)
      stmt.executeUpdate()
    }
  }

  def update(idPembelian: Int, idPelanggan: Int, idObat: Int, qty: Int, idTransaksi: Int): Unit = {
    db.withConnection { implicit connection =>
      val stmt = connection.prepareStatement(
        "UPDATE pembelian_obat SET id_pelanggan = ?, id_obat = ?, qty = ?, id_transaksi = ? WHERE id_pembelian = ?"
      )
      stmt.setInt(1, idPelanggan)
      stmt.setInt(2, idObat)
      stmt.setInt(3, qty)
      stmt.setInt(4, idTransaksi)
      stmt.setInt(5, idPembelian)
      stmt.executeUpdate()
    }
  }

  def findRawById(idPembelian: Int): Option[(Int, Int, Int, Int)] = {
    db.withConnection { implicit connection =>
      val stmt = connection.prepareStatement("SELECT * FROM pembelian_obat WHERE id_pembelian = ?")
      stmt.setInt(1, idPembelian)
      val rs = stmt.executeQuery()
      if (rs.next()) {
        Some((
          rs.getInt("id_pelanggan"),
          rs.getInt("id_obat"),
          rs.getInt("qty"),
          rs.getInt("id_transaksi")
        ))
      } else None
    }
  }
}
