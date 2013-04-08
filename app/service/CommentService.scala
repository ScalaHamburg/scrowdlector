package service

import model.Comment

trait CommentService {
  def find(documentBlockIdentifier: String): Seq[Comment]
}