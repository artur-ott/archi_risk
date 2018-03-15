package de.htwg.se.scala_risk.model.impl

import org.scalatest.WordSpec
import de.htwg.se.scala_risk.model.{ Country => TCountry }
import de.htwg.se.scala_risk.model.impl.Colors._
import scala.util.control.Breaks._
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import org.scalatest.Matchers._

@RunWith(classOf[JUnitRunner])
class CountrySpec extends WordSpec {
  val world = new World

  "Two countries" should {
    "not be equal" in {
      world.countries.sibirien.equals("bla") should be(false)
      world.countries.westaustralien.equals(world.countries.ostafrika) should be(false)
    }

    "be equal" in {
      world.countries.ural.equals(world.countries.ural) should be(true)
    }

  }
  "listCountries" should {
    "contain 42 countries" in {
      world.countries.listCountries.length should be(42)
    }
    "contain BRASILIEN" in {
      world.countries.listCountries.contains(world.countries.brasilien) should be(true)
    }
  }

  "Every country in the countryList" should {
    "have a name" in {
      var bool: Boolean = true;
      breakable {
        world.countries.listCountries.foreach { x => if (x.getName == null) { bool = false; break } }
      }
      bool should be(true)
    }
    "have 0 troops" in {
      var bool: Boolean = true;
      breakable {
        world.countries.listCountries.foreach { x => if (x.getTroops != 0) { bool = false; break } }
      }
      bool should be(true)
    }
    "have a set of neighboring countries that is not empty" in {
      var bool: Boolean = true;
      breakable {
        world.countries.listCountries.foreach { x =>
          if (!x.getNeighboringCountries.isInstanceOf[Set[TCountry]] ||
            x.getNeighboringCountries.isEmpty) { bool = false; break }
        }
      }
      bool should be(true)
    }
    "be owned by no player (the default player)" in {
      var bool: Boolean = true;
      breakable {
        world.countries.listCountries.foreach { x => if (!x.getOwner.equals(world.players.Default)) { bool = false; break } }
      }
      bool should be(true)
    }
  }

  "The country ARGENTINIEN" should {
    "have the neighbors: BRASILIEN and PERU" in {
      val neighborsOfArgentinien = world.countries.argentinien.getNeighboringCountries
      neighborsOfArgentinien.contains(world.countries.brasilien) should be(true)
      neighborsOfArgentinien.contains(world.countries.peru) should be(true)
    }

    "have a string representation" in {
      world.countries.brasilien.toString() should not be (null)
    }

    "have a reference color" in {
      world.countries.brasilien.getRefColor() should be < 0
    }

    "have the DefaultPlayer as owner" in {
      world.countries.brasilien.getOwner() should be(world.players.Default)
    }
  }

  "The country BRASILIEN" should {
    "have ARGENTINIEN as its neighbor" in {
      world.countries.brasilien.getNeighboringCountries.contains(world.countries.argentinien) should be(true)
    }
  }

  "After assigning troops to BRASILIEN it" should {
    "contain the troops" in {
      world.countries.brasilien.setTroops(5)
      world.countries.brasilien.getTroops should be(5)
    }
  }

  "When trying to assign a negative number of troops to a country it" should {
    "not affect the number of troops" in {
      val troopsBefore = world.countries.aegypten.getTroops
      world.countries.aegypten.setTroops(-1)
      val troopsAfter = world.countries.aegypten.getTroops
      troopsBefore should be(troopsAfter)
    }
  }

  "After assigning a new owner to a country it" should {
    "have the owner set to the new owner" in {
      world.players.addPlayer("Nico", "RED")
      val player = world.players.playerList(0)
      world.countries.alaska.setOwner(player)
      world.countries.alaska.getOwner should be(player)
    }
  }

  "getOwner" should {
    "return the default player" in {
      world.countries.westaustralien.setOwner(null)
      world.countries.westaustralien.getOwner() should be(world.players.Default)
    }
  }

}