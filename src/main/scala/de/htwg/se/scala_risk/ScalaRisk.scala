package de.htwg.se.scala_risk

import de.htwg.se.scala_risk.view.TUI
import de.htwg.se.scala_risk.view.WelcomeScreen
import com.google.inject.Guice
import java.io.File
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip
import net.codingwell.scalaguice.InjectorExtensions._
import de.htwg.se.scala_risk.controller.impl.{ GameLogic => ImplGameLogic }

object ScalaRisk {
  def main(args: Array[String]): Unit = {
    val injector = Guice.createInjector(new RiskInjector)
    val tui: TUI = injector.instance[TUI]
    val gui: WelcomeScreen = injector.instance[WelcomeScreen]
    gui.setLocationRelativeTo(null)
    gui.setVisible(true)

    while (tui.setNextInput(scala.io.StdIn.readLine())) {}

  }
}