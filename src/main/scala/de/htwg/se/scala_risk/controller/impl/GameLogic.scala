package de.htwg.se.scala_risk.controller.impl

import de.htwg.se.scala_risk.util.Statuses
import de.htwg.se.scala_risk.controller.{GameLogic => TGameLogic}
import de.htwg.se.scala_risk.util.XML
import java.io.File
import java.io.FileOutputStream

import akka.actor.ActorSystem
import akka.http.scaladsl.model._
import akka.http.scaladsl.Http
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import javax.inject
import javax.inject.Inject
import javax.inject.Singleton

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import scala.xml.{Elem, Node}

object GameLogic {
  def main(args: Array[String]): Unit = {
    new GameLogic
  }
}

@Singleton
class GameLogic @Inject() () extends TGameLogic {
  private val MODEL_PORT = 8081
  private val MODEL_IP = "localhost"

  val world = new ModelHttp(MODEL_IP, MODEL_PORT)

  private[this] var status: Statuses.Value = Statuses.CREATE_GAME
  private[this] val INIT_TROOPS: Int = 3

  private[impl] var attackerDefenderIndex: (Int, Int) = (-1, -1)
  private[impl] var rolledDieces: (List[Int], List[Int]) = (Nil, Nil)
  private var lastState: scala.xml.Node = _
  //private[this] val world: World = new de.htwg.se.scala_risk.model.impl.World // Changed to test GUI

  def startGame : Unit ={
    this.setStatus(Statuses.INITIALIZE_PLAYERS)
  }

  def initializeGame() : Unit = {
    val players = world.getPlayerList

    if (players.length >= 2) {
      val countries = util.Random.shuffle(world.getCountriesList)

      countries.foreach { x =>
        {
          world.setTroops(x._1, INIT_TROOPS)
          world.setOwner(x._1, players(countries.indexOf(x) % players.length)._1)
        }
      }
      world.nextPlayer
      this.setStatus(Statuses.GAME_INITIALIZED)
      logic()
    } else {
      this.setErrorStatus(Statuses.NOT_ENOUGH_PLAYERS)
    }
  }

  def logic() : Unit = {
    this.status match {
      case Statuses.GAME_INITIALIZED =>
        checkContinents()
        this.troopsToSpread = world.getPlayerList(world.getCurrentPlayerIndex)._2
        this.setStatus(Statuses.PLAYER_SPREAD_TROOPS)
      case Statuses.PLAYER_SPREAD_TROOPS => this.setStatus(Statuses.PLAYER_ATTACK)
      case Statuses.PLAYER_ATTACK => this.setStatus(Statuses.PLAYER_MOVE_TROOPS)
      case Statuses.PLAYER_MOVE_TROOPS => {
        /// TODO: implement check game
        //      val oldPlayer = world.getCurrentPlayerIndex
        val nextPlayer = world.nextPlayer
        //        if (oldPlayer != -1 && world.getCurrentPlayerIndex == 0) {
        //          this.checkGame
        //        } else {
        //          this.troopsToSpread = nextPlayer.getTroops()
        //          this.setStatus(Statuses.PLAYER_SPREAD_TROOPS)
        //        }
        checkContinents()
        this.troopsToSpread = nextPlayer._2
        this.setStatus(Statuses.PLAYER_SPREAD_TROOPS)
      }
      case _ =>
    }
  }

  /*
   * TODO: implement check game
  private[this] def checkGame = {
    world.getPlayerList.foreach {
      e => {
        if (world.getCountriesList.exists { x => x.getOwner == e }) {
          
        } else {
          
        }
      }
    }
  }*/

  def endTurn : Unit = {
    this.lastState = this.toXml
    this.logic()
  }

  private[controller] def setStatus(status: Statuses.Value) : Unit = {
    this.status = status
    notifyObservers
  }

  private def setErrorStatus(status: Statuses.Value) : Unit = {
    val oldStatus = this.status
    this.setStatus(status)
    this.setStatus(oldStatus)
  }

