package com.smartlibrary.observer;

import com.smartlibrary.model.Notification;

// Observer interface for notifications.

public interface IObserver {
    void update(Notification notification);
}