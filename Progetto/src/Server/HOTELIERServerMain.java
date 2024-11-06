package src.Server;
import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HOTELIERServerMain {

    private static ExecutorService threadPool = Executors.newCachedThreadPool();


    /*public static void main(String args[]){
        String serverAddress = "localhost";
        int port = 8080;

        try  (ServerSocket serverSocket = new ServerSocket(port)){
            System.out.println("Server in ascolto sulla porta " + port);
            
            while(true){
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true)) {
                    System.out.println("Connessione accettata da " + clientSocket.getRemoteSocketAddress());
                    //Creare qui threadPool per inserire le connessioni 
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
*/

    public static void main(String args[]){
        int port = 8080;
        DataHandlerUsers dataHandler = new DataHandlerUsers();

        try  (ServerSocket serverSocket = new ServerSocket(port)){
            System.out.println("Server in ascolto sulla porta " + port);
            
            while (!dataHandler.isShutdownRequested()) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Connessione accettata da " + clientSocket.getRemoteSocketAddress());

                    dataHandler.handleClient(clientSocket);
                } catch (IOException e) {
                    if (dataHandler.isShutdownRequested()) {
                        System.out.println("Server in fase di spegnimento.");
                    } else {
                        System.out.println("Errore durante l'accettazione della connessione: " + e.getMessage());
                    }
                }
            }
            System.out.println("Chiusura del server.");
        } catch (IOException e) {
            System.out.println("Errore durante l'apertura del socket server: " + e.getMessage());
            e.printStackTrace();
        } finally {
            dataHandler.shutdown();
        }
    }
}
