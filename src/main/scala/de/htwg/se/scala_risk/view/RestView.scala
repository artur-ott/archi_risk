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

class RestView @Inject() (gameLogic: GameLogic) extends TObserver {

  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()

  val route =
    get {
      path("status") {
        this.gameLogic.getStatus match {
          case Statuses.INITIALIZE_PLAYERS => this.statusInitializePlayers
          case Statuses.PLAYER_SPREAD_TROOPS => this.gameStatusWithCountries
          case Statuses.PLAYER_ATTACK => this.gameStatusWithCountries
          case Statuses.PLAYER_MOVE_TROOPS => ???
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
      } ~
      path("save") {
        this.gameLogic.saveGame
        complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, ""))
      } ~
      path("load") {
        this.gameLogic.loadGame
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

  val bindingFuture = Http().bindAndHandle(route, "0.0.0.0", 8080)

  def statusInitializePlayers: StandardRoute = {
    val avColors = this.gameLogic.getAvailableColors.toString()
    complete (HttpEntity (ContentTypes.`application/json`, "{\"status\":\"" + gameLogic.getStatus + "\"," +
      "\"colors\":\"" + avColors.substring(5, avColors.length() - 1) + "\"}") )
  }

  def gameStatusWithCountries: StandardRoute = {
    val troopsToSpread = this.gameLogic.getTroopsToSpread
    val player = this.gameLogic.getCurrentPlayer._1
    val countries_string = "[" + this.gameLogic.getCountries.map(country => "[\"%s\", \"%s\", \"%d\"]".
      format(country._1, country._2, country._3)).mkString(", ") + "]"

    complete (HttpEntity (ContentTypes.`application/json`, ("{\"status\":\"%s\"," +
      "\"player\":\"%s\",\"amount\":\"%d\",\"countries\":%s}").format(this.gameLogic.getStatus, player, troopsToSpread, countries_string)) )
  }


  def update() = {
    this.gameLogic.getStatus match {
      case _ => ???
    }
  }
}
