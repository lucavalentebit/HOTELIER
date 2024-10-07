//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HOTELIERServer {
    private static Database DB;
    private LeggiHotelsFile leggiHotelsFile;
    private static String SERVER_ADDRESS;
    private static int SERVER_PORT;
    private static int RANKING_UPDATE_INTERVAL;
    private static int CLIENT_MCAST;
    private static String MCAST_IP;
    ExecutorService service = Executors.newCachedThreadPool();

    public HOTELIERServer(Database DB, LeggiHotelsFile leggiHotelsFile) {
        HOTELIERServer.DB = DB;
        this.leggiHotelsFile = leggiHotelsFile;
    }

    public static void main(String[] args) throws IOException {
        String percorsoFile = System.getProperty("user.dir") + System.getProperty("file.separator") + "database.json";
        DB = new Database(percorsoFile);
        LeggiHotelsFile leggiHotelsFile = new LeggiHotelsFile();
        loadConfig();
        System.out.println("Avvio del caricamento del database...");
        boolean databaseLoaded = DB.initDB();
        if (databaseLoaded) {
            System.out.println("Caricamento del database completato.");
        } else {
            System.out.println("Errore durante il caricamento del database.");
        }

        HOTELIERServer server = new HOTELIERServer(DB, leggiHotelsFile);
        server.start();
    }

    private static void loadConfig() throws IOException {
        InputStream input = new FileInputStream(System.getProperty("user.dir") + System.getProperty("file.separator") + "Config.proprieties");
        Properties properties = new Properties();
        properties.load(input);
        SERVER_ADDRESS = properties.getProperty("server_address");
        SERVER_PORT = Integer.parseInt(properties.getProperty("server_port"));
        RANKING_UPDATE_INTERVAL = Integer.parseInt(properties.getProperty("RANKING_UPDATE_INTERVAL"));
        MCAST_IP = properties.getProperty("MCAST_IP");
        CLIENT_MCAST = Integer.parseInt(properties.getProperty("MCAST_PORT"));
    }

    private void start() {
        try {
            loadConfig();
            DB.initDB();
            AggiornaDB aggiornatore = new AggiornaDB(DB);
            Thread aggiornatoreThread = new Thread(aggiornatore);
            aggiornatoreThread.start();
            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("Server In Attesa Di Connessioni...");
            Share sharer = new Share(MCAST_IP, CLIENT_MCAST);
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            scheduler.scheduleAtFixedRate(() -> {
                this.leggiHotelsFile.updateRank();
            }, 0L, (long)RANKING_UPDATE_INTERVAL, TimeUnit.SECONDS);

            while(true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connessione accettata: " + clientSocket);
                this.service.execute(new ServerHandler(clientSocket, DB, sharer));
            }
        } catch (IOException var7) {
            IOException e = var7;
            e.printStackTrace();
        }
    }
}


//public class LeggiHotelsFile {
//    private static final String[] CAPOLUOGHI_REGIONE = {
//            "Ancona", "Aosta", "Bari", "Bologna", "Cagliari",
//            "Campobasso", "Catanzaro", "Firenze", "Genova", "L'Aquila",
//            "Milano", "Napoli", "Palermo", "Perugia", "Potenza",
//            "Roma", "Sassari", "Torino", "Trento", "Trieste"
//    };
//    private final Map<String, Hotel> HT = new HashMap<>();
//
//    // metodo per leggere il contenuto del file hotels.json
//    public void leggiHotelsFile() throws FileNotFoundException {
//        //String filePath = "C:\\Users\\Utente\\Desktop\\Progetto_Finale\\src\\Hotels.json";
//        String filePath = System.getProperty("user.dir") +  System.getProperty("file.separator") +  "Hotels.json";
//        File dbfile = new File(filePath);
//        try (FileReader fr = new FileReader(dbfile)) {
//            JsonElement fileElement = JsonParser.parseReader(fr);
//            JsonArray jsonUserArray = fileElement.getAsJsonArray();
//            for (JsonElement userElement : jsonUserArray) {
//                JsonObject itemJsonObject = userElement.getAsJsonObject();
//                String id = itemJsonObject.get("id").getAsString();
//                String name = itemJsonObject.get("name").getAsString();
//                String description = itemJsonObject.get("description").getAsString();
//                String city = itemJsonObject.get("city").getAsString();
//                String phone = itemJsonObject.get("phone").getAsString();
//                JsonArray JsonService = itemJsonObject.get("services").getAsJsonArray();
//                List<String> service = new ArrayList<>();
//                for (JsonElement services : JsonService) {
//                    service.add(services.getAsString());
//                }
//                double rate = itemJsonObject.get("rate").getAsDouble();
//                JsonObject ratingsObject = itemJsonObject.getAsJsonObject("ratings");
//                int cleaning = ratingsObject.get("cleaning").getAsInt();
//                int position = ratingsObject.get("position").getAsInt();
//                int services = ratingsObject.get("services").getAsInt();
//                int quality = ratingsObject.get("quality").getAsInt();
//                Ratings r = new Ratings(cleaning, position, services, quality);
//
//                //___________________________//
//                List<Review> recensione = new ArrayList<>();
//                if (itemJsonObject.has("reviews")) {
//                    JsonArray jsonReviewsArray = itemJsonObject.getAsJsonArray("reviews");
//                    for (JsonElement reviewElement : jsonReviewsArray) {
//                        JsonObject reviewObject = reviewElement.getAsJsonObject();
//                        //String reviewContent = reviewObject.get("content").getAsString();
//                        int positionScore = reviewObject.get("positionScore").getAsInt();
//                        int cleanlinessScore = reviewObject.get("cleanlinessScore").getAsInt();
//                        int serviceScore = reviewObject.get("serviceScore").getAsInt();
//                        int priceScore = reviewObject.get("priceScore").getAsInt();
//                        Review rev = new Review( positionScore, cleanlinessScore, serviceScore, priceScore);
//                        recensione.add(rev);
//                    }
//                }
//                Hotel hotel = new Hotel(id, name, description, city, phone, service, rate, r, recensione);
//                HT.put(name, hotel);
//            }
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
//    }