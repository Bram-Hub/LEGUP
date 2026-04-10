# Goal Condition

The 'goal' of a goal condition is to make LEGUP more similar to a formal logic proof.
By default, the win condition of a puzzle is to `find all possible solutions`, which is unlike 
the end goal of any formal proof. Instead, a puzzle should be validated when a goal condition is met,
such as `Prove the cell at (1, 1) is forced to be White`. 

This markdown document is meant to record how the goal condition is meant to work, any changes 
made to the repository to allow the goal condition to show up in the puzzle, be validated by 
the puzzle checker.

## Functionality

When opening a puzzle, there is a goal statement that appears above the puzzle board indicating
what the current goal of the puzzle is. If it pertains to specific cells, these cells will be 
highlighted yellow, and can be hovered over for a tooltip stating what the user is supposed to
prove about that cell. All goals that pertain to specific cells can target anywhere from one cell
to every cell

The `check proof` button will reflect this goal condition when validating a proof. 

For the purposes of goal conditions, a "closed" leaf is one that resides on a branch that has 
been closed by a contradiction rule, verus an "open" one that has not. A "complete" leaf is 
one whose board holds a valid, proven solution to the puzzle, versus an "incomplete" leaf that
has not.

Any goal type that refers to "assuming a solution exists" can have an additional field specified
in the xml goal element (assumeSolution="true"). This will allow for the proof to be shortened, and
change the text of the goal to "Prove that if there is a solution, then...". This defaults to false
if the field is not present. It is not accounted for in other goalTypes, where it either wouldn't
make a difference (i.e. PROVE_MULTIPLE_CELL_VALUE) or would make no sense (i.e. PROVE_NO_SOLUTION)

| **GoalType**                    | **Goal**                                                                             | **Conditions of proof tree**                                                                                                    |
|---------------------------------|--------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------|
| **DEFAULT**                     | Prove all possible solutions, or prove none exist                                    | Every open leaf is a complete one                                                                                               |
| **PROVE_ANY_SOLUTION**          | Prove any solution                                                                   | There is a complete leaf                                                                                                        |
| **PROVE_NO_SOLUTION**           | Prove that there is no solution to the puzzle                                        | There are no open leaves                                                                                                        |
| **PROVE_CELL_MUST_BE**          | Prove that the given cell locations are forced into the given set of values          | All open leaves are proven to match the set, and one solution is proven / assumed to exist                                      |
| **PROVE_CELL_MIGHT_NOT_BE**     | Prove that the given cell locations are not forced into have the given set of values | Either:<br/> Any complete leaf has a different set of values<br/> Every open leaf has a different set of values                 |
| **PROVE_SINGLE_CELL_VALUE**     | Prove that the given cell locations are forced into one set of values                | The set of values at the goal locations are proven to match in every open branch, and one solution is proven / assumed to exist |
| **PROVE_MULTIPLE_CELL_VALUE**   | Prove that the given cell locations are not forced into one set of values            | The set of values at the goal locations are proven to match in at least two complete leaves                                     |
| **PROVE_VALUES_ARE_POSSIBLE**   | Prove that the given cell locations are able to have the given set of values         | Either:<br/>Any complete leaf matches the set<br/>Every open leaf matches the set and a solution is assumed to exist            |
| **PROVE_VALUES_ARE_IMPOSSIBLE** | Prove that the given cell locations are not able to have the given set of values     | Every open leaf has a different set of values                                                                                   |


## Extending to New Puzzles

In order to extend this functionality to existing puzzles, three changes need to be made:
- In that puzzle's Cell.java, override isKnown() to check for that puzzle's unknown value.
- In its Importer.java, copy in the if statement regarding goal importing.
- In its CellFactory.java, delete the importGoal() override method.
For examples of the code look at Nurikabe.

## Tests

Tests use the TestPuzzle class to create mock puzzles and proof trees using rules that are 
always valid or always invalid. The current testing suite covers all goal conditions that 
currently exist, and are meant to cover a wide variety of proof trees to ensure the logic is
correct.


## New conditions

There are currently no plans for new conditions. If there is in the future, it should be done
by:
- Adding a name for the condition in GoalType.java
- Adding logic for checking a proof to Puzzle.isPuzzleComplete()
- Adding test files to test/java/goalConditions/ and test/resources/goalConditions/


## Puzzle Editor
Not done yet
Ideas for UI:
- Default 'Goal' element highlight
  - Clicking on same cell cycles through goal types
  - Visual is curved arrow
- Highlight is independent of cell type