package service

import model.Comment

trait CommentService {
  def find(documentIdentifier: String): Seq[Comment] = {
    List()
  }
}