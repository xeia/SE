package ce2.textbuddy.cs2103;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class DataEngine {

    private UI ui;
    private Storage storage;

    ArrayList<String> taskList = new ArrayList<String>();

    private String _fileName;

    public DataEngine(String fileName, UI ui) {
        this.ui = ui;
        this._fileName = fileName;
        initStorage();
    }

    private Storage initStorage() {
        storage = new Storage(_fileName);
        boolean isFileExist = storage.initializeFile();
        if (isFileExist) {
            loadFile();
        } else {
            try {
                storage.initializeNewFile();
                ui.displayFormattedMessage(ui.MESSAGE_FILE_OUTPUT_SUCCESS, _fileName);
            } catch (IOException e) {
                ui.displayMessage(ui.ERROR_FILE_CREATE);
                terminateProgram();
            }
        }
        return storage;
    }

    void doAdd(String remainingInput) throws IOException {
        taskList.add(remainingInput);
        storage.saveFile(taskList);
        ui.displayFormattedMessage(ui.MESSAGE_ADD_SUCCESS, _fileName,
                remainingInput);
    }

    void doDisplay() {
        // TODO Auto-generated method stub
        if (taskList.isEmpty()) {
            ui.displayFormattedMessage(ui.MESSAGE_DISPLAY_EMPTY, _fileName);
        } else {
            ui.displayText(taskList);
        }
    }

    void doDelete(String remainingInput) throws IOException {
        // TODO Auto-generated method stub
        int lineToDelete = Integer.parseInt(remainingInput) - 1;
        try {
            String removedTask = taskList.remove(lineToDelete);
            storage.saveFile(taskList);
            ui.displayFormattedMessage(ui.MESSAGE_DELETE_SUCCESS, _fileName, removedTask);
        } catch (IndexOutOfBoundsException e) {
            ui.displayMessage(ui.MESSAGE_DELETE_INVALID_NUMBER);
        }
    }

    void doClear() throws IOException {
        // TODO Auto-generated method stub
        taskList.clear();
        storage.saveFile(taskList);
        ui.displayFormattedMessage(ui.MESSAGE_CLEAR, _fileName);
    }

    void doExit() {
        terminateProgram();
    }

    void doSort() {
        Collections.sort(taskList);
        ui.displayMessage(ui.MESSAGE_SORT_SUCCESS);
    }

    boolean resetAll() {
        // TODO Auto-generated method stub
        return storage.deleteOutputFile();
    }

    String getTask(int lineNumber) {
        return taskList.get(lineNumber-1);
    }

    int getTotalLines() {
        return taskList.size();
    }

    private void terminateProgram() {
        System.exit(0);
    }

    private void loadFile() {
        ui.displayMessage(ui.MESSAGE_FILE_OUTPUT_EXISTS);
        try {
            taskList = storage.loadContent();
            doDisplay();
            ui.displayMessage(ui.MESSAGE_FILE_OUTPUT_REMINDER + System.lineSeparator());
        } catch (FileNotFoundException e) {
            ui.displayMessage(ui.ERROR_FILE_NOT_FOUND);
            terminateProgram();
        } catch (IOException e) {
            ui.displayMessage(ui.ERROR_FILE_READ);
            terminateProgram();
        }
    }
}
