package org.smm.cfg;

import java.io.*;
import java.util.LinkedList;

public class App {


    public static LinkedList<String> codelines = new LinkedList<>();

    public static void main(String[] args) throws IOException {

        App app = new App();
        app.readCodeLines();


    }


    private LinkedList<String> readCodeLines() throws IOException {
        File file = new File("program_1.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = "";
        StringBuilder builder = new StringBuilder();
        while ((line = reader.readLine()) != null){
            codelines.add(line.trim());
        }
        reader.close();
        return codelines;
    }

}
