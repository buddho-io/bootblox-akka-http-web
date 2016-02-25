import WebKeys._
import JsEngineKeys._

lazy val root = (project in file("."))
  .enablePlugins(SbtTwirl, SbtWeb, GitVersioning, GitBranchPrompt)
  .settings(
    name := "bootblocks-akka-http-web",
    organization := "io.buddho.bootblox",

    git.baseVersion := "1.0",

    resolvers += Resolver.bintrayRepo("buddho", "mvn-public"),

    scalaVersion := "2.11.7",
    scalacOptions ++= Seq("-deprecation"),

    javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint"),

    pipelineStages := Seq(concat, uglify),

    // web settings

    engineType := EngineType.Node,
    (managedClasspath in Runtime) += (packageBin in Assets).value,
    packagePrefix in Assets := "public/",
    Concat.groups := Seq(
      "lib.js" -> group(Seq("lib/jquery/dist/jquery.js")),
      "app.js" -> group(Seq("js/main.js"))
    ),

    libraryDependencies ++=
      Dependencies.configStack ++
      Dependencies.commonStack ++
      Dependencies.akkaStack ++
      Dependencies.loggingStack ++
      Dependencies.webStack ++
      Dependencies.testStack,

    // can not run tests in parallel because of in memory H2 database
    parallelExecution in Test := false,

    licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0")),

    initialize := {
      val _ = initialize.value
      if (sys.props("java.specification.version") != "1.8") {
        sys.error("Java 8 is required for this project.")
      }
    }
  )


