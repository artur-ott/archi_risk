package de.htwg.se.scala_risk.controller.actors
import akka.actor.Actor
import de.htwg.se.scala_risk.controller.impl.ModelHttp

class ContinentActor extends Actor{
  def receive = {
    case ContinentActor.Init(continent, world) => checkContinent(continent, world)
    case _ => print("ContinentActor")
  }

  def checkContinent(continent: (String, Int, List[String], String), world: ModelHttp): Unit = {
    val coutries = world.getCountriesList.filter(c => continent._3.contains(c))
    if (coutries.forall(c => c._2.equals(coutries(0)._2))) {
      sender() ! WinActor.OwnedContinent(continent._4, Some(coutries(0)._2))
    } else {
      sender() ! WinActor.OwnedContinent(continent._4, None)
    }
  }
}

object ContinentActor {
  case class Init(continent: (String, Int, List[String], String), world: ModelHttp)
}
