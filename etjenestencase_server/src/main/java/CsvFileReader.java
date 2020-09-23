import java.io.*;
import java.util.ArrayList;

import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;

public class CsvFileReader {

    FileReader fileReader;

    public CsvFileReader(FileReader fileReader) {
        this.fileReader = fileReader;
    }

    public ArrayList readFile() {
        ArrayList fileContents = new ArrayList();
        try {
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String row;

            while ((row = bufferedReader.readLine()) != null) {
                String[] data = row.split(",");
                if(!(data[0]).equals("seq")) {
                    User user = new User(parseInt(data[0]), data[1], data[2], parseInt(data[3]), data[4], data[5], data[6], parseFloat(data[7]), parseFloat(data[8]), parseFloat(data[9]));
                    fileContents.add(user);
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return fileContents;
    }
}
