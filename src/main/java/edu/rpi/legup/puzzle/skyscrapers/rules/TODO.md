## TODO

spreadsheet : https://docs.google.com/spreadsheets/d/1l7aUZtavtysM8dtGnaEIXhBKMRGxekhnLIVoYIHYZi8/edit#gid=0

 1. Basic Rules:
     - Rename to fit uses 
       - Last visible/singular number/cell?
 2. Contradiction Rules:
     - Generalize visibility rules to non-full lines
     - Figure out why these aren't static methods
 3. Case Rules:
     - Cell for number - waiting on ui
 4. Refactoring:
    - Remove references to lightup and treetent in variable names
    - document utility functions in the reference sheet, COMMENTS!
    - review and identify dead code
    - create and add rule icons
    - check for overrides of by cell functions (ie checkContradiction)
    - replace row,col,rowClues,colClues and corresponding functions with appropriate names
 5. Flags
    - review all basic/contradiction rules to put in terms of the new cases / add flags
    - edit exporter to include flags in xml file format
    - discuss use/not of unresolved flag
 6. Documentation
    - UML diagram(s)