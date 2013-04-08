package model

import hashing.TextBlockHash._
import play.api.templates.Html
import eu.henkelmann.actuarius.ActuariusTransformer

case class Document (val rawText: String, val docType:DocumentType) {
  
  private type HTMLConverter = String => Html
  
  private def anyToHtml : HTMLConverter = {
    block:String => Html(block)
  } 
  
  private def markdownToHtml : HTMLConverter = {
    block:String => Html(new ActuariusTransformer()(block))
  } 

  val rawBlocks = breakIntoIdentifiableBlocks(rawText, docType.blockStrategy,  docType.idStrategy)
  
  val blocks=  rawBlocks.map( tpl => (Html(new ActuariusTransformer()(tpl._1)),tpl._2))
  
  
  // TODO implement me
  def toJson() = {
    """{
	  		"blocks":
	  		[
	  			{"text": "Hallo Welt\nAbastz1", "hash": "hash1"},
      			{"text": "Absatz 2\nmit mehr Text\nals Abastz1", "hash": "hash2"},
      			{"text": "Absatz 3", "hash": "hash3"}
	  		]
	  }
    """
  }
}