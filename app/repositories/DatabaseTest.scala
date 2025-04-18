package repositories

import javax.inject.Inject
import play.api.db.Database

class DatabaseTest @Inject()(db: Database) {
  def testConnection(): Boolean = {
    try {
      db.withConnection { conn =>
        val stmt = conn.createStatement()
        val rs = stmt.executeQuery("SELECT 1")
        rs.next()
        true
      }
    } catch {
      case e: Exception => false
    }
  }
}