  def getStatus: Statuses.Value = this.status

  def getRolledDieces: (List[Int], List[Int]) = this.rolledDieces

  /* Country operations */
  def getCountries: List[(String, String, Int, Int)] =
    world.getCountriesList.map { x => (x._1, x._2, x._3, x._4) }

  def getCandidates(country: String = ""): List[(String, String, Int)] = {
    this.status match {
      case Statuses.PLAYER_SPREAD_TROOPS => {
        val list = world.getCountriesList.filter { x => x._2.equals(world.getPlayerList(world.getCurrentPlayerIndex)._1) }
        list.map { x => (x._1, x._2, x._3) }.toList
      }
      case Statuses.PLAYER_ATTACK => {
        val candidates = getNeighbours(country).filterNot(x => x._2.equals(world.getPlayerList(world.getCurrentPlayerIndex)._1))
        candidates.map { x => (x._1, x._2, x._3) }
      }
      case Statuses.PLAYER_MOVE_TROOPS => {
        val candidates = getNeighbours(country).filter { x => x._2.equals(world.getPlayerList(world.getCurrentPlayerIndex)._1) }
        candidates.map { x => (x._1, x._2, x._3) }
      }
      case _ => Nil
    }
  }

  def getAttackerDefenderCountries: ((String, String, Int, Int), (String, String, Int, Int)) = {
    if (this.attackerDefenderIndex._1 < 0 || this.attackerDefenderIndex._2 < 0) {
      this.setErrorStatus(Statuses.COUNTRY_NOT_FOUND)
      return null
    }

    (getCountryAsString(this.attackerDefenderIndex._1), getCountryAsString(this.attackerDefenderIndex._2))
  }

  private def getCountryAsString(index: Int): (String, String, Int, Int) = {
    val country = world.getCountriesList(index)
    (country._1, country._2, country._3, country._4)
  }

  private def getNeighbours(country: String): List[(String, String, Int, Int, List[String])] = {
    val index = this.getCountryIndexByString(country)
    if (index < 0) this.setErrorStatus(Statuses.COUNTRY_NOT_FOUND)
    val countries = world.getCountriesList
    countries.filter(c => countries(index)._5.contains(c._1))
  }

  def getAttackerDefenderIndex: (Int, Int) = this.attackerDefenderIndex

  /* Player operations */
  var troopsToSpread = 3//world.getPlayerList(world.getCurrentPlayerIndex).getTroops()

  def getAvailableColors: List[String] = world.getPlayerColorList

  def setPlayer(player: (String, String)) : Unit = {
    world.addPlayer(player._1, player._2)
    notifyObservers
  }

  def getCurrentPlayer: (String, String) = (
    world.getPlayerList(world.getCurrentPlayerIndex)._1,
    world.getPlayerList(world.getCurrentPlayerIndex)._3
  )

  def getTroopsToSpread: Int = this.troopsToSpread

  def addTroops(country: String, troops: Int) : Unit = {
    val index = this.getCountryIndexByString(country)
    if (index >= 0) {
      if (world.getCountriesList(index)._2.equals(world.getPlayerList(world.getCurrentPlayerIndex)._1)) {
        if (troops <= troopsToSpread) {
          this.lastState = this.toXml
          val countryList = world.getCountriesList.toList
          world.setTroops(countryList(index)._1, countryList(index)._3 + troops)
          troopsToSpread -= troops
          if (troopsToSpread == 0) logic()
          else notifyObservers
        } else {
          this.setErrorStatus(Statuses.NOT_ENOUGH_TROOPS_TO_SPREAD)
        }
      } else this.setErrorStatus(Statuses.COUNTRY_DOES_NOT_BELONG_TO_PLAYER)
    } else this.setErrorStatus(Statuses.COUNTRY_NOT_FOUND)
  }

