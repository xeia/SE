package ce2.textbuddy.cs2103;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * CE1: TextBuddy
 *
 * This class is based on the sample execution and program behavior found within
 * the module handbook. It is used to manipulate text in a file via various
 * commands that is supported and the manipulations are all saved to the output document after each command operation.
 *
 * A file name for the output file created by this program is required when
 * attempting to run it, although it has been modified to be able to accept a
 * file name after running as well for convenience purposes.
 * E.g. 'java textbuddy.cs2103.TextBuddy output.txt' will output a file named output.txt with all the text
 * manipulations done through this program.
 *
 * The commands that this program support and their forms are as follow:
 *      add         < text to be added >
 *      delete      < line number of entry displayed by the 'display command' >
 *      display
 *      clear
 *      exit
 *      edit        < line number of entry > < text to replace with >
 *
 * Some assumptions that this program makes:
 * - The amount of content kept within the text file output by this program is reasonable
 * - The text file used by the program when it is running is not being directly modified outside of the program
 *   i.e. all modifications to the text file outside of this program is made while the program is not running
 * - The input file name might refer to an existing file and is assumed to be a text file that is manipulatable.
 * - Commands are entered exactly as specified and with the correct number of parameters.
 * - File name inputs are given correctly
 *
 * A side note, as the 'delete' command uses file creation,deletion and rename,
 * the console should have sufficient privileges to carry out the command.
 *
 * @author Tan Xue Si
 *
 */
public class TextBuddy {

    // List of Strings used, separated into: Program Messages,
    private static final String MESSAGE_WELCOME = "Welcome to TextBuddy. %s is ready for use";
    private static final String MESSAGE_COMMAND_INPUT = "command: ";
    private static final String MESSAGE_COMMAND_INVALID = "Invalid command specified.";
    private static final String MESSAGE_COMMAND_EMPTY = "Please enter a command.";
    private static final String MESSAGE_TERMINATE = "Program will now terminate...";
    private static final String MESSAGE_OUTPUT_EXISTS = "Output file exists, showing all contents in file:";
    private static final String MESSAGE_OUTPUT_APPEND_MODE = "File opened in append mode. Type 'clear' to remove all contents.";

    // Command Related
    private static final String MESSAGE_ADD_EMPTY = "Unable to add with empty input parameters.";
    private static final String MESSAGE_ADD_SUCCESS = "added to %s: \"%s\"";
    private static final String MESSAGE_DISPLAY_EMPTY = "%s is empty";
    private static final String MESSAGE_CLEAR = "all content deleted from %s";
    private static final String MESSAGE_DELETE_INVALID_NUMBER = "Parameter specified as line number is invalid.";
    private static final String MESSAGE_DELETE_EMPTY = "Line number specified for deletion does not exist.";
    private static final String MESSAGE_DELETE_SUCCESS = "deleted from %s: \"%s\"";
    private static final String MESSAGE_DELETE_MULTIPLE = "Please provide only one line number at a time.";
    private static final String MESSAGE_EXIT = "Exiting TextBuddy... Goodbye!";

    private static final String MESSAGE_EDIT_EMPTY = "Line number specified is for editing does not exist.";
    private static final String MESSAGE_EDIT_SUCCESS = "\"%s\" has been modified to \"%s\".";

    // Output Filename Related
    private static final String MESSAGE_OUTPUT_FILENAME_EMPTY = "Output filename cannot be empty.";
    private static final String MESSAGE_OUTPUT_FILENAME_MULTIPLE = "Multiple filenames provided.";
    private static final String MESSAGE_OUTPUT_FILENAME_NONE = "No file name provided.";

    // Error Messages
    private static final String ERROR_FILE_CREATE = "Error! Unable to create output file.";
    private static final String ERROR_FILE_RENAME = "Error! Unable to rename temp file.";
    private static final String ERROR_FILE_DELETE = "Error! Unable to delete file.";
    private static final String ERROR_FILE_WRITE = "Error! Unable to write to output file.";
    private static final String ERROR_FILE_READ = "Error! Unable to read from output file.";
    private static final String ERROR_FILE_NOT_FOUND = "Error! File does not exist.";

    // This is the list of commands the program accepts. Will be used in a case
    // switch later on.
    private static final String COMMAND_ADD = "add";
    private static final String COMMAND_DISPLAY = "display";
    private static final String COMMAND_DELETE = "delete";
    private static final String COMMAND_CLEAR = "clear";
    private static final String COMMAND_EXIT = "exit";
    private static final String COMMAND_EDIT = "edit";

