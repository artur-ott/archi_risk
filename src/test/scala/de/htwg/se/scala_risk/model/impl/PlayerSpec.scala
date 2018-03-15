package de.htwg.se.scala_risk.model.impl
import org.scalatest.WordSpec

import de.htwg.se.scala_risk.model.impl.Colors._
import org.scalatest.Matchers._
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import org.scalatest.WordSpec

@RunWith(classOf[JUnitRunner])
class PlayerSpec extends WordSpec {
  val world = new World

  "A Player" should {
    val player1 = Player("Peter", RED, 3, world)
    "have a name" in {
      player1.name should be("Peter")
      player1.getName() should be("Peter")
    }
    "have a color" in {
      player1.color should be(RED)
      player1.getColor() should be(RED)
    }

    "have 3 troops" in {
      player1.getTroops() should be(3)
    }

    "have more troops when setting to a higher number" in {
      player1.setTroops(10)
      player1.getTroops() should be(10)
    }
  }

  "Two Players" should {
    "be equal" in {
      val player1 = Player("Hans", RED, 0, world)
      val player2 = Player("Hans", RED, 0, world)
      player1.equals(player2) should be(true)

      val player3 = Player("Test", null, 0, world)
      val player4 = Player("Test", null, 0, world)
      player3.equals(player4) should be(true)
    }

  }
  "Two Players" should {
    "not be equal" in {
      val player1 = Player("Hans", BLUE, 0, world)
      val player2 = Player("Hans", RED, 0, world)
      player1.equals(player2) should be(false)

      val player5 = Player("Elfriede", null, 0, world)
      val player6 = Player("Norbert", null, 0, world)
      player5.equals(player6) should be(false)
    }
  }

  "Two players with different instances" should {
    val player1 = Player("Hans", BLUE, 0, world)
    val player2 = "Hans"
    "not be equal" in {
      player1.equals(player2) should be(false)
    }
  }

  "The Default Player" should {
    "have no name" in {
      world.players.Default.name should be("")
    }
    "have no color" in {
      world.players.Default.color should be(null)
    }
  }

  "The playerList after adding two (not equal) players" should {
    val player1 = Player("Hans", RED, 0, world)
    val player2 = Player("Peter", BLUE, 0, world)
    "contain two players" in {
      update()

      world.players.addPlayer("Hans", "RED")
      world.players.addPlayer("Peter", "BLUE")
      world.players.playerList.length should be(2)
      world.players.playerList(0) should be(player1)
      world.players.playerList(1) should be(player2)
    }
  }

  "The colorList after adding two (not equal) players" should {
    "not contain their colors" in {
      world.players.colorList.contains(RED) should be(false)
      world.players.colorList.contains(BLUE) should be(false)
    }
    "still contain the other colors" in {
      world.players.colorList.contains(YELLOW) should be(true)
      world.players.colorList.contains(GREEN) should be(true)
    }
  }

  "The playerList after adding two players with the same color" should {
    val player1 = Player("Julia", GREEN, 0, world)
    val player2 = Player("Anna", GREEN, 0, world)
    "contain only the first player" in {
      update()

      world.players.addPlayer("Julia", "GREEN")
      world.players.addPlayer("Anna", "GREEN")
      world.players.playerList.length should be(1)
      world.players.playerList.contains(player1) should be(true)
    }
    "not contain the second player" in {
      world.players.playerList.contains(player1) should be(true)
      world.players.playerList.contains(player2) should be(false)

    }
  }

  "The colorList after adding two player with the same color" should {
    "not contain this color anymore" in {
      world.players.colorList.contains(GREEN) should be(false)
    }
  }

  "The playerList after adding three players (two with the same color)" should {
    val player1 = Player("Julia", GREEN, 0, world)
    val player2 = Player("Anna", GREEN, 0, world)
    val player3 = Player("Christian", YELLOW, 0, world)
    "contain only the players with different colors" in {
      update()

      world.players.addPlayer("Julia", "GREEN")
      world.players.addPlayer("Anna", "GREEN")
      world.players.addPlayer("Christian", "YELLOW")
      world.players.playerList.length should be(2)
      world.players.playerList.contains(player1) should be(true)
      world.players.playerList.contains(player3) should be(true)
    }
    "not contain the other player with the same color" in {
      world.players.playerList.contains(player2) should be(false)
    }
  }