  def attack(countryAttacker: String, countryDefender: String) : Unit = {
    /* Make check if the player attacks his own country easier */
    if (this.getCurrentPlayer._1.toUpperCase().equals(this.getOwnerName(countryDefender))) {
      this.setErrorStatus(Statuses.PLAYER_ATTACKING_HIS_COUNTRY)
    } else {
      /* Check, if the country to attack is a neighboring country of the
       * attacker country was missing!
       */
      if (!this.getNeighbours(countryAttacker).map { x => x._1.toUpperCase() }.contains(countryDefender.toUpperCase())) {
        this.setErrorStatus(Statuses.NOT_A_NEIGHBORING_COUNTRY)
      } else {
        if (this.status == Statuses.PLAYER_ATTACK) {
          this.lastState = this.toXml
          this.attackerDefenderIndex = getAttackIndexes(countryAttacker, countryDefender)
          if (attackerDefenderIndex._1 != -1) {
            this.rolledDieces = this.rollDice(
              world.getCountriesList(attackerDefenderIndex._1),
              world.getCountriesList(attackerDefenderIndex._2)
            )
            this.setStatus(Statuses.DIECES_ROLLED)
            val min = Math.min(this.rolledDieces._1.length, this.rolledDieces._2.length)
            var extantTroopsAttacker = world.getCountriesList(attackerDefenderIndex._1)._3
            var extantTroopsDefender = world.getCountriesList(attackerDefenderIndex._2)._3
            val extant = this.getExtantTroops(extantTroopsAttacker, extantTroopsDefender, min)
            extantTroopsAttacker = extant._1
            extantTroopsDefender = extant._2
            world.setTroops(world.getCountriesList(attackerDefenderIndex._1)._1, extantTroopsAttacker)
            world.setTroops(world.getCountriesList(attackerDefenderIndex._2)._1, extantTroopsDefender)
            this.checkConquered(extantTroopsDefender, countryDefender)
          }
        }
      }
    }
  }

  private[impl] def getExtantTroops(troopsAttacker: Int, troopsDefender: Int, min: Int): (Int, Int) = {
    var extantTroopsAttacker = troopsAttacker
    var extantTroopsDefender = troopsDefender
    for (i <- 0 until min) {
      if (this.rolledDieces._1(i) > this.rolledDieces._2(i)) {
        extantTroopsDefender -= 1
      } else {
        extantTroopsAttacker -= 1
      }
    }
    (extantTroopsAttacker, extantTroopsDefender)
  }

  private[impl] def checkConquered(extantTroopsDefender: Int, countryDefender: String) : Unit = {
    if (extantTroopsDefender == 0) {
      world.setOwner(world.getCountriesList(attackerDefenderIndex._2)._1, world.getCountriesList(attackerDefenderIndex._1)._2)

      if (this.getContinentOwner(countryDefender).toUpperCase().equals(this.getOwnerName(countryDefender).toUpperCase())) {
        this.setStatus(Statuses.PLAYER_CONQUERED_A_CONTINENT)
      } else {
        this.setStatus(Statuses.PLAYER_CONQUERED_A_COUNTRY)
      }
    } else {
      this.clearAttack()
      this.setStatus(Statuses.PLAYER_ATTACK)
    }
  }

  private[impl] def getAttackIndexes(countryAttacker: String, countryDefender: String): (Int, Int) = {
    val indexAttacker = this.getCountryIndexByString(countryAttacker)
    val indexDefender = this.getCountryIndexByString(countryDefender)
    if (indexAttacker > -1 && indexDefender > -1) {
      if (world.getCountriesList(indexAttacker)._2.equals(world.getPlayerList(world.getCurrentPlayerIndex)._1) &&
        !world.getCountriesList(indexDefender)._2.equals(world.getPlayerList(world.getCurrentPlayerIndex)._1)) {
        return (indexAttacker, indexDefender)
      } else if (world.getCountriesList(indexAttacker)._2.equals(world.getPlayerList(world.getCurrentPlayerIndex)._1) &&
        world.getCountriesList(indexDefender)._2.equals(world.getPlayerList(world.getCurrentPlayerIndex)._1)) {
        this.setErrorStatus(Statuses.PLAYER_ATTACKING_HIS_COUNTRY)
      } else if (!world.getCountriesList(indexAttacker)._2.equals(world.getPlayerList(world.getCurrentPlayerIndex)._1)) {
        this.setErrorStatus(Statuses.COUNTRY_DOES_NOT_BELONG_TO_PLAYER)
      }
    } else this.setErrorStatus(Statuses.COUNTRY_NOT_FOUND)
    (-1, -1)
  }

