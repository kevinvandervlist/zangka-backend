package nl.soqua.zangka

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{StatusCodes, HttpRequest}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.scaladsl.{Sink, Source}
import org.scalatest._
import org.scalatest.concurrent._
import akka.stream.ActorMaterializer
import scala.concurrent.duration._

import scala.concurrent.Await
import scala.language.postfixOps

class ZangkaAppSpec extends FlatSpec with Matchers with ScalaFutures with BeforeAndAfterAll {

  implicit val testSystem = ActorSystem("test-system")

  import testSystem.dispatcher

  implicit val fm = ActorMaterializer()
  val server = new ZangkaApp {}

  override def afterAll() = testSystem.terminate()

  def sendRequest(req: HttpRequest) = Source.single(req).via(
    Http().outgoingConnection(host = "localhost", port = 8080)
  ).runWith(Sink.head)

  val warmup = sendRequest(HttpRequest())
  Await.result(warmup, 1000 millis)

  "The app" should "return index.html on a GET to /" in {
    whenReady(sendRequest(HttpRequest())) { response =>
      whenReady(Unmarshal(response.entity).to[String]) { str =>
        str should include("Hello World!")
      }
    }
  }

  "The app" should "return 404 on a GET to /foo" in {
    whenReady(sendRequest(HttpRequest(uri = "/foo"))) { response =>
      response.status shouldBe StatusCodes.NotFound
    }
  }
}