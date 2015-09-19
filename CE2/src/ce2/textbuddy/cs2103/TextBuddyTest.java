package ce2.textbuddy.cs2103;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TextBuddyTest {
    private static final String[] outputFileName = {"output.txt"};
    Logic tb = new Logic();

    // Imported Strings from the UI component, replaced with output.txt to be used for assertions.
//    private static final String MESSAGE_ADD_EMPTY = "Unable to add with empty input parameters. Please input one or more characters after the 'add' command.";
//    private static final String MESSAGE_ADD_SUCCESS = "added to output.txt: \"%s\"";
//    private static final String MESSAGE_DISPLAY_EMPTY = "output.txt is empty";
//    private static final String MESSAGE_CLEAR = "all content deleted from output.txt";
//    private static final String MESSAGE_DELETE_INVALID_NUMBER = "Parameter specified as line number is invalid. Please use integers only.";
//    private static final String MESSAGE_DELETE_EMPTY = "Line number specified is invalid, there is no line available for deletion. Please try again.";
//    private static final String MESSAGE_DELETE_SUCCESS = "deleted from output.txt: \"%s\"";
//    private static final String MESSAGE_DELETE_MULTIPLE = "Multiple parameters detected. Please provide only one line number at a time.";
    private static final String MESSAGE_ADD_EMPTY = "Unable to add with empty input parameters.";
    private static final String MESSAGE_ADD_SUCCESS = "added to output.txt: \"%s\"";
    private static final String MESSAGE_CLEAR = "all content deleted from output.txt";
    private static final String MESSAGE_DELETE_EMPTY = "Please provide a line number.";
    private static final String MESSAGE_DELETE_INVALID_NUMBER = "Parameter specified as line number is invalid.";
    private static final String MESSAGE_DELETE_SUCCESS = "deleted from output.txt: \"%s\"";
    private static final String MESSAGE_DELETE_MULTIPLE = "Please provide only one line number at a time.";
    private static final String MESSAGE_DISPLAY_EMPTY = "output.txt is empty";
    private static final String MESSAGE_PARAMETERS_INVALID = "Invalid parameters specified.";

    @Before
    public void setUp() {
        // initialize a new TextBuddy with an empty output.txt for each test run to isolate the test conditions
        tb.testInitialize(outputFileName);
    }

    @After
    public void tearDown() {
        // deletes the corresponding output file after each run so that each test is started new
        tb.reset();
    }

    @Test
    public void testProcessing(){
        String addCommand = "add Hello";
        String clearCommand = "clear";
        String deleteCommand = "delete 1";
        String displayCommand = "display";

        String expectedProcessedAdd = "add";
        String expectedProcessedAddInputs = "Hello";
        String expectedProcessedClear = "clear";
        String expectedProcessedDelete = "delete";
        String expectedProcessedDeleteInputs = "1";
        String expectedProcessedDisplay = "display";

        assertTrue(expectedProcessedAdd.equals(tb.testProcessCommand(addCommand)[0]));
        assertTrue(expectedProcessedAddInputs.equals(tb.testProcessCommand(addCommand)[1]));
        assertTrue(expectedProcessedClear.equals(tb.testProcessCommand(clearCommand)[0]));
        assertTrue(expectedProcessedDelete.equals(tb.testProcessCommand(deleteCommand)[0]));
        assertTrue(expectedProcessedDeleteInputs.equals(tb.testProcessCommand(deleteCommand)[1]));
        assertTrue(expectedProcessedDisplay.equals(tb.testProcessCommand(displayCommand)[0]));
    }

    @Test
    public void testDisplayEmpty() {
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        String[] commandParts = tb.testProcessCommand("display");
        tb.testExecuteInput(commandParts);

        assertEquals("Display empty message", MESSAGE_DISPLAY_EMPTY + "\r\n", outContent.toString());
    }

    @Test
    public void testDisplay() {
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        String[] commandParts = tb.testProcessCommand("add hihi");
        tb.testExecuteInput(commandParts);

        System.setOut(new PrintStream(outContent));
        commandParts = tb.testProcessCommand("display");
        tb.testExecuteInput(commandParts);

        assertEquals("Display #item message", "Displaying 1 tasks:\r\n1. hihi\r\n", outContent.toString());
    }

    @Test
    public void testAdd() {
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        String[] commandParts = tb.testProcessCommand("add hello");
        tb.testExecuteInput(commandParts);
        String outputString = String.format(MESSAGE_ADD_SUCCESS, "hello");
        assertEquals("Add success message", outputString + "\r\n", outContent.toString());
    }

    @Test
    public void testAddEmpty() {
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        String[] commandParts = tb.testProcessCommand("add");
        tb.testExecuteInput(commandParts);
        assertEquals("Add empty message", MESSAGE_ADD_EMPTY + "\r\n", outContent.toString());
    }

    @Test
    public void testDeleteInvalidCharacter() {
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        String[] commandParts = tb.testProcessCommand("delete x");
        tb.testExecuteInput(commandParts);
        assertEquals("Delete invalid character message", MESSAGE_DELETE_INVALID_NUMBER + "\r\n", outContent.toString());
    }

    @Test
    public void testDeleteNoParam() {
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        String[] commandParts = tb.testProcessCommand("delete");
        tb.testExecuteInput(commandParts);
        assertEquals("Delete invalid line message", MESSAGE_DELETE_EMPTY + "\r\n", outContent.toString());
    }

    @Test
    public void testDeleteMultiple() {
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        String[] commandParts = tb.testProcessCommand("delete 1 2 3");
        tb.testExecuteInput(commandParts);
        assertEquals("Delete multiple message", MESSAGE_DELETE_MULTIPLE + "\r\n", outContent.toString());
    }

    @Test
    public void testDeleteInvalidLine() {
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        String[] commandParts = tb.testProcessCommand("delete 99999");
        tb.testExecuteInput(commandParts);
        assertEquals("Delete invalid character message", MESSAGE_DELETE_INVALID_NUMBER + "\r\n", outContent.toString());
    }

    @Test
    public void testDelete() {
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        String[] commandParts = tb.testProcessCommand("add TESTDELETE");
        tb.testExecuteInput(commandParts);
        System.setOut(new PrintStream(outContent));
        commandParts = tb.testProcessCommand("delete 1");
        tb.testExecuteInput(commandParts);
        String outputString = String.format(MESSAGE_DELETE_SUCCESS, "TESTDELETE");
        assertEquals("Delete success message", outputString + "\r\n", outContent.toString());
    }

    @Test
    public void testClear() {
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        String[] commandParts = tb.testProcessCommand("add TESTDELETE");
        tb.testExecuteInput(commandParts);
        System.setOut(new PrintStream(outContent));
        commandParts = tb.testProcessCommand("clear");
        tb.testExecuteInput(commandParts);
        assertEquals("Clear message", MESSAGE_CLEAR + "\r\n", outContent.toString());
    }

    // Test for commands that will be deemed as invalid if extra parameters are given
    @Test
    public void testExtraParameters() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        String[] commandParts = tb.testProcessCommand("clear EXTRA");
        tb.testExecuteInput(commandParts);
        assertEquals("Invalid parameters message", MESSAGE_PARAMETERS_INVALID + "\r\n", outContent.toString());

        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        commandParts = tb.testProcessCommand("display EXTRA");
        tb.testExecuteInput(commandParts);
        assertEquals("Invalid parameters message", MESSAGE_PARAMETERS_INVALID + "\r\n", outContent.toString());

        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        commandParts = tb.testProcessCommand("exit EXTRA");
        tb.testExecuteInput(commandParts);
        assertEquals("Invalid parameters message", MESSAGE_PARAMETERS_INVALID + "\r\n", outContent.toString());
    }
}
