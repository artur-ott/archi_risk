package de.htwg.se.scala_risk.view

import javax.swing._
import java.awt._
import javax.imageio.ImageIO
import java.io.File
import java.awt._
import java.awt.event.ActionListener
import java.awt.event.ItemListener
import java.awt.event.ItemEvent
import java.awt.event.ActionEvent
import java.awt.event.ItemEvent
import de.htwg.se.scala_risk.controller.GameLogic
import de.htwg.se.scala_risk.util.observer.TObserver
import de.htwg.se.scala_risk.util.Statuses

class EnterPlayers(gameLogic: GameLogic) extends JFrame with ActionListener with ItemListener with TObserver {
  gameLogic.add(this)
  this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
  var listenToUpdates = true
  this.setTitle("Spieler eingeben")
  this.setResizable(false)
  val backgroundImage = Scale.getScaledImage(
    ImageIO.read(getClass().getResource("/images/enter_player.PNG")),
    1169, 780
  )

  var actionListenerActive = true
  val x0 = new JPanel()
  x0.setLayout(new BorderLayout())

  val enter_player_background = new JLabel() { setIcon(new ImageIcon(backgroundImage)) }
  enter_player_background.setLayout(null)

  val continueButton = new JButton("START") { this.setBounds(1000, 307, 100, 425) }
  continueButton.addActionListener(this)

  val player3Check = new JCheckBox("") { this.setBounds(430, 447 + 24, 20, 20) }
  val player4Check = new JCheckBox("") { this.setBounds(430, 518 + 24, 20, 20) }
  val player5Check = new JCheckBox("") { this.setBounds(430, 589 + 24, 20, 20) }
  val player6Check = new JCheckBox("") { this.setBounds(430, 660 + 24, 20, 20) }

  val checkList = Array(player3Check, player4Check, player5Check, player6Check)
  checkList.foreach { x => x.setOpaque(false); x.addItemListener(this) }

  val player1Name = new JTextField("")
  val player2Name = new JTextField("")
  val player3Name = new JTextField("") { this.setVisible(false) }
  val player4Name = new JTextField("") { this.setVisible(false) }
  val player5Name = new JTextField("") { this.setVisible(false) }
  val player6Name = new JTextField("") { this.setVisible(false) }

  val colorstemp = gameLogic.getAvailableColors.toArray
  val colors = colorstemp.+:("")

  val player1Color = new JComboBox(colors)
  val player2Color = new JComboBox(colors)
  val player3Color = new JComboBox(colors) { this.setVisible(false) }
  val player4Color = new JComboBox(colors) { this.setVisible(false) }
  val player5Color = new JComboBox(colors) { this.setVisible(false) }
  val player6Color = new JComboBox(colors) { this.setVisible(false) }

  val comboArray = Array(player1Color, player2Color, player3Color,
    player4Color, player5Color, player6Color)
  comboArray.foreach { x => x.addActionListener(this) }

  val playerGrid = new JPanel() {
    this.setLayout(new GridLayout(6, 3))
    this.add(player1Name); this.add(new JLabel("")); this.add(player1Color)
    this.add(player2Name); this.add(new JLabel("")); this.add(player2Color)
    this.add(player3Name); this.add(new JLabel("")); this.add(player3Color)
    this.add(player4Name); this.add(new JLabel("")); this.add(player4Color)
    this.add(player5Name); this.add(new JLabel("")); this.add(player5Color)
    this.add(player6Name); this.add(new JLabel("")); this.add(player6Color)
    this.setOpaque(false)
    this.setBounds(497, 304, 440, 428)
  }
  enter_player_background.add(playerGrid)
  enter_player_background.add(continueButton)
  enter_player_background.add(player3Check)
  enter_player_background.add(player4Check)
  enter_player_background.add(player5Check)
  enter_player_background.add(player6Check)

  x0.add(enter_player_background, BorderLayout.NORTH)
  this.setContentPane(x0)
  this.pack()
  this.setLocationRelativeTo(null)

