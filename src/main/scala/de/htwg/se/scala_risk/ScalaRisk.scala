package de.htwg.se.scala_risk

import de.htwg.se.scala_risk.view.TUI
import de.htwg.se.scala_risk.view.WelcomeScreen
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip
import de.htwg.se.scala_risk.controller.impl.{ GameLogic => ImplGameLogic }

object ScalaRisk {
  def main(args: Array[String]): Unit = {

    val worldFactory = new WorldFactory()
    val world = worldFactory.getWorld()
    val gameLogic = new ImplGameLogic(world)
    val tui : TUI = new TUI(gameLogic)
    val gui : WelcomeScreen = new WelcomeScreen(gameLogic)

    gui.setLocationRelativeTo(null)
    gui.setVisible(true)

    val clip = AudioSystem.getClip();
    clip.open(AudioSystem.getAudioInputStream(this.getClass().getResource(("/music/after_the_fall.wav"))))
    clip.loop(Clip.LOOP_CONTINUOUSLY);
    Thread.sleep(1000)

    while (tui.setNextInput(scala.io.StdIn.readLine())) {}

  }
}