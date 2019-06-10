package tools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class LnxParser {
    public static void parse(String fileName){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line = reader.readLine();
            while (line != null){
                System.out.println(line);
                String[] parts = line.split("\\s+");
                line = reader.readLine();
            }
            reader.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readOwnData(){

    }
}
