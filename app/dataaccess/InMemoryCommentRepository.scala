package dataaccess

import model.Comment

class InMemoryCommentRepository extends CommentRepository {
  var comments = List(
    new Comment("Nice header!", "-707665951"),
    new Comment("Really nice header, indeed!!!", "-707665951"),
    new Comment("I would suggest \"SCrowdLector\" or even \"S-CrowdLector\" to avoid the people asking WTF is a scrowd ???", "-1462145730"))

  def find(documentBlockIdentifier: String): Seq[Comment] = {
    comments.filter(_.documentBlockIdentifier == documentBlockIdentifier)
  }

  def add(comment: String, documentBlockIdentifier: String) = {
    comments = new Comment(comment, documentBlockIdentifier) :: comments
  }
}
