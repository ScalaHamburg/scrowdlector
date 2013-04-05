package model

import hashing.TextBlockHash._
import play.api.templates.Html
import eu.henkelmann.actuarius.ActuariusTransformer

case class Document (val rawText: String) {
  val rawBlocks = blockify(rawText).map(hashBlock(_))
  
  val transformer = new ActuariusTransformer()
  
  val blocks=  rawBlocks.map( tpl => (Html(transformer(tpl._1)),tpl._2))
  
  
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