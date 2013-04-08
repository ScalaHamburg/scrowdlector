package model

import hashing.TextBlockHash._
import play.api.templates.Html
import eu.henkelmann.actuarius.ActuariusTransformer

object Document{
  type HTMLConverter = String => Html

  def anyToHtml: HTMLConverter = {
    block: String => Html(block)
  }

  def codeToHtml: HTMLConverter = {
    		block: String => Html("<pre>"+block+"</pre>")
  }

  def markdownToHtml: HTMLConverter = {
    block: String => Html(new ActuariusTransformer()(block))
  } 
  
}
case class Document (val rawText: String, val docType:DocumentType) {

  val rawBlocks = breakIntoIdentifiableBlocks(rawText, docType.blockStrategy,  docType.idStrategy)
  
  val blocks=  rawBlocks.map( tpl => (docType.displayStrategy(tpl._1),tpl._2))
  
  
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