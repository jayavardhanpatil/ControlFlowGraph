package org.smm.cfg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Test {

    public static void main(String[] args) throws IOException {

        ProcessBuilder processBuilder = new ProcessBuilder();
        String path = System.getProperty("user.dir");

        processBuilder.command("bash", "-c", "open "+path+"/sample.png").start();


        Process process = processBuilder.start();

       /* BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()));

        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
*/
    }


}
