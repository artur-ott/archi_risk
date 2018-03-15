package de.htwg.se.scala_risk.model.impl

import org.scalatest.WordSpec
import de.htwg.se.scala_risk.model.{ Country => TCountry }
import de.htwg.se.scala_risk.model.impl.Colors._
import scala.util.control.Breaks._
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import org.scalatest.Matchers._

@RunWith(classOf[JUnitRunner])
class WorldSpec extends WordSpec {
  val world = new World
  "getContinentList" should {
    "return 6 continents" in {
      world.getContinentList.length should be(6)
    }
  }

  "getPlayerColorList" should {
    world.addPlayer("Jörg", "PINK")
    world.addPlayer("Bettina", "ORANGE")
    "not contain the colors of the added players" in {
      println(world.getPlayerColorList)
      world.getPlayerColorList.contains(PINK) should be(false)
      world.getPlayerColorList.contains(ORANGE) should be(false)
    }
  }

  "getCurrentPlayerIndex" should {
    "return the index of the current player" in {
      world.getCurrentPlayerIndex should be(-1)
    }
  }

  "getPlayerList" should {
    println(world.getPlayerList)
    "return the list of the current players" in {
      world.getPlayerList.contains(new Player("Jörg", PINK, 3, world))
      world.getPlayerList.contains(new Player("Bettina", ORANGE, 3, world))
    }
  }

  "nextPlayer" should {
    "return the next player" in {
      world.nextPlayer.getName should be("Jörg")
    }
  }

  "getColor" should {
    "return the reference color of a country" in {
      world.countries.getColor("alaska") should be(-3993841)
    }
  }

}