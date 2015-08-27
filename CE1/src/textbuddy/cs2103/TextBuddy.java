package textbuddy.cs2103;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class TextBuddy {

    private static final String MESSAGE_WELCOME = "Welcome to TextBuddy. %s is ready for use";
    private static final String MESSAGE_COMMAND_INPUT = "command: ";
    private static final String MESSAGE_COMMAND_INVALID = "Invalid command specified.";
    private static final String MESSAGE_COMMAND_EMPTY = "Please enter a command.";
    private static final String MESSAGE_TERMINATE = "Terminating program...";
    
    private static final String MESSAGE_ADD_EMPTY = "Unable to add with empty input parameters. Please input one or more characters after the 'add' command.";
    private static final String MESSAGE_ADD_SUCCESS = "added to %s: \"%s\"";
    
    private static final String MESSAGE_DISPLAY_EMPTY = "%s is empty";
    private static final String MESSAGE_CLEAR = "all content deleted from %s";
    
    private static final String MESSAGE_OUTPUT_FILENAME_EMPTY = "The output filename cannot be empty. Please input one or more characters.";
    private static final String MESSAGE_OUTPUT_FILENAME_MULTIPLE = "There are multiple filenames provided. Please provide only one filename.";
    private static final String MESSAGE_OUTPUT_FILENAME_NONE = "There is no filenames provided. Please provide a filename.";

    private static final String ERROR_FILE_CREATE = "Error! Unable to create output file.";
    private static final String ERROR_FILE_EXISTS = "Error! A file with the input filename already exists!";
    private static final String ERROR_FILE_WRITE = "Error! Unable to write to output file.";
    private static final String ERROR_FILE_READ = "Error! Unable to read from output file.";
    private static final String ERROR_FILE_NOT_FOUND = "Error! File does not exist.";
    
    private static final String COMMAND_ADD = "add";
    private static final String COMMAND_DISPLAY = "display";
    private static final String COMMAND_DELETE = "delete";
    private static final String COMMAND_CLEAR = "clear";
    private static final String COMMAND_EXIT = "exit";

    private static Scanner sc = new Scanner(System.in);
    private static File outputFile;
    private static String fileName;
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

    private static void readInputs() {
        // TODO Auto-generated method stub
        String userInput;
        while (true) {
            displayMessageInline(MESSAGE_COMMAND_INPUT);
            userInput = sc.nextLine();
            processCommand(userInput);
        }
    }

    private static void processCommand(String userInput) {
        // TODO Auto-generated method stub
        if (checkEmptyString(userInput)) {
            displayMessage(MESSAGE_COMMAND_EMPTY);
            return;
        }
        String userCommand = getCommand(userInput);
        String remainingInput = removeCommand(userInput, userCommand);
        switch (userCommand) {
        case COMMAND_ADD:
            doAdd(remainingInput);
            break;
            
        case COMMAND_DISPLAY:
            // add a method for checking additional parameters.
            doDisplay();
            break;
            
        case COMMAND_DELETE:
            //return doDelete(remainingInput)
            break;
            
        case COMMAND_CLEAR:
            //return doClear();
            break;
            
        case COMMAND_EXIT:
            exitCheck(remainingInput);
            break;
            
        default:
            displayMessage(MESSAGE_COMMAND_INVALID);
            return;
        }
    }


    private static void doAdd(String remainingInput) {
        // TODO Auto-generated method stub
        String textToAdd = remainingInput.trim();
        if (checkEmptyString(textToAdd)) {
            displayMessage(MESSAGE_ADD_EMPTY);
            return;
        } else {
            try {
                fileWriter = new FileWriter(outputFile, true);
                bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(remainingInput);
                bufferedWriter.newLine();
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
                displayFormattedMessage(MESSAGE_ADD_SUCCESS, fileName, remainingInput);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                displayError(ERROR_FILE_WRITE);
                e.printStackTrace();
            } 
        }
        
    }

    private static void doDisplay() {
        // TODO Auto-generated method stub
        try {
            fileReader = new FileReader(outputFile);
            bufferedReader = new BufferedReader(fileReader);
            String currLine = bufferedReader.readLine();
            int count = 1;
            
            if (currLine == null) {
                displayFormattedMessage(MESSAGE_DISPLAY_EMPTY, fileName);
            }
            
            while (currLine != null) {
                displayMessageInline(formatCount(count));
                displayMessage(currLine);
                currLine = bufferedReader.readLine();
                count++;
            }
            bufferedReader.close();
            fileReader.close();            
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            displayError(ERROR_FILE_NOT_FOUND);
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            displayError(ERROR_FILE_READ);
            e.printStackTrace();
        }
    }

    private static Object doDelete(String remainingInput) {
        // TODO Auto-generated method stub
        return null;
    }
    
    private static Object doClear() {
        // TODO Auto-generated method stub
        return null;
    }
 
    private static void processOutputFile(String inputName) {
        // TODO Auto-generated method stub
        if (checkEmptyString(inputName)) {
            displayMessage(MESSAGE_OUTPUT_FILENAME_EMPTY);
            terminateProgramWithMessage();
        }
        fileName = inputName;
        outputFile = new File(fileName);
        if (!outputFile.exists()) {
            try {
                outputFile.createNewFile();
            } catch (IOException e) {
                displayError(ERROR_FILE_CREATE);
                e.printStackTrace();
            }
        } else {
            displayError(ERROR_FILE_EXISTS);
            terminateProgramWithMessage();
        }
    }

    private static boolean isValidArguments(String[] args) {
        // TODO Auto-generated method stub
        if (args.length == 1) {
            // valid
            return true;
        } else if (args.length == 0) {
            // no file name given
            displayMessage(MESSAGE_OUTPUT_FILENAME_NONE);
            return false;
        } else {
            // multiple or invalid filename given
            displayMessage(MESSAGE_OUTPUT_FILENAME_MULTIPLE);
            return false;
        }
    }

    private static void displayMessage(String message) {
        // TODO Auto-generated method stub
        System.out.println(message);
    }

    private static void displayMessageInline(String message) {
        // TODO Auto-generated method stub
        System.out.print(message);
    }

    private static void displayFormattedMessage(String message,
            Object... keywords) {
        // TODO Auto-generated method stub
        message = message.concat("%n");
        System.out.format(message, keywords);
    }

    private static void displayError(String errorMessage) {
        // TODO Auto-generated method stub
        System.err.println(errorMessage);
    }

    private static void terminateProgramWithMessage() {
        System.out.println(MESSAGE_TERMINATE);
        System.exit(0);
    }
    
    private static void exitCheck(String remainingInput) {
        if (remainingInput.isEmpty()) {
            System.exit(0);
        } else {
            displayMessage(MESSAGE_COMMAND_INVALID);
        }
    }
    
    private static boolean checkEmptyString(String checkString) {
        return checkString.trim().isEmpty();
    }

    private static String getCommand(String userInput) {
        return userInput.trim().split("\\s+")[0];
    }
    
    private static String removeCommand(String userInput, String userCommand) {
        return userInput.replaceFirst(userCommand, "").trim();
    }
    
    private static String formatCount(int count) {
        String countDisplay = Integer.toString(count) + ". ";
        return countDisplay;
    }
}
