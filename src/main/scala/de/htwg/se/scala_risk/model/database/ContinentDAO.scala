package de.htwg.se.scala_risk.model.database

import slick.jdbc.H2Profile.api._

class ContinentDAO(tag: Tag) extends Table[(Int, String, Int, Int, Int)] (tag, "Continent"){
  def id = column[Int]("CONTINENT_ID", O.PrimaryKey, O.AutoInc)
  def name = column[String]("NAME")
  def numberCountries = column[Int]("NumberContries") // should be changed to countries: ArrayBuffer[TCountry]
  def bonusTroops = column[Int]("BONUS_TROOPS")
  def world = column[Int]("world") // should be changed later to world: TWorld
  def * = (id, name, numberCountries, bonusTroops, world)

}
