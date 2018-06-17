package de.htwg.se.scala_risk.controller

trait CountryController {
  def getCountries: List[(String, String, Int, Int)]
}