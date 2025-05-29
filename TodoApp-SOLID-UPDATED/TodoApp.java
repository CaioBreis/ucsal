package todoapp;

import todoapp.ui.ConsoleUI;

public class TodoApp {
    public static void main(String[] args) {
        System.out.println("L I S T A    D E    T A R E F A S");
        ConsoleUI ui = new ConsoleUI();
        ui.start();
    }
}