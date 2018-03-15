package de.htwg.se.scala_risk.model.impl

import de.htwg.se.scala_risk.model.World._

object ContinentsWorksheet {
   Continents.countriesOfContinent1               //> res0: scala.collection.immutable.Set[Int] = Set(0, 1, 2, 3)
   Continents.listContinents                      //> res1: scala.collection.mutable.Buffer[de.htwg.se.scala_risk.model.Continent]
                                                  //|  = ArrayBuffer(Name: SUEDAMERIKA, containing countries: VENEZUELA PERU ARGEN
                                                  //| TINIEN BRASILIEN , bonus troops: 2, owner: Player(,null), Name: AFRIKA, cont
                                                  //| aining countries: AEGYPTEN SUEDAFRIKA MADAGASKAR NORDAFRIKA OSTAFRIKA ZENTRA
                                                  //| LAFRIKA , bonus troops: 3, owner: Player(,null))
   Players.addPlayer("Petra", "RED")
   Continents.listContinents(0).getIncludedCountries().contains(Countries.listCountries(0))
                                                  //> res2: Boolean = true
   Continents.listContinents(0).getIncludedCountries().foreach { x => x.setOwner(Players.playerList(0)) }
   Continents.listContinents(0).getIncludedCountries()
                                                  //> res3: scala.collection.immutable.Set[de.htwg.se.scala_risk.model.Country] = 
                                                  //| Set(Name: VENEZUELA, Neigbors: PERU BRASILIEN , troops: 0, owner: Petra, Nam
                                                  //| e: PERU, Neigbors: VENEZUELA ARGENTINIEN BRASILIEN , troops: 0, owner: Petra
                                                  //| , Name: ARGENTINIEN, Neigbors: PERU BRASILIEN , troops: 0, owner: Petra, Nam
                                                  //| e: BRASILIEN, Neigbors: VENEZUELA PERU ARGENTINIEN NORDAFRIKA , troops: 0, o
                                                  //| wner: Petra)
   Continents.listContinents(0).getIncludedCountries().contains(Countries.listCountries(0))
                                                  //> res4: Boolean = true
   
}