  private[this] def clearAttack() : Unit = {
    this.attackerDefenderIndex = (-1, -1)
    this.rolledDieces = (Nil, Nil)
  }

  def moveTroops(count: Int) : Unit = {
    if (this.status == Statuses.PLAYER_CONQUERED_A_COUNTRY || this.status == Statuses.PLAYER_CONQUERED_A_CONTINENT || this.status == Statuses.PLAYER_MOVE_TROOPS) {
      val currentTroops = world.getCountriesList(this.attackerDefenderIndex._1)._3
      if (count < 1 || count >= currentTroops)
        this.setErrorStatus(Statuses.INVALID_QUANTITY_OF_TROOPS_TO_MOVE)
      else if (this.status == Statuses.PLAYER_CONQUERED_A_COUNTRY || this.status == Statuses.PLAYER_CONQUERED_A_CONTINENT) {
        world.setTroops(world.getCountriesList(this.attackerDefenderIndex._1)._1, currentTroops - count)
        world.setTroops(world.getCountriesList(this.attackerDefenderIndex._2)._1, count)
        this.clearAttack()
        this.setStatus(Statuses.PLAYER_ATTACK)
      } else {
        this.lastState = this.toXml
        world.setTroops(world.getCountriesList(this.attackerDefenderIndex._1)._1, currentTroops - count)
        world.setTroops(world.getCountriesList(this.attackerDefenderIndex._2)._1, world.getCountriesList(this.attackerDefenderIndex._2)._3 + count)
        this.clearAttack()
        this.setStatus(Statuses.PLAYER_MOVE_TROOPS)
      }
    }
  }

  private[this] def getCountryIndexByString(country: String): Int = world.getCountriesList.indexWhere { x => x._1.toUpperCase() == country.toUpperCase() }

  def dragTroops(countryFrom: String, countryTo: String, troops: Int) : Unit = {
    if (this.status == Statuses.PLAYER_MOVE_TROOPS) {
      this.attackerDefenderIndex = (
        this.getCountryIndexByString(countryFrom),
        this.getCountryIndexByString(countryTo)
      )
      if (this.attackerDefenderIndex._1 < 0 || this.attackerDefenderIndex._2 < 0) {
        this.clearAttack()
        this.setErrorStatus(Statuses.COUNTRY_NOT_FOUND)
      } else {
        if (!this.getNeighbours(countryFrom).map { x => x._1.toUpperCase() }.contains(countryTo)) {
          this.setErrorStatus(Statuses.NOT_A_NEIGHBORING_COUNTRY)
        } else {
          this.moveTroops(troops)
        }
        
      }
    }
  }

  // Function to attack a country. TCountry is the trait type!
  def rollDice(attacker: (String, String, Int, Int, List[String]),
               defender: (String, String, Int, Int, List[String])): (List[Int], List[Int]) = {
    val troopsAttacker = attacker._3
    val toopsDefender = defender._3
    var dicesAttacker: List[Int] = Nil
    var dicesDefender: List[Int] = Nil
    //return (6 :: 6 :: 6 :: Nil, 5 :: 5 :: 5 :: Nil) // TODO: for testing remove comment
    if (troopsAttacker > 1) {
      troopsAttacker match {
        case 2 => dicesAttacker = List.fill(1)(randomDice())
        case 3 => dicesAttacker = List.fill(2)(randomDice())
        case _ => dicesAttacker = List.fill(3)(randomDice())
      }
      toopsDefender match {
        case 1 => dicesDefender = List.fill(1)(randomDice())
        case _ => dicesDefender = List.fill(2)(randomDice())
        //case _ => dicesDefender = List.fill(3)(randomDice())
      }
      (dicesAttacker.sortWith(_ > _), dicesDefender.sortWith(_ > _))
    } else {
      this.setErrorStatus(Statuses.NOT_ENOUGH_TROOPS_TO_ATTACK)
      (Nil, Nil)
    }
  }
  // Function to get dice values from 1 to 6
  def randomDice(): Int = ((Math.random() * 6) + 1).toInt

