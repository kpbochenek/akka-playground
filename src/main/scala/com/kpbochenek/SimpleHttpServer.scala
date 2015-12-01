package com.kpbochenek

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.ExecutionContext


object SimpleHttpServer extends App with JsonMarshalingCalculator with LazyLogging {
  implicit val system = ActorSystem("http-system")
  implicit val ec: ExecutionContext = system.dispatcher
  implicit val flowMaterializer = ActorMaterializer()

  val routes = {
    calculatorRoute
  }

  Http().bindAndHandle(routes, "localhost", 8000)
}