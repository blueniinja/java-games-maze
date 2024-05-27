package com.jakobniinja;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

class Cell extends JPanel {

  private static final long serialVersionUID = 1L;

  private static final int SIZE = 20;

  public static final int TOP = 0;

  public static final int RIGHT = 1;

  public static final int BOTTOM = 2;

  public static final int LEFT = 3;

  private int row, col;

  private boolean[] wall = {true, true, true, true};

  private boolean[] path = {false, false, false, false};

  private boolean current = false;

  private boolean end = false;

  public Cell(int row, int col) {
    this.row = row;
    this.col = col;
    setPreferredSize(new Dimension(SIZE, SIZE));
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

  public boolean hasAllWalls() {
    return wall[TOP] && wall[RIGHT] && wall[BOTTOM] && wall[LEFT];
  }

  public void setCurrent(boolean current) {
    this.current = current;
    repaint();
  }

  public void setEnd(boolean end) {
    this.end = end;
    repaint();
  }

  public void addPath(int side) {
    path[side] = true;
    repaint();
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    g.setColor(Color.WHITE);
    g.fillRect(0, 0, SIZE, SIZE);
    g.setColor(Color.BLACK);
    if (wall[TOP]) {
      g.drawLine(0, 0, SIZE, 0);
    }
    if (wall[RIGHT]) {
      g.drawLine(SIZE, 0, SIZE, SIZE);
    }
    if (wall[BOTTOM]) {
      g.drawLine(0, SIZE, SIZE, SIZE);
    }
    if (wall[LEFT]) {
      g.drawLine(0, 0, 0, SIZE);
    }

    g.setColor(Color.GREEN);
    if (path[TOP]) {
      g.drawLine(SIZE / 2, 0, SIZE / 2, SIZE / 2);
    }
    if (path[RIGHT]) {
      g.drawLine(SIZE / 2, SIZE / 2, SIZE, SIZE / 2);
    }
    if (path[BOTTOM]) {
      g.drawLine(SIZE / 2, SIZE / 2, SIZE / 2, SIZE);
    }
    if (path[LEFT]) {
      g.drawLine(0, SIZE / 2, SIZE / 2, SIZE / 2);
    }

    if (end) {
      g.setColor(Color.RED);
      g.fillOval(3, 3, SIZE - 6, SIZE - 6);
    } else if (current) {
      g.setColor(Color.BLACK);
      g.fillOval(3, 3, SIZE - 6, SIZE - 6);
    }
  }
}