  def getCurrentPlayerColor(): String = {
    world.getPlayerList(world.getCurrentPlayerIndex)._3
  }

  def getOwnerColor(owner: String): Int = {
    val playerList = world.getPlayerList
    var intcolor = 0
    var color = ""
    playerList.foreach { x => if (x._1.toUpperCase().equals(owner.toUpperCase())) { color = x._3.toUpperCase() } }

    color match {
      case "RED" => intcolor = -57312
      case "YELLOW" => intcolor = -5888
      case "GREEN" => intcolor = -10420362
      case "BLUE" => intcolor = -8350209
      case "PINK" => intcolor = -563473
      case "ORANGE" => intcolor = -355265
      case _ => intcolor = 0
    }
    intcolor
  }

  def getOwnerName(country: String): String = {
    val countryList = world.getCountriesList
    var ownerName = ""
    countryList.foreach { x => if (x._1.toUpperCase().equals(country.toUpperCase())) { ownerName = x._2.toUpperCase() } }
    ownerName
  }

  def checkContinents() {
    val playerList = world.getPlayerList
    val continentList = world.getContinentList

    playerList.foreach { x =>
      var troops = 0
      continentList.foreach { c =>
        if (c._1 == x._1) {
          troops += c._2
        }
      }
      world.setPlayerTroops(x._1, this.INIT_TROOPS + troops);
    }

  }
  
  def getContinentOwner(countryName: String): String = {
    val continentList = world.getContinentList
    var continentName = ""
    continentList.foreach { x => if (x._3.contains(countryName)) { continentName = x._1 } }
    continentName
  }

  def saveGame : Unit = {
    val file: File = new File("./save/savegame.xml")
    val fos: FileOutputStream = new FileOutputStream(file, false)
    val saveXML: Array[Byte] = this.toXml.toString().getBytes
    fos.write(saveXML)
    fos.close()
  }

  def loadGame : Unit = {
    val filename = "./save/savegame.xml"
    //this.fromXml(scala.xml.XML.loadFile(filename))
    this.fromXml(scala.xml.XML.load(new java.io.InputStreamReader(new java.io.FileInputStream(filename), "UTF-8")))
  }

  def toXml: scala.xml.Node = {
    <GameLogic>
      <status>{ this.status.toString() }</status>
      <attackerDefenderIndex>{ this.getAttackerDefenderIndexXml(this.attackerDefenderIndex) }</attackerDefenderIndex>
      <rolledDieces>{ this.getRolledDiecesXml(this.rolledDieces) }</rolledDieces>
      <troopsToSpread>{ this.troopsToSpread }</troopsToSpread>
      <world>{ this.world.toXml }</world>
    </GameLogic>
  }

  def getRolledDiecesXml(node: (List[Int], List[Int])): scala.xml.Elem = {
    val xml = <tuple></tuple>
    var firstList:scala.xml.Elem = <list id="first"></list>
    var secondList:scala.xml.Elem = <list id="second"></list>
    node._1.foreach(x => {
      val xmlEl:scala.xml.Elem = <listEl>{ x }</listEl>
      firstList = XML.addXmlChild(firstList, xmlEl).asInstanceOf[scala.xml.Elem]
    })
    node._2.foreach(x => {
      val xmlEl = <listEl>{ x }</listEl>
      secondList = XML.addXmlChild(secondList, xmlEl).asInstanceOf[scala.xml.Elem]
    })
    XML.addXmlChild(XML.addXmlChild(xml, firstList).asInstanceOf[scala.xml.Elem], secondList).asInstanceOf[scala.xml.Elem]
  }

