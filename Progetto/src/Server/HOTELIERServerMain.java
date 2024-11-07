
package src.Server;

import java.io.*;
import java.net.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import src.Data.*;

public class HOTELIERServerMain {

    private Database DB;
    private LeggiHotelsFile hf;
    ExecutorService threadPool = Executors.newCachedThreadPool();


    // Inizializzazione con costruttore di Database e LeggiHotelsFile
    HOTELIERServerMain(Database DB, LeggiHotelsFile hf) {
        this.DB = DB;
        this.hf = hf;
    }


    public static void main(String args[]) {
        String percorsoFileDB = System.getProperty("user.dir") + File.separator + "src" + File.separator + "Data" + File.separator + "Users.json";
        Database DB = new Database(percorsoFileDB);
        String percorsoFileHotel = System.getProperty("user.dir") + File.separator + "src" + File.separator + "Data" + File.separator + "Hotels.json";
        LeggiHotelsFile hf = new LeggiHotelsFile();
        try {
            // Avviamo il DB e verifichiamo che esso venga caricato correttamente
            DB.initDB();

        } catch (Exception e) {
            System.err.println(e);
        }

        HOTELIERServerMain server = new HOTELIERServerMain(DB, hf);
        server.start();
    }

    private void start() {
        DB.initDB();
        int port = 8080;
        // notWorking:DataHandlerUsers dataHandler = new DataHandlerUsers();

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server in ascolto sulla porta " + port);

            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Connessione accettata da " + clientSocket.getRemoteSocketAddress());
                    threadPool.execute(new ThreadWorker(clientSocket, DB, hf));
                    // devi passarci pure lo sharer che sarebbe la connessione MultiCast
                    // notWorking:dataHandler.handleClient(clientSocket);
                } catch (IOException e) {
                    // notWorking: System.out.println("Server in fase di spegnimento.");
                    // notWorking: } else {
                    // notWorking: System.out.println("Errore durante l'accettazione della
                    // connessione: " + e.getMessage());
                    // notWorking: }
                }
            }
            // notWorking:System.out.println("Chiusura del server.");
        } catch (IOException e) {
            System.out.println("Errore durante l'apertura del socket server: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // notWorking:dataHandler.shutdown();
        }
    }
}
