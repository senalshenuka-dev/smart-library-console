package com.smartlibrary.model;

import com.smartlibrary.observer.IObserver;
import com.smartlibrary.strategy.IFineStrategy;
import java.util.ArrayList;
import java.util.List;

// Domain user representing library members.

public class User implements IObserver {
    private final String userId;
    private final String name;
    private final String email;
    private final String contact;
    private final MembershipType membershipType;
    private IFineStrategy fineStrategy;
    private final List<BorrowRecord> borrowedRecords = new ArrayList<>();

    public User(String userId, String name, String email, String contact, MembershipType membershipType) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.membershipType = membershipType;
        // set a default fine strategy based on membership type
        switch (membershipType) {
            case STUDENT -> this.fineStrategy = new com.smartlibrary.strategy.StudentFineStrategy();
            case FACULTY -> this.fineStrategy = new com.smartlibrary.strategy.FacultyFineStrategy();
            case GUEST -> this.fineStrategy = new com.smartlibrary.strategy.GuestFineStrategy();
        }
    }

    public String getUserId() { return userId; }
    public String getName() { return name; }

    
    //Returns the user's email address.

    public String getEmail() { return email; }

    //Returns the user's contact phone number.
    
    public String getContact() { return contact; }

    public MembershipType getMembershipType() { return membershipType; }

    public void addBorrowRecord(BorrowRecord r) { borrowedRecords.add(r); }
    public List<BorrowRecord> getBorrowedRecords() { return borrowedRecords; }

    public void setFineStrategy(IFineStrategy s) { this.fineStrategy = s; }
    public IFineStrategy getFineStrategy() { return fineStrategy; }


    // Receives notifications from the NotificationService. For demo we print to console.
    // In a real system this could send email/SMS/push notifications.
    
    public void update(Notification n) {
        System.out.printf("Notification for %s (%s): %s - %s\n", name, userId, n.getTitle(), n.getBody());
    }

    public String toString() {
        return String.format("%s [%s] (%s)", name, userId, membershipType);
    }
}