  def getAttackerDefenderIndexXml(node: (Int, Int)): scala.xml.Elem = {
    val xml = <tuple></tuple>
    val first = <element id="first">{ node._1 }</element>
    val second = <element id="second">{ node._2 }</element>
    XML.addXmlChild(XML.addXmlChild(xml, first).asInstanceOf[scala.xml.Elem], second).asInstanceOf[scala.xml.Elem]
  }

  def fromXml(node: scala.xml.Node) : Unit = {
    // get status
    this.status = Statuses.withName((node \ "status").text.toUpperCase())
    // get attackerDefenderIndex
    this.attackerDefenderIndex = ((node \ "attackerDefenderIndex").head.child.head.child.head.text.toInt, (node \ "attackerDefenderIndex").head.child.head.child.last.text.toInt)
    // get rolledDieces
    var first = List[Int]()
    var second = List[Int]()
    (node \ "rolledDieces").head.child.head.child.head.child.foreach(x => first = x.text.toInt :: first)
    (node \ "rolledDieces").head.child.head.child.last.child.foreach(x => second = x.text.toInt :: second)
    this.rolledDieces = (first.sortWith(_ > _), second.sortWith(_ > _))
    // get troops to spreed
    this.troopsToSpread = (node \ "troopsToSpread").text.toInt
    this.world.fromXml((node \ "world").head)
    this.notifyObservers
  }

  def undo : Unit = {
    if (this.lastState != null) {
      val temp = this.lastState
      this.lastState = null
      this.fromXml(temp)
    }
  }

}

