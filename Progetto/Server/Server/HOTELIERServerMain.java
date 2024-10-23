package Server;

import java.io.*;
import java.net.*;

public class HOTELIERServerMain {
    public static void main(String args[]){
        String serverAddress = "localhost";
        int port = 8080;

        try  (ServerSocket serverSocket = new ServerSocket(port)){
            System.out.println("Server in ascolto sulla porta " + port);
            
            while (true){
                try (Socket clientSocket = serverSocket.accept();
                    BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true)) {
                    System.out.println("Connessione accettata da " + clientSocket.getRemoteSocketAddress());
                    String request = input.readLine();
                    handleMessages(input);
                    System.out.println("Richiesta ricevuta: " + request);
                } catch (Exception e) {
                    System.out.println("Errore durante la comunicazione con il client.");
                    e.printStackTrace();
                }
            }     
        }
        catch (IOException e) {
            System.out.println("Errore durante l'apertura del socket server.");
            e.printStackTrace();
        }
    }
    private static void handleMessages(BufferedReader input) {
        try {
            String message = input.readLine();
            System.out.println("Messaggio ricevuto: " + message);
            if (message.equals("7")) {
                System.out.println("Chiusura della connessione.");
                ServerSocket socket = null;
                Socket clientSocket = null;
                PrintWriter output = null;
                try {
                    output = new PrintWriter(clientSocket.getOutputStream(), true);
                    System.out.println("Connessione accettata da " + clientSocket.getRemoteSocketAddress());
                    String request = input.readLine();
                    handleMessages(input);
                    System.out.println("Richiesta ricevuta: " + request);
                } catch (IOException e) {
                    System.out.println("Errore durante la comunicazione con il client.");
                    e.printStackTrace();
                } finally {
                    if (socket != null) {
                        socket.close();
                    }
                    System.exit(0);
                }
            }
        } catch (IOException e) {
            System.out.println("Errore durante la ricezione del messaggio.");
        }
    }
}
