package com.jax.drcorn;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Sockets {
    static Socket s;
    static PrintWriter pw;


    public static void Conn() throws IOException {
        try {
            s = new Socket("raspberrypi", 8000);
            pw = new PrintWriter(s.getOutputStream());
            pw.write("Hello Doc");
            pw.flush();
            pw.close();
            s.close();
        } catch (UnknownHostException e){
            System.out.println("Fail");
            e.printStackTrace();
        } catch (IOException e){
            System.out.println("Fail");
            e.printStackTrace();
        }

    }
}
