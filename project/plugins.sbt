// Comment to get more information during initialization
logLevel := Level.Warn

// The Typesafe repository 
resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

// Idea Plugin
addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.2.0")

// Use the Play sbt plugin for Play projects
addSbtPlugin("play" % "sbt-plugin" % "2.1.1")

// Typesafe start script plugin, used to start the app on Heroku
addSbtPlugin("com.typesafe.sbt" % "sbt-start-script" % "0.7.0")
