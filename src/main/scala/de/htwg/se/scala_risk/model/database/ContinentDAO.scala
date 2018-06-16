package de.htwg.se.scala_risk.model.database

import slick.jdbc.H2Profile.api._

class ContinentDAO(tag: Tag) extends Table[(Int, String, Int, Int)] (tag, "Continent"){
  def id = column[Int]("CONTINENT_ID", O.PrimaryKey, O.AutoInc)
  def name = column[String]("NAME")
  def bonusTroops = column[Int]("BONUS_TROOPS")
  def world_id = column[Int]("world_id")
  def * = (id, name, bonusTroops, world_id)

}
