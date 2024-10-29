import java.io.*;
import java.net.*;

public class HOTELIERServerMain {
    public static void main(String args[]){
        String serverAddress = "localhost";
        int port = 8080;

        try  (ServerSocket serverSocket = new ServerSocket(port)){
            System.out.println("Server in ascolto sulla porta " + port);
            
            while(true){
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true)) {
                    System.out.println("Connessione accettata da " + clientSocket.getRemoteSocketAddress());
                    handleCommands(input, output, serverSocket);
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

    private static void handleCommands(BufferedReader input, PrintWriter output, ServerSocket serverSocket) throws IOException {
        String command = input.readLine();
        switch(command){
            case "7":
                System.out.println("Ricevuta richiesta di chiusura del server.");
                output.println("Server chiuso.");
                serverSocket.close();
                System.exit(0);
                break;
                
                default:
                    System.out.println("Comando sconosciuto.");
                    output.println("Comando sconosciuto.");
                    break;
        }
    }       
}
