package textbuddy.cs2103;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class TextBuddy {

    private static final String MESSAGE_WELCOME = "Welcome to TextBuddy. %s is ready for use";
    private static final String MESSAGE_TERMINATE = "Terminating program...";
    
    private static final String MESSAGE_OUTPUT_FILENAME_EMPTY = "The output filename cannot be empty. Please provide a filename.";
    private static final String MESSAGE_OUTPUT_FILENAME_MULTIPLE = "There are multiple filenames provided. Please provide only one filename.";
    private static final String MESSAGE_OUTPUT_FILENAME_NONE = "There is no filenames provided. Please provide a filename.";
    
    private static final String ERROR_FILE_CREATE = "Error! Unable to create output file!";
    private static final String ERROR_FILE_EXISTS = "Error! A file with the input filename already exists!";
    private static final String ERROR_FILE_READ = "";
    private static final String ERROR_FILE_WRITE = "";
        
    private static Scanner sc = new Scanner(System.in);
    private static File outputFile;
    private static String fileName;
    
    public static void main(String[] args) {
        if (isValidArguments(args)) {
            processOutputFile(args[0]);
            displayFormattedMessage(MESSAGE_WELCOME, fileName);
        } else {
            System.exit(0);
        }
    }

    private static void processOutputFile(String inputName) {
        // TODO Auto-generated method stub
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
            terminateProgram();
        }
    }

    private static boolean isValidArguments(String[] args) {
        // TODO Auto-generated method stub
        if (args.length == 1) {
            // valid
            return true;
        } else if (args.length == 0) {
            // no file name given
            displayMessage(MESSAGE_OUTPUT_FILENAME_EMPTY);
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
    
    private static void displayFormattedMessage(String message, String keyword) {
        // TODO Auto-generated method stub
        System.out.println(String.format(message, keyword));
    }
    
    private static void displayError(String errorMessage) {
        // TODO Auto-generated method stub
        System.err.println(errorMessage);
    }
    
    private static void terminateProgram(){
        System.out.println(MESSAGE_TERMINATE);
        System.exit(0);
    }
}
