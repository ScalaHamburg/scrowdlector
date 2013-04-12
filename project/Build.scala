import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

    val appName         = "scrowdlector"
    val appVersion      = "0.1"

    val appDependencies = Seq(
      // Add your project dependencies here,
    )
	
	scalaVersion := "2.10.0" 
	
    val main = play.Project(appName, appVersion, appDependencies).settings(
		// Add your own project settings here   
		libraryDependencies ++= Seq("eu.henkelmann" % "actuarius_2.10.0" % "0.2.5" ,
		 "com.typesafe" %% "scalalogging-slf4j" % "1.0.1")
		
    )

}
