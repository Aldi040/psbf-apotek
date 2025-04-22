package repositories

import javax.inject._
import play.api.db.DBApi
import java.sql.Connection
import models.Pelanggan

@Singleton
class PelangganRepo @Inject()(dbapi: DBApi) {
  private val db = dbapi.database("default")

  // Mengambil semua data pelanggan
  def getAllPelanggan(): Seq[Pelanggan] = {
    db.withConnection { implicit connection =>
      val stmt = connection.createStatement()
      val rs = stmt.executeQuery("SELECT * FROM pelanggan")
      val buffer = scala.collection.mutable.ListBuffer[Pelanggan]()
      while (rs.next()) {
        buffer += Pelanggan(
          rs.getInt("id_pelanggan"),
          rs.getString("nama_pelanggan"),
          rs.getString("jenis_kelamin")
        )
      }
      buffer.toList
    }
  }

  // Menyimpan pelanggan baru
  def insert(p: Pelanggan): Unit = {
    db.withConnection { implicit connection =>
      val stmt = connection.prepareStatement("INSERT INTO pelanggan (nama_pelanggan, jenis_kelamin) VALUES (?, ?)")
      stmt.setString(1, p.nama)
      stmt.setString(2, p.jenisKelamin)
      stmt.executeUpdate()
    }
  }

  // Memperbarui data pelanggan yang sudah ada
  def update(p: Pelanggan): Unit = {
    db.withConnection { implicit connection =>
      val stmt = connection.prepareStatement(
        "UPDATE pelanggan SET nama_pelanggan = ?, jenis_kelamin = ? WHERE id_pelanggan = ?"
      )
      stmt.setString(1, p.nama)
      stmt.setString(2, p.jenisKelamin)
      stmt.setInt(3, p.id)
      stmt.executeUpdate()
    }
  }

  // Mencari pelanggan berdasarkan ID
  def findById(id: Int): Option[Pelanggan] = {
    db.withConnection { implicit connection =>
      val stmt = connection.prepareStatement("SELECT * FROM pelanggan WHERE id_pelanggan = ?")
      stmt.setInt(1, id)
      val rs = stmt.executeQuery()
      if (rs.next()) {
        Some(Pelanggan(
          rs.getInt("id_pelanggan"),
          rs.getString("nama_pelanggan"),
          rs.getString("jenis_kelamin")
        ))
      } else None
    }
  }

  // Menghapus pelanggan berdasarkan ID
  def delete(id: Int): Unit = {
    db.withConnection { implicit connection =>
      val stmt = connection.prepareStatement("DELETE FROM pelanggan WHERE id_pelanggan = ?")
      stmt.setInt(1, id)
      stmt.executeUpdate()
    }
  }
  def searchByName(keyword: String): List[Pelanggan] = {
    db.withConnection { implicit connection => 
      val stmt = connection.prepareStatement("SELECT * FROM pelanggan WHERE LOWER(nama_pelanggan) LIKE ?")
      stmt.setString(1, "%" + keyword.toLowerCase + "%")
      val rs = stmt.executeQuery()
      val buffer = scala.collection.mutable.ListBuffer[Pelanggan]()
      while (rs.next()) {
        buffer += Pelanggan(
          rs.getInt("id_pelanggan"),
          rs.getString("nama_pelanggan"),
          rs.getString("jenis_kelamin"),
        )
      }
      buffer.toList
    }    
  }  
}
