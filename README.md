# Bar Software

This software is used at ENSEIRB-MATMECA's BAR to manage clients' accounts.

## Build

### Software

Build system is using Gradle. To build the project :

```
$ ./gradlew build
```

To run the tests :

```
$ ./gradlew test
```

To run directly the project :

```
$ ./gradlew run
```

### Documentation

You can build the documentation with Javadoc by running :

```
$ ./gradlew javadoc
```

Then you can find the HTML docs under `build/docs/javadoc`


## JAR packaging

You can package the project in a JAR archive with the IDE of your choice or on the command line.

## Dependencies

### Development 

All dependencies are specified in the Gradle project.

### Data

Some private data are not provided here.
You will need a data folder next to the JAR file containing :

- `data/db/bar.db` : A SQLite database with two tables `student |login|name|money|year|sector|` and `history |login_student|product|price|date|transaction|`.

- `data/items/items.json` : A list of available items in following JSON format
```json
[
    {
        "name": "Product name",
        "price": 120,
        "description": "Promotion"
    },
    ...
]
```

## Authors

Author : pjdevs

Contributors : 