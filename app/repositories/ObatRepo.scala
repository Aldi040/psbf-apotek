package repositories

import javax.inject._
import play.api.db.DBApi
import java.sql.Connection
import models.Obat

@Singleton
class ObatRepo @Inject()(dbapi: DBApi) {
  private val db = dbapi.database("default")


  def getAllObat(): Seq[Obat] = {
    db.withConnection { implicit connection =>
      val stmt = connection.createStatement()
      val rs = stmt.executeQuery("SELECT * FROM obat")
      val buffer = scala.collection.mutable.ListBuffer[Obat]()
      while (rs.next()) {
        buffer += Obat(
          rs.getInt("id_obat"),
          rs.getString("nama_obat"),
          Option(rs.getString("kategori")),
          Option(rs.getString("keterangan")),
          Option(rs.getInt("jumlah_stock")),
          Option(rs.getDouble("harga")),
          Option(rs.getDate("exp")),
          Option(rs.getInt("id_supplier")),
        )
      }
      buffer.toList
    }
  }


  def insert(o: Obat): Unit = {
    db.withConnection { implicit connection =>
      val stmt = connection.prepareStatement("INSERT INTO obat (nama_obat, kategori, keterangan, jumlah_stock, harga, exp, id_supplier) VALUES (?, ?, ?, ?, ?, ?, ?)")
      stmt.setString(1, o.nama_obat)
      stmt.setString(2, o.kategori.getOrElse(null))
      stmt.setString(3, o.keterangan.getOrElse(null))
      
      o.jumlah_stock match {
        case Some(value) => stmt.setInt(4, value)
        case None        => stmt.setNull(4, java.sql.Types.INTEGER)
      }

      o.harga match {
        case Some(value) => stmt.setDouble(5, value)
        case None        => stmt.setNull(5, java.sql.Types.DOUBLE)
      }

      o.exp match {
        case Some(value) => stmt.setDate(6, new java.sql.Date(value.getTime))
        case None        => stmt.setNull(6, java.sql.Types.DATE)
      }

      o.id_supplier match {
        case Some(value) => stmt.setInt(7, value)
        case None        => stmt.setNull(7, java.sql.Types.INTEGER)
      }

      stmt.executeUpdate()
    }
  }


  def update(o: Obat): Unit = {
    db.withConnection { implicit connection =>
      val stmt = connection.prepareStatement(
        "UPDATE obat SET nama_obat = ?, kategori = ?, keterangan = ?, jumlah_stock = ?, harga = ?, exp = ?, id_supplier = ? WHERE id_obat = ?"
      )
      stmt.setString(1, o.nama_obat)
      stmt.setString(2, o.kategori.getOrElse(null))
      stmt.setString(3, o.keterangan.getOrElse(null))
      
      o.jumlah_stock match {
        case Some(value) => stmt.setInt(4, value)
        case None        => stmt.setNull(4, java.sql.Types.INTEGER)
      }

      o.harga match {
        case Some(value) => stmt.setDouble(5, value)
        case None        => stmt.setNull(5, java.sql.Types.DOUBLE)
      }

      o.exp match {
        case Some(value) => stmt.setDate(6, new java.sql.Date(value.getTime))
        case None        => stmt.setNull(6, java.sql.Types.DATE)
      }

      o.id_supplier match {
        case Some(value) => stmt.setInt(7, value)
        case None        => stmt.setNull(7, java.sql.Types.INTEGER)
      }
      stmt.setInt(8, o.id)

      stmt.executeUpdate()
    }
  }


  def findById(id: Int): Option[Obat] = {
    db.withConnection { implicit connection =>
      val stmt = connection.prepareStatement("SELECT * FROM obat WHERE id_obat = ?")
      stmt.setInt(1, id)
      val rs = stmt.executeQuery()
      if (rs.next()) {
        Some(Obat(
          rs.getInt("id_obat"),
          rs.getString("nama_obat"),
          Option(rs.getString("kategori")),
          Option(rs.getString("keterangan")),
          Option(rs.getInt("jumlah_stock")),
          Option(rs.getDouble("harga")),
          Option(rs.getDate("exp")),
          Option(rs.getInt("id_supplier")),
        ))
      } else None
    }
  }

  def searching(keyword: String): List[Obat] = {
  db.withConnection { implicit connection => 
    val stmt = connection.prepareStatement("SELECT * FROM obat WHERE LOWER(nama_obat) LIKE ?")
    stmt.setString(1, "%" + keyword.toLowerCase + "%")
    val rs = stmt.executeQuery()
    val buffer = scala.collection.mutable.ListBuffer[Obat]()
    
    while (rs.next()) {
      buffer += Obat(
          rs.getInt("id_obat"),
          rs.getString("nama_obat"),
          Option(rs.getString("kategori")),
          Option(rs.getString("keterangan")),
          Option(rs.getInt("jumlah_stock")),
          Option(rs.getDouble("harga")),
          Option(rs.getDate("exp")),
          Option(rs.getInt("id_supplier")),  
      )
    }
    buffer.toList
  }
}

  def delete(id: Int): Unit = {
    db.withConnection { implicit connection =>
      val stmt = connection.prepareStatement("DELETE FROM obat WHERE id_obat = ?")
      stmt.setInt(1, id)
      stmt.executeUpdate()
    }
  }
}
