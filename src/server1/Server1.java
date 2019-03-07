/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server1;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class Server1 {

    public static final int PORT = 4444;
//  Parte ASINCRONA
    private static final long PAUSE_BETWEEEN_MSGS = 10; // millisecs
    // a map to store all the ThreadedCliHandlers
    private static ConcurrentHashMap<String, ThreadCliHandler> chm
            = new ConcurrentHashMap<String, ThreadCliHandler>();
    // for statistics
    private static int msg = 0;
    private static Date date = new Date();
    private static int threads_created = 0;
//

    public static void main(String[] args) throws IOException {
        // Establece el puerto en el que escucha peticiones
        ServerSocket socketServidor = null;
        try {
            socketServidor = new ServerSocket(PORT);
        } catch (IOException e) {
            System.out.println("No puede escuchar en el puerto: " + PORT);
            System.exit(-1);
        }

        Socket socketCliente = null;
        BufferedReader entrada = null;
        PrintWriter salida = null;

        System.out.println("Escuchando: " + socketServidor);
        try {
            // Se bloquea hasta que recibe alguna petición de un cliente
            // abriendo un socket para el cliente
            socketCliente = socketServidor.accept();
            System.out.println("Connexión acceptada: " + socketCliente);
            // Establece canal de entrada
            entrada = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
            // Establece canal de salida
            salida = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socketCliente.getOutputStream())), true);

            // Hace eco de lo que le proporciona el cliente, hasta que recibe "Adios"
            while (true) {
                String str = entrada.readLine();
                System.out.println("Cliente: " + str);
                salida.println(str);
                if (str.equals("Adios")) {
                    break;
                }
            }

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
        salida.close();
        entrada.close();
        socketCliente.close();
        socketServidor.close();
    }

    /**
     * This method sends messages to a random outPutStream
     */
    private static void sendMsgsToRandomClients() {
        new Thread("Send-to-Clients") {
            public void run() {
                try {
                    boolean showInfo = true;
                    while (true) {
                        Random generator = new Random();
                        for (; chm.size() > 0; msg++) {
                            //gets a random Key from the list
                            String randomKey = new ArrayList<String>(
                                    chm.keySet()).get(generator.nextInt(chm
                                    .keySet().size()));
                            ThreadCliHandler cc = chm.get(randomKey);
                            //sends the message
                            if (!cc.socket.isClosed()) {
                                cc.out.println("From server to client "
                                        + randomKey + " MSG sent: " + msg + "\r");
                                cc.out.flush();
                            } else {
                                chm.remove(randomKey);
                            }
                            Thread.sleep(PAUSE_BETWEEEN_MSGS);
                            showInfo = true;
                        }
                        Thread.sleep(PAUSE_BETWEEEN_MSGS);
                        if (showInfo) {
                            System.out.println(
                                    "Array size: " + chm.keySet().size()
                                    + " msgs sent: " + msg
                                    + " threads-created: " + threads_created
                                    + " server up since: " + date);
                            showInfo = false;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

}
