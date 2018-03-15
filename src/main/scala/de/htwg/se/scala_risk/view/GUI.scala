package de.htwg.se.scala_risk.view

import java.awt.Color
import de.htwg.se.scala_risk.util.observer.TObserver
import javax.swing.ImageIcon
import javax.imageio.ImageIO
import java.io.File
import javax.swing._
import java.awt.GridLayout
import java.awt._
import java.awt.event.MouseEvent
import de.htwg.se.scala_risk.controller.GameLogic
import de.htwg.se.scala_risk.controller.impl.{ GameLogic => ImpGameLogic }
import java.awt.event.MouseAdapter
import java.awt.event.ActionListener
import java.awt.event.ActionEvent
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import de.htwg.se.scala_risk.util.Statuses

object GUIJava {
}

class GUI(gL: GameLogic, fsc: Boolean) extends JFrame with TObserver with ActionListener {
  val gameLogic = gL
  val fullscreen = fsc
  /* Register the GUI as a subscriber in the gameLogic.
   * As something changes in the gameLogic, the GUI
   * will be notified.
   */
  gameLogic.add(this)

  /* Close the whole process if the "x" was clicked */
  this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)

  /* Boolean that determines if the game is stopped */
  var running = true

  /* Current status of the game */
  var status = null.asInstanceOf[Statuses.Value]

  /* Define the buttons and labels */
  val gameStatusLabel = new JLabel("GameStatus Label", SwingConstants.CENTER)
  val currentPlayer = new JLabel("Current Player", SwingConstants.CENTER)
  val selectedCountry1 = new JLabel("", SwingConstants.CENTER)
  val selectedCountry2 = new JLabel("", SwingConstants.CENTER)
  val troopsToSpreadLabel = new JLabel("", SwingConstants.CENTER)
  troopsToSpreadLabel.setVisible(false)
  val endTurnButton = new JButton("Zug beenden")
  endTurnButton.addActionListener(this)
  val undoButton = new JButton("Rückgängig")
  undoButton.addActionListener(this)
  undoButton.setBounds(1080, 15, 120, 30)
  undoButton.addActionListener(this)
  
  val leftGrid = new JPanel() {
    this.setLayout(new GridLayout(1, 5))
    this.add(currentPlayer)
    this.add(gameStatusLabel)
    this.add(troopsToSpreadLabel)
    this.add(selectedCountry1)
    this.add(selectedCountry2)
  }
  
  var map_grey = map_grey_small
  var map_ref = map_ref_small
  var map_legend = map_legend_small
  
  /* Small maps */
  /* Plane map (grey) */
  val map_grey_small = Scale.getScaledImage(
    ImageIO.read(getClass().getResource("/images/map_grey.jpg")),
    1238, 810 
  )  
  
  /* Map to be displayed as legend */ 
  val map_legend_small = Scale.getScaledImage(
    ImageIO.read(getClass().getResource("/images/map_legend.png")),
    1238, 810
  )

  /* Reference map (BufferedImage) with different color for each country to determine
   * the country the player selected.
   */
  val map_ref_small = Scale.getScaledImage(
    ImageIO.read(getClass().getResource("/images/map_ref.png")),
    1238, 810
  )
  
  /* Big maps */
  val map_grey_big = Scale.getScaledImage(
    ImageIO.read(getClass().getResource("/images/map_grey.jpg")),
    Toolkit.getDefaultToolkit.getScreenSize.getWidth.toInt, Toolkit.getDefaultToolkit.getScreenSize.getHeight.toInt - 100
  )
  val map_legend_big = Scale.getScaledImage(
    ImageIO.read(getClass().getResource("/images/map_legend.png")),
    Toolkit.getDefaultToolkit.getScreenSize.getWidth.toInt, Toolkit.getDefaultToolkit.getScreenSize.getHeight.toInt - 100
  )
  val map_ref_big = Scale.getScaledImage(
    ImageIO.read(getClass().getResource("/images/map_ref.png")),
    Toolkit.getDefaultToolkit.getScreenSize.getWidth.toInt, Toolkit.getDefaultToolkit.getScreenSize.getHeight.toInt - 100
  )
  
  if (fullscreen) {
    map_grey = map_grey_big
    map_ref = map_ref_big
    map_legend = map_legend_big
  } else {
    map_grey = map_grey_small
    map_ref = map_ref_small
    map_legend = map_legend_small    
  }
  
  /* Map as a Label (Component) */
  val map = getMap()

  /* Create county labels */
  /* Nordamerika */
  val alaska = new JLabel("0", SwingConstants.CENTER) { this.setBounds(73 - 15, 126 - 15, 30, 30) }
  val nwt = new JLabel("0", SwingConstants.CENTER) { this.setBounds(187 - 15, 128 - 15, 30, 30) }
  val alberta = new JLabel("0", SwingConstants.CENTER) { this.setBounds(171 - 15, 187 - 15, 30, 30) }
  val ontario = new JLabel("0", SwingConstants.CENTER) { this.setBounds(247 - 15, 210 - 15, 30, 30) }
  val groenland = new JLabel("0", SwingConstants.CENTER) { this.setBounds(408 - 15, 88 - 15, 30, 30) }
  val ostkanada = new JLabel("0", SwingConstants.CENTER) { this.setBounds(323 - 15, 215 - 15, 30, 30) }
  val weststaaten = new JLabel("0", SwingConstants.CENTER) { this.setBounds(174 - 15, 281 - 15, 30, 30) }
  val oststaaten = new JLabel("0", SwingConstants.CENTER) { this.setBounds(253 - 15, 319 - 15, 30, 30) }
  val mittelamerika = new JLabel("0", SwingConstants.CENTER) { this.setBounds(210 - 15, 410 - 15, 30, 30) }
  /* Suedamerika */
  val venezuela = new JLabel("0", SwingConstants.CENTER) { this.setBounds(268 - 15, 468 - 15, 30, 30) }
  val peru = new JLabel("0", SwingConstants.CENTER) { this.setBounds(293 - 15, 572 - 15, 30, 30) }
  val brasilien = new JLabel("0", SwingConstants.CENTER) { this.setBounds(371 - 15, 555 - 15, 30, 30) }
  val argentinien = new JLabel("0", SwingConstants.CENTER) { this.setBounds(291 - 15, 689 - 15, 30, 30) }
  /* Afrika */
  val nordafrika = new JLabel("0", SwingConstants.CENTER) { this.setBounds(558 - 15, 523 - 15, 30, 30) }
  val aegypten = new JLabel("0", SwingConstants.CENTER) { this.setBounds(651 - 15, 484 - 15, 30, 30) }
  val ostafrika = new JLabel("0", SwingConstants.CENTER) { this.setBounds(726 - 15, 584 - 15, 30, 30) }
  val zentralafrika = new JLabel("0", SwingConstants.CENTER) { this.setBounds(652 - 15, 624 - 15, 30, 30) }
  val suedafrika = new JLabel("0", SwingConstants.CENTER) { this.setBounds(651 - 15, 732 - 15, 30, 30) }
  val madagaskar = new JLabel("0", SwingConstants.CENTER) { this.setBounds(763 - 15, 736 - 15, 30, 30) }
  /* Europa */
  val westeuropa = new JLabel("0", SwingConstants.CENTER) { this.setBounds(509 - 15, 382 - 15, 30, 30) }
  val suedeuropa = new JLabel("0", SwingConstants.CENTER) { this.setBounds(624 - 15, 355 - 15, 30, 30) }
  val nordeuropa = new JLabel("0", SwingConstants.CENTER) { this.setBounds(600 - 15, 275 - 15, 30, 30) }
  val grossbrit = new JLabel("0", SwingConstants.CENTER) { this.setBounds(498 - 15, 265 - 15, 30, 30) }
  val island = new JLabel("0", SwingConstants.CENTER) { this.setBounds(510 - 15, 164 - 15, 30, 30) }
  val skandinavien = new JLabel("0", SwingConstants.CENTER) { this.setBounds(594 - 15, 170 - 15, 30, 30) }
  val russland = new JLabel("0", SwingConstants.CENTER) { this.setBounds(704 - 15, 232 - 15, 30, 30) }
  /* Asien */
  val mittosten = new JLabel("0", SwingConstants.CENTER) { this.setBounds(741 - 15, 445 - 15, 30, 30) }
  val afghanistan = new JLabel("0", SwingConstants.CENTER) { this.setBounds(815 - 15, 317 - 15, 30, 30) }
  val ural = new JLabel("0", SwingConstants.CENTER) { this.setBounds(841 - 15, 206 - 15, 30, 30) }
  val sibirien = new JLabel("0", SwingConstants.CENTER) { this.setBounds(903 - 15, 159 - 15, 30, 30) }
  val jakutien = new JLabel("0", SwingConstants.CENTER) { this.setBounds(989 - 15, 109 - 15, 30, 30) }
  val kamtschatka = new JLabel("0", SwingConstants.CENTER) { this.setBounds(1074 - 15, 114 - 15, 30, 30) }
  val japan = new JLabel("0", SwingConstants.CENTER) { this.setBounds(1114 - 15, 302 - 15, 30, 30) }
  val mongolei = new JLabel("0", SwingConstants.CENTER) { this.setBounds(994 - 15, 288 - 15, 30, 30) }
  val irkutsk = new JLabel("0", SwingConstants.CENTER) { this.setBounds(970 - 15, 213 - 15, 30, 30) }
  val china = new JLabel("0", SwingConstants.CENTER) { this.setBounds(965 - 15, 375 - 15, 30, 30) }
  val indien = new JLabel("0", SwingConstants.CENTER) { this.setBounds(889 - 15, 446 - 15, 30, 30) }
  val suedostasien = new JLabel("0", SwingConstants.CENTER) { this.setBounds(990 - 15, 470 - 15, 30, 30) }
  /* Australien */
  val indonesien = new JLabel("0", SwingConstants.CENTER) { this.setBounds(1008 - 15, 596 - 15, 30, 30) }
  val neuguinea = new JLabel("0", SwingConstants.CENTER) { this.setBounds(1114 - 15, 576 - 15, 30, 30) }
  val westaustr = new JLabel("0", SwingConstants.CENTER) { this.setBounds(1053 - 15, 731 - 15, 30, 30) }
  val ostaustr = new JLabel("0", SwingConstants.CENTER) { this.setBounds(1165 - 15, 735 - 15, 30, 30) }

  val countryArray = Array(alaska, nwt, alberta, ontario, ostkanada, groenland, weststaaten, oststaaten, mittelamerika,
    venezuela, peru, brasilien, argentinien, nordafrika, aegypten, ostafrika, zentralafrika,
    suedafrika, madagaskar, westeuropa, suedeuropa, nordeuropa, grossbrit, island,
    skandinavien, russland, mittosten, afghanistan, ural, sibirien, jakutien,
    kamtschatka, japan, mongolei, irkutsk, china, indien, suedostasien, indonesien,
    neuguinea, westaustr, ostaustr)

  /* Build the frame */
  this.setTitle("SCALA_RISK")
  this.setResizable(false)
  this.setJMenuBar(new GUIMenuBar(this))

  val x0 = new JPanel()
  x0.setLayout(new BorderLayout())

  val x1 = new JPanel()
  x1.setLayout(new BoxLayout(x1, BoxLayout.Y_AXIS))
  map.setLayout(null)
  countryArray.foreach { x => map.add(x) }
  map.add(undoButton)
  x1.add(map)

  val x2 = new JPanel()
  x2.setLayout(new GridLayout(1, 4))
  x2.add(leftGrid)
  x2.add(endTurnButton)

  x0.add(x1, BorderLayout.NORTH)
  x0.add(x2, BorderLayout.CENTER)
