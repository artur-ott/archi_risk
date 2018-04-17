package de.htwg.se.scala_risk.controller.actors

import akka.actor.Actor

class WinActor extends Actor {

   def receive = {
     case _ => println("hi")
   }

}

object WinActor {
  case class Init()
}