private[impl] class ModelHttp(val ip: String, val port: Int) {
  import scala.util.parsing.json.JSON
  implicit val system = ActorSystem("TUI")
  implicit val executionContext = system.dispatcher
  implicit val materializer = ActorMaterializer()

  val modelOnlineTest: Future[HttpResponse] = Http().singleRequest(HttpRequest(uri = "http://%s:%d/status".format(ip, port)))
  try {
    Await.result(modelOnlineTest, Duration.Inf)
  } catch {
    case _: akka.stream.StreamTcpException => System.err.println("Model microservice not started!")
  }

  def call(route: String, method: HttpMethod = HttpMethods.GET, entity: RequestEntity = HttpEntity.Empty): Future[HttpResponse] =
    Http().singleRequest(HttpRequest(method, Uri("http://%s:%d/%s".format(ip, port, route)), Nil, entity))

  def nextPlayer: (String, Int) = {
    val responseFuture: Future[HttpResponse] = call("nextplayer", HttpMethods.PUT)
    val result = Await.result(responseFuture.map {
      case HttpResponse(StatusCodes.OK, _, entity, _) =>
        Unmarshal(entity).to[String]
      case _ => ""
    }, Duration.Inf).toString.drop("FullfiledFuture(".length).dropRight(1)
    val map = JSON.parseFull(result).get.asInstanceOf[Map[String, Any]]
    (map("name").asInstanceOf[String], map("troops").asInstanceOf[Double].toInt)
  }

  def toXml: Elem = {
    val responseFuture: Future[HttpResponse] = call("toxml")
    val result = Await.result(responseFuture.map {
      case HttpResponse(StatusCodes.OK, _, entity, _) =>
        Unmarshal(entity).to[String]
      case _ => ""
    }, Duration.Inf).toString.drop("FullfiledFuture(".length).dropRight(1)
    val map = JSON.parseFull(result).get.asInstanceOf[Map[String, String]]
    scala.xml.XML.load(new java.io.InputStreamReader(new java.io.ByteArrayInputStream(map("xml").toCharArray.map(_.toByte)), "UTF-8"))
  }

  def getCurrentPlayerIndex: Int = {
    val responseFuture: Future[HttpResponse] = call("currentplayerindex")
    val result = Await.result(responseFuture.map {
      case HttpResponse(StatusCodes.OK, _, entity, _) =>
        Unmarshal(entity).to[String]
      case _ => ""
    }, Duration.Inf).toString.drop("FullfiledFuture(".length).dropRight(1)
    val map = JSON.parseFull(result).get.asInstanceOf[Map[String, Double]]
    map("index").toInt
  }

  def getCountriesList: List[(String, String, Int, Int, List[String])] = {
    val responseFuture: Future[HttpResponse] = call("countrieslist")
    val result = Await.result(responseFuture.map {
      case HttpResponse(StatusCodes.OK, _, entity, _) =>
        Unmarshal(entity).to[String]
      case _ => ""
    }, Duration.Inf).toString.drop("FullfiledFuture(".length).dropRight(1)
    val map = JSON.parseFull(result).get.asInstanceOf[Map[String, Map[String, Any]]]
    map.map(c => (
      c._1, c._2("owner").asInstanceOf[String],
      c._2("troops").asInstanceOf[Double].toInt,
      c._2("color").asInstanceOf[Double].toInt,
      c._2("neighbours").asInstanceOf[List[String]]
    )).asInstanceOf[List[(String, String, Int, Int, List[String])]]
  }

  def setOwner(country: String, player: String): Unit  = {
    val responseFuture: Future[HttpResponse] = call("setowner",
      HttpMethods.POST, FormData(("country", country), ("player", player)).toEntity)
    Await.result(responseFuture, Duration.Inf)
  }
  def setTroops(country: String, troops: Int): Unit  = {
    val responseFuture: Future[HttpResponse] = call("settroops",
      HttpMethods.POST, FormData(("country", country), ("troops", troops.toString)).toEntity)
    Await.result(responseFuture, Duration.Inf)
  }

  def addPlayer(name: String, color: String): Unit = {
    val responseFuture: Future[HttpResponse] = call("addplayer",
      HttpMethods.POST, FormData(("player", name), ("color", color)).toEntity)
    Await.result(responseFuture, Duration.Inf)
  }

  def getPlayerColorList: List[String] = {
    val responseFuture: Future[HttpResponse] = call("playercolorlist")
    val result = Await.result(responseFuture.map {
      case HttpResponse(StatusCodes.OK, _, entity, _) =>
        Unmarshal(entity).to[String]
      case _ => ""
    }, Duration.Inf).toString.drop("FullfiledFuture(".length).dropRight(1)
    val map = JSON.parseFull(result).get.asInstanceOf[Map[String, List[String]]]
    map("colors")
  }

  def getPlayerList: List[(String, Int, String)] = {
    val responseFuture: Future[HttpResponse] = call("playerlist")
    val result = Await.result(responseFuture.map {
      case HttpResponse(StatusCodes.OK, _, entity, _) =>
        Unmarshal(entity).to[String]
      case _ => ""
    }, Duration.Inf).toString.drop("FullfiledFuture(".length).dropRight(1)
    val map = JSON.parseFull(result).get.asInstanceOf[Map[String, List[Map[String, Any]]]]
    map("players").map(p => (p("name").toString, p("troops").asInstanceOf[Double].intValue(), p("color").toString))
  }

  def setPlayerTroops(player: String, troops: Int): Unit = {
    val responseFuture: Future[HttpResponse] = call("setplayertroops",
      HttpMethods.POST, FormData(("player", player), ("troops", troops.toString)).toEntity)
    Await.result(responseFuture, Duration.Inf)
  }

  def fromXml(node: Node): Unit = ???

  def getContinentList: List[(String, Int, List[String])] = {
    val responseFuture: Future[HttpResponse] = call("continentlist")
    val result = Await.result(responseFuture.map {
      case HttpResponse(StatusCodes.OK, _, entity, _) =>
        Unmarshal(entity).to[String]
      case _ => ""
    }, Duration.Inf).toString.drop("FullfiledFuture(".length).dropRight(1)
    val map = JSON.parseFull(result).get.asInstanceOf[List[Map[String, Any]]]
    map.map(c => (c("owner").toString, c("btroops").asInstanceOf[Double].intValue(), c("countries").asInstanceOf[List[String]]))
  }
}