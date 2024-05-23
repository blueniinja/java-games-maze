package com.jakobniinja;

import static java.awt.Color.BLACK;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class MazeGenerator extends JFrame {

  private static final long serialVersionUID = 1L;

  private int rows = 5;

  private int cols = 5;

  private Cell[][] cell = new Cell[rows][cols];

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

    // center panel
    JPanel centerPanel = new JPanel();
    centerPanel.setBackground(BLACK);
    add(centerPanel, BorderLayout.CENTER);

    // maze panel
    newMaze();
    centerPanel.add(mazePanel);

    // button panel

    // listeners

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
  }

  public static void main(String[] args) {
    try {
      String className = UIManager.getCrossPlatformLookAndFeelClassName();
      UIManager.setLookAndFeel(className);
    } catch (Exception e) {
    }
    EventQueue.invokeLater(MazeGenerator::new);
    //  TODO: Listing 7-19
  }
}
