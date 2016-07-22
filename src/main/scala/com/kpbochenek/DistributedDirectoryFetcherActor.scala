package com.kpbochenek

import akka.actor.{Actor, ActorContext, ActorRef, ActorRefFactory, ActorSystem, Props}
import akka.http.scaladsl.server.Directives
import akka.util.Timeout
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._

/** Created by kpbochenek on 7/22/16. */


class DirectoryActor extends Actor {
  import sys.process._

  override def receive: Receive = {
    case Some(path) =>
      val command: String = "ls -al " + path
      val result: String = (command !!)
      sender() ! result
    case None =>
      sender() ! ""
  }
}


trait DistributedDirectoryFetcherActor extends Directives with LazyLogging {
  import akka.pattern.ask

  implicit val ec: ExecutionContext
  implicit val system: ActorSystem

  implicit val timeout: Timeout = 2.seconds

  val distributedRoute =
    path("directory") {
      parameters('dir.?, 'dir2.?, 'dir3.?) { (dir, dir2, dir3) =>
        get {
          complete(computeLs(dir, dir2, dir3))
        }
      }
  }

  def computeLs(dir: Option[String], dir2: Option[String], dir3: Option[String]): Future[String] = {
    val f1 = (system.actorOf(Props[DirectoryActor]) ? dir).mapTo[String]
    val f2 = (system.actorOf(Props[DirectoryActor]) ? dir2).mapTo[String]
    val f3 = (system.actorOf(Props[DirectoryActor]) ? dir3).mapTo[String]

    Future.sequence(List(f1, f2, f3)).map(_.mkString("\n----------------\n"))
  }
}
