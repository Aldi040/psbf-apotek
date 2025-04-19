package repositories

import javax.inject._
import play.api.db.DBApi
import java.sql.Connection
import models.Supplier

@Singleton
class SupplierRepo @Inject()(dbapi: DBApi) {
  private val db = dbapi.database("default")

  // Mengambil semua supplier
  def getAllSupplier(): Seq[Supplier] = {
    db.withConnection { implicit connection =>
      val stmt = connection.createStatement()
      val rs = stmt.executeQuery("SELECT * FROM supplier")
      val buffer = scala.collection.mutable.ListBuffer[Supplier]()
      while (rs.next()) {
        buffer += Supplier(
          rs.getInt("id_supplier"),
          rs.getString("nama_supplier"),
          Option(rs.getString("alamat_supplier")),
          Option(rs.getString("telepon_supplier")),
          Option(rs.getString("email_supplier"))
        )
      }
      buffer.toList
    }
  }

  // Menyimpan supplier baru
  def insert(s: Supplier): Unit = {
    db.withConnection { implicit connection =>
      val stmt = connection.prepareStatement(
        "INSERT INTO supplier (nama_supplier, alamat_supplier, telepon_supplier, email_supplier) VALUES (?, ?, ?, ?)"
      )
      stmt.setString(1, s.nama)
      stmt.setString(2, s.alamat.getOrElse(null))
      stmt.setString(3, s.telepon.getOrElse(null))
      stmt.setString(4, s.email.getOrElse(null))
      stmt.executeUpdate()
    }
  }

  // Memperbarui data supplier
  def update(s: Supplier): Unit = {
    db.withConnection { implicit connection =>
      val stmt = connection.prepareStatement(
        "UPDATE supplier SET nama_supplier = ?, alamat_supplier = ?, telepon_supplier = ?, email_supplier = ? WHERE id_supplier = ?"
      )
      stmt.setString(1, s.nama)
      stmt.setString(2, s.alamat.getOrElse(null))
      stmt.setString(3, s.telepon.getOrElse(null))
      stmt.setString(4, s.email.getOrElse(null))
      stmt.setInt(5, s.id)
      stmt.executeUpdate()
    }
  }

  // Mencari supplier berdasarkan ID
  def findById(id: Int): Option[Supplier] = {
    db.withConnection { implicit connection =>
      val stmt = connection.prepareStatement("SELECT * FROM supplier WHERE id_supplier = ?")
      stmt.setInt(1, id)
      val rs = stmt.executeQuery()
      if (rs.next()) {
        Some(Supplier(
          rs.getInt("id_supplier"),
          rs.getString("nama_supplier"),
          Option(rs.getString("alamat_supplier")),
          Option(rs.getString("telepon_supplier")),
          Option(rs.getString("email_supplier"))
        ))
      } else None
    }
  }

  // Menghapus supplier berdasarkan ID
  def delete(id: Int): Unit = {
    db.withConnection { implicit connection =>
      val stmt = connection.prepareStatement("DELETE FROM supplier WHERE id_supplier = ?")
      stmt.setInt(1, id)
      stmt.executeUpdate()
    }
  }
}
