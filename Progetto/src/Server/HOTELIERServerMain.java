package src.Server;
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
        System.out.println("Comando ricevuto: " + command);
        switch(command){
            case "1":
            input.readLine();
                if (input.readLine().equals("1")) {
                    output.println("Inserire username:");
                    String username = input.readLine();
                    if(username.equals("admin")){
                        output.println("Username già in uso.");
                        break;
                    }
                    output.println("Inserire password:");
                    String password = input.readLine();
                    
                } else {
                    output.println("Registrazione fallita.");
                }                    
            output.println("Registrazione effettuata.");
                break;
            case "2":
                //inserire richiamo a metodo per il login
                output.println("Login effettuato.");
                break;
            case "3":
                //inserire richiamo a metodo per la ricerca di un hotel
                output.println("Hotel trovato.");
                break;
            case "4":
                //inserire richiamo a metodo per la ricerca di tutti gli hotel di una città
                output.println("Hotel trovati.");
                break;
            case "5":
                //inserire richiamo a metodo per l'inserimento di una recensione
                output.println("Recensione inserita.");
                break;
            case "6":
                //inserire richiamo a metodo per la visualizzazione dei badge
                output.println("Badge visualizzati.");
                break;
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
