package de.htwg.se.scala_risk.view

import javax.swing._
import java.awt._
import javax.imageio.ImageIO
import java.io.File
import java.awt._
import java.awt.event.ActionListener
import java.awt.event.ActionEvent
import de.htwg.se.scala_risk.controller.GameLogic
import de.htwg.se.scala_risk.util.observer.TObserver
import de.htwg.se.scala_risk.util.Statuses
import java.awt.event.WindowEvent

class Dices(gameLogic: GameLogic) extends JFrame with ActionListener with TObserver {

  gameLogic.add(this)
  this.setTitle("Würfeln...")
  this.setResizable(false)
  var running = true
  val backgroundImage = Scale.getScaledImage(
    ImageIO.read(getClass().getResource("/images/dices_background.jpg")),
    504, 753
  )

  val dice_1 = new JLabel() { this.setBounds(75, 338, 95, 95) }
  val dice_2 = new JLabel() { this.setBounds(75, 448, 95, 95) }
  val dice_3 = new JLabel() { this.setBounds(75, 558, 95, 95) }
  val dice_4 = new JLabel() { this.setBounds(335, 338, 95, 95) }
  val dice_5 = new JLabel() { this.setBounds(335, 448, 95, 95) }
  val dice_6 = new JLabel() { this.setBounds(335, 558, 95, 95) }

  val diceLabelListA = Array(dice_1, dice_2, dice_3)
  val diceLabelListB = Array(dice_4, dice_5, dice_6)

  val dice1_img = Scale.getScaledImage(
    ImageIO.read(getClass().getResource("/images/1.jpg")),
    95, 95
  )
  val dice2_img = Scale.getScaledImage(
    ImageIO.read(getClass().getResource("/images/2.jpg")),
    95, 95
  )
  val dice3_img = Scale.getScaledImage(
    ImageIO.read(getClass().getResource("/images/3.jpg")),
    95, 95
  )
  val dice4_img = Scale.getScaledImage(
    ImageIO.read(getClass().getResource("/images/4.jpg")),
    95, 95
  )
  val dice5_img = Scale.getScaledImage(
    ImageIO.read(getClass().getResource("/images/5.jpg")),
    95, 95
  )
  val dice6_img = Scale.getScaledImage(
    ImageIO.read(getClass().getResource("/images/6.jpg")),
    95, 95
  )

  val diceImageList = Array(dice1_img, dice2_img, dice3_img, dice4_img, dice5_img, dice6_img)

  /* Get current dices */

  var diceStatus = gameLogic.getRolledDieces

  def updateAttackerDices() {
    var i = 0
    for (i <- 0 to diceStatus._1.length - 1) {
      diceStatus._1(i) match {
        case 1 =>
          diceLabelListA(i).setIcon(new ImageIcon(dice1_img)); diceLabelListA(i).setVisible(true)
        case 2 =>
          diceLabelListA(i).setIcon(new ImageIcon(dice2_img)); diceLabelListA(i).setVisible(true)
        case 3 =>
          diceLabelListA(i).setIcon(new ImageIcon(dice3_img)); diceLabelListA(i).setVisible(true)
        case 4 =>
          diceLabelListA(i).setIcon(new ImageIcon(dice4_img)); diceLabelListA(i).setVisible(true)
        case 5 =>
          diceLabelListA(i).setIcon(new ImageIcon(dice5_img)); diceLabelListA(i).setVisible(true)
        case 6 =>
          diceLabelListA(i).setIcon(new ImageIcon(dice6_img)); diceLabelListA(i).setVisible(true)
        case _ =>
      }
    }
    if (diceStatus._1.length != 3) {
      var l = 0
      for (l <- diceStatus._1.length to 2) {
        diceLabelListA(l).setVisible(false)
      }
    }
  }

