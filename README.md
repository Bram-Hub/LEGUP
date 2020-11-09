# LEGUP
### A Project by Dr. van Heuveln
Logic Engine for Grid-Using Puzzles - a better way to learn formal logic
test
## Goal
The main goal of the LEGUP project is to provide a better interface for students to learn basic principles of logical reasoning.

Dr. van Heuveln has taught logic courses on a frequent basis for the past 15 years, and noted that a good number of students struggle with the systems of modern formal logic that were developed in the late 1800's and early 1900's, and that have been universally used in logic courses since. These traditional systems use abstract linear symbol strings such as (P & Q) -> (R v S), and deploy even more abstract rules such as & Elim to infer new symbol strings from old ones, thus engaging the edu.rpi.legup.user in logical reasoning.

This project brings about the idea that there are more pedagogically effective ways for students to learn the basic and important principles of logical reasoning.

LEGUP uses a more visual representation in a more concrete and engaging environment. These and other features of the LEGUP interface are suspected to have several advantages over more traditional interfaces in terms of learning logic.

## Use Cases

The LEGUP interface allows the edu.rpi.legup.user to solve different types of grid-based logical puzzles. Probably the best known example of such a edu.rpi.legup.puzzle is the popular Sudoku edu.rpi.legup.puzzle, but there are many other types of puzzles that are based on the principle of filling in cells of a square or rectangular grid with different kinds of objects. In all cases, the edu.rpi.legup.user is provided certain clues that will force a unique configuration of objects in the grid. These types of puzzles are often advertised as 'logic puzzles', and are claimed to train one's logical mind as, using deduction, users should be able to infer which object goes where.

So, how does the LEGUP interface differ from online platforms for grid-based games? The most important difference is that the LEGUP interface requires the edu.rpi.legup.user to explicitly indicate their logical reasoning. Thus, solving the edu.rpi.legup.puzzle due to some lucky guesses is no longer an option! The interface will congratulate the edu.rpi.legup.user less on the fact that the edu.rpi.legup.user was able to solve the edu.rpi.legup.puzzle, but more on how the edu.rpi.legup.user solved the edu.rpi.legup.puzzle. This is essential to logic: logic is not about the truth or the correct or best answer, but about deductive implication and valid inference: what follows from what, and why?

LEGUP also provides a single interface that is capable of supporting many different types of puzzles. Since most of the interface remains the same, however, users wil start to recognize certain similarities between the different puzzles. In particular, since they have to explicitly state their reasoning, users should start to see strong similarities in their logical reasoning patterns from edu.rpi.legup.puzzle to edu.rpi.legup.puzzle, which is the very basis of the abstract logical reasoning principles taught in traditional logic courses. However, rather than being 'thrown in the water' with abstract principles based on obscure symbols, users instead are dealing with a concrete, fun, and engaging logic edu.rpi.legup.puzzle. As such, LEGUP aims to give its users a 'leg up' when it comes to the understanding of logic.

## Gradle

This project uses Gradle for dependency management.

## XML Board Specifications

An example for the Battleship edu.rpi.legup.puzzle demonstrates the proper format for XML files to be read in. Puzzles have particular x and y values associated with a location of each puzzleElement. The board size dictates the square size of the board. Currently LEGUP supports Battleship, Fillapix, LightUp, Masyu, Nurikabe, Sudoku, and Treetent puzzles.

```
<edu.rpi.legup.Legup>
    <edu.rpi.legup.puzzle qualifiedClassName="edu.rpi.legup.puzzle.battleship.BattleShip">
        <board size="10">
            <puzzleElement>
                <puzzleElement value="1" x="2" y="0"/>
                <puzzleElement value="1" x="6" y="0"/>
                <puzzleElement value="2" x="1" y="1"/>
                <puzzleElement value="-1" x="8" y="1"/>
                <puzzleElement value="-1" x="2" y="2"/>
            </puzzleElement>
        </board>
    </edu.rpi.legup.puzzle>
</edu.rpi.legup.Legup>
```
Element values are dependent on the type of edu.rpi.legup.puzzle. This is specified in each edu.rpi.legup.puzzle's documentation.

## Running Legup
In most cases, you should be able to run Legup either with the command `java -jar Legup.jar` or by double clicking
the `Legup.jar` file. If you are on Windows and have errors running the jar file, run Powershell as an administrator (search for "powershell", right click, and select "Run as administrator"), navigate to the directory containing `Legup.jar`, and then run `java -jar Legup.jar`.
