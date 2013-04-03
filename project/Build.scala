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
    )

}
