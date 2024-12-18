
package src.Server;

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import src.Data.*;
import java.util.concurrent.atomic.AtomicInteger;


public class HOTELIERServerMain {
    private static final int SERVER_PORT = 8080;
    //private static final String SERVER_ADDRESS = "localhost";
    private ServerSocket serverSocket;;
    private Database DB;
    private LeggiHotelsFile hf;
    ExecutorService threadPool;
    private AtomicInteger activeConnections = new AtomicInteger(0);
    private volatile boolean isShuttingDown = false;
    
    // private static int CLIENT_MCAST;
    // private static String MCAST_IP;

    // Inizializzazione con costruttore di Database e LeggiHotelsFile
    HOTELIERServerMain(Database DB, LeggiHotelsFile hf) {
        this.DB = DB;
        this.hf = hf;
    }

    public static void main(String args[]) {
        String percorsoFileDB = System.getProperty("user.dir") + File.separator + "src" + File.separator + "Data"
                + File.separator + "Users.json";
        Database DB = new Database(percorsoFileDB);
        //String percorsoFileHotel = System.getProperty("user.dir") + File.separator + "src" + File.separator + "Data"+ File.separator + "Hotels.json";
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

    public void start() {
        DB.initDB();
        
        threadPool = Executors.newCachedThreadPool();
        try {
            Share sharer = new Share("225.225.225.225", 8080);
            serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("Server in ascolto sulla porta " + SERVER_PORT);
            while (!isShuttingDown) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    if (isShuttingDown) {
                        clientSocket.close();
                        break;
                    }
                    activeConnections.incrementAndGet();
                    threadPool.execute(new ThreadWorker(clientSocket, DB, hf, activeConnections, this, sharer));
                } catch (IOException e) {
                    if (isShuttingDown) {
                        System.out.println("Server in fase di shutdown.");
                    } else {
                        System.out.println("Errore durante l'accettazione della connessione: " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Errore durante l'apertura del socket server: " + e.getMessage());
            e.printStackTrace();
        } finally {
            shutdown();
        }
    }

    public synchronized void initiateShutdown() {
        if (!isShuttingDown) {
            isShuttingDown = true;
            System.out.println("Avvio dello shutdown del server...");
            try {
                // Chiudi il ServerSocket per interrompere l'accettazione di nuove connessioni
                serverSocket.close();
            } catch (IOException e) {
                System.out.println("Errore durante la chiusura del ServerSocket: " + e.getMessage());
            }
        }
    }
    


    private void shutdown() {
        try {
            threadPool.shutdown();
            if (!threadPool.awaitTermination(30, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
            // Pausa per la persistenza dei dati
            System.out.println("Persistenza dei dati in corso...");
            Thread.sleep(5000); // 5 secondi
            DB.aggiornaDB();
            System.out.println("Persistenza completata. Server shutdown completo.");
        } catch (InterruptedException e) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }


}
