package com.kpbochenek

import akka.actor.{ActorLogging, ActorSystem, Props}
import akka.persistence.{PersistentActor, PersistentView, RecoveryCompleted, Update}

/** Created by kpbochenek on 8/3/16. */


class MyActor extends PersistentActor with ActorLogging {
  override def receiveRecover: Receive = {
    case RecoveryCompleted => log.info("RECOVERED")
    case x: String => log.info(s"recover with $x")
  }

  override def receiveCommand: Receive = {
    case msg: String => persist(msg) { evt =>
      log.info(s"persisted! $msg")
    }
  }

  override def persistenceId: String = "MyID"
}

class MyView extends PersistentView with ActorLogging {
  override def viewId: String = "ViewID"

  override def persistenceId: String = "MyID"

  override def receive: Receive = {
    case m => log.info(s"VIEW READ $m")
  }
}


object MyPersistentView {

  def main(args: Array[String]): Unit = {
    val system: ActorSystem = ActorSystem("pw")

    val actor = system.actorOf(Props[MyActor])

    val view = system.actorOf(Props[MyView])

    actor ! "AAAAAAAA"
    actor ! "11111111"

//    view ! Update.create(await = false)

    Thread.sleep(2000)

    view ! Update(await = false)
    Thread.sleep(6000)

//    view ! Update.create(await = false)

    actor ! "BBBBBBBBBBB"
    Thread.sleep(2000)

    view ! Update(await = false)

    actor ! "CCCCCCCCCCC"

    Thread.sleep(9000)

    system.terminate()
  }
}
