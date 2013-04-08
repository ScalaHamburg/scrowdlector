package service

import model.Comment

trait CommentService {
  def find(documentBlockIdentifier: String): Seq[Comment]
  
  def add(comment: String, documentBlockIdentifier: String)
}