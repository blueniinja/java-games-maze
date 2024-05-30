package com.jakobniinja;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

class MazeGenerator extends JFrame {

  private static final long serialVersionUID = 1L;

  private int rows = 12;

  private int cols = 12;

  private Cell[][] cell = new Cell[rows][cols];

  private int row = 0;

  private int col = 0;

  private int endRow = rows - 1;

  private int endCol = cols - 1;

  private TitleLabel titleLabel = new TitleLabel("Maze");

  private JPanel mazePanel = new JPanel();

  public MazeGenerator() {
    initGUI();
    setTitle("Maze Generator");
    setResizable(false);
    pack();
    setLocationRelativeTo(null);
    setVisible(true);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
  }

  private void initGUI() {
    add(titleLabel, BorderLayout.PAGE_START);

    JPanel centerPanel = new JPanel();
    centerPanel.setBackground(Color.BLACK);
    add(centerPanel, BorderLayout.CENTER);

    newMaze();
    centerPanel.add(mazePanel);

    // Button panel
    JPanel buttonPanel = new JPanel();
    buttonPanel.setBackground(Color.BLACK);
    add(buttonPanel, BorderLayout.PAGE_END);

    JButton newMazeButton = new JButton("New Maze");
    newMazeButton.setFocusable(false);
    newMazeButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        newMaze();
      }
    });
    buttonPanel.add(newMazeButton);

    // Key listener for moving through the maze
    addKeyListener(new KeyAdapter() {
      public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        moveBall(keyCode);
      }
    });
  }

  private void newMaze() {
    mazePanel.removeAll();
    mazePanel.setLayout(new GridLayout(rows, cols));

    cell = new Cell[rows][cols];
    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < cols; c++) {
        cell[r][c] = new Cell(r, c);
        mazePanel.add(cell[r][c]);
      }
    }

    generateMaze();
    row = 0;
    col = 0;
    endRow = rows - 1;
    endCol = cols - 1;
    cell[row][col].setCurrent(true);
    cell[endRow][endCol].setEnd(true);
    mazePanel.revalidate();
  }

  private void generateMaze() {
    ArrayList<Cell> tryLaterCell = new ArrayList<Cell>();
    int totalCells = rows * cols;
    int visitedCells = 1;

    Random rand = new Random();
    int r = rand.nextInt(rows);
    int c = rand.nextInt(cols);

    while (visitedCells < totalCells) {
      ArrayList<Cell> neighbors = new ArrayList<Cell>();
      if (isAvailable(r - 1, c)) {
        neighbors.add(cell[r - 1][c]);
      }
      if (isAvailable(r + 1, c)) {
        neighbors.add(cell[r + 1][c]);
      }
      if (isAvailable(r, c - 1)) {
        neighbors.add(cell[r][c - 1]);
      }
      if (isAvailable(r, c + 1)) {
        neighbors.add(cell[r][c + 1]);
      }

      if (neighbors.size() > 0) {
        if (neighbors.size() > 1) {
          tryLaterCell.add(cell[r][c]);
        }
        int pick = rand.nextInt(neighbors.size());
        Cell neighbor = neighbors.get(pick);
        cell[r][c].openTo(neighbor);
        r = neighbor.getRow();
        c = neighbor.getCol();
        visitedCells++;
      } else {
        Cell nextCell = tryLaterCell.remove(0);
        r = nextCell.getRow();
        c = nextCell.getCol();
      }
    }
  }

  private boolean isAvailable(int r, int c) {
    return r >= 0 && r < rows && c >= 0 && c < cols && cell[r][c].hasAllWalls();
  }

  private void moveBall(int direction) {
    switch (direction) {
      case KeyEvent.VK_UP:
        if (!cell[row][col].isWall(Cell.TOP)) {
          moveTo(row - 1, col, Cell.TOP, Cell.BOTTOM);
          while (!cell[row][col].isWall(Cell.TOP) && cell[row][col].isWall(Cell.LEFT) && cell[row][col].isWall(Cell.RIGHT)) {
            moveTo(row - 1, col, Cell.TOP, Cell.BOTTOM);
          }
        }
        break;
      case KeyEvent.VK_DOWN:
        if (!cell[row][col].isWall(Cell.BOTTOM)) {
          moveTo(row + 1, col, Cell.BOTTOM, Cell.TOP);
          while (!cell[row][col].isWall(Cell.BOTTOM) && cell[row][col].isWall(Cell.LEFT) && cell[row][col].isWall(Cell.RIGHT)) {
            moveTo(row + 1, col, Cell.BOTTOM, Cell.TOP);
          }
        }
        break;
      case KeyEvent.VK_LEFT:
        if (!cell[row][col].isWall(Cell.LEFT)) {
          moveTo(row, col - 1, Cell.LEFT, Cell.RIGHT);
          while (!cell[row][col].isWall(Cell.LEFT) && cell[row][col].isWall(Cell.TOP) && cell[row][col].isWall(Cell.BOTTOM)) {
            moveTo(row, col - 1, Cell.LEFT, Cell.RIGHT);
          }
        }
        break;
      case KeyEvent.VK_RIGHT:
        if (!cell[row][col].isWall(Cell.RIGHT)) {
          moveTo(row, col + 1, Cell.RIGHT, Cell.LEFT);
          while (!cell[row][col].isWall(Cell.RIGHT) && cell[row][col].isWall(Cell.TOP) && cell[row][col].isWall(Cell.BOTTOM)) {
            moveTo(row, col + 1, Cell.RIGHT, Cell.LEFT);
          }
        }
        break;
    }

    if (row == endRow && col == endCol) {
      String message = "Congratulations! You solved it.";
      JOptionPane.showMessageDialog(this, message);
    }
  }

  private void moveTo(int nextRow, int nextCol, int firstDirection, int secondDirection) {
    cell[row][col].setCurrent(false);
    cell[row][col].addPath(firstDirection);
    row = nextRow;
    col = nextCol;
    cell[row][col].setCurrent(true);
    cell[row][col].addPath(secondDirection);
  }

  public static void main(String[] args) {
    try {
      String className = UIManager.getCrossPlatformLookAndFeelClassName();
      UIManager.setLookAndFeel(className);
    } catch (Exception e) {
    }

    EventQueue.invokeLater(new Runnable() {
      public void run() {
        new MazeGenerator();
      }
    });
  }
}
