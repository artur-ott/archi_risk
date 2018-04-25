package de.htwg.se.scala_risk.controller.actors
import akka.actor.Actor
import de.htwg.se.scala_risk.model.Continent
import de.htwg.se.scala_risk.model.World

class ContinentActor extends Actor{
  def receive = {
    case ContinentActor.Init(continent) => checkContinent(continent)
    case _ => print("ContinentActor")
  }

  def checkContinent(continent: Continent): Unit = {
    if (continent.getIncludedCountries().forall(c => c.getOwner.equals(continent.getIncludedCountries()(0).getOwner))) {
      sender() ! WinActor.OwnedContinent(continent.getName(), Some(continent.getIncludedCountries()(0).getOwner.getName))
    } else {
      sender() ! WinActor.OwnedContinent(continent.getName(), None)
    }
  }
}

object ContinentActor {
  case class Init(continent: Continent)
}
