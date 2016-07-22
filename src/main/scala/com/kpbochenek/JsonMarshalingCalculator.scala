package com.kpbochenek

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives
import com.typesafe.scalalogging.LazyLogging
import spray.json.{JsString, JsValue, RootJsonFormat, DefaultJsonProtocol}

import scala.concurrent.ExecutionContext


sealed trait Operator
object Operator {
  case object plus extends Operator
  case object minus extends Operator
  case object mul extends Operator
  case object div extends Operator

}

case class CalculatorRequest(id: String, a: Int, b: Int, op: Operator)
case class CalculatorResponse(id: String, value: Option[Int])


trait SimpleCalculator {
  def calculate(request: CalculatorRequest): CalculatorResponse = {
    val value = request.op match {
      case Operator.plus => Some(request.a + request.b)
      case Operator.minus => Some(request.a - request.b)
      case Operator.mul => Some(request.a * request.b)
      case Operator.div =>
        if (request.b != 0) Some(request.a / request.b)
        else None
    }
    CalculatorResponse(request.id, value)
  }
}


trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit object OperatorJsonFormat extends RootJsonFormat[Operator] {
    override def read(json: JsValue): Operator = json match {
      case JsString("+") => Operator.plus
      case JsString("-") => Operator.minus
      case JsString("*") => Operator.mul
      case JsString("/") => Operator.div
      case other => throw new UnsupportedOperationException(other.toString)
    }

    override def write(operator: Operator): JsValue = operator match {
      case Operator.plus => JsString("+")
      case Operator.minus => JsString("+")
      case Operator.mul => JsString("+")
      case Operator.div => JsString("+")
    }
  }

  implicit val requestFormat = jsonFormat4(CalculatorRequest)
  implicit val responseFormat = jsonFormat2(CalculatorResponse)
}


trait JsonMarshalingCalculator extends Directives with JsonSupport with SimpleCalculator with LazyLogging {
  logger.info("Created instance of JsonMarshalingCalculator")

  implicit val ec: ExecutionContext

  val calculatorRoute =
    path("calculate") {
      post {
        entity(as[CalculatorRequest]) { simpleJson =>
          complete {
            calculate(simpleJson)
          }
        }
      }
    }
}