//<<<<<<< HEAD
  
  if (fullscreen) {
    this.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH)
    this.setUndecorated(true)    
  } else {
  x0.setPreferredSize(new Dimension(1238, 950)) 
  }

//=======
//  x0.setPreferredSize(new Dimension(1238, 950))
////  this.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH)
////  this.setUndecorated(true)
//>>>>>>> c37e9736bdd16b49a9589d329ce6a23d85bd6b84
  this.setContentPane(x0)
  this.pack()
  
  /* Position in the center of the screen */
  this.setLocationRelativeTo(null)
  this.setVisible(true)

  def getMap(): JLabel = {
    val parent = this
    val map = new JLabel {
      this.setIcon(new ImageIcon(map_grey))
      this.addMouseListener(new MouseAdapter() {

        override def mouseClicked(e: MouseEvent) {
          if (running) {
            /* Determine the color of the country in the reference map */
            val country = map_ref.getRGB(e.getPoint.x, e.getPoint.y)
            //println(map_ref.getRGB(e.getPoint.x, e.getPoint.y))
            val c = new Color(country)
            val r = c.getRed
            val g = c.getGreen
            val b = c.getBlue
            //println(r,g,b)
            parent.status match {
              case Statuses.PLAYER_SPREAD_TROOPS =>
                updateTroopsSpreadLabel()
                troopsToSpreadLabel.setText(gameLogic.getTroopsToSpread.toString())
                troopsToSpreadLabel.setVisible(true)
                spreadTroops(country)
              case Statuses.PLAYER_ATTACK => { attack(country) }
              case Statuses.PLAYER_MOVE_TROOPS => moveTroopsEnd(country)
            }
          }
        }
      })
    }
    //map.setPreferredSize(new Dimension(1238, 810))
    return map
  }

  def updateTroopCount(country: Int, troops: Int) {
    val selectedCountry = getSelectedCountry(country)
    val countryLabel = selectedCountry._2
    if (country != 0) {
      countryLabel.setText((Integer.valueOf(countryLabel.getText()) + troops).toString())
    }

  }

  def getSelectedCountry(c: Int): (Int, JLabel) = {
    var selectedCountryLabel = null.asInstanceOf[JLabel]
    var selectedCountry = 0
    var country = ""
    gameLogic.getCountries.foreach { x => if (c == x._4) { country = x._1.toUpperCase() } }
    country match {
      case "ALASKA" => { selectedCountryLabel = alaska }
      case "NORDWEST-TERRITORIEN" => { selectedCountryLabel = nwt }
      case "GROENLAND" => { selectedCountryLabel = groenland }
      case "ALBERTA" => { selectedCountryLabel = alberta }
      case "ONTARIO" => { selectedCountryLabel = ontario }
      case "OSTKANADA" => { selectedCountryLabel = ostkanada }
      case "WESTSTAATEN" => { selectedCountryLabel = weststaaten }
      case "OSTSTAATEN" => { selectedCountryLabel = oststaaten }
      case "MITTELAMERIKA" => { selectedCountryLabel = mittelamerika }
      case "VENEZUELA" => { selectedCountryLabel = venezuela }
      case "PERU" => { selectedCountryLabel = peru }
      case "BRASILIEN" => { selectedCountryLabel = brasilien }
      case "ARGENTINIEN" => { selectedCountryLabel = argentinien }
      case "NORDAFRIKA" => { selectedCountryLabel = nordafrika }
      case "AEGYPTEN" => { selectedCountryLabel = aegypten }
      case "ZENTRALAFRIKA" => { selectedCountryLabel = zentralafrika }
      case "OSTAFRIKA" => { selectedCountryLabel = ostafrika }
      case "SUEDAFRIKA" => { selectedCountryLabel = suedafrika }
      case "MADAGASKAR" => { selectedCountryLabel = madagaskar }
      case "WESTEUROPA" => { selectedCountryLabel = westeuropa }
      case "GROSSBRITANNIEN" => { selectedCountryLabel = grossbrit }
      case "ISLAND" => { selectedCountryLabel = island }
      case "SUEDEUROPA" => { selectedCountryLabel = suedeuropa }
      case "NORDEUROPA" => { selectedCountryLabel = nordeuropa }
      case "SKANDINAVIEN" => { selectedCountryLabel = skandinavien }
      case "RUSSLAND" => { selectedCountryLabel = russland }
      case "MITTLERER-OSTEN" => { selectedCountryLabel = mittosten }
      case "AFGHANISTAN" => { selectedCountryLabel = afghanistan }
      case "URAL" => { selectedCountryLabel = ural }
      case "SIBIRIEN" => { selectedCountryLabel = sibirien }
      case "JAKUTIEN" => { selectedCountryLabel = jakutien }
      case "KAMTSCHATKA" => { selectedCountryLabel = kamtschatka }
      case "JAPAN" => { selectedCountryLabel = japan }
      case "IRKUTSK" => { selectedCountryLabel = irkutsk }
      case "MONGOLEI" => { selectedCountryLabel = mongolei }
      case "CHINA" => { selectedCountryLabel = china }
      case "INDIEN" => { selectedCountryLabel = indien }
      case "SUEDOSTASIEN" => { selectedCountryLabel = suedostasien }
      case "INDONESIEN" => { selectedCountryLabel = indonesien }
      case "NEUGUINEA" => { selectedCountryLabel = neuguinea }
      case "WESTAUSTRALIEN" => { selectedCountryLabel = westaustr }
      case "OSTAUSTRALIEN" => { selectedCountryLabel = ostaustr }

    }

    selectedCountry = c
    return (selectedCountry, selectedCountryLabel)
  }

  var counter = 0
  def repaintCountry(country: Int, color: Int) {
    if (country != 0) {
      //println("Color: " + color)
      if (color != 0) {
        //println("not null")
        for (y <- 0 to map_grey.getHeight - 1) {
          for (x <- 0 to map_grey.getWidth - 1) {
            if (map_ref.getRGB(x, y) == country) {
              map_grey.setRGB(x, y, color)
            }
          }
        }
      }
    }
    map.repaint()
    //this.map.setIcon(new ImageIcon(map_legend))

  }

  /* Colorize all countries according to the color of the owner. */
  initialize()
  def initialize() {
    gameLogic.getCountries.foreach { x => repaintCountry(x._4, gameLogic.getOwnerColor(x._2)) }
    updateLabels()
  }

  def getCountryFromColor(color: Int): String = {
    var country = ""
    gameLogic.getCountries.foreach { x => if (x._4 == color) { country = x._1 } }
    return country
  }

  override def actionPerformed(e: ActionEvent) {
    if (e.getSource == endTurnButton) {
      val c = JOptionPane.showConfirmDialog(
        this,
        "Wollen Sie Ihren Zug beenden?",
        "Beenden?",
        JOptionPane.YES_NO_OPTION
      )
      if (c == JOptionPane.YES_OPTION) {
        clearActionCountries()
        gameLogic.endTurn
      }
    }
    if (e.getSource == undoButton) {
      this.gameLogic.undo
      this.initialize()
      this.updatePlayerLabel()
      this.updateTroopsSpreadLabel()

      gameLogic.getStatus match {
        case Statuses.PLAYER_ATTACK =>
          troopsToSpreadLabel.setVisible(false)
          this.endTurnButton.setEnabled(true)
        case Statuses.PLAYER_MOVE_TROOPS =>
          troopsToSpreadLabel.setVisible(false)
          this.endTurnButton.setEnabled(true)
        case Statuses.PLAYER_SPREAD_TROOPS =>
          troopsToSpreadLabel.setVisible(true)
          this.endTurnButton.setEnabled(false)

      }
    }
  }

  def update() {
    gameLogic.getStatus match {
      case Statuses.PLAYER_SPREAD_TROOPS =>
        setStatusText("Aufrüsten")
        updateTroopsSpreadLabel()
        this.troopsToSpreadLabel.setVisible(true)
        this.endTurnButton.setEnabled(false)
        updatePlayerLabel()
        //clearActionCountries()
        this.status = Statuses.PLAYER_SPREAD_TROOPS
      case Statuses.PLAYER_ATTACK =>
        setStatusText("Angreifen")
        troopsToSpreadLabel.setVisible(false);
        this.endTurnButton.setEnabled(true)
        this.updateLabels()
        this.status = Statuses.PLAYER_ATTACK
      case Statuses.PLAYER_MOVE_TROOPS =>
        setStatusText("Verschieben")
        this.status = Statuses.PLAYER_MOVE_TROOPS
      case Statuses.DIECES_ROLLED => rollDices()
      case Statuses.PLAYER_CONQUERED_A_COUNTRY => if (this.running) {
        updateLabels()
        repaintCountry(
          gameLogic.getAttackerDefenderCountries._2._4,
          gameLogic.getOwnerColor(gameLogic.getAttackerDefenderCountries._1._2)
        )
        moveTroops()
      }
      case Statuses.PLAYER_CONQUERED_A_CONTINENT => if (this.running) {
        conqueredAContinent()
        updateLabels()
        repaintCountry(
          gameLogic.getAttackerDefenderCountries._2._4,
          gameLogic.getOwnerColor(gameLogic.getAttackerDefenderCountries._1._2)
        )
        moveTroops()
      }

      // Errors
      case Statuses.NOT_ENOUGH_TROOPS_TO_ATTACK => {}
      case Statuses.COUNTRY_DOES_NOT_BELONG_TO_PLAYER => notYourCountry()
      case Statuses.NOT_ENOUGH_TROOPS_TO_SPREAD => println("NOT_ENOUGH_TROOPS_TO_SPREAD")
      case Statuses.COUNTRY_NOT_FOUND => println("COUNTRY_NOT_FOUND")
      case Statuses.INVALID_QUANTITY_OF_TROOPS_TO_MOVE => println("INVALID_QUANTITY_OF_TROOPS_TO_MOVE")
      case Statuses.PLAYER_ATTACKING_HIS_COUNTRY => yourOwnCountry()
      case Statuses.NOT_A_NEIGHBORING_COUNTRY => notANeighboringCountry()
    }
  }

  /* List of countries (name, unique color) to operate with (attack, add troops, ...) */
  var actionCountries = scala.List[(String, String, Int, Int)]() /* Countryname, ownername, troops, refcolor */

  def spreadTroops(country: Int) {
    var countryName = ""
    gameLogic.getCountries.foreach { x => if (x._4 == country) { actionCountries = actionCountries.:+(x); countryName = x._1 } }
    if (actionCountries.length == 1 && gameLogic.getTroopsToSpread > 0) {
      gameLogic.addTroops(countryName, 1)
      updateLabels()
      actionCountries = scala.List[(String, String, Int, Int)]()
    }
  }

  def attack(country: Int) {
    gameLogic.getCountries.foreach { x => if (x._4 == country) { actionCountries = actionCountries.:+(x) } }
    if (actionCountries.length == 1) {
      if (actionCountries(0)._2 == gameLogic.getCurrentPlayer._1) {
        this.selectedCountry1.setText(actionCountries(0)._1)
      }
    }
    if (actionCountries.length == 2) {
      this.selectedCountry2.setText(actionCountries(1)._1)
      gameLogic.attack(actionCountries(0)._1, actionCountries(1)._1)
      updateLabels()
      if (this.running) {
        clearActionCountries()
      }
    }
  }

  def notYourCountry() {
    JOptionPane.showMessageDialog(this, "Dieses Land gehört Dir (noch) nicht!",
      "Nicht dein Land!", JOptionPane.ERROR_MESSAGE)
  }

  def yourOwnCountry() {
    JOptionPane.showMessageDialog(this, "Dieses Land gehört Dir selbst!",
      "Dein eigenes Land!", JOptionPane.ERROR_MESSAGE)
  }

  def notANeighboringCountry() {
    JOptionPane.showMessageDialog(this, "Dieses Land ist kein Nachbarland!",
      "Kein Nachbarland!", JOptionPane.ERROR_MESSAGE)
  }

  def updateLabels() {
    gameLogic.getCountries.foreach { x => getSelectedCountry(x._4)._2.setText(x._3.toString()) }
  }

  def updatePlayerLabel() {
    this.currentPlayer.setText("" + gameLogic.getCurrentPlayer._1 + ",   " + gameLogic.getCurrentPlayer._2)
  }

  def rollDices() {
    if (running && !gameLogic.getRolledDieces._1.isEmpty) {
      this.setEnabled(false)
      this.running = false
      val parent = this
      val dices = new Dices(gameLogic)
      dices.addWindowListener(new WindowAdapter() {
        override def windowClosing(e: WindowEvent) {
          gameLogic.remove(dices)
          parent.setEnabled(true)
          parent.running = true
          parent.clearActionCountries()
          parent.updateLabels()
          parent.update()
        }
      })

      dices.setVisible(true)
    } else {
      if (gameLogic.getRolledDieces._1.isEmpty) {
        this.toFront()
      }

    }

  }

  def moveTroops() {
    val t = scala.Array.range(1, gameLogic.getAttackerDefenderCountries._1._3)
    val troopsToMove = t.map { x => x.asInstanceOf[Object] }

    val choice = JOptionPane.showInputDialog(this, "Wie viele Truppen sollen im neu eroberten Land stationiert werden?",
      "Stationiere Truppen...", JOptionPane.PLAIN_MESSAGE,
      null, troopsToMove,
      "1")
    var choiceAsInt = 1
    if (choice != null) {
      choiceAsInt = Integer.valueOf(choice.toString())
    }
    gameLogic.moveTroops(choiceAsInt)
  }

  def moveTroopsEnd(countryColor: Int) {
    var country = null.asInstanceOf[(String, String, Int, Int)]

    gameLogic.getCountries.foreach { x => if (x._4 == countryColor) { country = x } }
    if (country != null && country._2 == gameLogic.getCurrentPlayer._1) {
      actionCountries = actionCountries.:+(country)
    }

    if (country != null && actionCountries.length == 1) {
      this.selectedCountry1.setText(country._1)
    }

    if (country != null && actionCountries.length == 2) {
      this.selectedCountry2.setText(actionCountries(1)._1)
      if (actionCountries(0)._2 == gameLogic.getCurrentPlayer._1) {       
        val t = scala.Array.range(0, actionCountries(0)._3)
        val troopsToMove = t.map { x => x.asInstanceOf[Object] }

        val choice = JOptionPane.showInputDialog(this, "Wie viele Truppen möchten Sie verschieben?",
          "Verschiebe Truppen...", JOptionPane.PLAIN_MESSAGE,
          null, troopsToMove,
          "0")

        
        var choiceAsInt = 0
        if (choice != null) {
          choiceAsInt = Integer.valueOf(choice.toString())
        }
        gameLogic.dragTroops(actionCountries(0)._1, actionCountries(1)._1, choiceAsInt)
        updateLabels()
      }
      clearActionCountries()
    }
  }

  def setStatusText(text: String) {
    this.gameStatusLabel.setText(text)
  }

  def conqueredAContinent() {
    JOptionPane.showMessageDialog(this, "Kontinent erobert!",
      "SIEG!", JOptionPane.INFORMATION_MESSAGE)
  }

  def clearActionCountries() {
    this.actionCountries = scala.List[(String, String, Int, Int)]()
    this.selectedCountry1.setText("")
    this.selectedCountry2.setText("")
  }

  def loadGame() {
    gameLogic.loadGame
    this.initialize()
    this.updatePlayerLabel()
    this.updateTroopsSpreadLabel()
  }

  def saveGame() {
    gameLogic.saveGame
  }

  def updateTroopsSpreadLabel() {
    this.troopsToSpreadLabel.setText(gameLogic.getTroopsToSpread.toString())
  }

}

