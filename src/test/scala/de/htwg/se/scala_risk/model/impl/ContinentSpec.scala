package de.htwg.se.scala_risk.model.impl

import org.scalatest.WordSpec
import de.htwg.se.scala_risk.model.{ Country => TCountry }
import de.htwg.se.scala_risk.model.impl.Colors._
import scala.util.control.Breaks._
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import org.scalatest.Matchers._
import scala.collection._

@RunWith(classOf[JUnitRunner])
class ContinentSpec extends WordSpec {
  val world = new World
  val afrika = world.countries.afrika
  "toString" should {
    "return a string representation of the country" in {
      afrika.toString() should not be (null)
    }
  }
  "listContinents" should {
    "contain 6 continents" in {
      world.countries.listContinents.length should be(6)
    }
    "contain AFRIKA" in {
      world.countries.listContinents.contains(world.countries.afrika)
    }
  }

  "The continent AFRIKA" should {
    "have a name" in {
      afrika.getName().toUpperCase() should be("AFRIKA")
    }
    "have 3 bonusTroops" in {
      afrika.getBonusTroops() should be(3)
    }
  }

  "After one player owns all countries of AFRIKA it" should {
    "be owned by this player" in {
      world.players.addPlayer("Jasmine", "RED")
      afrika.getIncludedCountries.foreach { x => x.setOwner(world.players.playerList(0)) }
      afrika.getOwner() should be(world.players.playerList(0))

    }
  }

  "After a player owning a continent loses one country of it,it" should {
    "not be the owner of the continent anymore" in {
      update()

      world.players.addPlayer("Rick", "YELLOW")
      world.players.addPlayer("Rebecca", "BLUE")
      afrika.getIncludedCountries.foreach { x => x.setOwner(world.players.playerList(0)) }
      afrika.getOwner() should be(world.players.playerList(0))

      afrika.getIncludedCountries().head.setOwner(world.players.playerList(1))
      afrika.getOwner() should be(world.players.Default)
      afrika.getOwner().getName == "Rick" should be(false)
    }
  }

  "allOwnedByOne" should {
    "return true" in {
      update()

    }
  }

  def update() = {
    // Clean playerList and colorList.
    world.players.playerList = scala.collection.mutable.ArrayBuffer()
    world.players.colorList = List(RED, YELLOW, GREEN, BLUE, PINK, ORANGE)
  }
}