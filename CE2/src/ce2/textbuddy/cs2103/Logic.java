package ce2.textbuddy.cs2103;

import java.io.IOException;
import java.util.Scanner;

public class Logic {

    // This is the list of commands the program accepts. Will be used in a case
    // switch later on.
    private static final String COMMAND_ADD = "add";
    private static final String COMMAND_DISPLAY = "display";
    private static final String COMMAND_DELETE = "delete";
    private static final String COMMAND_CLEAR = "clear";
    private static final String COMMAND_EXIT = "exit";
    private static final String COMMAND_SORT = "sort";

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
        String[] commandParts;
        while (true) {
            userInput = waitForUserInput();
            commandParts = processCommand(userInput);
            executeCommand(commandParts);
        }
    }

    private String[] processCommand(String userInput) {
        if (isEmptyString(userInput)) {
            ui.displayMessage(ui.MESSAGE_COMMAND_EMPTY);
            return null;
        }
        String[] commandParts = new String[2];
        commandParts[0] = getCommand(userInput);
        commandParts[1] = removeCommand(userInput, getCommand(userInput));
        return commandParts;
    }

    private void executeCommand(String[] commandParts) {
        String userCommand = commandParts[0];
        String remainingInput = commandParts[1];
        try {
            switch (userCommand.toLowerCase()) {
            case COMMAND_ADD :
                executeAdd(remainingInput);
                break;

            case COMMAND_DISPLAY :
                executeDisplay(remainingInput);
                break;

            case COMMAND_DELETE :
                executeDelete(remainingInput);
                break;

            case COMMAND_CLEAR :
                executeClear(remainingInput);
                break;

            case COMMAND_EXIT :
                executeExit(remainingInput);
                break;

            case COMMAND_SORT :
                executeSort(remainingInput);
                break;

            default :
                ui.displayMessage(ui.MESSAGE_COMMAND_INVALID);
                return;
            }
        } catch (IOException e) {
            ui.displayMessage(ui.ERROR_FILE_WRITE);
            terminateProgramWithMessage();
        }
    }

    private void executeAdd(String remainingInput) throws IOException {
        String textToAdd = remainingInput.trim();
        if (isEmptyString(textToAdd)) {
            ui.displayMessage(ui.MESSAGE_ADD_EMPTY);
        } else {
            dataEngine.doAdd(textToAdd);
        }
    }

    private void executeClear(String remainingInput) throws IOException {
        if (isEmptyString(remainingInput)) {
            dataEngine.doClear();
        } else {
            ui.displayMessage(ui.MESSAGE_PARAMETERS_INVALID);
        }
    }

    private void executeDelete(String remainingInput) throws IOException {
        boolean isValidNumber = numberCheck(remainingInput);
        if (isValidNumber) {
            dataEngine.doDelete(remainingInput);
        }
    }

    private void executeDisplay(String remainingInput) {
        if (isEmptyString(remainingInput)) {
            dataEngine.doDisplay();
        } else {
            ui.displayMessage(ui.MESSAGE_PARAMETERS_INVALID);
        }
    }

    private void executeExit(String remainingInput){
        if (remainingInput.isEmpty()) {
            ui.displayMessage(ui.MESSAGE_EXIT);
            dataEngine.doExit();
        } else {
            ui.displayMessage(ui.MESSAGE_PARAMETERS_INVALID);
        }
    }

    private void executeSort(String remainingInput){
        if (remainingInput.isEmpty()) {
            dataEngine.doSort();
        } else {
            ui.displayMessage(ui.MESSAGE_PARAMETERS_INVALID);
        }
    }

    private boolean checkMultipleParam(String input) {
        // TODO Auto-generated method stub
        String[] parameters = splitString(input);
        if (parameters.length > 1) {
            return true;
        }
        return false;
    }

    private boolean isEmptyString(String checkString) {
        return checkString.trim().isEmpty();
    }

    private boolean isNotPositiveInteger(String input) {
        // TODO Auto-generated method stub
        int number;
        try {
            number = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            // TODO: handle exception
            return true;
        }
        if (number < 1) {
            return true;
        }
        return false;
    }

    private boolean numberCheck(String input) {
        // TODO Auto-generated method stub
        if (isEmptyString(input)) {
            ui.displayMessage(ui.MESSAGE_DELETE_EMPTY);
            return false;
        } else if (checkMultipleParam(input)) {
            ui.displayMessage(ui.MESSAGE_DELETE_MULTIPLE);
            return false;
        } else if (isNotPositiveInteger(input)) {
            ui.displayMessage(ui.MESSAGE_DELETE_INVALID_NUMBER);
            return false;
        } else {
            return true;
        }
    }

    private String getCommand(String userInput) {
        return userInput.trim().split("\\s+")[0];
    }

    private String removeParameter(String input, String parameterToRemove) {
        return input.replaceFirst(parameterToRemove, "").trim();
    }

    private String removeCommand(String userInput, String userCommand) {
        return removeParameter(userInput, userCommand);
    }

    private String getFileName(String[] args) {
        return args[0];
    }

    private String waitForUserInput() {
        ui.displayMessageInline(ui.MESSAGE_COMMAND_INPUT);
        String userInput = ui.getUserInput();
        return userInput;
    }

    private String[] splitString(String stringToSplit) {
        return stringToSplit.trim().split("\\s+");
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
            ui.displayMessage(ui.MESSAGE_OUTPUT_FILENAME_NONE);
            return false;
        } else {
            ui.displayMessage(ui.MESSAGE_OUTPUT_FILENAME_MULTIPLE);
            return false;
        }
    }

    private void initializeComponents(String[] args) {
        _fileName = getFileName(args);
        initUi();
        initDataEngine(_fileName, ui);
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
        ui.displayMessage(ui.MESSAGE_WELCOME);
        return ui;
    }

    private void terminateProgramWithMessage() {
        ui.displayMessage(ui.MESSAGE_TERMINATE);
        System.exit(0);
    }

    // Test methods
    void testInitialize(String[] outputfilename) {
        initializeComponents(outputfilename);
    }

    void testExecuteInput(String[] commandParts) {
        executeCommand(commandParts);
    }

    String[] testProcessCommand(String input) {
        return processCommand(input);
    }

    int getNumberOfLines() {
        return dataEngine.getTotalLines();
    }

    String getTaskWithIndex(int index) {
        return dataEngine.getTask(index);
    }

    boolean reset() {
        return dataEngine.resetAll();
    }

}
