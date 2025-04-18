package models

import play.api.db._
import anorm._
import anorm.SqlParser._

case class Pelanggan(
  id: Int,
  nama: String,
  jenisKelamin: String
)

object Pelanggan {
  val parser: RowParser[Pelanggan] = {
    get[Int]("ID_PELANGGAN") ~
    get[String]("NAMA_PELANGGAN") ~
    get[String]("JENIS_KELAMIN") map {
      case id ~ nama ~ jk => Pelanggan(id, nama, jk)
    }
  }

  def getAll(implicit db: Database): List[Pelanggan] = {
    db.withConnection { implicit c =>
      SQL"SELECT * FROM pelanggan".as(parser.*)
    }
  }

  def getById(id: Int)(implicit db: Database): Option[Pelanggan] = {
    db.withConnection { implicit c =>
      SQL"SELECT * FROM pelanggan WHERE ID_PELANGGAN = $id".as(parser.singleOpt)
    }
  }

  def create(nama: String, jenisKelamin: String)(implicit db: Database): Option[Long] = {
    db.withConnection { implicit c =>
      SQL"""
        INSERT INTO pelanggan (NAMA_PELANGGAN, JENIS_KELAMIN) 
        VALUES ($nama, $jenisKelamin)
      """.executeInsert()
    }
  }

  def update(id: Int, nama: String, jenisKelamin: String)(implicit db: Database): Int = {
    db.withConnection { implicit c =>
      SQL"""
        UPDATE pelanggan 
        SET NAMA_PELANGGAN = $nama, JENIS_KELAMIN = $jenisKelamin
        WHERE ID_PELANGGAN = $id
      """.executeUpdate()
    }
  }

  def delete(id: Int)(implicit db: Database): Int = {
    db.withConnection { implicit c =>
      SQL"DELETE FROM pelanggan WHERE ID_PELANGGAN = $id".executeUpdate()
    }
  }
}