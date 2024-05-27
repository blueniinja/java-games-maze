package com.jakobniinja;

import static java.awt.Event.*;

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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class MazeGenerator2 extends JFrame {

  private static final long serialVersionUID = 1L;

  private int rows = 30;

  private int cols = 20;

  private Cell2[][] cell = new Cell2[rows][cols];

  private int row = 0;

  private int col = 0;

  private int endRow = rows - 1;

  private int endCol = cols - 1;

  private JLabel titleLabel = new JLabel("Anti-Maze");

  private JPanel mazePanel = new JPanel();

  public MazeGenerator2() {
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

    // center panel
    JPanel centerPanel = new JPanel();
    centerPanel.setBackground(Color.BLACK);
    add(centerPanel, BorderLayout.CENTER);

    // maze panel
    newMaze();
    centerPanel.add(mazePanel);

    // button panel
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

    // listeners
    addKeyListener(new KeyAdapter() {
      public void keyReleased(KeyEvent e) {

      }
    });

    addKeyListener(new KeyAdapter() {
      public void keyReleased(KeyEvent e) {
        char c = e.getKeyChar();
        System.out.println(c);
      }
    });
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

    cell = new Cell2[rows][cols];

    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < cols; c++) {
        cell[r][c] = new Cell2(r, c);
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

  private boolean isAvailable(int r, int c) {
    boolean available = false;
    if (r >= 0 && r < rows && c >= 0 && c < cols
        && cell[r][c].hasAllWalls()) {
      available = true;
    }
    return available;
  }


  private void generateMaze() {
    ArrayList<Cell2> tryLaterCell = new ArrayList<Cell2>();
    int totalCells = rows * cols;
    int visitedCells = 1;

    // start at a random cell
    Random rand = new Random();
    int r = rand.nextInt(rows);
    int c = rand.nextInt(cols);

    // find all neighbors with all walls intact
    ArrayList<Cell2> neighbors = new ArrayList<Cell2>();
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

    // while not all cells have yet been visited
    while (visitedCells < totalCells) {
      // find all neighbors not yet in maze

      // if one or more was found
      if (neighbors.size() > 0) {

        // if more than one was found, add this
        // cell to the list to try again
        if (neighbors.size() > 1) {
          tryLaterCell.add(cell[r][c]);
        }

        // pick a neighbor and remove the wall
        int pick = rand.nextInt(neighbors.size());
        Cell2 neighbor = neighbors.get(pick);
        cell[r][c].openTo(neighbor);

        // go to the neighbor and increment
        // the number visited
        r = neighbor.getRow();
        c = neighbor.getCol();
        visitedCells++;

      } else {
        // if none was found, go to one of the
        // cells that was saved to try later
        Cell2 nextCell = tryLaterCell.remove(0);
        r = nextCell.getRow();
        c = nextCell.getCol();

      }
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


  private void moveBall(int direction) {
    switch (direction) {
      case UP:
        // move up if this cell does not have a top wall
        if (!cell[row][col].isWall(Cell2.TOP)) {
          moveTo(row - 1, col, Cell2.TOP, Cell2.BOTTOM);
          // move up more if this is a long column
          while (!cell[row][col].isWall(Cell2.TOP)
              && cell[row][col].isWall(Cell2.LEFT)
              && cell[row][col].isWall(Cell2.RIGHT)) {
            moveTo(row - 1, col, Cell2.TOP, Cell2.BOTTOM);
          }
        }
        break;
      case DOWN:
        // move down if this cell does not have a bottom wall
        if (!cell[row][col].isWall(Cell2.BOTTOM)) {
          moveTo(row + 1, col, Cell2.BOTTOM, Cell2.TOP);
          // move down more if this is a long column
          while (!cell[row][col].isWall(Cell2.BOTTOM)
              && cell[row][col].isWall(Cell2.LEFT)
              && cell[row][col].isWall(Cell2.RIGHT)) {
            moveTo(row + 1, col, Cell2.BOTTOM, Cell2.TOP);
          }
        }
        break;
      case LEFT:
        // move left if this cell does not have a left wall
        if (!cell[row][col].isWall(Cell2.LEFT)) {
          moveTo(row, col - 1, Cell2.LEFT, Cell2.RIGHT);
          // move left more if this is a long row
          while (!cell[row][col].isWall(Cell2.LEFT)
              && cell[row][col].isWall(Cell2.TOP)
              && cell[row][col].isWall(Cell2.BOTTOM)) {
            moveTo(row, col - 1, Cell2.LEFT, Cell2.RIGHT);
          }
        }
        break;
      case RIGHT:
        // move right if this cell does not have a right wall
        if (!cell[row][col].isWall(Cell2.RIGHT)) {
          moveTo(row, col + 1, Cell2.RIGHT, Cell2.LEFT);
          // move right more if this is a long row
          while (!cell[row][col].isWall(Cell2.RIGHT)
              && cell[row][col].isWall(Cell2.TOP)
              && cell[row][col].isWall(Cell2.BOTTOM)) {
            moveTo(row, col + 1, Cell2.RIGHT, Cell2.LEFT);
          }
        }
        break;
    }
    // puzzle solved?
    if (row == endRow && col == endCol) {
      String message = "Congratulations! You solved it.";
      JOptionPane.showMessageDialog(this, message);
    }
  }


  public static void main(String[] args) {
    try {
      String className = UIManager.getCrossPlatformLookAndFeelClassName();
      UIManager.setLookAndFeel(className);
    } catch (Exception e) {
    }

    EventQueue.invokeLater(new Runnable() {
      public void run() {
        new MazeGenerator2();
      }
    });
  }
}