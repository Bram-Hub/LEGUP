# Legup
Legup (Logic Engine for Grid-Using Puzzles), is a better way to learn formal logic. It was created by Dr. Bram van Heuveln, whose goal for this project is to provide a better interface for students to learn the basic principles of logical reasoning.

> Note: A web version of Legup ([Bram-Hub/LegupWeb](https://github.com/Bram-Hub/LegupWeb)) based on this app version of Legup is actively being developed. However, it is very much in the early stages of development and will not be ready for general use for quite a while. Contributions to both versions of Legup are greatly appreciated. If you are interested in using Legup for educational purposes, please use this app version.

## Table of Contents
- [Background](#background)
- [Use Cases](#use-cases)
- [Documentation](#documentation)
- [Contributing](#contributing)
- [License](#license)

## Background
Dr. van Heuveln has taught logic courses on a frequent basis for the past 15 years, and noted that a good number of students struggle with the systems of modern formal logic that were developed in the late 1800's and early 1900's, and that have been universally used in logic courses since. These traditional systems use abstract linear symbol strings such as `(P & Q) -> (R v S)`, and deploy even more abstract rules such as & Elim to infer new symbol strings from old ones, thus engaging the user in logical reasoning. 

This project brings about the idea that there are more pedagogically effective ways for students to learn the basic and important principles of logical reasoning. 

Legup uses a more visual representation in a more concrete and engaging environment. These and other features of the Legup interface are suspected to have several advantages over more traditional interfaces in terms of learning logic.

## Use Cases
The Legup interface allows the user to solve different types of grid-based logical puzzles. Probably the best known example of such a puzzle is the popular Sudoku puzzle, but there are many other types of puzzles that are based on the principle of filling in cells of a square or rectangular grid with different kinds of objects. In all cases, the user is provided certain clues that will force a unique configuration of objects in the grid. These types of puzzles are often advertised as 'logic puzzles', and are claimed to train one's logical mind as, using deduction, users should be able to infer which object goes where.

So, how does the Legup interface differ from online platforms for grid-based games? The most important difference is that the Legup interface requires the user to explicitly indicate their logical reasoning. Thus, solving the puzzle due to some lucky guesses is no longer an option! The interface will congratulate the user less on the fact that the user was able to solve the puzzle, but more on how the user solved the puzzle. This is essential to logic: logic is not about the truth or the correct or best answer, but about deductive implication and valid inference: what follows from what, and why? 

Legup also provides a single interface that is capable of supporting many different types of puzzles. Since most of the interface remains the same, however, users wil start to recognize certain similarities between the different puzzles. In particular, since they have to explicitly state their reasoning, users should start to see strong similarities in their logical reasoning patterns from puzzle to puzzle, is the very basis of the abstract logical reasoning principles taught in traditional logic courses. However, rather than being 'thrown in the water' with abstract principles based on obscure symbols, users instead are dealing with a concrete, fun, and engaging logic puzzle. As such, LEGUP aims to give its users a 'leg up' when it comes to the understanding of logic.

## Documentation

## Contributing
All contributions to Legup will be greatly appreciated. Currently, we need the most help in the following areas:
- Documentation
- Test suites

Please read our [contribution guidelines](CONTRIBUTING.md) for more detailed guidelines on how to contribute to Legup.

## Code of Conduct
> Note: The Code of Conduct is being worked on, the link below does not exist right now.

You can find our code of conduct and how it is enforced here.

## License
Legup is licensed under the GPL-3.0 license, which can be viewed [here](LICENSE).
