package nl.soqua.zangka

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives._

trait ZangkaApp {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  sys.addShutdownHook({ system.terminate() })

  val route =
    path("") {
      getFromResource("web/index.html")
    }

  Http().bindAndHandle(route, "localhost", 8080)
}

object Main extends App with ZangkaApp {

}
