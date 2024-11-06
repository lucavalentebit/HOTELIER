
package src.Server;
import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import src.Data.Database;
import src.Data.HotelFile;

public class HOTELIERServerMain {

    private static Database DB;
    private static HotelFile hf;

    HOTELIERServerMain(Database DB, HotelFile hf){
        this.DB = DB;
        this.hf = hf;
    }

    ExecutorService threadPool = Executors.newCachedThreadPool();

    public static void main(String args[]){


        String percorsoFileDB = "../src/Data/Users.json";
        
        DB = new Database(percorsoFileDB);
        
        HotelFile hf = new HotelFile("src/Data/Hotels.json");

    try {
        //Startiamo il DB e verifichiamo che esso venga caricato correttamente
        DB.initDB();

    } catch (Exception e) {
        System.err.println(e);
    }

    HOTELIERServerMain server = new HOTELIERServerMain(DB, hf);
    server.start();

    }

    private void start(){
        DB.initDB();
        int port = 8080;
        //DataHandlerUsers dataHandler = new DataHandlerUsers();

        try  (ServerSocket serverSocket = new ServerSocket(port)){
            System.out.println("Server in ascolto sulla porta " + port);
            
            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    DataHandlerHotels dataHandlerHotels = new DataHandlerHotels();
                    System.out.println("Connessione accettata da " + clientSocket.getRemoteSocketAddress());
                    threadPool.execute(new ClientHandler(clientSocket, DB)); 
                    // devi passarci pure lo sharer che sarebbe la connessione MultiCast
                    //dataHandler.handleClient(clientSocket);
                } catch (IOException e) {
                    // if (dataHandler.isShutdownRequested()) {
                    //     System.out.println("Server in fase di spegnimento.");
                    // } else {
                    //     System.out.println("Errore durante l'accettazione della connessione: " + e.getMessage());
                    // }
                }
            }
            //System.out.println("Chiusura del server.");
        } catch (IOException e) {
            System.out.println("Errore durante l'apertura del socket server: " + e.getMessage());
            e.printStackTrace();
        } finally {
            //dataHandler.shutdown();
        }
    }
}
