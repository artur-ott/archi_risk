package de.htwg.se.scala_risk

import de.htwg.se.scala_risk.view.TUI
import de.htwg.se.scala_risk.view.WelcomeScreen
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip

import com.google.inject.Guice
import de.htwg.se.scala_risk.controller.impl.{GameLogic => ImplGameLogic}
import net.codingwell.scalaguice.InjectorExtensions._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success, Try}


object ScalaRisk {

  def main(args: Array[String]): Unit = {

    val injector = Guice.createInjector(RiskInjector)

    def startTui : Try[TUI] = {
      val tui : TUI = injector.instance[TUI]
      Try(tui)
    }

    val future1 = Future(startTui)

    future1.onComplete {
      case Success(tui) => println("tui successful started")
      case Failure(e) => println("error starting TUI")
    }

    def startGui : Try[WelcomeScreen] = {
      val gui : WelcomeScreen = injector.instance[WelcomeScreen]
      Try(gui)
    }

    val future2 = Future(startGui)

    future2.onComplete {
      case Success(gui) => {
        gui.get.setLocationRelativeTo(null)
        gui.get.setVisible(true)
        println("gui successful started")
      }
      case Failure(e) => println("error starting GUI")
    }

    // sleep loop is needed here to avoid program exits before futures completed.
    while (true) sleep(1)

    /*
    val clip = AudioSystem.getClip();
    clip.open(AudioSystem.getAudioInputStream(this.getClass().getResource(("/music/after_the_fall.wav"))))
    clip.loop(Clip.LOOP_CONTINUOUSLY);
    Thread.sleep(1000)

    while (tui.setNextInput(scala.io.StdIn.readLine())) {}

*/
  }

  def sleep(time: Long) { Thread.sleep(time) }
}