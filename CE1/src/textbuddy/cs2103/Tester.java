package textbuddy.cs2103;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class Tester {
    private static final String outputFileName = "output.txt";
    TextBuddy tb = new TextBuddy();
    
    // Imported Strings from TextBuddy, replaced with output.txt
    private static final String MESSAGE_ADD_EMPTY = "Unable to add with empty input parameters. Please input one or more characters after the 'add' command.";
    private static final String MESSAGE_ADD_SUCCESS = "added to output.txt: \"%s\"";
    private static final String MESSAGE_DISPLAY_EMPTY = "output.txt is empty";
    private static final String MESSAGE_CLEAR = "all content deleted from output.txt";
    private static final String MESSAGE_DELETE_INVALID_NUMBER = "Parameter specified as line number is invalid. Please use integers only.";
    private static final String MESSAGE_DELETE_EMPTY = "Line number specified is invalid, there is no line available for deletion. Please try again.";
    private static final String MESSAGE_DELETE_SUCCESS = "deleted from output.txt: \"%s\"";
    private static final String MESSAGE_DELETE_MULTIPLE = "Multiple parameters detected. Please provide only one line number at a time.";
    
    @Before
    public void setUp() {
        // initialize a new TextBuddy with output.txt for each test run 
        tb.testInitialize(outputFileName);
    }
    
    @After
    public void tearDown() {
        // deletes the corresponding output file after each run so that each test is started new
        tb.testEnd();
    }
    
    // Test Display when there is no items and when one item is added
    @Test
    public void testDisplayEmpty() {
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        tb.testCommand("display");
        assertEquals("Should be display empty message", "\r\n" + MESSAGE_DISPLAY_EMPTY + "\r\n", outContent.toString());
    }
    
    @Test
    public void testDisplay() {
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        tb.testCommand("add hihi");
        System.setOut(new PrintStream(outContent));
        tb.testCommand("display");
        assertEquals("Should be display 1 item 'hihi'", "\r\n1. hihi\r\n", outContent.toString());
    }
    
    // Test Add using a 'hello' text and with empty parameters
    @Test
    public void testAdd() {
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        tb.testCommand("add hello");
        String outputString = String.format(MESSAGE_ADD_SUCCESS, "hello");
        assertEquals("Should be add success message", "\r\n" + outputString + "\r\n", outContent.toString());
    }
    
    @Test
    public void testAddEmpty() {
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        tb.testCommand("add");
        assertEquals("Should be add empty message", "\r\n" + MESSAGE_ADD_EMPTY + "\r\n", outContent.toString());
    }
    
    // Test Delete using non-integer(invalid char), non-existent line number, multiple lines and delete after adding one item.
    @Test
    public void testDeleteInvalidCharacter() {
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        tb.testCommand("delete b");
        assertEquals("Should be delete invalid character message", "\r\n" + MESSAGE_DELETE_INVALID_NUMBER + "\r\n", outContent.toString());
    }
    
    @Test
    public void testDeleteInvalidLine() {
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        tb.testCommand("delete 1");
        assertEquals("Should be delete invalid line message", "\r\n" + MESSAGE_DELETE_EMPTY + "\r\n", outContent.toString());
    }
    
    @Test
    public void testDeleteMultiple() {
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        tb.testCommand("delete 1 2 3");
        assertEquals("Should be delete multiple message", "\r\n" + MESSAGE_DELETE_MULTIPLE + "\r\n", outContent.toString());
    }
    
    @Test
    public void testDelete() {
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        tb.testCommand("add todelete");
        System.setOut(new PrintStream(outContent));
        tb.testCommand("delete 1");
        String outputString = String.format(MESSAGE_DELETE_SUCCESS, "todelete");
        assertEquals("Should be delete success message", "\r\n" + outputString + "\r\n", outContent.toString());
    }
    
    // Test Clear
    @Test
    public void testClear() {
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        tb.testCommand("add todelete");
        System.setOut(new PrintStream(outContent));
        tb.testCommand("clear");
        assertEquals("Should be clear message", "\r\n" + MESSAGE_CLEAR + "\r\n", outContent.toString());
    }

}