  def updateDefenderDices() {
    var j = 0
    for (j <- 0 to diceStatus._2.length - 1) {
      diceStatus._2(j) match {
        case 1 =>
          diceLabelListB(j).setIcon(new ImageIcon(dice1_img)); diceLabelListB(j).setVisible(true)
        case 2 =>
          diceLabelListB(j).setIcon(new ImageIcon(dice2_img)); diceLabelListB(j).setVisible(true)
        case 3 =>
          diceLabelListB(j).setIcon(new ImageIcon(dice3_img)); diceLabelListB(j).setVisible(true)
        case 4 =>
          diceLabelListB(j).setIcon(new ImageIcon(dice4_img)); diceLabelListB(j).setVisible(true)
        case 5 =>
          diceLabelListB(j).setIcon(new ImageIcon(dice5_img)); diceLabelListB(j).setVisible(true)
        case 6 =>
          diceLabelListB(j).setIcon(new ImageIcon(dice6_img)); diceLabelListB(j).setVisible(true)
        case _ =>
      }
    }
    if (diceStatus._2.length != 3) {
      var l = 0
      for (l <- diceStatus._2.length to 2) {
        diceLabelListB(l).setVisible(false)
      }
    }
  }

  updateAttackerDices()
  updateDefenderDices()

  val x0 = new JPanel()
  x0.setLayout(new BorderLayout())

  val dices_background = new JLabel() { setIcon(new ImageIcon(backgroundImage)) }

  val panelName = new JPanel()
  panelName.setLayout(new GridLayout(3, 3))
  panelName.setBounds(43, 118, 390, 200) //43 417

  val attackerName = this.gameLogic.getAttackerDefenderCountries._1._2
  val defenderName = this.gameLogic.getAttackerDefenderCountries._2._2
  val attackerCountry = this.gameLogic.getAttackerDefenderCountries._1._1
  val defenderCountry = this.gameLogic.getAttackerDefenderCountries._2._1

  val country1Label = new JLabel(attackerCountry, SwingConstants.CENTER)
  val country2Label = new JLabel(defenderCountry, SwingConstants.CENTER)
  val player1Name = new JLabel(attackerName, SwingConstants.CENTER)
  val player2Name = new JLabel(defenderName, SwingConstants.CENTER)

  dices_background.setLayout(null)

  panelName.add(player1Name)
  panelName.add(new JLabel(""))
  panelName.add(player2Name)
  panelName.add(country1Label)
  panelName.add(new JLabel(""))
  panelName.add(country2Label)
  panelName.add(new JLabel(""))
  panelName.add(new JLabel(""))
  panelName.add(new JLabel(""))
  panelName.setOpaque(false)

  val roll_dice = new JButton("Würfeln") { this.setBounds(175, 270, 155, 40) }
  roll_dice.addActionListener(this)
  val diceArray = Array(dice_1, dice_2, dice_3, dice_4, dice_5, dice_6)
  diceArray.foreach { x => dices_background.add(x) }
  dices_background.add(roll_dice)
  dices_background.add(panelName)

  x0.add(dices_background, BorderLayout.NORTH)
  this.setContentPane(x0)
  this.pack()

  override def actionPerformed(e: ActionEvent) {
    if (e.getSource == roll_dice) {
      gameLogic.attack(attackerCountry, defenderCountry)

    }
  }

  def update() {
    gameLogic.getStatus match {
      case Statuses.DIECES_ROLLED => rollAgain()
      case Statuses.PLAYER_CONQUERED_A_COUNTRY => conquered()
      case Statuses.PLAYER_ATTACK =>
      //      // Errors
      case Statuses.NOT_ENOUGH_TROOPS_TO_ATTACK => { notEnoughTroops() }
      case Statuses.PLAYER_CONQUERED_A_CONTINENT => conquered()

    }
  }

  def rollAgain() {
    this.diceStatus = gameLogic.getRolledDieces
    updateAttackerDices()
    updateDefenderDices()
  }

  def conquered() {
    if (running) {
      this.running = false
      JOptionPane.showMessageDialog(this, "Land erfolgreich erobert!",
        "SIEG!", JOptionPane.INFORMATION_MESSAGE)
      this.setVisible(false)
      gameLogic.remove(this)
      this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
  }

  def notEnoughTroops() {
    JOptionPane.showMessageDialog(this, "Nicht genügend Truppen zum Angreifen!",
      "Nicht genügend Truppen...", JOptionPane.ERROR_MESSAGE)
    this.setVisible(false)
    gameLogic.remove(this)
    this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
  }

}