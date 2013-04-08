package controllers

import play.api._
import play.api.mvc._
import model.Document
import service.CommentService
import service.CommentService
import model.Comment
import play.api.libs.json.Json
import model.MarkdownText
import model.ScalaCode
import service.DocumentService

object Application extends Controller {
  
  def index = Action {
    Ok(views.html.index())
  }
  
  def view(url: String) = Action {
    // load content from DocumentService by url!
    // TODO what about the trait - do we need to specialize here?
    val docService = new DocumentService(){}
    
    docService.find(url).map{
      d  => Ok(views.html.document(d))
    }.getOrElse(NotFound)

  }
  
  def comments(documentBlockIdentifier: String) = Action {
    // TODO load content from CommentsService by documentBlockIdentifier!
    val comments = List(new Comment("Nice article!", documentBlockIdentifier), new Comment("Really nice article, indeed!!!", documentBlockIdentifier))
    
    val json = Json.toJson(comments)
    Ok(json).as("application/json")
  }
  
} 