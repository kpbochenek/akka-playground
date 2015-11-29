package com.kpbochenek

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives
import akka.stream.ActorMaterializer
import spray.json.DefaultJsonProtocol

import scala.concurrent.{ExecutionContext, Future}


case class CalculatorRequest(id: String, a: Int, b: Int, op: String)
case class CalculatorResponse(id: String, value: Option[Int])


trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val requestFormat = jsonFormat4(CalculatorRequest)
  implicit val responseFormat = jsonFormat2(CalculatorResponse)
}


class SimpleHttpServer(implicit val ec: ExecutionContext) extends Directives with JsonSupport {

  def parseIpAddress(ip: String): Future[Either[String, String]] = Future {
    // pretend to actually do something
    if (ip.count(_ == '.') == 3 && ip.length >= 7 && ip.length < 16) Right("Correct IP! " + ip)
    else Left("Failed to parse IP " + ip)
  }

  def calculate(task: CalculatorRequest): Future[CalculatorResponse] = Future {
    task.op match {
      case "+" => CalculatorResponse(task.id, Some(task.a + task.b))
      case "-" => CalculatorResponse(task.id, Some(task.a - task.b))
      case "*" => CalculatorResponse(task.id, Some(task.a * task.b))
      case "/" =>
        if (task.b != 0) CalculatorResponse(task.id, Some(task.a / task.b))
        else CalculatorResponse(task.id, None)
    }
  }

  val routes = {
    logRequestResult("akka-http-microservice") {
      pathPrefix("ip") {
        (get & path(Segment)) { ip =>
          complete {
            parseIpAddress(ip).map[ToResponseMarshallable] {
              case Right(ipInfo) => ipInfo
              case Left(errorMessage) => BadRequest -> errorMessage
            }
          }
        }
      } ~
        (get & path("info")) { ctx =>
          ctx.complete(ctx.request.toString)
        } ~
        (post & path("json")) {
          entity(as[CalculatorRequest]) { simpleJson =>
            complete {
              calculate(simpleJson)
            }
          }
        }
    }
  }
}


object SimpleHttpServerMain extends App with JsonSupport {
  implicit val system = ActorSystem("http-system")
  implicit val ec = system.dispatcher
  implicit val flowMaterializer = ActorMaterializer()

  val logger = Logging(system, getClass)

  val server = new SimpleHttpServer()

  println("START")

  Http().bindAndHandle(server.routes, "localhost", 8888)

  println("END ASYNC")

  //  val handler = Sink.foreach[Http.IncomingConnection] { conn =>
  //    println("Client connected from: " + conn.remoteAddress)
  ////    conn handleWith Flow[HttpRequest, HttpResponse, Unit]
  //  }

  //val connections = Http().bind("localhost", 8888)
  //val binding = connections.to(handler).run()

//  binding.onComplete {
//    case Success(b) =>
//      println("Server started, listening on: " + b.localAddress)
//    case Failure(e) =>
//      println(s"Server could not bind: ${e.getMessage}")
//      system.shutdown()
//  }

}