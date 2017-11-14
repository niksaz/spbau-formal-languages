# spbau-formal-languages
Lexer for L language.

### How to build?
Run the following to build the jar file for the lexer:
```
./gradlew jar
```
You will be able to find the jar at **./build/libs/lexer-1.0.jar**.

### How to run?
Run the aforementioned jar file. It expects that it will be run like this: 
```
java -jar lexer-1.0.jar sourceFilePath
```

For example, running from the root directory the lexer for the example source file:
```
java -jar build/libs/lexer-1.0.jar src/test/resources/example1.lang
```

### Example source files

You can find source files used for testing in directory for 
[test resources](https://github.com/niksaz/spbau-formal-languages/tree/hw04/src/test/resources).


### Justifications for project choices 

To handle integer literals, the LIntegerLiteral was introduced as a separate lexeme.

Some rules for integer and floating-point literals from 
[Java specification](https://docs.oracle.com/javase/specs/jls/se7/html/jls-3.html#jls-3.10)
were omitted for the sake of brevity. It only supports integer decimal and float 
decimal literals. The others could be easily added if needed.
