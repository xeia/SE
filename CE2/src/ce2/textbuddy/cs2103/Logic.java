package ce2.textbuddy.cs2103;

import java.util.Scanner;

public class Logic {

    private static final String MESSAGE_COMMAND_INPUT = "command: ";
    private static final String MESSAGE_OUTPUT_FILENAME_MULTIPLE = "Please provide only one filename.";
    private static final String MESSAGE_OUTPUT_FILENAME_NONE = "There is no filename provided.";
    private static final String MESSAGE_TERMINATE = "Program will now terminate...";
    private static final String MESSAGE_WELCOME = "Welcome to TextBuddy. %s is ready for use";

    private DataEngine dataEngine;
    private UI ui;

    private Scanner _scanner;
    private String _fileName;

    /**
     * Initializes the various components that it needs to communicate with (UI, DataEngine and Scanner)
     * since it is in charge of retrieving commands from the user and passing it to the DataEngine for processing
     * The UI is initialized here as some messages are printed in this stage and it is passed to the DataEngine as well.
     * @param args
     *          - contains output filename retrieved from main in TextBuddy
     */
    public void initialize(String[] args) {
        if (isValidArguments(args)) {
            initializeComponents(args);
            readInputs();
        } else {
            terminateProgramWithMessage();
        }
    }

    /**
     * This method is in-charge of scanning for user input and passing each input for
     * processing in the DataEngine.
     */
    private void readInputs() {
        String userInput;
        while (true) {
            userInput = waitForUserInput();
            dataEngine.processCommand(userInput);
        }
    }

    private String getFileName(String[] args) {
        return args[0];
    }

    private String waitForUserInput() {
        ui.displayMessage(MESSAGE_COMMAND_INPUT);
        String userInput = ui.getUserInput();
        return userInput;
    }

    /**
     * Checks the number of parameters provided when running the program since only 1
     * parameter as filename is required.
     *
     * @param args
     *            - the filename for the output file of the program
     * @return boolean
     *            - true if there is only one filename provided when running the program.
     */
    private boolean isValidArguments(String[] args) {
        if (args.length == 1) {
            return true;
        } else if (args.length == 0) {
            ui.displayMessage(MESSAGE_OUTPUT_FILENAME_NONE);
            return false;
        } else {
            ui.displayMessage(MESSAGE_OUTPUT_FILENAME_MULTIPLE);
            return false;
        }
    }

    private void initializeComponents(String[] args) {
        initUi();
        String fileName = getFileName(args);
        initDataEngine(fileName, ui);
        initScanner();
    }

    private DataEngine initDataEngine(String fileName, UI ui) {
        dataEngine = new DataEngine(fileName, ui);
        return dataEngine;
    }

    private Scanner initScanner() {
        _scanner = new Scanner(System.in);
        return _scanner;
    }

    private UI initUi(){
        ui = new UI();
        ui.displayFormattedMessage(MESSAGE_WELCOME, _fileName);
        return ui;
    }

    private void terminateProgramWithMessage() {
        ui.displayMessage(MESSAGE_TERMINATE);
        System.exit(0);
    }
}
