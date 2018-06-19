package de.htwg.se.scala_risk.model.database

import slick.jdbc.H2Profile.api._

final case class WorldDAO(id: Int, content: String)

final class WorldTable(tag: Tag) extends Table[WorldDAO](tag, "worldDAO") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def content = column[String]("content")
  def * = (id, content).mapTo[WorldDAO] }

