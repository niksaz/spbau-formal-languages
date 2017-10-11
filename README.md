# spbau-formal-languages
Lexer for L language.


### How to run?

To run the app on Unix execute the command from the repository's root directory:

./gradlew run Dargs=PATH_TO_L_FILE

Use gradlew.bat for Windows instead.


### Justifications for project choices 

To handle integer literals, the LIntegerLiteral was introduced as a separate lexeme.

Some rules for integer and floating-point literals from 
[Java specification](https://docs.oracle.com/javase/specs/jls/se7/html/jls-3.html#jls-3.10)
were omitted for the sake of brevity. It only supports integer decimal and float 
decimal literals. The others could be easily added if needed.


### Example source files

You can find source files used for testing in directory for 
[test resources](https://github.com/niksaz/spbau-formal-languages/tree/hw04/src/test/resources).