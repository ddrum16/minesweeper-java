# Terminal Minesweeper (Java)

A fully playable Minesweeper game written in Java that runs in the terminal, with four difficulty levels, flagging, and colour-coded output.

<p align="center">
<img width="890" height="788" alt="Screenshot 2026-11-20 183406" src="https://github.com/user-attachments/assets/5ac9eebb-8b95-4b31-8a78-295583a5bdb3" />
</p>
<p align="center"><i>Final CAD model (Inventor) · Minesweeper Terminal Game Board</i></p>

## Features

- **4 difficulty modes** on a 9x9 board: Easy (10 bombs), Medium (13), Hard (17), and Extreme (24)
- **Random bomb placement**, so every game is different
- **Automatic number calculation** showing how many bombs touch each cell, including diagonals
- **Cascading reveal:** revealing an empty cell opens all connected empty cells and the numbers around them
- **Flagging:** mark suspected bombs with a flag
- **Win and lose detection:** hit a bomb and every bomb on the board is revealed; reveal all safe cells to win
- **Input checking:** moves outside the board or on already revealed cells are rejected and re-requested
- **Colour-coded board** using ANSI colours: bombs in red, flags in blue, numbers in yellow

## How to Play

1. Choose a difficulty by entering `1` to `4`.
2. Each turn, enter `1` to reveal a cell or `2` to place a flag.
3. Enter the **row and column** of the cell, separated by a space (counting from 1).
4. Each number shows how many bombs touch that cell. Reveal every safe cell to win, and avoid the bombs.

```
Type 1 To Continue : Type 2 For A Flag
1
Enter The Row And Column You Want:
5 5
```

## Running the Game

Requires Java (JDK 8 or newer). The colours need a terminal that supports ANSI colour codes. Most do, but the older Windows Command Prompt may show stray characters.

```bash
javac Minesweeper.java
java Minesweeper
```

## How It Works

- **Two boards:** a hidden 2D array holds the bombs and numbers, and a second array holds what the player has revealed
- **Bomb placement:** bombs are placed at random positions, and the program picks again if a spot already has a bomb
- **Number calculation:** for each safe cell, the program checks its 8 neighbours using row and column offset arrays, skipping cells that fall off the edge, and counts the bombs
- **Cascading reveal:** when the player reveals a cell with zero neighbouring bombs, a queue-based search reveals its neighbours and keeps going through any other zero cells it finds, without using recursion
- **Win check:** the game is won when the number of revealed safe cells equals the board size minus the bomb count

```java
// Count the bombs around a cell, skipping positions outside the board
for (int i = 0; i < 8; i++) {
    int newRow = r + x[i];
    int newCol = c + y[i];
    if (newRow >= 0 && newRow < size && newCol >= 0 && newCol < size
            && invisBoard[newRow][newCol] == 'B') {
        bombCount++;
    }
}
```

## Tools

`Java`

## What I Learned

Working with 2D arrays, breaking a program into small methods, and using a queue to implement the cascading reveal.
