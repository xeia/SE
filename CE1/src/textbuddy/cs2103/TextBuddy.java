package textbuddy.cs2103;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class TextBuddy {

    private static final String MESSAGE_WELCOME = "Welcome to TextBuddy. %s is ready for use";
    
    private static final String MESSAGE_OUTPUT_FILENAME_EMPTY = "The output filename cannot be empty. Please provide a filename.";
    private static final String MESSAGE_OUTPUT_FILENAME_MULTIPLE = "There are multiple filenames provided. Please provide only one filename.";
    private static final String MESSAGE_OUTPUT_FILENAME_NONE = "There is no filenames provided. Please provide a filename.";
    
    private static final String ERROR_CREATING_FILE = "Error in creating output file.";
    private static final String ERROR_READING_FILE = "";
    private static final String ERROR_WRITING_FILE = "";
        
    private static Scanner sc = new Scanner(System.in);
    private static File outputFile;
    
    public static void main(String[] args) {
        if (isValidArguments(args)) {
            processOutputFile(args[0]);
            displayMessage(MESSAGE_WELCOME);
        } else {
            System.exit(0);
        }
    }

    private static void processOutputFile(String fileName) {
        // TODO Auto-generated method stub
        outputFile = new File(fileName);
        if (!outputFile.exists()) {
            try {
                outputFile.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
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
}
