package ce2.textbuddy.cs2103;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

/**
 * DataEngine handles all the command execution procedures and interacts
 * with the Storage component to load and store the taskList of TextBuddy.
 *
 * @author Xue Si
 *
 */
public class DataEngine {

    private UI _ui;
    private Storage _storage;
    private String _fileName;
    private ArrayList<String> _taskList = new ArrayList<String>();

    public DataEngine(String fileName, UI ui) {
        this._ui = ui;
        this._fileName = fileName;
        initStorage();
    }

    /**
     * Initializes the Storage component and loads the content of the output file
     * if it exists or creates a new file to store the task list by TextBuddy
     * @return Storage
     */
    private Storage initStorage() {
        _storage = new Storage(_fileName);
        boolean isFileExist = _storage.initializeFile();
        if (isFileExist) {
            loadFile();
        } else {
            try {
                _storage.initializeNewFile();
                _ui.displayFormattedMessage(_ui.MESSAGE_FILE_OUTPUT_SUCCESS, _fileName);
            } catch (IOException e) {
                _ui.displayMessage(_ui.ERROR_FILE_CREATE);
                terminateProgram();
            }
        }
        return _storage;
    }

    /**
     * Adds the input to the task list and stores it into the output file,
     * save occurs after each add operations to prevent data loss as well.
     * @param remainingInput
     * @throws IOException, thrown up to make it cleaner since
     *                      multiple commands throw it as well
     */
    void doAdd(String remainingInput) throws IOException {
        _taskList.add(remainingInput);
        _storage.saveFile(_taskList);
        _ui.displayFormattedMessage(_ui.MESSAGE_ADD_SUCCESS, _fileName,
                remainingInput);
    }

    void doDisplay() {
        if (_taskList.isEmpty()) {
            _ui.displayFormattedMessage(_ui.MESSAGE_DISPLAY_EMPTY, _fileName);
        } else {
            _ui.displayText(_taskList);
        }
    }

    /**
     * @param remainingInput
     * @throws IOException, thrown up to make it cleaner since
     *                      multiple commands throw it as well
     */
    void doDelete(String remainingInput) throws IOException {
        int lineToDelete = Integer.parseInt(remainingInput) - 1;
        try {
            String removedTask = _taskList.remove(lineToDelete);
            _storage.saveFile(_taskList);
            _ui.displayFormattedMessage(_ui.MESSAGE_DELETE_SUCCESS, _fileName, removedTask);
        } catch (IndexOutOfBoundsException e) {
            _ui.displayMessage(_ui.MESSAGE_DELETE_INVALID_NUMBER);
        }
    }

    /**
     * @throws IOException, thrown up to make it cleaner since
     *                      multiple commands throw it as well
     */
    void doClear() throws IOException {
        _taskList.clear();
        _storage.saveFile(_taskList);
        _ui.displayFormattedMessage(_ui.MESSAGE_CLEAR, _fileName);
    }

    void doExit() {
        terminateProgram();
    }

    void doSearch(String searchTerm) {
        ArrayList<String> resultList = searchFor(searchTerm);
        if (resultList.isEmpty()) {
            _ui.displayFormattedMessage(_ui.MESSAGE_SEARCH_EMPTY, searchTerm);
        } else {
            _ui.displayFormattedMessage(_ui.MESSAGE_SEARCH_SUCCESS, searchTerm);
            _ui.displayText(resultList);
        }
    }

    /**
     * @throws IOException, thrown up to make it cleaner since
     *                      multiple commands throw it as well
     */
    void doSort() throws IOException {
        Collections.sort(_taskList, Collator.getInstance(Locale.ENGLISH));
        _storage.saveFile(_taskList);
        _ui.displayMessage(_ui.MESSAGE_SORT_SUCCESS);
    }

    /**
     * A case-insensitive search function and allows for fuzzy search as well.
     * @param searchTerm
     * @return resultList, new ArrayList to be formatted and displayed
     *                     and not overwrite the current task list
     */
    private ArrayList<String> searchFor(String searchTerm) {
        ArrayList<String> resultList = new ArrayList<String>();
        for (String task : _taskList) {
            if (task.toLowerCase().contains(searchTerm.toLowerCase())) {
                resultList.add(task);
            }
        }
        return resultList;
    }

    // Used in the JUnit test class to remove the output file after each test.
    boolean resetAll() {
        return _storage.deleteOutputFile();
    }

    String getTask(int lineNumber) {
        return _taskList.get(lineNumber-1);
    }

    int getTotalLines() {
        return _taskList.size();
    }

    private void terminateProgram() {
        System.exit(0);
    }

    /**
     * Prevent overwriting of output file if it exists. Reads the
     * existing output file and store them before displaying
     * the contents to the user.
     */
    private void loadFile() {
        _ui.displayMessage(_ui.MESSAGE_FILE_OUTPUT_EXISTS);
        try {
            _taskList = _storage.loadContent();
            doDisplay();
            _ui.displayMessage(_ui.MESSAGE_FILE_OUTPUT_REMINDER + System.lineSeparator());
        } catch (FileNotFoundException e) {
            _ui.displayMessage(_ui.ERROR_FILE_NOT_FOUND);
            terminateProgram();
        } catch (IOException e) {
            _ui.displayMessage(_ui.ERROR_FILE_READ);
            terminateProgram();
        }
    }
}
