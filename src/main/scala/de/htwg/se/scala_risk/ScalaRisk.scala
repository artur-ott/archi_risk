package de.htwg.se.scala_risk

import de.htwg.se.scala_risk.view.{TUI, WelcomeScreen, RestView}
import com.google.inject.Guice
import net.codingwell.scalaguice.InjectorExtensions._

object ScalaRisk {
  def main(args: Array[String]): Unit = {
    val injector = Guice.createInjector(new RiskInjector)
    val tui: TUI = injector.instance[TUI]
    val rest: RestView = injector.instance[RestView]
    // TODO: remove escaping
    /*val gui: WelcomeScreen = injector.instance[WelcomeScreen]
    gui.setLocationRelativeTo(null)
    gui.setVisible(true)

    while (tui.setNextInput(scala.io.StdIn.readLine())) {}*/

  }
}