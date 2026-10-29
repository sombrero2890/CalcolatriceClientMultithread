package it.bianconi;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        // Inserendo il socket nel try-with-resources, Java chiuderà automaticamente 
        // prima gli stream e poi il socket in modo sicuro alla fine del blocco.
        try (Socket socket = this.clientSocket;
             DataInputStream in = new DataInputStream(socket.getInputStream());
             DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {

            // Lettura dei dati dal client
            double op1 = in.readDouble();
            char operatore = in.readChar();
            double op2 = in.readDouble();

            System.out.println("Richiesta da " + socket.getInetAddress() + ": " + op1 + " " + operatore + " " + op2);

            // Elaborazione e invio della risposta
            elabora(op1, operatore, op2, out);

            System.out.println("Connessione chiusa con: " + socket.getInetAddress() + "\n");

        } catch (IOException e) {
            System.err.println("Errore con il client: " + e.getMessage());
        }
    }

    private void elabora(double op1, char operatore, double op2, DataOutputStream out) throws IOException {
        double risultato;

        switch (operatore) {
            case '+':
                risultato = op1 + op2;
                break;
            case '-':
                risultato = op1 - op2;
                break;
            case '*':
                risultato = op1 * op2;
                break;
            case '/':
                if (op2 == 0) {
                    inviaErrore(out, "Errore: divisione per zero non consentita.");
                    return;
                }
                risultato = op1 / op2;
                break;
            default:
                inviaErrore(out, "Errore: operatore '" + operatore + "' non riconosciuto.");
                return;
        }

        out.writeBoolean(true);   // esito positivo
        out.writeDouble(risultato);
        out.flush();
    }

    private void inviaErrore(DataOutputStream out, String messaggio) throws IOException {
        out.writeBoolean(false);  // esito negativo
        out.writeUTF(messaggio);
        out.flush();
    }
}
