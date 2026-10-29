package it.bianconi;

import java.io.*;
import java.net.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Client {
    private static final String HOST = "localhost";
    private static final int PORTA = 12345;

    public static void main(String[] args) {
        //per prendere l'input del client;
        Scanner scanner = new Scanner(System.in);

        try {
            // Acquisizione dati da tastiera
            System.out.print("Primo numero: ");
            double op1 = scanner.nextDouble();

            System.out.print("Operatore (+, -, *, /): ");
            String s = scanner.next();
            if (s.length() != 1) {
                System.out.println("Inserire un solo carattere come operatore.");
                return;
            }
            char operatore = s.charAt(0);

            System.out.print("Secondo numero: ");
            double op2 = scanner.nextDouble();

            // Connessione e scambio dati
            try (Socket socket = new Socket(HOST, PORTA);
                 DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                 DataInputStream in = new DataInputStream(socket.getInputStream())) {

                out.writeDouble(op1);
                out.writeChar(operatore);
                out.writeDouble(op2);
                out.flush();

                boolean successo = in.readBoolean();
                if (successo) {
                    double risultato = in.readDouble();
                    System.out.println("\nRisultato: " + op1 + " " + operatore + " " + op2
                            + " = " + risultato);
                } else {
                    System.out.println("\n" + in.readUTF());
                }

            } catch (UnknownHostException e) {
                System.err.println("Host sconosciuto: " + HOST);
            } catch (IOException e) {
                System.err.println("Errore di comunicazione: " + e.getMessage());
            }

        } catch (InputMismatchException e) {
            System.err.println("Input non valido: inserire un numero valido.");
        } finally {
            scanner.close();
        }
    }
}
