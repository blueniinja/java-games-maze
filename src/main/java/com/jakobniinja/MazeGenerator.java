package com.jakobniinja;

import static java.awt.Color.BLACK;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class MazeGenerator extends JFrame {

  private static final long serialVersionUID = 1L;

  private int rows = 0;

  private int cols = 0;

  private Cell[][] cell = new Cell[rows][cols];

  private int row = 0;

  private int col = 0;

  private int endRow = rows - 1;

  private int endCol = cols - 1;


  private JLabel tileLabel = new JLabel("Maze");

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
    add(tileLabel, BorderLayout.PAGE_START);

    // Center panel
    JPanel centerPanel = new JPanel();
    centerPanel.setBackground(BLACK);
    add(centerPanel, BorderLayout.CENTER);

    // Maze panel
    newMaze();
    centerPanel.add(mazePanel);

    // Button panel

    // Listeners

  }

  private void newMaze() {
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
  }

  public boolean isAvailable(int r, int c) {
    boolean available = false;
    if (r > 0 && r < rows && c >= 0 && c < cols && cell[r][c].hasAllWalls()) {
      available = true;

    }
    return available;
  }

  private void generateMaze() {
    ArrayList<Cell> tryLaterCell = new ArrayList<>();
    int totalCells = rows * rows;
    int visitedCells = 1;

    // Start at a random cell
    Random random = new Random();
    int r = random.nextInt(rows);
    int c = random.nextInt(cols);

    // While not all cells have yet been initialized
    while (visitedCells < totalCells) {

      // Find all neighbors not yet in maze

      // Find neighbor with all walls intact
      ArrayList<Cell> neighbors = new ArrayList<>();
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

      // If one or more was found
      if (neighbors.size() > 0) {

        // If more than one was found, add this
        // Cell to the list to try again
        if (neighbors.size() > 1) {
          tryLaterCell.add(cell[r][c]);
        }

        // Pick a neighbor and remove the wall
        int pick = random.nextInt(neighbors.size());
        Cell neighbor = neighbors.get(pick);
        cell[r][c].openTo(neighbor);

        // Go to the neighbor and increment
        // The number visited
        r = neighbor.getRow();
        c = neighbor.getCol();
        visitedCells++;

      } else {
        // If one was found, go to the one of the
        // Cells that was saved to try later

        Cell nextCell = tryLaterCell.remove(0);
        r = nextCell.getRow();
        c = nextCell.getCol();
      }
    }
  }

  public static void main(String[] args) {
    try {
      String className = UIManager.getCrossPlatformLookAndFeelClassName();
      UIManager.setLookAndFeel(className);
    } catch (Exception e) {
    }
    EventQueue.invokeLater(MazeGenerator::new);
    //  TODO: Listing 7-41
  }
}
