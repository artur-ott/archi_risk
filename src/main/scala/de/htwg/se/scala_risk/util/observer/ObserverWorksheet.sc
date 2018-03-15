package de.htwg.se.scala_risk.util.observer

class GUI extends TObserver {
	def update() = controller.getStatus
}

class TUI extends TObserver {
	def update() = println("Tui Upgedatet!")
}

class Model(obs:Obserable) {
	def test(t:Int) = t match {
		case 1 => obs.notifyObservers
		case _ => println("Nicht Upgedatet!")
	}
}

object ObserverWorksheet {
	val gui = new GUI()                       //> gui  : de.htwg.se.scala_risk.util.observer.GUI = de.htwg.se.scala_risk.util.
                                                  //| observer.GUI@4e515669
	val gui1 = new GUI()                      //> gui1  : de.htwg.se.scala_risk.util.observer.GUI = de.htwg.se.scala_risk.util
                                                  //| .observer.GUI@2acf57e3
	val tui = new TUI()                       //> tui  : de.htwg.se.scala_risk.util.observer.TUI = de.htwg.se.scala_risk.util.
                                                  //| observer.TUI@506e6d5e
	val obs = new Obserable()                 //> obs  : de.htwg.se.scala_risk.util.observer.Obserable = de.htwg.se.scala_risk
                                                  //| .util.observer.Obserable@96532d6
	val model = new Model(obs)                //> model  : de.htwg.se.scala_risk.util.observer.Model = de.htwg.se.scala_risk.u
                                                  //| til.observer.Model@3796751b
	obs.add(gui)
	obs.add(gui1)
	obs.add(tui)
	model.test(1)                             //> Gui Upgedatet!
                                                  //| Gui Upgedatet!
                                                  //| Tui Upgedatet!
}