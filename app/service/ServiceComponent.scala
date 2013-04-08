package service

trait ServiceComponent {
  // TODO Add self typed annotation to Dao layer (if existent...)
  
  val commentService: CommentService
  val documentService: DocumentService
}