package dataaccess

import model.Comment

class InMemoryCommentRepository extends CommentRepository {
  var comments = List(new Comment("Nice header!", "-707665951"), new Comment("Really nice header, indeed!!!", "-707665951"))

  def find(documentBlockIdentifier: String): Seq[Comment] = {
    comments.filter(_.documentBlockIdentifier == documentBlockIdentifier)
  }

  def add(comment: String, documentBlockIdentifier: String) = {
    comments = new Comment(comment, documentBlockIdentifier) :: comments
  }
}