  "The colorList after adding three players (two with the same color)" should {
    "not contain the taken colors anymore" in {
      world.players.colorList.contains(GREEN) should be(false)
      world.players.colorList.contains(YELLOW) should be(false)
    }

  }

  "The playerList after adding two players with different color spelling" should {
    val player1 = Player("Marcel", RED, 0, world)
    val player2 = Player("Josef", YELLOW, 0, world)
    "accept both players" in {
      update()

      world.players.addPlayer("Marcel", "red")
      world.players.addPlayer("Josef", "yeLLoW")
      world.players.playerList.length should be(2)
      world.players.playerList.contains(player1) should be(true)
      world.players.playerList.contains(player2) should be(true)
    }
  }

  "The colorList after adding two players with different color spelling" should {
    "not contain these colors anymore" in {
      world.players.colorList.contains(RED) should be(false)
      world.players.colorList.contains(YELLOW) should be(false)
    }
  }

  "The playerList after adding a player with a wrong color" should {
    "not contain this player" in {
      update()

      world.players.addPlayer("Michael", "wrongColor!")
      world.players.playerList.length should be(0)
    }
  }

  "The colorList after adding a player with a wrong color" should {
    "still contain all colors" in {
      world.players.colorList.length should be(4)
    }
  }

  "A player after assigning some countries to him it" should {
    "return these countries when asked for his owned countries" in {
      update()

      world.players.addPlayer("Carl", "GREEN")
      world.players.addPlayer("Kunigunde", "BLUE")
      val player = world.players.playerList(0)
      val player2 = world.players.playerList(1)
      val idxcountry1 = 0
      val idxcountry2 = 1
      val idxcountry3 = 2
      val idxcountry4 = 4
      val idxcountry5 = 5
      world.countries.listCountries(idxcountry1).setOwner(player)
      world.countries.listCountries(idxcountry2).setOwner(player)
      world.countries.listCountries(idxcountry3).setOwner(player)
      world.countries.listCountries(idxcountry4).setOwner(player)
      world.countries.listCountries(idxcountry5).setOwner(player2)
      player.getOwnedCountries.contains(world.countries.listCountries(idxcountry1)) should be(true)
      player.getOwnedCountries.contains(world.countries.listCountries(idxcountry2)) should be(true)
      player.getOwnedCountries.contains(world.countries.listCountries(idxcountry3)) should be(true)
      player.getOwnedCountries.contains(world.countries.listCountries(idxcountry4)) should be(true)
      player.getOwnedCountries.contains(world.countries.listCountries(idxcountry5)) should be(false)
      world.countries.listCountries(idxcountry1).getOwner should be(player)
      world.countries.listCountries(idxcountry2).getOwner should be(player)
      world.countries.listCountries(idxcountry3).getOwner should be(player)
      world.countries.listCountries(idxcountry4).getOwner should be(player)
    }
  }

  "After searching an existing player by a valid color (String) it" should {
    "return this player" in {
      update()

      world.players.addPlayer("Artur", "BLUE")
      val returnPlayer = world.players.getPlayerFromColorString("BLUE")
      returnPlayer should be(world.players.playerList(0))
    }
  }

  "After searching an existing player by a valid color with mixed upper-/lowercases it" should {
    "return this player" in {
      update()
      world.players.addPlayer("Sandra", "YELLOW")
      val returnPlayer = world.players.getPlayerFromColorString("yElLOw")
      returnPlayer should be(world.players.playerList(0))
    }
  }

  "After searching a player by an invalid or non existing color it" should {
    "return the Default Player" in {
      update()

      var returnPlayer = world.players.getPlayerFromColorString("BLUE")
      returnPlayer should be(world.players.Default)

      returnPlayer = world.players.getPlayerFromColorString("wrongColor")
      returnPlayer should be(world.players.Default)
    }
  }

  def update() = {
    // Clean playerList and colorList.
    world.players.playerList = scala.collection.mutable.ArrayBuffer()
    world.players.colorList = List(RED, YELLOW, GREEN, BLUE)
  }

  // TODO: Add tests for return values of the addPlayer function!

}