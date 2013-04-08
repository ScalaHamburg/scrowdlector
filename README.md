# Scrowdlector

Eine Play-App zum kommentieren von Text-Dateien.

Diese Datei dient aber auch gleich als markdown test.

## Was ist überhaupt das Problem? ##
------------------------------

Ein Wiki ist geeignet zur Darstellung des aktuellen Konsenses. Wenn es Kontroversen gibt, muss oft gegen den "jeder darf editieren" verstoßen werden. Für den Wiki-Inhalt zeichnet niemand verantwortlich. Ein Wiki beruht auf Symmetrie zwischen den Benutzern.

Gelegentlich ist diese Symmetrie nicht gegeben oder nicht gewünscht. Vielleicht soll ein Text entstehen, der einem Autor / einer Gruppe zuordenbar ist, da diese(r) am Ende dafür verantwortlich gemacht wird. Solche Texte schreibt man gegenwärtig meist allein. Das sollte inzwischen besser gehen.

Ein typisches Beispiel ist die Erstellung einer Spezifikation, bei der die Meinung der Autoren normativ ist; Kommentatoren aber essentiell wichtig sind, um den Text verständlich und lesbar zu machen.

Wichtig: Das Prozess-Ziel ist, dass die Kommentare in ihrer Rolle als TODO verschwinden. Entweder werden sie erledigt, oder abgewiesen, oder aber als weiterführende Kommentare [wording?] behalten.

Wichtig: Jemand, der kommentiert, soll sich sicher sein können, dass sein Text nicht unwiederbringlich verschwindet. (Deswegen Datenhaltung mit Git, denn das kann jeder. :)

Unklar: Skaliert das? Ich vermute, dass dieses Modell nur für eine überschaubare Anzahl von Kommentatoren funktioniert.


## Roles, Concepts, Use cases, Processes ##

### Roles ###
#### Author ####
There may be several of them. Autors have the right to edit the main text.
#### Commenter ####
There may be many of them. Commenters, well, comment.

### Concepts ###
#### Text ####
This is what the authors write. Note that is not always best to see a text as a tree.

#### Sniplets ####

Question: Do sniplets have to be contigous? (This makes stuff much easier, but sometimes it does not reflect the text structure.)


## Architectural intentions ##

### Agnostic ###
The only assumption about the content is that it's text files. This affects mainly the text store and the inner link structure.
It is desirable that these text files be human readable in raw form, but thats not part of the spec.
(We may think to use Markdown, Org-mode, and Literate Haskell (or Idris) as a start.)

### Only the text store is specified. ###
The user interface will depend on the kind of texts delivered by it. Diversity is a good thing. Modularity too.

## Layer B: Git based text store ##

## Layers A: User interface ##


## Wish list ##
### RSS feed ###
*** Categories of comments
Authors need a place to specify categories of comments. (Default: Spelling, Formulation, Mistake, Ommission, ..)

# Layer B #

## Architecture Sketch(es) ##

I still ponder two of them:

### (A) ###
One branch to hold the authors text.

One branch to hold the comments. If the authors branch has a directory structure, comment branch might mirror this.

### (B) ###
Comments are held in a dedicated subdirectory like, say, .comments.


## Open topics (annotated with architecture sketch they apply to) ##
### Comment links & branching (A) ###
A comment refers to a point in the text. The text can be branched. Are there any issues with this?

### Is branching the comments sensible? (A) ###
At all?
Must we even do it in parallel to the text?

# Technologische Aspekte #

## Eclipse Projekt erzeugen ##
sbt "eclipse with-source=true"

## Lokalen Server starten ##
sbt run

## show document ##
http://localhost:9000/view/example.md
or
http://localhost:9000/view/example.scala

## Testing Specs with eclipse ##
In order to be able to select your JUnit4 classes, you need to add the output directory of your project to your build path:

- Select your project
- Go to "Build Path / Configure Build Path"
- In the "Libraries" tab, "Add Class Folder"
- In the "Class Folder Selection" dialog, do "Create New Folder", name it testOutput
- In the "New Folder" dialog, select "Advanced" and "Link to folder in the file system"
- Select the output folder of your project by selecting variables.../PROJECT_LOC/.target
- In the "Order and Export" tab, make sure that the newly created alias is placed before the main/scala/test directory
- Refresh your project (F5 on the project's folder)  

An example Specification is the TextBlockHashSpec.
further documentation: http://code.google.com/p/specs/wiki/RunningSpecs#Run_your_specification_with_JUnit4_in_Eclipse

