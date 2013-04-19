package dataaccess

import model.Document

trait DocumentRepository {
  def find(url: String): Option[Document]
}