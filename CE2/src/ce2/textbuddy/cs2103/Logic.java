package ce2.textbuddy.cs2103;

import java.io.IOException;

/**
 * Logic handles most of the operations between the user and TextBuddy.
 * It also manages the parsing of the inputs and redirects work to the
 * other components, except Storage which is handled by DataEngine.
 *
 * @author Xue Si
 *
 */
public class Logic {

    // This is the list of commands that the program accepts only
    // and will be used in a case switch later on to reject all other commands.
    private static final String COMMAND_ADD = "add";
    private static final String COMMAND_DISPLAY = "display";
    private static final String COMMAND_DELETE = "delete";
    private static final String COMMAND_CLEAR = "clear";
    private static final String COMMAND_EXIT = "exit";
    private static final String COMMAND_SEARCH = "search";
    private static final String COMMAND_SORT = "sort";

    private DataEngine _dataEngine;
    private UI _ui;

    private String _fileName;

    /**
     * Initializes the various components that it needs to communicate with (UI,
     * DataEngine and Scanner). Since it is in charge of retrieving commands from
     * the user and passing it to the DataEngine for processing the UI is
     * initialized here as well. Some messages are also printed in this stage and
     * the UI is passed to the DataEngine as well.
     *
     * @param args, contains output filename retrieved from main in TextBuddy
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
     * This method is in-charge of getting the user input and parsing it
     * before deciding how to process it and sending it to the DataEngine.
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

    /**
     * Splits the userInput into an array that contains two components to allow
     * cleaner visibility and easier execution of the command and its parameters.
     * @param userInput, user command to be parsed if not empty
     * @return String[2], in the order of: command, parameters
     */
    private String[] processCommand(String userInput) {
        if (isEmptyString(userInput)) {
            _ui.displayMessage(_ui.MESSAGE_COMMAND_EMPTY);
            return null;
        }
        String[] commandParts = new String[2];
        commandParts[0] = getCommand(userInput);
        commandParts[1] = removeCommand(userInput, getCommand(userInput));
        return commandParts;
    }

    /**
     * Checks the command type and calls the corresponding functions if available.
     * @param commandParts, allow easy retrieval of the command and parameters
     */
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

            case COMMAND_SEARCH :
                executeSearch(remainingInput);
                break;

            case COMMAND_SORT :
                executeSort(remainingInput);
                break;

