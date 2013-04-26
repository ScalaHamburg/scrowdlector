package dataaccess

import java.net.URL
import scala.io.Source
import model.Document
import model.Default

class DelegatingDocumentRepository extends DocumentRepository {

  val HTTP = "http(.*)".r

  val httpRepo = new HTTPDocumentRepository
  val memRepo = new InMemoryDocumentRepository

  def find(url: String): Option[Document] = {
    url match {
      case HTTP(rest) => httpRepo.find(url)
      case _ => memRepo.find(url)
    }

  }
}