    // This list of variables are declared for the entire class as they are
    // accessed in multiple methods and allows the standardization of the
    // variable names.
    private static Scanner sc = new Scanner(System.in);
    private static String fileName;
    private static File outputFile;
    private static BufferedReader bufferedReader;
    private static BufferedWriter bufferedWriter;
    private static FileWriter fileWriter;
    private static FileReader fileReader;

    public static void main(String[] args) {
        if (isValidArguments(args)) {
            processOutputFile(args[0]);
            displayFormattedMessage(MESSAGE_WELCOME, fileName);
            readInputs();
        } else {
            terminateProgramWithMessage();
        }
    }

    /**
     * This method scans for the user input and passes each input for
     * processing.
     */
    private static void readInputs() {
        String userInput;
        while (true) {
            displayMessageInline(MESSAGE_COMMAND_INPUT);
            userInput = sc.nextLine();
            processCommand(userInput);
        }
    }

    /**
     * This method breaks down the entire line of input received through the
     * scanner IO into a command and the remaining inputs. It decides which
     * method to call to depending on the command argument. If an empty input is
     * received, the program prompts the user to enter another valid input.
     *
     * @param userInput
     *            - Entire string entered by the user
     */
    private static void processCommand(String userInput) {
        if (isEmptyString(userInput)) {
            displayMessage(MESSAGE_COMMAND_EMPTY);
            return;
        }
        String userCommand = getCommand(userInput);
        String remainingInput = removeCommand(userInput, userCommand);
        createLineBreak();
        switch (userCommand.toLowerCase()) {
        case COMMAND_ADD :
            doAdd(remainingInput);
            break;

        case COMMAND_DISPLAY :
            doDisplay();
            break;

        case COMMAND_DELETE :
            doDelete(remainingInput);
            break;

        case COMMAND_CLEAR :
            doClear();
            break;

        case COMMAND_EXIT :
            exitCheck(remainingInput);
            break;

        case COMMAND_EDIT :
            doEdit(remainingInput);
            break;

        default :
            displayMessage(MESSAGE_COMMAND_INVALID);
            return;
        }
    }

    private static void createLineBreak() {
        System.out.println();
    }

    /**
     * Adds a string into the output file if the string is not empty. Else it
     * prompts the user that empty strings are not accepted by the command.
     *
     * @param remainingInput
     *            - Contains the parameters in the form: TextToAdd, the input
     *            string without the command word to be appended to the end of
     *            the output file generated.
     */
    private static void doAdd(String remainingInput) {
        String textToAdd = remainingInput.trim();
        if (isEmptyString(textToAdd)) {
            displayMessage(MESSAGE_ADD_EMPTY);
            return;
        } else {
            try {
                initWriterAppendMode(outputFile);
                writeLine(remainingInput);
                closeWriter();
                displayFormattedMessage(MESSAGE_ADD_SUCCESS, fileName,
                        remainingInput);
            } catch (IOException e) {
                displayError(ERROR_FILE_WRITE);
                terminateProgramWithMessage();
            }
        }
    }

    /**
     * Lists the current list of content in the output file with an entry number
     * to be used for deletion. Displays a string message if the file is empty.
     */
    private static void doDisplay() {
        try {
            initReader();
            String currLine = bufferedReader.readLine();
            int count = 1;

            if (currLine == null) {
                displayFormattedMessage(MESSAGE_DISPLAY_EMPTY, fileName);
            } else {
                while (currLine != null) {
                    displayMessageInline(formatCount(count));
                    displayMessage(currLine);
                    currLine = bufferedReader.readLine();
                    count++;
                }
            }
            closeReader();
        } catch (FileNotFoundException e) {
            displayError(ERROR_FILE_NOT_FOUND);
            terminateProgramWithMessage();
        } catch (IOException e) {
            displayError(ERROR_FILE_READ);
            terminateProgramWithMessage();
        }
    }

