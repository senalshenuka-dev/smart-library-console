package com.smartlibrary.commands;


// Command interface for borrow/return/reserve actions.

public interface ICommand {
    CommandResult execute();
    CommandResult undo();
}