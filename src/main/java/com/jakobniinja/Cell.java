package com.jakobniinja;

import static java.awt.Color.BLACK;
import static java.awt.Color.GREEN;
import static java.awt.Color.RED;
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

  private boolean[] path = {false, false, false, false};

  private boolean current = false;

  private boolean end = false;

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

  public void setCurrent(boolean current) {
    this.current = current;
    repaint();
  }

  public void setEnd(boolean end) {
    this.end = end;
    repaint();
  }

  public void printComponent(Graphics g) {
    // Draw the background
    g.setColor(WHITE);
    g.fillRect(0, 0, SIZE, SIZE);
    g.setColor(BLACK);
    g.drawRect(0, 0, SIZE, SIZE);
    g.setColor(GREEN);
    g.fillOval(3, 3, SIZE - 6, SIZE - 6);

    // Draw the walls
    if (isWall(TOP)) {
      g.drawLine(0, 0, SIZE, 0);
    }
    if (isWall(LEFT)) {
      g.drawLine(0, 0, 0, SIZE);
    }

    // Draw the path
    g.setColor(GREEN);
    if (path[TOP]) {
      g.drawLine(SIZE / 2, 0, SIZE / 2, SIZE / 2);
    }
    if (path[BOTTOM]) {
      g.drawLine(SIZE / 2, SIZE, SIZE / 2, SIZE / 2);
    }
    if (path[LEFT]) {
      g.drawLine(0, SIZE / 2, SIZE / 2, SIZE / 2);
    }
    if (path[RIGHT]) {
      g.drawLine(SIZE, SIZE / 2, SIZE / 2, SIZE / 2);
    }

    // Draw the balls
    if (current) {
      g.setColor(GREEN);
      g.fillOval(3, 3, SIZE - 6, SIZE - 6);
    } else if (end) {
      g.setColor(RED);
      g.fillOval(3, 3, SIZE - 6, SIZE - 6);
    }
  }

  public boolean hasAllWalls() {
    return wall[0] && wall[1] && wall[2] && wall[3];
  }

  public void removeWall(int w) {
    wall[w] = false;
    repaint();
  }

  public void openTo(Cell neighbor) {
    if (row < neighbor.getRow()) {
      removeWall(BOTTOM);
      neighbor.removeWall(TOP);
    } else if (row > neighbor.getRow()) {
      removeWall(TOP);
      neighbor.removeWall(BOTTOM);
    } else if (col < neighbor.getCol()) {
      removeWall(RIGHT);
      neighbor.removeWall(LEFT);
    } else if (col > neighbor.getCol()) {
      removeWall(LEFT);
      neighbor.removeWall(RIGHT);
    }
  }

  public void addPath(int side) {
    path[side] = true;
    repaint();
  }
}
