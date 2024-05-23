package com.jakobniinja;

import static java.awt.Color.BLACK;
import static java.awt.Color.GREEN;
import static java.awt.Color.WHITE;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

public class Cell extends JPanel {

  private static final long serialVersionUID = 1L;

  private static final int SIZE = 20;

  public static final int TOP = 0;

  public static final int RIGHT = 1;

  public static final int BOTTOM = 2;

  public static final int LEFT = 3;

  private int row = -1;

  private int col = -1;

  private boolean[] wall = {true, true, true, true};

  public Cell(int row, int col) {
    this.row = row;
    this.col = col;
  }

  public int getRow() {
    return row;
  }

  public int getCol() {
    return col;
  }

  public boolean isWall(int index) {
    return wall[index];
  }

  public void paintComponent(Graphics g) {
    g.drawLine(0, 0, SIZE, SIZE);
  }

  public Dimension getPreferredSize() {
    return new Dimension(SIZE, SIZE);
  }

  public void printComponent(Graphics g) {
    // draw the background
    g.setColor(WHITE);
    g.fillRect(0, 0, SIZE, SIZE);
    g.setColor(BLACK);
    g.drawRect(0, 0, SIZE, SIZE);
    g.setColor(GREEN);
    g.fillOval(3, 3, SIZE - 6, SIZE - 6);

    // draw the walls
    if (isWall(TOP)) {
      g.drawLine(0, 0, SIZE, 0);
    }
    if (isWall(LEFT)) {
      g.drawLine(0, 0, 0, SIZE);
    }

    // draw the path

    // draw the balls
  }

  public boolean hasAllWalls() {
    return wall[0] && wall[1] && wall[2] && wall[3];
  }

  public void removeWall(int w) {
    wall[w] = false;
    repaint();
  }
}
