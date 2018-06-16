package de.htwg.se.scala_risk.model.database

import slick.jdbc.H2Profile.api._

class CountryDAO(tag: Tag) extends Table[(Int, String, Int, Int, String, Int, Int)] (tag, "Country"){
  def id = column[Int]("COUNTRY_ID", O.PrimaryKey, O.AutoInc)
  def name = column[String]("NAME")
  def troops = column[Int]("troops")
  def color = column[Int] ("color")
  def owner = column[String] ("owner")
  def world = column[Int]("world_id")
  def neighboring_id = column[Int]("neighboring_id", O.AutoInc)
  def * = (id, name, troops, color, owner, world, neighboring_id)

}
