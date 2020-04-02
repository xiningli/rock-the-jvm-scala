name := "rock-the-jvm-scala"

version := "0.1"
resolvers ++= Seq(
  "Typesafe" at "http://repo.typesafe.com/typesafe/releases/",
  "Maven" at "https://repo1.maven.org/maven2/",
)

scalaVersion := "2.13.0"
libraryDependencies += "org.scala-lang" % "scala-compiler" % "2.13.1"
//libraryDependencies += "com.twitter" %% "util-eval" % "6.40.0"
libraryDependencies += "org.scala-lang" % "scala-reflect" % "2.13.1"

retrieveManaged := true

updateOptions := updateOptions.value.withCachedResolution(true)
XitrumPackage.copy("dirToCopy", "fileToCopy")
