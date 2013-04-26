package app

import dataaccess.DataAccessLayer
import dataaccess.InMemoryDocumentRepository
import dataaccess.InMemoryCommentRepository
import service.DocumentService
import service.DocumentComponent
import dataaccess.HTTPDocumentRepository
import dataaccess.DelegatingDocumentRepository

object ApplicationContext extends DocumentComponent with DataAccessLayer {
  
  val documentService = new DefaultDocumentService
  
  val documentRepository = new DelegatingDocumentRepository //InMemoryDocumentRepository
  val commentRepository = new InMemoryCommentRepository
  
}
