# spbau-formal-languages
Parser for L language.

### How to build?
Run the following to build the jar file for the parser:
```
./gradlew jar
```
You will be able to find the jar at **./build/libs/lang-parser-1.0.jar**.

### How to run?
Run the aforementioned jar file. It expects that it will be run like this: 
```
java -jar lang-parser-1.0.jar sourceFilePath
```

As an example, consider running the parser from the root directory for a test source file:
```
java -jar build/libs/lang-parser-1.0.jar src/test/resources/proc.lang
```

### Example source files

You can find source files used for testing in directory for 
test resources: **./src/test/resources/**.