    /**
     * Scans the content of the output file and deletes the entry specified.
     *
     * @param remainingInput
     *            - Only takes in one number as parameter in the form:
     *            LineNumber
     */
    private static void doDelete(String remainingInput) {
        String[] parameters = splitString(remainingInput);
        if (parameters.length == 0) {
            displayMessage(MESSAGE_DELETE_EMPTY);
            return;
        } else if (parameters.length > 1) {
            displayMessage(MESSAGE_DELETE_MULTIPLE);
            return;
        } else {
            String lineNumber = getFirstParameter(parameters);
            try {
                int lineNumberToRemove = Integer.parseInt(lineNumber);

                File tempFile = createTempFile();
                initReader();
                initWriter(tempFile);
                String currLine = bufferedReader.readLine();

                int count = 1;
                boolean deleted = false;
                String deletedLine = null;

                while (currLine != null) {
                    if (lineNumberToRemove == count) {
                        deleted = true;
                        deletedLine = currLine;
                        currLine = bufferedReader.readLine();
                        count++;
                    } else {
                        writeLine(currLine);
                        currLine = bufferedReader.readLine();
                        count++;
                    }
                }
                closeReader();
                closeWriter();

                if (!deleted) {
                    displayMessage(MESSAGE_DELETE_EMPTY);
                }

                boolean isSuccessfullyDeleted = isDeleteSuccessful();
                boolean isSuccessfullyRenamed = isRenameSuccessful(tempFile);

                if (isSuccessfullyRenamed && isSuccessfullyDeleted && deleted) {
                    displayFormattedMessage(MESSAGE_DELETE_SUCCESS, fileName,
                            deletedLine);
                }
            } catch (NumberFormatException e) {
                displayMessage(MESSAGE_DELETE_INVALID_NUMBER);
                return;
            } catch (FileNotFoundException e) {
                displayError(ERROR_FILE_NOT_FOUND);
                terminateProgramWithMessage();
            } catch (IOException e) {
                displayError(ERROR_FILE_WRITE);
                terminateProgramWithMessage();
            }
        }
    }

    /**
     * initializes a new empty file to overwrite the current existing output
     * file.
     */
    private static void doClear() {
        try {
            reinitializeFile();
            displayFormattedMessage(MESSAGE_CLEAR, fileName);

        } catch (IOException e) {
            displayError(ERROR_FILE_WRITE);
            terminateProgramWithMessage();
        }
    }

    /**
     * Scans the entire content of the output file and modifies the content of
     * the entry specified to the input provided.
     *
     * @param remainingInput
     *            - Contains the parameters in the form: LineNumber and TextToReplace
     */
    private static void doEdit(String remainingInput) {
        String[] parameters = splitString(remainingInput);
        String lineNumber = getFirstParameter(parameters);
        String textToReplace = removeParameter(remainingInput, lineNumber);
        try {
            int lineNumberToEdit = Integer.parseInt(lineNumber);

            File tempFile = createTempFile();
            initReader();
            initWriter(tempFile);
            String currLine = bufferedReader.readLine();

            int count = 1;
            boolean edited = false;
            String editedLine = null;

            while (currLine != null) {
                if (lineNumberToEdit == count) {
                    edited = true;
                    editedLine = currLine;
                    writeLine(textToReplace);
                    currLine = bufferedReader.readLine();
                    count++;
                } else {
                    writeLine(currLine);
                    currLine = bufferedReader.readLine();
                    count++;
                }
            }
            closeReader();
            closeWriter();

            if (!edited) {
                displayMessage(MESSAGE_EDIT_EMPTY);
            }

            boolean isSuccessfullyDeleted = isDeleteSuccessful();
            boolean isSuccessfullyRenamed = isRenameSuccessful(tempFile);

            if (isSuccessfullyRenamed && isSuccessfullyDeleted && edited) {
                displayFormattedMessage(MESSAGE_EDIT_SUCCESS, editedLine,
                        textToReplace);
            }
        } catch (NumberFormatException e) {
            displayMessage(MESSAGE_DELETE_INVALID_NUMBER);
            return;
        } catch (FileNotFoundException e) {
            displayError(ERROR_FILE_NOT_FOUND);
            terminateProgramWithMessage();
        } catch (IOException e) {
            displayError(ERROR_FILE_WRITE);
            terminateProgramWithMessage();
        }
    }

    /**
     * Checks if the file name provided is not empty before initializing a new
     * file with the file name provided.
     *
     * @param inputName
     *            - Name to be used for the file to be created.
     */
    private static void processOutputFile(String inputName) {
        if (isEmptyString(inputName)) {
            displayMessage(MESSAGE_OUTPUT_FILENAME_EMPTY);
            terminateProgramWithMessage();
        }
        initializeNewFile(inputName);
    }

    /**
     * Creates a file with the inputName provided.
     *
     * @param inputName
     *            - Name to be used for the file to be created.
     */
    private static void initializeNewFile(String inputName) {
        createFileWithName(inputName);
        if (!outputFile.exists()) {
            try {
                outputFile.createNewFile();
            } catch (IOException e) {
                displayError(ERROR_FILE_CREATE);
                terminateProgramWithMessage();
            }
        } else {
            displayMessageInline(MESSAGE_OUTPUT_EXISTS);
            doDisplay();
            displayMessage(MESSAGE_OUTPUT_APPEND_MODE);
        }
    }

