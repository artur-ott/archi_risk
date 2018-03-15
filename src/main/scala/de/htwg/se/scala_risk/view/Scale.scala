package de.htwg.se.scala_risk.view
import java.awt.Image
import java.awt.image.BufferedImage
import java.awt.RenderingHints

object Scale {
  def getScaledImage(srcImg: Image, w: Int, h: Int): BufferedImage = {
    val resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    val g2 = resizedImg.createGraphics();

    g2.setRenderingHint(
      RenderingHints.KEY_INTERPOLATION,
      RenderingHints.VALUE_INTERPOLATION_BILINEAR
    );
    g2.drawImage(srcImg, 0, 0, w, h, null);
    g2.dispose();
    return resizedImg;
  }
}

