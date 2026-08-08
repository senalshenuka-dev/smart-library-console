package com.smartlibrary.commands;

import java.util.Stack;


//Invoker that executes commands and keeps history for undo.

public class CommandInvoker {
    private final Stack<ICommand> history = new Stack<>();

    public void executeCommand(ICommand cmd) {
        CommandResult res = cmd.execute();
        System.out.println(res.getMessage());
        if (res.isSuccess()) history.push(cmd);
    }

    public void undoLast() {
        if (history.isEmpty()) {
            System.out.println("Nothing to undo.");
            return;
        }
        ICommand last = history.pop();
        CommandResult res = last.undo();
        System.out.println(res.getMessage());
    }
}