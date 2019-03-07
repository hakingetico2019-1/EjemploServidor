/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author 2098325
 */
   class ThreadCliHandler extends Thread {
     
     
        private static ConcurrentHashMap<String, ThreadCliHandler> chm
            = new ConcurrentHashMap<String, ThreadCliHandler>();
        public Socket socket;
        public BufferedReader in;
        public PrintWriter out;
        public String nameSocket = "";
        public ThreadCliHandler(Socket socket) {
            this.socket = socket;
        }
        public void run() {
            try {
                // Create streams for the socket.
                in = new BufferedReader(new InputStreamReader(
                        socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                out.print("Insert Client Name:");
                out.flush();
                // The first input line is the client name in this example
                nameSocket = in.readLine();
                // stores the hander in the list
                chm.put(nameSocket, this);
                System.out.println("SRV-REC new client: " + nameSocket);
                while (true) {
                    String inLine = in.readLine();
                    // its only null if something went wrong
                    if (inLine != null && inLine.toLowerCase().indexOf("bye") == -1) {
                        System.out.println("SRV-REC " + nameSocket + ": "
                                + inLine);
                    } else {
                        System.out.println("SRV-REC " + nameSocket + ": "
                                + inLine);
                        break;
                    }
                }
            } catch (SocketException es) {
                // happens if the client disconnects unexpectedly
                System.out.println(es);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                chm.remove(nameSocket);
                try {
                    socket.close();
                } catch (IOException e) {}
            }
        }
    }