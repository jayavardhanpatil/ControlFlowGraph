package org.smm.cfg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Test {

    public static void main(String[] args) throws IOException {

        /*ProcessBuilder processBuilder = new ProcessBuilder();
        String path = System.getProperty("user.dir");

        processBuilder.command("bash", "-c", "open "+path+"/sample.png").start();


        Process process = processBuilder.start();
        */

        int a = 6;

        if(a==0){
            System.out.println("0");
        }else if(a==2){
            System.out.println("2");
        }else if(a==6){
            System.out.println("6");
        }else{
            System.out.println("nothing");
        }


    }


}