    /**
     * Initializes a file object with the inputName
     *
     * @param inputName
     *            - Name of the File object to be created
     */
    private static void createFileWithName(String inputName) {
        fileName = inputName; // -> init file method
        outputFile = new File(fileName);
    }

    /**
     * Checks the number of parameters provided when running the program, only 1
     * parameter as filename is required.
     *
     * @param args
     *            - the filename for the output file of the program
     * @return boolean - true if there is only one filename provided when
     *         running the program.
     */
    private static boolean isValidArguments(String[] args) {
        if (args.length == 1) {
            return true;
        } else if (args.length == 0) {
            displayMessage(MESSAGE_OUTPUT_FILENAME_NONE);
            return false;
        } else {
            displayMessage(MESSAGE_OUTPUT_FILENAME_MULTIPLE);
            return false;
        }
    }

    /**
     * Creates a temporary file to write contents, used by delete method
     *
     * @return File - new temporary file
     */
    private static File createTempFile() {
        String tempFileName = fileName + ".tmp";
        File tempFile = new File(tempFileName);
        return tempFile;
    }

    /**
     * Checks if the renaming of the temporary file is successful.
     *
     * @param tempFile
     *            - File to be renamed
     * @return boolean - true if renaming is successful.
     */
    private static boolean isRenameSuccessful(File tempFile) {
        boolean isRenameSuccess = tempFile.renameTo(outputFile);
        if (!isRenameSuccess) {
            displayError(ERROR_FILE_RENAME);
        }
        return isRenameSuccess;
    }

    /**
     * Checks if the deletion of the current output file is successful.
     *
     * @return boolean - true if deletion is successful.
     */
    private static boolean isDeleteSuccessful() {
        boolean isDeleteSuccess = outputFile.delete();
        if (!isDeleteSuccess) {
            displayError(ERROR_FILE_DELETE);
        }
        return isDeleteSuccess;
    }

    // Other Helper Functions
    private static boolean isEmptyString(String checkString) {
        return checkString.trim().isEmpty();
    }

    private static String[] splitString(String stringToSplit) {
        return stringToSplit.trim().split("\\s+");
    }

    private static String getFirstParameter(String[] parameters) {
        return parameters[0];
    }

    private static String getCommand(String userInput) {
        return userInput.trim().split("\\s+")[0];
    }

    private static String removeCommand(String userInput, String userCommand) {
        return removeParameter(userInput, userCommand);
    }

    private static String formatCount(int count) {
        String countDisplay = Integer.toString(count) + ". ";
        return countDisplay;
    }

    private static void exitCheck(String remainingInput) {
        if (remainingInput.isEmpty()) {
            displayMessage(MESSAGE_EXIT);
            System.exit(0);
        } else {
            displayMessage(MESSAGE_COMMAND_INVALID);
        }
    }

    private static void initWriter(File fileToWrite) throws IOException {
        fileWriter = new FileWriter(fileToWrite);
        bufferedWriter = new BufferedWriter(fileWriter);
    }

    private static void initReader() throws FileNotFoundException {
        fileReader = new FileReader(outputFile);
        bufferedReader = new BufferedReader(fileReader);
    }

    private static void initWriterAppendMode(File fileToWrite) throws IOException {
        fileWriter = new FileWriter(fileToWrite, true);
        bufferedWriter = new BufferedWriter(fileWriter);
    }

    private static void writeLine(String remainingInput) throws IOException {
        bufferedWriter.write(remainingInput);
        bufferedWriter.newLine();
    }

    private static void closeWriter() throws IOException {
        bufferedWriter.close();
        fileWriter.close();
    }

    private static void closeReader() throws IOException {
        bufferedReader.close();
        fileReader.close();
    }

    private static void reinitializeFile() throws IOException {
        fileWriter = new FileWriter(outputFile);
        fileWriter.close();
    }

    private static void displayMessage(String message) {
        System.out.println(message);
    }

    private static void displayMessageInline(String message) {
        System.out.print(message);
    }

    private static void displayFormattedMessage(String message, Object... keywords) {
        message = message.concat("%n");
        System.out.format(message, keywords);
    }

    private static void displayError(String errorMessage) {
        System.err.println(errorMessage);
    }

    private static void terminateProgramWithMessage() {
        System.out.println(MESSAGE_TERMINATE);
        System.exit(0);
    }

    private static String removeParameter(String input, String parameterToRemove) {
        return input.replaceFirst(parameterToRemove, "").trim();
    }

    // Test Methods - package protected
    void testCommand(String input) {
        processCommand(input);
    }

    void testInitialize(String fileName) {
        processOutputFile(fileName);
    }

    void testEnd() {
        isDeleteSuccessful();
    }

}
