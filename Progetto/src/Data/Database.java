package src.Data;

import com.google.gson.*;
import com.google.gson.stream.JsonWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import src.H_U_R.User;
import src.H_U_R.Review;

public class Database {
    
    private String DBFILE;
    private ConcurrentHashMap<String, User> DB;

    public Database(String dbfilename) {
        DBFILE = dbfilename;
        DB = new ConcurrentHashMap<>();
    }

    public boolean initDB() {
        File dbfile = new File(DBFILE);

        try {
            if (!dbfile.exists() || dbfile.length() == 0) {
                // se il file non esiste o è vuoto, inizializza il database con un array JSON vuoto
                FileWriter fw = new FileWriter(DBFILE);
                fw.write("[]");
                fw.close();
            } else {
                // altrimenti, leggi gli utenti dal file
                try (FileReader fr = new FileReader(dbfile)) {
                    JsonElement fileElement = JsonParser.parseReader(fr);
                    if (!fileElement.isJsonArray()) {
                        // il file non contiene un array JSON valido, quindi non fa nulla
                        return false;
                    }
                    JsonArray jsonUserArray = fileElement.getAsJsonArray();
                    for (JsonElement userElement : jsonUserArray) {
                        JsonObject itemJsonObject = userElement.getAsJsonObject();
                        if (itemJsonObject.isJsonObject()) {
                            // controlla se l'oggetto JSON contiene tutti i campi necessari per rappresentare un utente
                            if (itemJsonObject.has("username") && itemJsonObject.has("password") && itemJsonObject.has("totalReviews") &&
                                    itemJsonObject.has("experienceLevel") && itemJsonObject.has("reviews")) {
                                // estrai i valori dei campi per creare un utente
                                String username = itemJsonObject.get("username").getAsString();
                                String password = itemJsonObject.get("password").getAsString();
                                int totalReviews = itemJsonObject.get("totalReviews").getAsInt();
                                int experienceLevel = itemJsonObject.get("experienceLevel").getAsInt();
                                JsonArray JsonReview = itemJsonObject.get("reviews").getAsJsonArray();
                                ArrayList<Review> reviews = new ArrayList<>();

                                for (JsonElement rev : JsonReview) {
                                    JsonObject reviewObject = rev.getAsJsonObject();
                                    int positionScore = reviewObject.get("positionScore").getAsInt();
                                    int cleanlinessScore = reviewObject.get("cleanlinessScore").getAsInt();
                                    int serviceScore = reviewObject.get("serviceScore").getAsInt();
                                    int priceScore = reviewObject.get("priceScore").getAsInt();
                                    Review review = new Review(positionScore, cleanlinessScore, serviceScore, priceScore);
                                    reviews.add(review);
                                }
                                // crea un oggetto User e aggiungilo al database

            // CAMBIARE COSTRUTTORE E SALVARE TUTTI I CAMPI (username, password, BADGE, numberOfReviews, reviews, loggedIn)

                                User user = new User(totalReviews, experienceLevel, reviews);
                                DB.put(username, user); 
                                // CAMBIARE

                            } else {
                                // se i campi dell'utente non sono presenti o non corretti, segnala un errore
                                System.out.println("Errore: Campi utente mancanti o non corretti.");
                            }
                        } else {
                            // se l'elemento non è un oggetto JSON valido, segnala un errore
                            System.out.println("Errore: Elemento non valido nel file JSON.");
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File " + DBFILE + " non trovato: " + e.getMessage());
            return false;
        } catch (IOException e) {
            System.out.println("Errore durante la lettura del file " + DBFILE + ": " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("Errore generico durante il caricamento del database: " + e.getMessage());
            return false;
        }
        return true;
    }

    public User TrovaUser (String username) {
        return DB.getOrDefault(username, null);
    }


    public void inserisci (User user) {
        String username = user.getUsername();
        DB.put(username, user);
        System.out.println("Utente inserito nel database: " + username);
        aggiornaDB();

    }

    public void addReviewToUser(String username, Review review) {
        //System.out.println("Username ricevuto dal client: " + username);
        User user = DB.get(username);
        if (user != null) {
            List<Review> userReviews = user.getReview();
            user.addReview(review);
            user.setReviews(userReviews);
            user.incrementTotalReviews();
            user.updateBadge();

            aggiornaDB();
        } else {
            System.out.println("Utente non trovato nel database.");
        }
    }


    public void aggiornaDB () {
        synchronized (DB) {

            try (FileWriter fw = new FileWriter(DBFILE); JsonWriter writer = new JsonWriter(fw)) {
                writer.setIndent(" "); //serve a formattare il file in modo leggibile
                writer.beginArray(); //tutti gli oggetti utente verranno inclusi all'interno di questo array
                for (ConcurrentHashMap.Entry<String, User> entry : DB.entrySet()) {
                    User user = entry.getValue();
                    writer.beginObject(); //oggetto singolo utente
                    writer.name("username").value(user.getUsername());
                    writer.name("password").value(user.getPassword());
                    writer.name("totalReviews").value(user.getNumberOfReviews());
                    writer.name("experienceLevel").value(user.getBadge());
                    // scrivi le recensioni dell'utente
                    writer.name("reviews").beginArray();
                    for (Review review : user.getReview()) {
                        writer.beginObject();
                        //writer.name("reviewContent").value(review.getReviewContent());
                        //writer.name("score").value(review.getScore());
                        writer.name("positionScore").value(review.getPositionScore());
                        writer.name("cleanlinessScore").value(review.getCleanlinessScore());
                        writer.name("serviceScore").value(review.getServiceScore());
                        writer.name("priceScore").value(review.getPriceScore());
                        //writer.name("publicationDate").value(review.getPublicationDate().getTime()); // Converte la data in millisecondi
                        writer.endObject();
                    }
                    writer.endArray();

                    writer.endObject();
                }
                writer.endArray();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }System.out.println("Aggiornamento del database completato.");
        System.out.println("Benvenuto su HOTELIER! Seleziona un'opzione:");
        System.out.println("1. Register (Registra un nuovo utente)");
        System.out.println("2. Login (Effettua il login)");
        System.out.println("3. SearchHotel (Cerca un hotel)");
        System.out.println("4. SearchAllHotels (Cerca tutti gli hotel di una città)");
        System.out.println("5. InsertReview - Inserisci una recensione");
        System.out.println("6. ShowMyBadges - Mostra i tuoi badge");
        System.out.println("7. Logout - Effettua il logout");
        System.out.println("8. Exit - Esci dal programma");
    }

    /*public void printUsersInDatabase() {
        System.out.println("Elenco degli utenti nel database:");
        for (User user : DB.values()) {
            System.out.println("- Username: " + user.getUsername() + ", Total Reviews: " + user.getTotalReviews() + ", Experience Level: " + user.getExperienceLevel());
        }
    }*/

}


