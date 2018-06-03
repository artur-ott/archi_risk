package de.htwg.se.scala_risk.view

import de.htwg.se.scala_risk.controller.GameLogic
import de.htwg.se.scala_risk.util.observer.TObserver
import de.htwg.se.scala_risk.util.Statuses
import javax.inject.Inject

import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.actor.ActorSystem
import akka.http.scaladsl.server.StandardRoute
import akka.stream.ActorMaterializer
import org.apache.log4j._

class TUI @Inject() (gameLogic: GameLogic) {
  val LENGTH = 30

  //gameLogic.add(this)

  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()

  val route =
    get {
      path("status") {
        this.gameLogic.getStatus match {
          case Statuses.INITIALIZE_PLAYERS => printPlayerInitialisation
            /*
          case Statuses.GAME_INITIALIZED => printPitch
          case Statuses.PLAYER_SPREAD_TROOPS => printSpreadTroops
          case Statuses.PLAYER_ATTACK => printAttack
          case Statuses.PLAYER_MOVE_TROOPS => printMoveTroopss
          case Statuses.DIECES_ROLLED => printRolledDieces
          case Statuses.PLAYER_CONQUERED_A_COUNTRY => printConquered
          case Statuses.PLAYER_CONQUERED_A_CONTINENT => logger.info("You conquered a continent!")
          */
          case _ => complete (HttpEntity (ContentTypes.`application/json`, "{\"status\":\"" + this.gameLogic.getStatus + "\"}") )
        }
      }
    } ~
      put {
        path("start") {
          this.gameLogic.startGame
          val avColors = gameLogic.getAvailableColors.toString()
          complete(HttpEntity(ContentTypes.`application/json`, "{\"status\":\"GAME_STARTED\", \"colors\":\"" + avColors.substring(5, avColors.length() - 1) + "\"}"))
        } ~
          path("initialize") {
            this.gameLogic.initializeGame
            complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, ""))
          } ~
          path("spread") {
            formFields('country.as[String], 'amount.as[Int]) { (country: String, amount: Int) =>
              this.gameLogic.addTroops(country, amount)
              complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, ""))
            }
          } ~
          path("attack") {
            formFields('attacker.as[String], 'defender.as[String]) { (attacker: String, defender: String) =>
              this.gameLogic.attack(attacker, defender)
              val dices = gameLogic.getRolledDieces
              complete(HttpEntity(ContentTypes.`application/json`,
                "{\"dices\":[%s, %s]}".format("[" + dices._1.mkString(",") + "]", "[" + dices._2.mkString(",") + "]")))
            }
          } ~
          path("endturn") {
            this.gameLogic.endTurn
            complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, ""))
          }
      } ~
      post {
        path("player") {
          formFields('player.as[String], 'color.as[String]) { (player: String, color: String) =>
            this.gameLogic.setPlayer((player, color))
            complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, ""))
          }
        }
      }

  val logger = Logger.getLogger(getClass.getName)

  // TODO: REMOVE INIT
  /*----------------HERE---------------------*/
  //  gameLogic.setStatus(Statuses.INITIALIZE_PLAYERS)
  //	gameLogic.setPlayer(("Test", "BLUE"))
  //	gameLogic.setPlayer(("Test1", "RED"))
  //  gameLogic.initializeGame 
  //  this.parseSpreadTroops("VENEZUELA, 3")
  //  this.parseAttack("VENEZUELA, PERU")
  //this.parseAttack("e")
  //this.parseMoveTroops("NORDAFRIKA, OSTAFRIKA, 2")
  /*----------------HERE---------------------*/

  if (gameLogic.getStatus == Statuses.CREATE_GAME) {
    logger.info("\n_______________________________________________________________________\n");
    logger.info("\n______________________To start the game press s________________________\n");
    logger.info("\n_______________________________________________________________________\n");

  }

  def setNextInput(input: String): Boolean = {
    logger.debug("Player pressed: '" + input + "'")
    if (input.equals("q")) {
      System.exit(0)
    }

    if (input.equals("n"))
      gameLogic.startGame
    else if (input.equals("save")) {
      gameLogic.saveGame
    } else if (input.equals("load")) {
      gameLogic.loadGame
    } else if (input.equals("undo")) {
      gameLogic.undo
    } else {
      logger.debug("Current state: " + gameLogic.getStatus)
      gameLogic.getStatus match {
        case Statuses.CREATE_GAME => if (input.equals("s")) gameLogic.startGame
        case Statuses.INITIALIZE_PLAYERS => this.parsePlayer(input)
        case Statuses.PLAYER_SPREAD_TROOPS => this.parseSpreadTroops(input)
        case Statuses.PLAYER_ATTACK => this.parseAttack(input)
        case Statuses.PLAYER_MOVE_TROOPS => this.parseMoveTroops(input)
        case Statuses.PLAYER_CONQUERED_A_COUNTRY => this.gameLogic.moveTroops(input.toInt)
        case Statuses.GAME_INITIALIZED =>
        case Statuses.PLAYER_CONQUERED_A_CONTINENT => this.gameLogic.moveTroops(input.toInt)
        case _ => logger.debug(gameLogic.getStatus)
      }

    }
    return true
  }

  def update() = {
    logger.debug("Status after update: " + gameLogic.getStatus)
    gameLogic.getStatus match {

        /*
        case Statuses.INITIALIZE_PLAYERS => this.printPlayerInitialisation
        case Statuses.GAME_INITIALIZED => printPitch
        case Statuses.PLAYER_SPREAD_TROOPS => printSpreadTroops
        case Statuses.PLAYER_ATTACK => printAttack
        case Statuses.PLAYER_MOVE_TROOPS => printMoveTroops
        case Statuses.DIECES_ROLLED => printRolledDieces
        case Statuses.PLAYER_CONQUERED_A_COUNTRY => printConquered
        case Statuses.PLAYER_CONQUERED_A_CONTINENT => logger.info("You conquered a continent!")
        */

    // Errors
      case Statuses.COUNTRY_DOES_NOT_BELONG_TO_PLAYER => logger.error("COUNTRY_DOES_NOT_BELONG_TO_PLAYER")
      case Statuses.NOT_ENOUGH_TROOPS_TO_SPREAD => logger.error("NOT_ENOUGH_TROOPS_TO_SPREAD")
      case Statuses.COUNTRY_NOT_FOUND => logger.error("COUNTRY_NOT_FOUND")
      case Statuses.INVALID_QUANTITY_OF_TROOPS_TO_MOVE => logger.error("INVALID_QUANTITY_OF_TROOPS_TO_MOVE")
      case Statuses.PLAYER_ATTACKING_HIS_COUNTRY => logger.error("PLAYER_ATTACKING_HIS_COUNTRY")
      case Statuses.NOT_A_NEIGHBORING_COUNTRY => { /*TODO: implement*/ }
      case Statuses.NOT_ENOUGH_TROOPS_TO_ATTACK => { /*TODO: implement*/ }
    }
  }

  private def parsePlayer(player: String) {
    if (player.equals("v")) {
      logger.debug("Player pressed 'v'")
      gameLogic.initializeGame
    } else {
      val playerData = player.split(", ")
      if (playerData.length >= 2) {
        logger.debug("Player entered: " + playerData(0) + "  " + playerData(1))
        gameLogic.setPlayer((playerData(0), playerData(1)))
      } else {
        gameLogic.setPlayer(("", ""))
      }
    }
  }

  private def parseSpreadTroops(input: String) {
    val splitInput = input.split(", ")
    splitInput.length match {
      case 1 => {
        if (input.equals("b")) {
          printSpreadTroops
        } else if (input.equals("show")) {
          logger.info("-----------------------------------------------------------------------");
          this.showCandidates(gameLogic.getCandidates())
          logger.info("\n_______________________________________________________________________\n");
          logger.info("b:     back");
          logger.info("_______________________________________________________________________\n");
        }
      }
      case x if x >= 2 => gameLogic.addTroops(splitInput(0), splitInput(1).toInt)
      case _ =>
    }
  }

  private def parseAttack(input: String) {
    val splitInput = input.split(", ")
    splitInput.length match {
      case 1 => {
        if (input.equals("b")) {
          printAttack
        } else if (input.equals("e"))
          this.gameLogic.endTurn
        else {
          logger.info("-----------------------------------------------------------------------");
          this.showCandidates(gameLogic.getCandidates(splitInput(0)))
          logger.info("\n_______________________________________________________________________\n");
          logger.info("b:     back");
          logger.info("_______________________________________________________________________\n");
        }
      }
      case x if x >= 2 => gameLogic.attack(splitInput(0), splitInput(1))
      case _ =>
    }
  }

  private def parseMoveTroops(input: String) {
    val splitInput = input.split(", ")
    splitInput.length match {
      case 1 => {
        if (input.equals("b")) {
          printMoveTroops
        } else if (input.equals("e"))
          this.gameLogic.endTurn
        else {
          logger.info("-----------------------------------------------------------------------");
          this.showCandidates(gameLogic.getCandidates(splitInput(0)))
          logger.info("\n_______________________________________________________________________\n");
          logger.info("b:     back");
          logger.info("_______________________________________________________________________\n");
        }
      }
      case x if x >= 3 => gameLogic.dragTroops(splitInput(0), splitInput(1), splitInput(2).toInt)
      case _ =>
    }
  }

  private def printAttack = {
    printPitch
    printMenu
  }

  private def printConquered = {
    logger.info("-----------------------------------------------------------------------");
    logger.info("You have " + (this.gameLogic.getAttackerDefenderCountries._1._3 - 1) + " troops to move.");
    printMenu
  }

  private def printMoveTroops = {
    printPitch
    printMenu
  }

  private def printRolledDieces = {
    logger.info("-----------------------------------------------------------------------");
    val dieces = gameLogic.getRolledDieces
    val max = Math.max(dieces._1.length, dieces._2.length)
    val attackerName = this.gameLogic.getAttackerDefenderCountries._1._2
    val defenderName = this.gameLogic.getAttackerDefenderCountries._2._2
    val maxSpace = attackerName.length + 2
    val text: String = "%s%" + maxSpace + "s"
    logger.info(text.format(attackerName, defenderName))
    var i = 0
    for (i <- 0 to max - 1) {
      var text1 = ""
      var text2 = ""
      if (dieces._1.length > i) text1 = dieces._1(i).toString()
      else text1 = "-"
      if (dieces._2.length > i) text2 = dieces._2(i).toString()
      else text2 = "-"
      logger.info(text.format(text1, text2))
    }
  }

  /*
  private def printPlayerInitialisation = {
    logger.info("-----------------------------------------------------------------------");
    val avColors = gameLogic.getAvailableColors.toString()
    logger.info("Following colors are still available: " + avColors.substring(5, avColors.length() - 1));
    logger.info("Pleas enter v to start the game or a name and color which is available (name, color) to create a player:");
  } */

  val bindingFuture = Http().bindAndHandle(route, "0.0.0.0", 8080)

  private def printPlayerInitialisation: StandardRoute = {
    val avColors = gameLogic.getAvailableColors.toString()
    complete (HttpEntity (ContentTypes.`application/json`, "{\"-----------------------------------------------------------------------\n\""
      + "Following colors are still available: " + avColors.substring(5, avColors.length() - 1) + "\n\""
      + "Pleas enter v to start the game or a name and color which is available (name, color) to create a player:" + "\"}") )
  }

  private def printSpreadTroops = {
    logger.info("-----------------------------------------------------------------------");
    logger.info("Troops to spread: " + gameLogic.getTroopsToSpread)
    printPitch
    printMenu
  }

  private def printPitch = {
    logger.info("\n[" + gameLogic.getCurrentPlayer._1 + "]\n");

    val countries = gameLogic.getCountries

    for (c: (String, String, Int, Int) <- countries) {

      val s1 = c._1;

      val s2 = c._2;

      val s3 = c._3;

      val p1 = LENGTH - s1.length();

      val p2 = LENGTH;
      val text: String = "%s:%" + p1 + "s%" + p2 + "d\n"

      logger.info(text.format(s1, s2, s3));

    }
  }

  private def showCandidates(candidates: List[(String, String, Int)]) = {
    for (c: (String, String, Int) <- candidates) {

      val s1 = c._1;

      val s2 = c._2;

      val s3 = c._3;

      val p1 = LENGTH - s1.length();

      val p2 = LENGTH;
      val text: String = "%s:%" + p1 + "s%" + p2 + "d\n"

      logger.info(text.format(s1, s2, s3));

    }
  }

  private def printMenu = {
    logger.info("\n_______________________________________________________________________\n");

    gameLogic.getStatus match {
      case Statuses.PLAYER_SPREAD_TROOPS => logger.info("q:     quit           country, x:                spread\nn:     new game             show:                show candidates\nload:  load game            save:                save game");
      case Statuses.PLAYER_ATTACK => logger.info("q:     quit           country, country:                attack\nn:     new game                country:                show candidates\ne      end turn\nload:  load game            save:                save game");
      case Statuses.PLAYER_MOVE_TROOPS => logger.info("q:     quit                  country, country, x:                move\nn:     new game                          country:                show candidates\ne      end turn\nload:  load game            save:                save game");
      case Statuses.PLAYER_CONQUERED_A_COUNTRY => logger.info("q:     quit                  x:                move\nn:     new game\nload:  load game            save:                save game");
    }

    //logger.info("q:     quit              country1, country2:     attack\nn:     new game          country, x:             recruit\ne:     end turn          country:                show candidates");

    logger.info("_______________________________________________________________________\n");
  }
}
