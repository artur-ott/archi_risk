package de.htwg.se.scala_risk.model.database

import slick.jdbc.H2Profile.api._

class WorldDAO(tag: Tag) extends Table[(Int, String, Int, Int)] (tag, "World"){
  def id = column[Int]("WORLD_ID", O.PrimaryKey, O.AutoInc)
  def name = column[String]("NAME")
  def color = column[Int]("COLOR")
  def world = column[Int]("world") // should be changed later to world: TWorld
  def * = (id, name, color, world)

}
