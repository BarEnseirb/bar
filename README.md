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

#### Build

You can build the documentation with Javadoc by running :

```
$ ./gradlew javadoc
```

Then you can find the HTML docs under `build/docs/javadoc`

#### Publish

The docs under `docs/` is published with Github Pages at https://pjdevs.github.io/bar/. It must be updated for every new version.

## JAR packaging

You can package the project in a JAR archive with the IDE of your choice or on the command line.

## Dependencies

### Development 

All dependencies are specified in the Gradle project.

### Data

Some private data are not provided here.
You will need a data folder next to the JAR file containing :

- `data/db/bar.db` : A SQLite database with two tables `student |login|name|money|year|sector|` and `history |login_student|product|price|date|transaction|`.
It can be created with the following SQL statements :

```sql
CREATE TABLE "student" (
	"login"	TEXT NOT NULL,
	"name"	TEXT NOT NULL,
	"money"	INT NOT NULL,
	"year"	INT NOT NULL,
	"sector"	CHAR(1) NOT NULL,
	PRIMARY KEY("login"),
	CHECK("year" >= 0),
	CHECK("money" >= 0),
	CHECK("sector" IN ('N', 'E', 'I', 'M', 'R', 'S', 'T'))
);
```

```sql
CREATE TABLE "history" (
	"login_student"	TEXT NOT NULL,
	"product"	TEXT NOT NULL,
	"price"	INTEGER NOT NULL,
	"date"	TEXT NOT NULL,
	"transaction"	TEXT NOT NULL,
	CHECK("transaction" IN ('Achat', 'Mouvement')),
	FOREIGN KEY("login_student") REFERENCES "student_save"("login")
);
```

- `data/items/items.json` : A list of available items in following JSON format that can be created if not present
```json
[
    {
        "name": "Product name",
        "price": 120,
        "description": "Promotion",
        "color": "0xffffffff"
    },
    ...
]
```

## Authors

Author : pjdevs

Contributors : 