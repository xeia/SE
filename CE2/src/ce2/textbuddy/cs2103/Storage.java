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

    boolean initializeFile() {
        outputFile = createFileWithName(_fileName);
        return checkFileExist();
    }

    void initializeNewFile() throws IOException {
        outputFile.createNewFile();
    }

    ArrayList<String> loadContent() throws IOException {
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

    boolean deleteOutputFile() {
        boolean isDeleteSuccess = outputFile.delete();
        return isDeleteSuccess;
  }

    private File createFileWithName(String inputName) {
        return new File(inputName);
    }

    private boolean checkFileExist() {
        return outputFile.exists();
    }

    private void initWriter(File fileToWrite) throws IOException {
        fileWriter = new FileWriter(fileToWrite);
        bufferedWriter = new BufferedWriter(fileWriter);
    }

    private void initReader() throws FileNotFoundException {
        fileReader = new FileReader(outputFile);
        bufferedReader = new BufferedReader(fileReader);
    }

    private void closeWriter() throws IOException {
        bufferedWriter.close();
        fileWriter.close();
    }

    private void closeReader() throws IOException {
        bufferedReader.close();
        fileReader.close();
    }
}