            default :
                _ui.displayMessage(_ui.MESSAGE_COMMAND_INVALID);
                return;
            }
        } catch (IOException e) {
            _ui.displayMessage(_ui.ERROR_FILE_WRITE);
            terminateProgramWithMessage();
        }
    }

    /**
     * Adds the input into TextBuddy as a task if it is not empty.
     * @param remainingInput, to be added if it is not empty to prevent clutter
     * @throws IOException, thrown up from DataEngine to make it cleaner
     *                  since multiple commands throw it as well
     */
    private void executeAdd(String remainingInput) throws IOException {
        String textToAdd = remainingInput.trim();
        if (isEmptyString(textToAdd)) {
            _ui.displayMessage(_ui.MESSAGE_ADD_EMPTY);
        } else {
            _dataEngine.doAdd(textToAdd);
        }
    }

    /**
     *
     * @param remainingInput, required to be empty for command to execute
     * @throws IOException, thrown up from DataEngine to make it cleaner
     *                  since multiple commands throw it as well
     */
    private void executeClear(String remainingInput) throws IOException {
        if (isEmptyString(remainingInput)) {
            _dataEngine.doClear();
        } else {
            _ui.displayMessage(_ui.MESSAGE_PARAMETERS_INVALID);
        }
    }

    /**
     * Checks the parameter given to make sure it is valid as a line number.
     * @param remainingInput, required to be a digit for command to execute
     * @throws IOException, thrown up from DataEngine to make it cleaner
     *                  since multiple commands throw it as well
     */
    private void executeDelete(String remainingInput) throws IOException {
        boolean isValidNumber = numberCheck(remainingInput);
        if (isValidNumber) {
            _dataEngine.doDelete(remainingInput);
        }
    }

    /**
     * Displays the current task list in a list format, shows the
     * line entry number to be used in deletion and the task.
     * @param remainingInput, required to be empty for command to execute
     */
    private void executeDisplay(String remainingInput) {
        if (isEmptyString(remainingInput)) {
            _dataEngine.doDisplay();
        } else {
            _ui.displayMessage(_ui.MESSAGE_PARAMETERS_INVALID);
        }
    }

    /**
     *
     * @param remainingInput, required to be empty for command to execute
     */
    private void executeExit(String remainingInput) {
        if (remainingInput.isEmpty()) {
            _ui.displayMessage(_ui.MESSAGE_EXIT);
            _dataEngine.doExit();
        } else {
            _ui.displayMessage(_ui.MESSAGE_PARAMETERS_INVALID);
        }
    }

    /**
     * Searches the current task list for the term and is case-insensitive
     * to allow ease of use and retrieves more results.
     * @param remainingInput, to be used as the search term
     */
    private void executeSearch(String remainingInput) {
        if (remainingInput.isEmpty()) {
            _ui.displayMessage(_ui.MESSAGE_SEARCH_INVALID);
        } else {
            _dataEngine.doSearch(remainingInput);
        }
    }

    /**
     * Sorts the current task list and overwrites the current ordering,
     * sort order is determined by lower case first to make it more intuitive.
     * @param remainingInput, required to be empty for command to execute
     * @throws IOException, thrown up from DataEngine to make it cleaner
     *                  since multiple commands throw it as well
     */
    private void executeSort(String remainingInput) throws IOException {
        if (remainingInput.isEmpty()) {
            _dataEngine.doSort();
        } else {
            _ui.displayMessage(_ui.MESSAGE_PARAMETERS_INVALID);
        }
    }

    private boolean checkMultipleParam(String input) {
        // Used in delete method as we do not allow multiple lines to
        // be deleted at one go
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
        // Checks for both an integer and making sure
        // that it is positive to prevent errors later
        // when we try to parse the input to int when deleting.
        int number;
        try {
            number = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            // as long as a character is not a number
            return true;
        }
        if (number < 1) {
            return true;
        }
        return false;
    }

    private boolean numberCheck(String input) {
        // Does all the necessary checks for smooth deletion
        // to take place.
        if (isEmptyString(input)) {
            _ui.displayMessage(_ui.MESSAGE_DELETE_EMPTY);
            return false;
        } else if (checkMultipleParam(input)) {
            _ui.displayMessage(_ui.MESSAGE_DELETE_MULTIPLE);
            return false;
        } else if (isNotPositiveInteger(input)) {
            _ui.displayMessage(_ui.MESSAGE_DELETE_INVALID_NUMBER);
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
        _ui.displayMessageInline(_ui.MESSAGE_COMMAND_INPUT);
        String userInput = _ui.getUserInput();
        return userInput;
    }

    private String[] splitString(String stringToSplit) {
        return stringToSplit.trim().split("\\s+");
    }

    /**
     * Checks the number of parameters provided when running the program since
     * only 1 parameter as filename is required.
     * @param args, the filename for the output file of the program
     * @return boolean, true iff only one filename provided when
     *         running the program.
     */
    private boolean isValidArguments(String[] args) {
        if (args.length == 1) {
            return true;
        } else if (args.length == 0) {
            _ui.displayMessage(_ui.MESSAGE_OUTPUT_FILENAME_NONE);
            return false;
        } else {
            _ui.displayMessage(_ui.MESSAGE_OUTPUT_FILENAME_MULTIPLE);
            return false;
        }
    }

    private void initializeComponents(String[] args) {
        _fileName = getFileName(args);
        initUi();
        initDataEngine(_fileName, _ui);
    }

    private DataEngine initDataEngine(String fileName, UI ui) {
        _dataEngine = new DataEngine(fileName, ui);
        return _dataEngine;
    }

    private UI initUi() {
        _ui = new UI();
        _ui.displayMessage(_ui.MESSAGE_WELCOME);
        return _ui;
    }

    private void terminateProgramWithMessage() {
        _ui.displayMessage(_ui.MESSAGE_TERMINATE);
        System.exit(0);
    }

    // Methods to be called from the JUnit test class
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
        return _dataEngine.getTotalLines();
    }

    String getTaskWithIndex(int index) {
        return _dataEngine.getTask(index);
    }

    boolean reset() {
        return _dataEngine.resetAll();
    }

}
