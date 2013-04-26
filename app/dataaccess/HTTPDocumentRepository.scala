package dataaccess

import java.net.URL
import scala.io.Source
import model.Document
import model.Default

class HTTPDocumentRepository extends DocumentRepository {

  def find(url: String): Option[Document] = {
    val source = Source.fromURL(new URL(url))
    
    if (source.hasNext) {
      val text = source.getLines.reduceLeft((s, tring) => s + "\n" + tring)
      Some(new Document(text, Default))
    } else {
      None
    }

  }
}