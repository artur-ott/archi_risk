package de.htwg.se.scala_risk.model.database

import slick.jdbc.H2Profile.api._

class NeighborDAO(tag: Tag) extends Table[(Int, Int)] (tag, "Neighbor"){
  def id = column[Int]("NEIGHBOR_ID")
  def value = column[Int]("value")
  def * = (id, value)

}
