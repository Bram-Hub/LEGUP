# Goal Condition

The 'goal' of a goal condition is to make LEGUP more similar to a formal logic proof.
Currently, the win condition of a puzzle is to `find all possible solutions`, which is unlike 
the end goal of any formal proof. Instead, a puzzle should be validated when a goal condition is met,
such as `Prove the cell at (1, 1) is White`. 

This markdown document is meant to record how the goal condition is meant to work, any changes 
made to the repository to allow the goal condition to show up in the puzzle, be validated by 
the puzzle checker.

## Expected Functionality

When opening a puzzle, there should a sentence for what the goal of the proof is. The 
full goal statement `Prove the cell at (1, 1) is White` would be best. Additionally, 
the cell on the board should be highlighted in `Some color lol`. `The color` should not be 
by any other puzzles.

The `check proof` button should then reflect this goal condition when validating a proof. 
Depending on the goal, the checker needs to look for different things depending on the goal. 

For the default goal condition, __all branches__ must either contain a full board or be 
closed by a contradiction rule for the puzzle to be solved.

| Goal scope \ Task type | **Prove**                            | **Disprove**                         |
|------------------------|--------------------------------------|--------------------------------------|
| **Specific type**      | - Prove cell value is as indicated   | - Prove cell might not be indicated  |
| **Unknown type**       | - Prove a cell must be a given value | - Prove a cell can be multiple types |

To prove a cell is as indicated, there must be one valid branch where the cell is as indicated. If
multiple branches have the indicated value, they __must__ be merged into one branch.

To prove a cell might not be as indicated, only __one branch__ needs to be valid and the cell not as 
indicated for the puzzle to valid. Alternatively, __all__ branches where the cell is as indicated must
be contradictory, and there must be one non-contradictory branch

To prove a cell must be one given type, the same condition as proving a specific type holds.

To prove a cell might be multiple types, only __two valid branches__ need to exist, where the cell is 
a different type in each, for the puzzle to be valid.


## Possible UI
Puzzle Board:
- Goal should be highlighted (in brown?) at all times
  - Hovering should specify exactly what the goal wants like how rule tooltips work
  - Different indicators for the different types of rules?
- Goal text nearby the board (below?)
  - The goal text will be different for each goal type
Puzzle Editor:
- Default 'Goal' element highlight
  - Clicking on same cell cycles through goal types
  - Visual is curved arrow
- Highlight is independent of cell type