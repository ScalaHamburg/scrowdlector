package app

import service.InMemoryCommentService
import service.InMemoryDocumentService
import service.ServiceComponent

object ApplicationContext extends ServiceComponent {
  val commentService = new InMemoryCommentService
  val documentService = new InMemoryDocumentService
}
