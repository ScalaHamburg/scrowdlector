package controllers

import play.api._
import play.api.mvc._
import model.Document

object Application extends Controller {
  
  def index = Action {
    Ok(views.html.index())
  }
  
  def view(url: String) = Action {
    // TODO load content from DocumentService by url!
    
    val document = new Document(
"""
# Scrowdlectorl

Eine Play-App zum kommentiewren von Text-Dateien.
Diese Datei dient aber auch gleich als markdown test.

## Was ist überhaupt das Problem?
------------------------------

Ein Wiki ist geeignet zur Darstellung des aktuellen Konsenses. Wenn es Kontroversen gibt, muss oft gegen den "jeder darf editieren" verstoßen werden. nhalt zeichnet niemand verantwortlich. Ein Wiki beruht auf Symmetrie zwischen den Benutzern.
Gelegentlich ist diese Symmetrie nicht gegeben oder nicht gewünscht. Vielleicht soll ein Text entstehen, der einem Autor / einer Gruppe zuordenbar ist, da diese(r) am Ende dafür verantwortlich gemacht wird. Solche Texte schreibt man gegenwärtig meist allein. Das sollte inzwischen besser gehen.

Ein typisches Beispiel ist die Erstellung einer Spezifikation, bei der die Meinung der Autoren normativ ist; Kommentatoren aber essentiell wichtig sind, um den Text verständlich und lesbar zu machen.

## Roles, Concepts, Use cases, Processes

### Roles
#### Author
There may be several of them. Autors have the right to edit the main text.
#### Commenter
There may be many of them. Commenters, well, comment.
""")
    println(document)
    println(document.blocks)
    Ok(views.html.document(document))
  }
  
}