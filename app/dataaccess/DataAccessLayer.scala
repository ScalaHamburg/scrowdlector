package dataaccess

trait DataAccessLayer {
  val documentRepository: DocumentRepository
  val commentRepository: CommentRepository
}