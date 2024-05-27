package com.jakobniinja;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

class TitleLabel extends JLabel {

  private static final long serialVersionUID = 1L;

  public TitleLabel(String text) {
    super(text);
    setFont(new Font(Font.DIALOG, Font.BOLD, 16));
    setBackground(Color.BLACK);
    setForeground(Color.WHITE);
    setOpaque(true);
    setHorizontalAlignment(SwingConstants.CENTER);
  }
}
