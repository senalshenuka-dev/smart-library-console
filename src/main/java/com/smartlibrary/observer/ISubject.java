package com.smartlibrary.observer;

import com.smartlibrary.model.Notification;

// Subject interface for notifications manager.

public interface ISubject {
    void attach(IObserver o);
    void detach(IObserver o);
    void notifyAllObservers(Notification n);
}