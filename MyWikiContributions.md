# Binary
Binary is a grid based puzzle game akin to Sudoku. You are given a grid of empty squares and squares filled with 1 or 0. Your task is to fill the board with 1s and 0s while adhering to the rules of the game.

Each binary puzzle has a unique solution. It is always possible to make a next step by reasoning. In other words, the solution can always be found without guessing.

### Interacting with the Binary Board
Clicking on a white square fills it with a 0

Clicking on a square with a 0 fills it with a 1

Clicking on a square with a 1 returns it white

# Rules
### Here are  the direct rules of the puzzle
1) Each square should contain either a zero or a one.
2) Three orthogonally adjacent zeros or ones is not allowed.
3) Each row and each column should contain an equal number of zeros and ones.
4) Each row is unique and each column is unique. Thus, any row cannot be exactly equal to another row, and any column cannot be exactly equal to another column.

# LEGUP Proof Rules

## Case Rules

### One or Zero

This rule is a direct consequence of rule #1. If a tile can be filled with a 0 or a 1 based on the current state of the board, create a split in the tree where the tile is a 0 in one path and 1 in the other to show two possible cases.

## Contradiction Rules

### Three in a Row

This rule is a direct consequence of Rule #2. If a sequence of three of the same digit exists on the board, then the board is in a state of contradiction.

### Unbalanced Row/Column Rule

This rule is a direct consequence of Rule #3. If a row or column contains more of one digit than the other, then the board is in a state of contradiction.

### Identical Row/Column Rule

This rule is a direct consequence of Rule #4. If a row is identical to another row or a column is identical to another column, then the board is in a state of contradiction.

## Direct Rules

### Surround Pair

This rule is a direct consequence of Rule #2. If two of the same digit exist adjacent to each other in any given row or column, then in order to avoid violating Rule #2, the pair must be surrounded with other digit on both sides.

### One Tile Gap 

This rule is a direct consequence of Rule #2. If two of the same digit exist with a one tile gap between them, then in order to avoid violating Rule #2, the pair must be separated by placing the other digit between them.

### Complete Row/Column 

This rule is a direct consequence of Rules #2 and #3. If a row or column is completely filled with 1s and 0s, there does not exist a grouping of three 1s or 0s anywhere in the row, and 
