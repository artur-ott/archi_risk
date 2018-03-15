package de.htwg.se.scala_risk
import de.htwg.se.scala_risk.model.World
import de.htwg.se.scala_risk.model.impl.{ World => ImplWorld }

class WorldFactory {
  def getWorld() : World = {
    return new ImplWorld
  }
}