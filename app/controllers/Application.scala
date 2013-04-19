package controllers

import play.api._
import play.api.mvc._
import model.Document
import model.Comment
import play.api.libs.json.Json
import app.ApplicationContext._
import com.typesafe.scalalogging.slf4j.Logger
import com.typesafe.scalalogging.slf4j.Logging

object Application extends Controller with Logging {
  
  def index = Action {
    Ok(views.html.index())
  }
  
  def view(url: String) = Action {
    val document = documentService.find(url)
    
  	logger.info("Loading Document from URL: {}", url)
    
  	document.map(d =>
      Ok(views.html.document(d))
    ).getOrElse(
      BadRequest("Document with url '" + url + "' not found")
    )
  }
  
  def comments(documentBlockIdentifier: String) = Action {
    val comments = commentRepository.find(documentBlockIdentifier)   
    val json = Json.toJson(comments)
    Ok(json).as("application/json")
  }
}