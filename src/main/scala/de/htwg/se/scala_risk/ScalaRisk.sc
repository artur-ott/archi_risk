import de.htwg.se.scala_risk.controller.impl.GameLogic
import de.htwg.se.scala_risk.controller.{GameLogic => TGameLogic}
import de.htwg.se.scala_risk.model.World.Players
import de.htwg.se.scala_risk.model.World.Countries
import de.htwg.se.scala_risk.view.TUI
import de.htwg.se.scala_risk.util.Statuses

object ScalaRiskWorksheet {
	ScalaRisk.getInstance()                   //> res0: SRisk = ScalaRisk$Risk@ee7d9f1
	val gl = new GameLogic                    //> gl  : de.htwg.se.scala_risk.controller.impl.GameLogic = de.htwg.se.scala_ris
                                                  //| k.controller.impl.GameLogic@69d9c55
	/*gl.setPlayer(("Test2", "YELLOW"))*/
	gl.getAvailableColors                     //> res1: List[String] = List(RED, YELLOW, GREEN, BLUE)
	 Players.playerList                       //> res2: scala.collection.mutable.ArrayBuffer[de.htwg.se.scala_risk.model.Playe
                                                  //| r] = ArrayBuffer()
	val tui = new TUI(gl)                     //> 
                                                  //| _______________________________________________________________________
                                                  //| 
                                                  //| 
                                                  //| ______________________To start the game press s________________________
                                                  //| 
                                                  //| 
                                                  //| _______________________________________________________________________
                                                  //| 
                                                  //| tui  : de.htwg.se.scala_risk.view.TUI = de.htwg.se.scala_risk.view.TUI@4667a
                                                  //| e56
	gl.setStatus(Statuses.INITIALIZE_PLAYERS) //> -----------------------------------------------------------------------
                                                  //| Following colors are still available: RED, YELLOW, GREEN, BLUE
                                                  //| Pleas enter v to start the game or a name and color which is available (name
                                                  //| , color) to create a player:
	gl.setPlayer(("Test", "BLUE"))
	gl.setPlayer(("Test1", "RED"))
  gl.initializeGame                               //> 
                                                  //| [Test]
                                                  //| 
                                                  //| VENEZUELA:                 Test                             3
                                                  //| 
                                                  //| PERU:                     Test1                             3
                                                  //| 
                                                  //| ARGENTINIEN:               Test                             3
                                                  //| 
                                                  //| BRASILIEN:                Test1                             3
                                                  //| 
                                                  //| NORDAFRIKA:                Test                             3
                                                  //| 
                                                  //| ZENTRALAFRIKA:            Test1                             3
                                                  //| 
                                                  //| SUEDAFRIKA:                Test                             3
                                                  //| 
                                                  //| MADAGASKAR:               Test1                             3
                                                  //| 
                                                  //| OSTAFRIKA:                 Test                             3
                                                  //| 
                                                  //| AEGYPTEN:                 Test1                             3
                                                  //| 
                                                  //| 
                                                  //| _______________________________________________________________________
                                                  //| 
                                                  //| q:     quit              country1, country2:     attack
                                                  //| n:     new game          country, x:             recruit
                                                  //| e:     end turn          country:                show cand
                                                  //| Output exceeds cutoff limit.
  gl.getCandidates()                              //> res3: List[(String, String, Int)] = List((VENEZUELA,Test,3), (ARGENTINIEN,Te
                                                  //| st,3), (NORDAFRIKA,Test,3), (SUEDAFRIKA,Test,3), (OSTAFRIKA,Test,3))
  gl.addTroops("VENEZUELA", 3)                    //> PLAYER_SPREAD_TROOPS
  gl.getCandidates("VENEZUELA")                   //> res4: List[(String, String, Int)] = List((PERU,"",0), (BRASILIEN,"",0))
  gl.attack("VENEZUELA", "BRASILIEN")             //> -----------------------------------------------------------------------
                                                  //| Test Test1
                                                  //| 6     5
                                                  //| 6     5
                                                  //| 6     5
  gl.moveTroops(2)
  gl.endTurn                                      //> PLAYER_ATTACK
  gl.endTurn                                      //> PLAYER_MOVE_TROOPS----------------------------------------------------------
                                                  //| -------------
                                                  //| Troops to spread: 3
                                                  //| 
                                                  //| [Test1]
                                                  //| 
                                                  //| VENEZUELA:                 Test                             4
                                                  //| 
                                                  //| PERU:                     Test1                             3
                                                  //| 
                                                  //| ARGENTINIEN:               Test                             3
                                                  //| 
                                                  //| BRASILIEN:                 Test                             2
                                                  //| 
                                                  //| NORDAFRIKA:                Test                             3
                                                  //| 
                                                  //| ZENTRALAFRIKA:            Test1                             3
                                                  //| 
                                                  //| SUEDAFRIKA:                Test                             3
                                                  //| 
                                                  //| MADAGASKAR:               Test1                             3
                                                  //| 
                                                  //| OSTAFRIKA:                 Test                             3
                                                  //| 
                                                  //| AEGYPTEN:                 Test1                             3
                                                  //| 
                                                  //| 
                                                  //| _______________________________________________________________________
                                                  //| 
                                                  //| q:     quit              country1, country2:     attack
                                                  //| Output exceeds cutoff limit.
	 
}