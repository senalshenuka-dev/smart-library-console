package com.smartlibrary;

import com.smartlibrary.commands.*;
import com.smartlibrary.model.*;
import com.smartlibrary.repository.*;
import com.smartlibrary.service.ReportGenerator;
import com.smartlibrary.strategy.*;
import java.util.Scanner;

//console UI for the Smart Library Management System.

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final BookRepository bookRepo = BookRepository.getInstance();
    private static final UserRepository userRepo = UserRepository.getInstance();
    private static final ReservationRepository reservationRepo = ReservationRepository.getInstance();
    private static final com.smartlibrary.observer.NotificationService notificationService = com.smartlibrary.observer.NotificationService.getInstance();
    private static final CommandInvoker invoker = new CommandInvoker();
    private static final ReportGenerator reportGenerator = new ReportGenerator();

    public static void main(String[] args) {
        // seed demo data and start interactive menu
        seedDemoData();
        loopMenu();
    }


    //Seed some demo books and users so the application is usable immediately.

    private static void seedDemoData() {
        Book b1 = new BookBuilder()
                .withTitle("Design Patterns")
                .withAuthor("Erich Gamma")
                .withCategory("Programming")
                .withISBN("978-0201633610")
                .build();
        Book b2 = new BookBuilder()
                .withTitle("Clean Code")
                .withAuthor("Robert C. Martin")
                .withCategory("Programming")
                .withISBN("978-0132350884")
                .build();

        bookRepo.save(b1);
        bookRepo.save(b2);

        User u1 = new User("U001", "Alice", "alice@example.com", "0771234567", MembershipType.STUDENT);
        User u2 = new User("U002", "Dr. Bob", "bob@example.edu", "0777654321", MembershipType.FACULTY);
        userRepo.save(u1);
        userRepo.save(u2);

        // Register users for notifications
        com.smartlibrary.observer.NotificationService.getInstance().attach(u1);
        com.smartlibrary.observer.NotificationService.getInstance().attach(u2);

        // Example: create a decorated view of a book (presentation only)
        IBook featured = new FeaturedDecorator(b2);
        // For demo persistence we save the core book (decorators are runtime wrappers)
        bookRepo.save(featured.getCore());
    }

    //Main interactive menu loop presenting choices and delegating to command handlers.

    private static void loopMenu() {
        while (true) {
            System.out.println("\n=== Smart Library Menu ===");
            System.out.println("1) View Book List");
            System.out.println("2) Add book");
            System.out.println("3) Add user");
            System.out.println("4) Borrow book");
            System.out.println("5) Return book");
            System.out.println("6) Reserve book");
            System.out.println("7) Cancel reservation");
            System.out.println("8) Reports");
            System.out.println("9) Undo last command");
            System.out.println("10) View registered users");
            System.out.println("11) Update book");
            System.out.println("12) Remove book");
            System.out.println("0) Exit");
            System.out.print("Choice: ");
            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case "1": listBooks(); break;
                    case "2": addBook(); break;
                    case "3": addUser(); break;
                    case "4": borrowBook(); break;
                    case "5": returnBook(); break;
                    case "6": reserveBook(); break;
                    case "7": cancelReservation(); break;
                    case "8": reportsMenu(); break;
                    case "9": undoLast(); break;
                    case "10": listUsers(); break;
                    case "11": updateBook(); break;
                    case "12": removeBook(); break;
                    case "0": System.out.println("Bye."); return;
                    default: System.out.println("Invalid choice."); break;
                }
            } catch (Exception ex) {
                // Catch-all for unexpected runtime errors so console app does not crash
                System.out.println("Error: " + ex.getMessage());
            }
        }
    }

    //Prints a simple list of all books (compact view).

    private static void listBooks() {
        System.out.println("\nBook List:");
        bookRepo.findAll().forEach(b -> {
            String badges = "";
            if (b instanceof BookDecorator) {
                badges = String.join(",", ((BookDecorator) b).getBadges());
            }
            System.out.printf("- %s | %s | %s | Status: %s %s\n",
                    b.getBookId(), b.getTitle(), b.getAuthor(), b.getState().getStatus(), badges);
        });
    }

    
    //Prints detailed information for all books.
    
    private static void listBooksDetailed() {
        System.out.println("\nDetailed Book List:");
        bookRepo.findAll().forEach(b -> {
            String badges = "";
            if (b instanceof BookDecorator) {
                badges = String.join(",", ((BookDecorator) b).getBadges());
            }
            System.out.println("--------------------------------------------------");
            System.out.printf("ID       : %s\n", b.getBookId());
            System.out.printf("Title    : %s\n", b.getTitle());
            System.out.printf("Author   : %s\n", b.getAuthor());
            System.out.printf("Category : %s\n", b.getCategory());
            System.out.printf("ISBN     : %s\n", b.getIsbn());
            System.out.printf("Status   : %s\n", b.getState().getStatus());
            System.out.printf("Badges   : %s\n", badges.isEmpty() ? "(none)" : badges);
            System.out.printf("Borrow History Count: %d\n", b.getBorrowHistory().size());
        });
        System.out.println("--------------------------------------------------");
    }


    // Interactive routine for adding a new book using the BookBuilder.

    private static void addBook() {
        // Show detailed current books before adding
        listBooksDetailed();
        // show users (without the extra header line)

        System.out.print("\nBook ID: ");
        String id = scanner.nextLine().trim();
        System.out.print("Title: ");
        String title = scanner.nextLine().trim();
        System.out.print("Author: ");
        String author = scanner.nextLine().trim();
        System.out.print("Category: ");
        String cat = scanner.nextLine().trim();
        System.out.print("ISBN: ");
        String isbn = scanner.nextLine().trim();

        Book b = new BookBuilder()
                .withBookId(id)
                .withTitle(title)
                .withAuthor(author)
                .withCategory(cat)
                .withISBN(isbn)
                .build();
        bookRepo.save(b);
        System.out.println("Book added.");

    }


    //Interactive routine for adding a new user and attaching appropriate fine strategy.
    //Shows current users before prompting for new user details.

    private static void addUser() {
        // Show current users to user before adding
        System.out.println("\n--- Registered Users ---");
        listUsers();

        System.out.print("\nUser ID: ");
        String id = scanner.nextLine().trim();
        System.out.print("Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Contact: ");
        String contact = scanner.nextLine().trim();
        System.out.print("Membership (STUDENT/FACULTY/GUEST): ");
        String mt = scanner.nextLine().trim().toUpperCase();
        MembershipType type = MembershipType.valueOf(mt);
        User u = new User(id, name, email, contact, type);
        // Configure fine strategy (Strategy pattern)
        switch (type) {
            case STUDENT -> u.setFineStrategy(new StudentFineStrategy());
            case FACULTY -> u.setFineStrategy(new FacultyFineStrategy());
            case GUEST -> u.setFineStrategy(new GuestFineStrategy());
        }
        userRepo.save(u);
        com.smartlibrary.observer.NotificationService.getInstance().attach(u);
        System.out.println("User added and registered for notifications.");

        // Show all registered users after adding a user
        System.out.println("\n--- Registered users ---");
        listUsers();
    }


    //Creates and executes a BorrowCommand via the invoker.

    private static void borrowBook() {
        System.out.print("User ID: ");
        String uid = scanner.nextLine().trim();
        System.out.print("Book ID: ");
        String bid = scanner.nextLine().trim();
        BorrowCommand cmd = new BorrowCommand(bid, uid, bookRepo, userRepo, reservationRepo, com.smartlibrary.observer.NotificationService.getInstance());
        invoker.executeCommand(cmd);
    }


    //Creates and executes a ReturnCommand via the invoker.

    private static void returnBook() {
        System.out.print("User ID: ");
        String uid = scanner.nextLine().trim();
        System.out.print("Book ID: ");
        String bid = scanner.nextLine().trim();
        ReturnCommand cmd = new ReturnCommand(bid, uid, bookRepo, userRepo, reservationRepo, com.smartlibrary.observer.NotificationService.getInstance());
        invoker.executeCommand(cmd);
    }

    //Creates and executes a ReserveCommand via the invoker.
    //If the book is available, the ReserveCommand currently advises borrowing instead.

    private static void reserveBook() {
        System.out.print("User ID: ");
        String uid = scanner.nextLine().trim();
        System.out.print("Book ID: ");
        String bid = scanner.nextLine().trim();
        ReserveCommand cmd = new ReserveCommand(bid, uid, reservationRepo, bookRepo, userRepo);
        invoker.executeCommand(cmd);
    }

    //Creates and executes a CancelReservationCommand via the invoker.

    private static void cancelReservation() {
        System.out.print("Reservation ID: ");
        String rid = scanner.nextLine().trim();
        CancelReservationCommand cmd = new CancelReservationCommand(rid, reservationRepo);
        invoker.executeCommand(cmd);
    }

    //Presents a small reports submenu and prints results from ReportGenerator.
    //Uses the new formatted report strings returned by ReportGenerator.

    private static void reportsMenu() {
        System.out.println("\nReports:");
        System.out.println("1) Most Borrowed");
        System.out.println("2) Active Borrowers");
        System.out.println("3) Overdue Books");
        System.out.print("Choice: ");
        String c = scanner.nextLine().trim();
        switch (c) {
            case "1":
                System.out.println(reportGenerator.generateMostBorrowed(5));
                break;
            case "2":
                System.out.println(reportGenerator.generateActiveBorrowers(5));
                break;
            case "3":
                System.out.println(reportGenerator.generateOverdueBooks());
                break;
            default:
                System.out.println("Invalid.");
        }
    }

    //Lists all registered users and key details for each user.
    //The output shows only values: id | name | email | contact | membership

    private static void listUsers() {
        System.out.println("\nRegistered Users:");
        userRepo.findAll().forEach(u -> {
            // Show only values: id | name | email | contact | membership
            System.out.printf("- %s | %s | %s | %s | %s\n",
                    u.getUserId(), u.getName(), u.getEmail(), u.getContact(), u.getMembershipType());

            // Print borrowed records summary
            if (u.getBorrowedRecords().isEmpty()) {
                System.out.println("    Current borrows: 0");
            } else {
                System.out.printf("    Current borrows: %d\n", u.getBorrowedRecords().size());
                u.getBorrowedRecords().forEach(br -> {
                    System.out.printf("      - book=%s borrowed=%s due=%s returned=%s\n",
                            br.getBookId(), br.getBorrowDate(), br.getDueDate(), br.getReturnDate());
                });
            }
        });
    }

    /**
     * Interactive update book routine.
     *
     * Prompts user for book id, shows current values and accepts new values.
     * Pressing Enter keeps the existing value. The updated Book preserves state
     * and borrow history.
     */
    private static void updateBook() {
        System.out.print("Book ID to update: ");
        String id = scanner.nextLine().trim();
        var ob = bookRepo.findById(id);
        if (ob.isEmpty()) {
            System.out.println("Book not found: " + id);
            return;
        }

        IBook ib = ob.get();
        Book core = ib.getCore();

        System.out.println("Leave blank to keep current value.");
        System.out.print("Title [" + core.getTitle() + "]: ");
        String title = scanner.nextLine().trim();
        if (title.isEmpty()) title = core.getTitle();

        System.out.print("Author [" + core.getAuthor() + "]: ");
        String author = scanner.nextLine().trim();
        if (author.isEmpty()) author = core.getAuthor();

        System.out.print("Category [" + core.getCategory() + "]: ");
        String category = scanner.nextLine().trim();
        if (category.isEmpty()) category = core.getCategory();

        System.out.print("ISBN [" + core.getIsbn() + "]: ");
        String isbn = scanner.nextLine().trim();
        if (isbn.isEmpty()) isbn = core.getIsbn();

        // Build a new Book keeping the same id
        Book updated = new Book(core.getBookId(), title, author, category, isbn);
        // preserve state and borrow history
        updated.setState(core.getState());
        for (BorrowRecord br : core.getBorrowHistory()) {
            updated.addBorrowRecord(br);
        }

        bookRepo.save(updated);
        System.out.println("Book updated: " + updated.getBookId());
        listBooksDetailed();
    }

    //Remove book by id (from in-memory repository).
    //If the book does not exist, informs the user.

    private static void removeBook() {
        System.out.print("Book ID to remove: ");
        String id = scanner.nextLine().trim();
        var ob = bookRepo.findById(id);
        if (ob.isEmpty()) {
            System.out.println("Book not found: " + id);
            return;
        }

        bookRepo.remove(id);
        System.out.println("Book removed: " + id);
        listBooksDetailed();
    }

    //Asks the invoker to undo the last successful command (if any).
    
    private static void undoLast() {
        invoker.undoLast();
    }
}