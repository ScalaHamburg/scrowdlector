package app

import dataaccess.DataAccessLayer
import dataaccess.InMemoryDocumentRepository
import dataaccess.InMemoryCommentRepository
import service.DocumentService
import service.DocumentComponent

object ApplicationContext extends DocumentComponent with DataAccessLayer {
  
  val documentService = new DefaultDocumentService()
  
  val documentRepository = new InMemoryDocumentRepository
  val commentRepository = new InMemoryCommentRepository()
  
}
