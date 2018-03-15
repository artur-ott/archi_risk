package de.htwg.se.scala_risk.model.impl
import de.htwg.se.scala_risk.model.World.Players
import de.htwg.se.scala_risk.model.World.Countries

object CountryWorksheed {
	"hi"                                      //> res0: String("hi") = hi
	var x = 1                                 //> x  : Int = 1
	x                                         //> res1: Int = 1
	"hi"                                      //> res2: String("hi") = hi
	Countries.country10                       //> res3: de.htwg.se.scala_risk.model.impl.Country = Name: VENEZUELA, Neigbors: 
                                                  //| PERU BRASILIEN , troops: 0, owner: 
	Countries.country11                       //> res4: de.htwg.se.scala_risk.model.impl.Country = Name: PERU, Neigbors: VENEZ
                                                  //| UELA ARGENTINIEN BRASILIEN , troops: 0, owner: 
	Countries.country12                       //> res5: de.htwg.se.scala_risk.model.impl.Country = Name: ARGENTINIEN, Neigbors
                                                  //| : PERU BRASILIEN , troops: 0, owner: 
	Countries.country13                       //> res6: de.htwg.se.scala_risk.model.impl.Country = Name: BRASILIEN, Neigbors: 
                                                  //| VENEZUELA PERU ARGENTINIEN NORDAFRIKA , troops: 0, owner: 
	Countries.country14                       //> res7: de.htwg.se.scala_risk.model.impl.Country = Name: NORDAFRIKA, Neigbors:
                                                  //|  BRASILIEN ZENTRALAFRIKA OSTAFRIKA AEGYPTEN , troops: 0, owner: 
	Countries.country15                       //> res8: de.htwg.se.scala_risk.model.impl.Country = Name: ZENTRALAFRIKA, Neigbo
                                                  //| rs: NORDAFRIKA SUEDAFRIKA OSTAFRIKA , troops: 0, owner: 
	Countries.country16                       //> res9: de.htwg.se.scala_risk.model.impl.Country = Name: SUEDAFRIKA, Neigbors:
                                                  //|  ZENTRALAFRIKA MADAGASKAR OSTAFRIKA , troops: 0, owner: 
	Countries.country17                       //> res10: de.htwg.se.scala_risk.model.impl.Country = Name: MADAGASKAR, Neigbors
                                                  //| : SUEDAFRIKA OSTAFRIKA , troops: 0, owner: 
	Countries.country18                       //> res11: de.htwg.se.scala_risk.model.impl.Country = Name: OSTAFRIKA, Neigbors:
                                                  //|  SUEDAFRIKA MADAGASKAR ZENTRALAFRIKA NORDAFRIKA AEGYPTEN , troops: 0, owner:
                                                  //|  
	Countries.country19                       //> res12: de.htwg.se.scala_risk.model.impl.Country = Name: AEGYPTEN, Neigbors: 
                                                  //| NORDAFRIKA OSTAFRIKA , troops: 0, owner: 
	
	val Player1 = Player("Pro", Colors.RED)   //> Player1  : de.htwg.se.scala_risk.model.impl.Player = Player(Pro,RED)
	val Player2 = Player("Noob", Colors.RED)  //> Player2  : de.htwg.se.scala_risk.model.impl.Player = Player(Noob,RED)
	Players.playerList                        //> res13: List[de.htwg.se.scala_risk.model.Player] = List()
	Player1 != Player2                        //> res14: Boolean = true
	Players.addPlayer("noob", "RED")
	Players.playerList.length                 //> res15: Int = 1
	
}