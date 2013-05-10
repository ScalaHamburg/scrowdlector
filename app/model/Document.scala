package model

import hashing.TextBlockHash._
import play.api.templates.Html
import eu.henkelmann.actuarius.ActuariusTransformer
import java.util.concurrent.atomic.AtomicInteger


object Document {
  type HTMLConverter = String => Html

  def anyToHtml: HTMLConverter = {
    block: String => Html(block)
  }

  val counter = new AtomicInteger(1)
  
  def codeToHtml: HTMLConverter = {
    block: String => 
      val code = Html("<textarea id=\"code"+counter+"\" name=\"code"+counter+"\">" + block + "</textarea>" +
    		"""
    <script>
      var editor = CodeMirror.fromTextArea(document.getElementById("code"""+counter+""""), {
        lineNumbers: false,
        matchBrackets: true,
        theme: "eclipse",
        mode: "text/x-scala"
      });
    </script>     
    """)
    counter.set(counter.get() + block.count(_=='\n'))
    code
  }

  def markdownToHtml: HTMLConverter = {
    block: String => Html(new ActuariusTransformer()(block))
  }

}
case class Document(val rawText: String, val docType: DocumentType) {

  val rawBlocks = breakIntoIdentifiableBlocks(rawText, docType.blockStrategy, docType.idStrategy)

  val blocks = rawBlocks.map(tpl => (docType.displayStrategy(tpl._1), tpl._2))


  
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