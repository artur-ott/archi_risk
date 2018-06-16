package de.htwg.se.scala_risk.model.database

import slick.jdbc.H2Profile.api._

class PlayerDAO(tag: Tag) extends Table[(Int, String, String, Int)] (tag, "Player"){
  def id = column[Int]("PLAYER_ID", O.PrimaryKey, O.AutoInc)
  def name = column[String]("NAME")
  def color = column[String]("COLOR")
  def troops = column[Int]("world") // should be changed later to world: TWorld
  def * = (id, name, color, troops)

}
