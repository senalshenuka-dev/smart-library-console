<<<<<<< HEAD
# Smart Library Console

A Java console application for managing books, library users, borrowing, returns, reservations, notifications, and library reports.

## Features

- Seeded demo books and users on startup
- Add, update, list, and remove books
- Add and list users with membership types:
  - Student
  - Faculty
  - Guest
- Borrow and return books
- Reserve books and cancel reservations
- Automatic notifications for relevant library events
- Reports for:
  - Most borrowed books
  - Active borrowers
  - Overdue books
- Undo the last successful command
- Book states for available, borrowed, and reserved books
- In-memory repositories for books, users, and reservations

## Requirements

- Java Development Kit (JDK) 24 or later
- Apache Maven 3.9 or later

The configured compiler release is Java 24, so an appropriate JDK must be available on your `PATH`.

## Getting Started

Clone the repository and enter the project directory:

```bash
git clone <repository-url>
cd smart-library-console
```

Build the project and run the tests:

```bash
mvn clean test
```

Start the application:

```bash
mvn package
java -cp target/classes com.smartlibrary.Main
```

The application starts with sample books and users, then displays the interactive menu.

## Menu

| Option | Operation |
| --- | --- |
| 1 | View book list |
| 2 | Add a book |
| 3 | Add a user |
| 4 | Borrow a book |
| 5 | Return a book |
| 6 | Reserve a book |
| 7 | Cancel a reservation |
| 8 | View reports |
| 9 | Undo the last command |
| 10 | View registered users |
| 11 | Update a book |
| 12 | Remove a book |
| 0 | Exit |

All data is held in memory and is reset when the application is restarted.

## Project Structure

```text
src/
├── main/java/com/smartlibrary/
│   ├── Main.java                  # Console entry point and menu
│   ├── commands/                 # Borrow, return, reserve, cancel, and undo commands
│   ├── model/                    # Books, users, reservations, and domain objects
│   │   └── state/                 # Book availability states
│   ├── observer/                 # Notification subject and observers
│   ├── repository/               # In-memory data repositories
│   ├── service/                  # Report generation
│   └── strategy/                 # Membership-specific fine strategies
└── test/java/                    # Test source directory
```

## Design Patterns

The project demonstrates several object-oriented design patterns:

- **Command:** Encapsulates library actions and supports undo through `CommandInvoker`.
- **Singleton:** Provides shared repository and notification service instances.
- **Builder:** Creates `Book` objects through `BookBuilder`.
- **State:** Models book availability using dedicated state classes.
- **Observer:** Sends notifications to registered users.
- **Strategy:** Applies different fine calculations for students, faculty, and guests.
- **Decorator:** Adds presentation badges such as featured-book information without changing the core book.

## Notes

- Demo data is loaded each time `Main` starts.
- There is no database or file-backed persistence.
- The application is operated through the terminal; prompts accept text entered from standard input.

## License

No license has been specified for this project yet.
=======
# smart-library-console
>>>>>>> 98162450e72ff84a505a0e0e7cdc1c7831f3009c
