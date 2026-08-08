package com.smartlibrary.service;

import com.smartlibrary.model.IBook;
import com.smartlibrary.repository.BookRepository;
import com.smartlibrary.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

// report generator.

public class ReportGenerator {
    private final BookRepository bookRepo = BookRepository.getInstance();
    private final UserRepository userRepo = UserRepository.getInstance();
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Generates "Most Borrowed" report as a formatted single String.

    public String generateMostBorrowed(int limit) {
        String heading = "Most Borrowed Books:";
        String generatedAt = LocalDateTime.now().format(dtf);
        List<String> content = bookRepo.findMostBorrowed(limit).stream()
                .map(b -> String.format("%s (%d borrows)", b.getTitle(), b.getBorrowHistory().size()))
                .collect(Collectors.toList());
        return buildReport(heading, generatedAt, content);
    }

    // Generates "Active Borrowers" report as a formatted single String.

    public String generateActiveBorrowers(int limit) {
        String heading = "Active Borrowers:";
        String generatedAt = LocalDateTime.now().format(dtf);
        List<String> content = userRepo.findActiveBorrowers(limit).stream()
                .map(u -> String.format("%s (%d current borrows)", u.getName(), u.getBorrowedRecords().size()))
                .collect(Collectors.toList());
        return buildReport(heading, generatedAt, content);
    }

    // Generates "Overdue Books" report as a formatted single String.

    public String generateOverdueBooks() {
        String heading = "Overdue Books:";
        String generatedAt = LocalDateTime.now().format(dtf);
        List<String> content = bookRepo.findAll().stream()
                .flatMap(b -> b.getBorrowHistory().stream())
                .filter(br -> br.isOverdue())
                .map(br -> String.format("Book %s overdue by %d days (user %s)", br.getBookId(), br.daysLate(), br.getUserId()))
                .collect(Collectors.toList());
        return buildReport(heading, generatedAt, content);
    }

    // Helper to assemble the full report with heading, timestamp, separator and content.

    private String buildReport(String heading, String generatedAt, List<String> content) {
        StringBuilder sb = new StringBuilder();
        sb.append(heading).append("\n");
        sb.append("Report Generated Time and Date: ").append(generatedAt).append("\n");
        sb.append("-------------------------------\n\n");
        if (content.isEmpty()) {
            sb.append("(No results)\n");
        } else {
            for (String line : content) {
                sb.append(line).append("\n");
            }
        }
        return sb.toString();
    }
}