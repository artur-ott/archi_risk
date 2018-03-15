package de.htwg.se.scala_risk

import com.google.inject.{ AbstractModule, PrivateModule }
import net.codingwell.scalaguice.{ ScalaModule, ScalaPrivateModule }
import de.htwg.se.scala_risk.model.World
import de.htwg.se.scala_risk.model.impl.{ World => ImplWorld }
import de.htwg.se.scala_risk.controller.GameLogic
import de.htwg.se.scala_risk.controller.impl.{ GameLogic => ImplGameLogic }

class RiskInjector extends AbstractModule with ScalaModule {
  def configure(): Unit = {
    bind[World].to[ImplWorld]
    bind[GameLogic].to[ImplGameLogic]
  }
}

