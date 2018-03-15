//<<<<<<< HEAD
package de.htwg.se.scala_risk.view

import javax.swing._
import java.awt.event.ActionListener
import java.awt.event.ActionEvent
import java.awt.event.KeyEvent;

class GUIMenuBar(parent: GUI) extends JMenuBar with ActionListener {
  /* Create menu. */
  val menu = new JMenu("Spiel");
  menu.setMnemonic(KeyEvent.VK_S);

  /* Create item1. */
  val item1 = new JMenuItem("Zeige Kontinente");
  item1.addActionListener(this);

  /* Create item2. */
  val item2 = new JMenuItem("Beenden");
  item2.addActionListener(this);

  /* Create item3. */
  val item3 = new JMenuItem("Spiel fortsetzen");
  item3.addActionListener(this);

  /* Create item4. */
  val item4 = new JMenuItem("Spiel laden");
  item4.addActionListener(this);

  /* Create item5. */
  val item5 = new JMenuItem("Spiel speichern");
  item5.addActionListener(this);
  
  /* Create item6 */
  val item6 = new JCheckBoxMenuItem("Vollbild")
  if (parent.fullscreen)
    item6.setSelected(true)
  else
    item6.setSelected(false)
  item6.addActionListener(this)

  /* Add items to menu. */
  menu.add(item4)
  menu.add(item5)
  menu.addSeparator()
  menu.add(item1)
  menu.add(item3)
  menu.addSeparator();
  menu.add(item2);
  menu.add(item6)

  this.add(menu)

  override def actionPerformed(e: ActionEvent) {
    if (e.getSource == item2) {
      val c = JOptionPane.showConfirmDialog(
        this.getParent,
        "Wollen Sie das Spiel wirklich beenden?",
        "Beenden?",
        JOptionPane.YES_NO_OPTION
      )
      if (c == JOptionPane.YES_OPTION) {
        System.exit(0)
      }
    }
    if (e.getSource == item4) {
      parent.loadGame()
    }
    if (e.getSource == item5) {
      parent.saveGame()
    }

    if (e.getSource == item1) {
      parent.running = false
      parent.countryArray.foreach { x => x.setVisible(false) }
      parent.map.setIcon(new ImageIcon(parent.map_legend))
    }

    if (e.getSource == item3) {
      parent.running = true
      parent.countryArray.foreach { x => x.setVisible(true) }
      parent.map.setIcon(new ImageIcon(parent.map_grey))
    }
    
    if (e.getSource == item6) {
      val checkBox = e.getSource.asInstanceOf[JCheckBoxMenuItem]
      var gui = null.asInstanceOf[GUI]
      if (checkBox.isSelected()) {
        gui = new GUI(parent.gameLogic, true)
      } else {
        gui = new GUI(parent.gameLogic, false)
      }
      gui.update()
      parent.gameLogic.remove(parent)
      parent.dispose()

    }
  }

//=======
//package de.htwg.se.scala_risk.view
//
//import javax.swing._
//import java.awt.event.ActionListener
//import java.awt.event.ActionEvent
//import java.awt.event.KeyEvent;
//
//class GUIMenuBar(parent: GUI) extends JMenuBar with ActionListener {
//  /* Create menu. */
//  val menu = new JMenu("Spiel");
//  menu.setMnemonic(KeyEvent.VK_S);
//
//  /* Create item1. */
//  val item1 = new JMenuItem("Zeige Kontinente");
//  item1.addActionListener(this);
//
//  /* Create item2. */
//  val item2 = new JMenuItem("Beenden");
//  item2.addActionListener(this);
//
//  /* Create item3. */
//  val item3 = new JMenuItem("Spiel fortsetzen");
//  item3.addActionListener(this);
//
//  /* Create item4. */
//  val item4 = new JMenuItem("Spiel laden");
//  item4.addActionListener(this);
//
//  /* Create item5. */
//  val item5 = new JMenuItem("Spiel speichern");
//  item5.addActionListener(this);
//  
//  /* Create item6 */
//  val item6 = new JMenuItem("Vollbild");
//  item6.addActionListener(this);
//
//  /* Add items to menu. */
//  menu.add(item4)
//  menu.add(item5)
//  menu.addSeparator()
//  menu.add(item1)
//  menu.add(item3)
//  menu.addSeparator();
//  menu.add(item2)
//  menu.add(item6)
//
//  this.add(menu)
//
//  override def actionPerformed(e: ActionEvent) {
//    if (e.getSource == item2) {
//      val c = JOptionPane.showConfirmDialog(
//        this.getParent,
//        "Wollen Sie das Spiel wirklich beenden?",
//        "Beenden?",
//        JOptionPane.YES_NO_OPTION
//      )
//      if (c == JOptionPane.YES_OPTION) {
//        System.exit(0)
//      }
//    }
//    if (e.getSource == item4) {
//      parent.loadGame()
//    }
//    if (e.getSource == item5) {
//      parent.saveGame()
//    }
//
//    if (e.getSource == item1) {
//      parent.running = false
//      parent.countryArray.foreach { x => x.setVisible(false) }
//      parent.map.setIcon(new ImageIcon(parent.map_legend))
//    }
//
//    if (e.getSource == item3) {
//      parent.running = true
//      parent.countryArray.foreach { x => x.setVisible(true) }
//      parent.map.setIcon(new ImageIcon(parent.map_grey))
//    }
//    
//    if (e.getSource == item6) {
//      val newFrame = new JFrame()
//      newFrame.add(parent.getContentPane)
//      newFrame.setJMenuBar(this)
//      newFrame.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH)
//      newFrame.setUndecorated(true)
//      newFrame.setVisible(true)
//      
//      parent.dispose()
//
//    }
//  }
}