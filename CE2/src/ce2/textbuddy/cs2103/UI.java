package ce2.textbuddy.cs2103;

import java.util.Scanner;

public class UI {

    private Scanner _scanner;

    public UI() {
        this._scanner = new Scanner(System.in);
    }

    String getUserInput() {
        return _scanner.nextLine();
    }

    void displayMessage(String msg) {
        System.out.println(msg);
    }

    void displayFormattedMessage(String message, Object... keywords) {
        message = message.concat("%n");
        System.out.format(message, keywords);
    }

    void displayMessageInline(String message) {
        System.out.print(message);
    }
}
