package de.htwg.se.scala_risk.controller.actors

import akka.actor.{Actor, Props}
import de.htwg.se.scala_risk.model.World

class WinActor extends Actor {

  val continents: Array[(String, Option[String])] = Array[(String, Option[String])](
    ("Afrika", None),
    ("Nordamerika", None),
    ("Europa", None),
    ("Australien", None),
    ("Südamerika", None),
    ("Asien", None),
  )
  var win: Win = null

  def receive = {
    case WinActor.Init(win, world) => this.init(win, world)
    case WinActor.OwnedContinent(continent, player) => ownedContinent(continent, player)
    case _ => println("WinActor")
  }

  def init(win: Win, world: World): Unit = {
    this.win = win
    world.getContinentList.foreach(continent => {
      context.actorOf(Props[ContinentActor], continent.getName().replaceAll("[äöüÄÖÜß]", "")) ! ContinentActor.Init(continent)
    })
  }

  def ownedContinent(continent: String, player: Option[String]): Unit = {
    player match {
      case p @ Some(_) =>  {
        this.continents(this.continents.zipWithIndex.filter(_._1._1.eq(continent)).map(_._2).apply(0)) = (continent, p)
        if (this.continents.forall(c => c._2.getOrElse("").eq(p.get))) {

          this.win.win(p)
        }
      }
      case None => //println("Continent: %s is not owned!".format(continent))
    }
  }
}

object WinActor {
  case class Init(logic: Win, world: World)
  case class OwnedContinent(country: String, player: Option[String])
}

trait Win {
  def win(player: Option[String])
}
