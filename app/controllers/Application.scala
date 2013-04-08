package controllers

import play.api._
import play.api.mvc._
import model.Document
import service.CommentService
import service.CommentService
import model.Comment
import play.api.libs.json.Json
import app.ApplicationContext._

object Application extends Controller {
  
  def index = Action {
    Ok(views.html.index())
  }
  
  def view(url: String) = Action {
    val document = documentService.find(url)
  	    
    document.map(d =>
      Ok(views.html.document(d))
    ).getOrElse(
      BadRequest("Document with url '" + url + "' not found")
    )
  }
  
  def comments(documentBlockIdentifier: String) = Action {
    val comments = commentService.find(documentBlockIdentifier)   
    val json = Json.toJson(comments)
    Ok(json).as("application/json")
  }
}