  var liste = scala.collection.immutable.List[String]()
  override def actionPerformed(e: ActionEvent) {
    if (this.actionListenerActive) {
      if (comboArray.contains(e.getSource)) {
        this.actionListenerActive = false

        val selectedItem = e.getSource().asInstanceOf[JComboBox[Array[String]]].getSelectedItem().toString()
        var li = scala.collection.immutable.List[String]()

        comboArray.foreach { x => if (x.getSelectedItem != "" && !li.contains(x.getSelectedItem)) { li = li.::(x.getSelectedItem.toString()) } }
        comboArray.foreach { x =>
          {
            val src = x.getSelectedItem.toString()
            x.removeAllItems()
            colors.foreach { y => if (!li.contains(y)) { x.addItem(y) } }
            if (src != "") { x.addItem(src); x.setSelectedItem(src) }
          }
        }
        this.actionListenerActive = true
      }

      if (e.getSource == continueButton) {
        if (player1Name.getText.isEmpty() || player1Color.getSelectedItem.toString().isEmpty() ||
          player2Name.getText.isEmpty() || player2Color.getSelectedItem.toString().isEmpty()) {
          JOptionPane.showMessageDialog(this, "Bitte für mindestens 2 Spieler Name und Farbe eingeben!",
            "Spieler unvollständig", JOptionPane.ERROR_MESSAGE)
        } else {
          gameLogic.setPlayer(player1Name.getText, player1Color.getSelectedItem.toString())
          gameLogic.setPlayer(player2Name.getText, player2Color.getSelectedItem.toString())
          if (player3Check.isSelected()) { gameLogic.setPlayer(player3Name.getText, player3Color.getSelectedItem.toString()) }
          if (player4Check.isSelected()) { gameLogic.setPlayer(player4Name.getText, player4Color.getSelectedItem.toString()) }
          if (player5Check.isSelected()) { gameLogic.setPlayer(player5Name.getText, player5Color.getSelectedItem.toString()) }
          if (player6Check.isSelected()) { gameLogic.setPlayer(player6Name.getText, player6Color.getSelectedItem.toString()) }
          gameLogic.initializeGame

        }
      }
    }
  }

  override def itemStateChanged(e: ItemEvent) {
    if (e.getSource() == player3Check) {
      if (e.getStateChange == ItemEvent.SELECTED) { player3Name.setVisible(true); player3Color.setVisible(true) }
      else { player3Name.setVisible(false); player3Color.setVisible(false); player3Color.setSelectedItem("") }
    }
    if (e.getSource() == player4Check) {
      if (e.getStateChange == ItemEvent.SELECTED) { player4Name.setVisible(true); player4Color.setVisible(true) }
      else { player4Name.setVisible(false); player4Color.setVisible(false); player4Color.setSelectedItem("") }
    }
    if (e.getSource() == player5Check) {
      if (e.getStateChange == ItemEvent.SELECTED) { player5Name.setVisible(true); player5Color.setVisible(true) }
      else { player5Name.setVisible(false); player5Color.setVisible(false); player5Color.setSelectedItem("") }
    }
    if (e.getSource() == player6Check) {
      if (e.getStateChange == ItemEvent.SELECTED) { player6Name.setVisible(true); player6Color.setVisible(true) }
      else { player6Name.setVisible(false); player6Color.setVisible(false); player6Color.setSelectedItem("") }
    }
  }

  def update() {
    if (listenToUpdates) {
      gameLogic.getStatus match {
        case Statuses.INITIALIZE_PLAYERS => {}
        case Statuses.GAME_INITIALIZED => continue()
        //      case Statuses.PLAYER_SPREAD_TROOPS => printSpreadTroops
        //      case Statuses.PLAYER_ATTACK => printAttack
        //      case Statuses.PLAYER_MOVE_TROOPS => printMoveTroops
        //      case Statuses.DIECES_ROLLED => printRolledDieces
        //      case Statuses.PLAYER_CONQUERED_A_COUNTRY => printConquered

        // Errors
        case Statuses.COUNTRY_DOES_NOT_BELONG_TO_PLAYER => println("COUNTRY_DOES_NOT_BELONG_TO_PLAYER")
        case Statuses.NOT_ENOUGH_TROOPS_TO_SPREAD => println("NOT_ENOUGH_TROOPS_TO_SPREAD")
        case Statuses.COUNTRY_NOT_FOUND => println("COUNTRY_NOT_FOUND")
        case Statuses.INVALID_QUANTITY_OF_TROOPS_TO_MOVE => println("INVALID_QUANTITY_OF_TROOPS_TO_MOVE")
        case Statuses.PLAYER_ATTACKING_HIS_COUNTRY => println("PLAYER_ATTACKING_HIS_COUNTRY")
      }
    }

  }

  def continue() {
    this.listenToUpdates = false
    gameLogic.remove(this)
    this.setVisible(false)
    new GUI(gameLogic, false)
  }

}