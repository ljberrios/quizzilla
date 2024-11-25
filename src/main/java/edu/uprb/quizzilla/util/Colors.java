package edu.uprb.quizzilla.util;

public final class Colors {

    private Colors() {}

    public static final String RESET = "\033[0m";

    public static final String BOLD	= "\u001B[1m";
    public static final String DIM = "\u001B[2m";
    public static final String ITALIC = "\u001B[3m";
    public static final String UNDERLINE = "\u001B[4m";

    public static final String BLACK = "\033[30m";
    public static final String RED = "\033[31m";
    public static final String GREEN = "\u001B[38;5;120m";
    public static final String YELLOW = "\033[33m";
    public static final String BLUE = "\033[34m";
    public static final String MAGENTA = "\033[35m";
    public static final String CYAN = "\u001B[38;5;87m";
    public static final String WHITE = "\u001B[38;5;15m";

    public static void print(String msg, Object... args) {
        System.out.format(msg + RESET + "\n", args);
    }

}
