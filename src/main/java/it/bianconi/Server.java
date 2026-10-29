package it.bianconi;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int PORTA = 12345;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORTA)) {
            System.out.println("Server in ascolto sulla porta " + PORTA + "...");

            while (true) {
                // Attende un client (chiamata bloccante)
                Socket client = serverSocket.accept();
                System.out.println("Client connesso: " + client.getInetAddress());

                // Avvia un nuovo thread per gestire il client in parallelo
                Thread clientThread = new Thread(new ClientHandler(client));
                clientThread.start();
            }
        } catch (IOException e) {
            System.err.println("Impossibile avviare il server: " + e.getMessage());
        }
    }
}
