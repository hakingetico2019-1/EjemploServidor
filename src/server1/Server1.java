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
        BufferedReader entradaAdmin = null;
        System.out.println("Escuchando: " + socketServidor);
        try {
            socketCliente = socketServidor.accept();
            System.out.println("Connexión acceptada: " + socketCliente);
            
            entradaAdmin = new BufferedReader(new InputStreamReader(socketCliente.getInputStream())); // Obtenemos el canal de salida
            entrada = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
            salida = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socketCliente.getOutputStream())), true);
            //
            
            BufferedReader inServer
                = new BufferedReader(new InputStreamReader(System.in));   
            String linea;
            while (true) {
                String str = entrada.readLine();
                System.out.println("Cliente: " + str);
                salida.println(str);
                //Añadiendo
                if (str.equals("Adios")) {
                    break;
                }
            }
            

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
        //sendMsgsToRandomClients();
        salida.close();
        entrada.close();
        socketCliente.close();
        socketServidor.close();
    }

    /**
     * This method sends messages to a random outPutStream
     */
}
