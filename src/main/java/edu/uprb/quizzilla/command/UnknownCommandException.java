package edu.uprb.quizzilla.command;

public class UnknownCommandException extends Exception {
    public UnknownCommandException(String label) {
        super("Unknown command: " + label);
    }
}
