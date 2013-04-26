package dataaccess

trait DataAccessLayer {
  def documentRepository: DocumentRepository
  def commentRepository: CommentRepository
}