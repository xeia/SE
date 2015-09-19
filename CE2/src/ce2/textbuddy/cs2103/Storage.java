package ce2.textbuddy.cs2103;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Storage {

    private String _fileName;
    private static File outputFile;
    private static BufferedReader bufferedReader;
    private static BufferedWriter bufferedWriter;
    private static FileWriter fileWriter;
    private static FileReader fileReader;

    public Storage(String fileName) {
        this._fileName = fileName;
    }

    boolean initializeFile(String fileName) {
        outputFile = createFileWithName(fileName);
        return checkFileExist();
    }

    void initializeNewFile() throws IOException {
        outputFile.createNewFile();
    }

    ArrayList<String> loadContent(String _fileName) throws IOException {
        initReader();
        String currLine;
        ArrayList<String> taskList = new ArrayList<String>();
        while ((currLine = bufferedReader.readLine()) != null) {
            taskList.add(currLine);
        }
        closeReader();
        return taskList;
    }

    void saveFile(ArrayList<String> taskList) throws IOException {
        initWriter(outputFile);
        for (int i = 0; i < taskList.size(); i++) {
            bufferedWriter.write(taskList.get(i) + "\n");
        }
        closeWriter();
    }

    private File createFileWithName(String inputName) {
        return new File(inputName);
    }

    private boolean checkFileExist() {
        return outputFile.exists();
    }

    /**
     * Creates a temporary file to write contents, used by delete method
     *
     * @return File - new temporary file
     */
    private File createTempFile() {
        String tempFileName = _fileName + ".tmp";
        File tempFile = new File(tempFileName);
        return tempFile;
    }

//    /**
//     * Checks if the renaming of the temporary file is successful.
//     *
//     * @param tempFile
//     *            - File to be renamed
//     * @return boolean - true if renaming is successful.
//     */
//    private boolean isRenameSuccessful(File tempFile) {
//        boolean isRenameSuccess = tempFile.renameTo(outputFile);
//        if (!isRenameSuccess) {
//            displayError(ERROR_FILE_RENAME);
//        }
//        return isRenameSuccess;
//    }
//
//    /**
//     * Checks if the deletion of the current output file is successful.
//     *
//     * @return boolean - true if deletion is successful.
//     */
//    private boolean isDeleteSuccessful() {
//        boolean isDeleteSuccess = outputFile.delete();
//        if (!isDeleteSuccess) {
//            displayError(ERROR_FILE_DELETE);
//        }
//        return isDeleteSuccess;
//    }

    private void initWriter(File fileToWrite) throws IOException {
        fileWriter = new FileWriter(fileToWrite);
        bufferedWriter = new BufferedWriter(fileWriter);
    }

    private void initReader() throws FileNotFoundException {
        fileReader = new FileReader(outputFile);
        bufferedReader = new BufferedReader(fileReader);
    }

//    private void initWriterAppendMode(File fileToWrite) throws IOException {
//        fileWriter = new FileWriter(fileToWrite, true);
//        bufferedWriter = new BufferedWriter(fileWriter);
//    }
//
//    private void writeLine(String remainingInput) throws IOException {
//        bufferedWriter.write(remainingInput);
//        bufferedWriter.newLine();
//    }

    private void closeWriter() throws IOException {
        bufferedWriter.close();
        fileWriter.close();
    }

    private void closeReader() throws IOException {
        bufferedReader.close();
        fileReader.close();
    }

    private void reinitializeFile() throws IOException {
        fileWriter = new FileWriter(outputFile);
        fileWriter.close();
    }
}
