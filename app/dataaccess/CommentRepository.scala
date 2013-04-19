package dataaccess

import model.Comment

trait CommentRepository {
  def find(documentBlockIdentifier: String): Seq[Comment]
  
  def add(comment: String, documentBlockIdentifier: String)
}