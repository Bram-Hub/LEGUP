<p align="center">
    <img src="https://user-images.githubusercontent.com/46334090/180582690-a65937c6-6766-40f7-a21e-c1d8bbb3b26a.png"></a>
    <br />
    <br />
    <a href="https://choosealicense.com/licenses/gpl-3.0/"><img src="https://img.shields.io/badge/license-GPL%203.0-red" alt="GPL 3.0 License"></a>
    <a href="https://discord.gg/Ym5p6zUQjE"><img src="https://img.shields.io/discord/882735190785527848.svg?label=discord&color=yellow&logo=discord" alt="Discord"></a>
    <br />
    <i>A tool to teach formal logic using puzzles</i>
</p>
<hr />

LEGUP (**L**ogic **E**ngine for **G**rid-**U**sing **P**uzzles) is a better way to learn formal logic. It was created by [Dr. Bram van Heuveln](https://science.rpi.edu/itws/faculty/bram-van-heuveln), whose goal for this project is to provide a better interface for students to learn the basic principles of logical reasoning.

> Note: A web version of LEGUP ([Bram-Hub/LegupWeb](https://github.com/Bram-Hub/LegupWeb)) was previously under development, but it has been halted due to no one actively working on the project. Contributions to both versions of LEGUP are greatly appreciated. If you are interested in using LEGUP for educational purposes, please use this app version.

## Table of Contents
- [Background](#background)
- [Use Cases](#use-cases)
- [For Educators](#for-educators)
- [For Students](#for-students)
- [Documentation](#documentation)
- [Contributing](#contributing)
- [License](#license)

## Background
Dr. van Heuveln has taught logic courses on a frequent basis for the past 15 years, and noted that a good number of students struggle with the systems of modern formal logic that were developed in the late 1800's and early 1900's, and that have been universally used in logic courses since. These traditional systems use abstract linear symbol strings such as `(P & Q) -> (R v S)`, and deploy even more abstract rules such as & Elim to infer new symbol strings from old ones, thus engaging the user in logical reasoning.

This project brings about the idea that there are more pedagogically effective ways for students to learn the basic and important principles of logical reasoning.

LEGUP uses a more visual representation in a more concrete and engaging environment. These and other features of the LEGUP interface are suspected to have several advantages over more traditional interfaces in terms of learning logic.

## Use Cases
The LEGUP interface allows the user to solve different types of grid-based logical puzzles. Probably the best known example of such a puzzle is the popular Sudoku puzzle, but there are many other types of puzzles that are based on the principle of filling in cells of a square or rectangular grid with different kinds of objects. In all cases, the user is provided certain clues that will force a unique configuration of objects in the grid. These types of puzzles are often advertised as "logic puzzles," and are claimed to train one's logical mind as, using deduction, users should be able to infer which object goes where.

So, how does the LEGUP interface differ from online platforms for grid-based games? The most important difference is that the LEGUP interface requires the user to explicitly indicate their logical reasoning. Thus, solving the puzzle due to some lucky guesses is no longer an option! The interface will congratulate the user less on the fact that the user was able to solve the puzzle, but more on how the user solved the puzzle. This is essential to logic. Logic is not about the truth or the correct or best answer, but about deductive implication and valid inference. What follows from what, and why?

LEGUP also provides a single interface that is capable of supporting many different types of puzzles. Since most of the interface remains the same, however, users wil start to recognize certain similarities between the different puzzles. In particular, since they have to explicitly state their reasoning, users should start to see strong similarities in their logical reasoning patterns from puzzle to puzzle, is the very basis of the abstract logical reasoning principles taught in traditional logic courses. However, rather than being "thrown in the water" with abstract principles based on obscure symbols, users instead are dealing with a concrete, fun, and engaging logic puzzle. As such, LEGUP aims to give its users a "leg up" when it comes to the understanding of logic.

## For Educators
If you are an educator interested in using LEGUP, go to the [releases page](https://github.com/Bram-Hub/Legup/releases) to download the latest release of LEGUP. You can have your students download LEGUP from the same page. Some sample puzzle files can be found in the [puzzle files folder](https://github.com/Bram-Hub/Legup/tree/master/puzzles%20files).

## For Students
If you are a student interested in learning the basics of logic, LEGUP is a great way for you to get started. If your instructor is using LEGUP in the classroom and you are looking for extra practice, you can reference the sample puzzle files can be found in the [puzzle files folder](https://github.com/Bram-Hub/Legup/tree/master/puzzles%20files) to get more practice. Go here [Proof Editor](https://github.com/Bram-Hub/LEGUP/wiki/Proof-Editor-Tutorial) or here [Puzzle Editor](https://github.com/Bram-Hub/Legup/wiki/Puzzle-Editor-Tutorial) for the tutorials for each respective aspect.

Additionally, if you are interested in computer science and programming, please consider contributing to LEGUP! Not only would it a great way to practice logical reasoning, but it is also a great way to dip your toes into open source software and contributing to open source projects.

## Documentation
Documentation is actively being worked on on the [LEGUP wiki](https://github.com/Bram-Hub/Legup/wiki).

The Javadocs for our application are currently hosted directly on [our GitHub Pages site](https://bram-hub.github.io/LEGUP/).

Documentation is very much in the early stages, and we would greatly appreciate anyone who is willing to help write and structure the documentation. Currently, the priority is to write detailed documentation on how Nurikabe works, as it is the puzzle that is the most developed within LEGUP.

## Contributing
All contributions to LEGUP will be greatly appreciated. Currently, we need the most help in the following areas:
- Documentation
- Test suites

Please read our [contribution guidelines](CONTRIBUTING.md) for more detailed guidelines on how to contribute to LEGUP.

## License
LEGUP is licensed under the GPL-3.0 license, which can be viewed [here](LICENSE).
```
LEGUP: A Better Way to Learn Formal Logic
Copyright (C) 2022, the LEGUP Developers

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.
 ```

The look and feel of LEGUP uses [FlatLaf](https://github.com/JFormDesigner/FlatLaf), which is licensed under the [Apache-2.0 license](https://www.apache.org/licenses/LICENSE-2.0.html).

Some of the icons used in LEGUP were taken from or derived from the icons found on https://fonts.google.com/icons, which is licensed under the [Apache-2.0 license](https://www.apache.org/licenses/LICENSE-2.0.html).
