package de.htwg.se.scala_risk.model.database

import slick.jdbc.H2Profile.api._

class CountryDAO(tag: Tag) extends Table[(Int, String, Int, Int, Int, Int, Int)] (tag, "Country"){
  def id = column[Int]("COUNTRY_ID", O.PrimaryKey, O.AutoInc)
  def name = column[String]("NAME")
  def world = column[Int]("world") // should be changed to world: TWorld
  def neighboring_countries = column[Int]("BONUS_TROOPS") // should be chanhed to var neighboring_countries: Set[TCountry]
  def troops = column[Int]("troops")
  def owner = column[Int] ("owner") // should be changed to owner: TPlayer = null
  def color = column[Int] ("color")
  def * = (id, name, world, neighboring_countries, troops, owner, color)

}
