package service

import model.Document

trait DocumentService {
  def find(url: String): Document
}