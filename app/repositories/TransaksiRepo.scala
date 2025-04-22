package repositories

import javax.inject._
import play.api.db.DBApi
import java.sql.{Connection, Date}
import java.time.LocalDate
import models.Transaksi

@Singleton
class TransaksiRepo @Inject()(dbapi: DBApi) {
  private val db = dbapi.database("default")

  // Mengambil semua data transaksi
 def getAllTransaksi(): List[Transaksi] = {
  db.withConnection { implicit connection =>
    val stmt = connection.createStatement()
    val rs = stmt.executeQuery("SELECT * FROM transaksi")
    val buffer = scala.collection.mutable.ListBuffer[Transaksi]()
    while (rs.next()) {
      buffer += Transaksi(
        Some(rs.getInt("id_transaksi")),
        Option(rs.getObject("id_pelanggan")).map(_.asInstanceOf[Int]),
        rs.getDate("tanggal_transaksi").toLocalDate,
        rs.getBigDecimal("total_harga"),
        rs.getString("metode_pembayaran")
      )
    }
    buffer.toList  // memastikan tipe data yang dikembalikan adalah List
  }
}


  // Menyimpan transaksi baru
  def insert(t: Transaksi): Unit = {
    db.withConnection { implicit connection =>
      val stmt = connection.prepareStatement(
        "INSERT INTO transaksi (id_pelanggan, tanggal_transaksi, total_harga, metode_pembayaran) VALUES (?, ?, ?, ?)"
      )

      t.idPelanggan match {
        case Some(id) => stmt.setInt(1, id)
        case None => stmt.setNull(1, java.sql.Types.INTEGER)
      }

      stmt.setDate(2, Date.valueOf(t.tanggalTransaksi))
      stmt.setBigDecimal(3, t.totalHarga.bigDecimal) // Pastikan BigDecimal diset dengan benar
      stmt.setString(4, t.metodePembayaran)
      stmt.executeUpdate()
    }
  }

  // Memperbarui transaksi yang sudah ada
  def update(t: Transaksi): Unit = {
    db.withConnection { implicit connection =>
      val stmt = connection.prepareStatement(
        "UPDATE transaksi SET id_pelanggan = ?, tanggal_transaksi = ?, total_harga = ?, metode_pembayaran = ? WHERE id_transaksi = ?"
      )

      t.idPelanggan match {
        case Some(id) => stmt.setInt(1, id)
        case None => stmt.setNull(1, java.sql.Types.INTEGER)
      }

      stmt.setDate(2, Date.valueOf(t.tanggalTransaksi))
      stmt.setBigDecimal(3, t.totalHarga.bigDecimal)
      stmt.setString(4, t.metodePembayaran)
      stmt.setInt(5, t.idTransaksi.getOrElse(throw new IllegalArgumentException("ID transaksi harus ada untuk update")))
      stmt.executeUpdate()
    }
  }

  // Mencari transaksi berdasarkan ID
  def findById(id: Int): Option[Transaksi] = {
    db.withConnection { implicit connection =>
      val stmt = connection.prepareStatement("SELECT * FROM transaksi WHERE id_transaksi = ?")
      stmt.setInt(1, id)
      val rs = stmt.executeQuery()
      if (rs.next()) {
        Some(Transaksi(
          Some(rs.getInt("id_transaksi")),
          Option(rs.getObject("id_pelanggan")).map(_.asInstanceOf[Int]),
          rs.getDate("tanggal_transaksi").toLocalDate,
          rs.getBigDecimal("total_harga"),
          rs.getString("metode_pembayaran")
        ))
      } else None
    }
  }

  // Menghapus transaksi berdasarkan ID
  def delete(id: Int): Unit = {
    db.withConnection { implicit connection =>
      val stmt = connection.prepareStatement("DELETE FROM transaksi WHERE id_transaksi = ?")
      stmt.setInt(1, id)
      stmt.executeUpdate()
    }
  }
}
