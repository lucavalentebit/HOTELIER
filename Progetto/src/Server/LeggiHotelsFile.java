package src.Server;

import com.google.gson.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import src.H_U_R.*;

public class LeggiHotelsFile {
    private static final String[] CAPOLUOGHI_REGIONE = {
            "Ancona", "Aosta", "Bari", "Bologna", "Cagliari",
            "Campobasso", "Catanzaro", "Firenze", "Genova", "L'Aquila",
            "Milano", "Napoli", "Palermo", "Perugia", "Potenza",
            "Roma", "Sassari", "Torino", "Trento", "Trieste"
    };
    private final ConcurrentHashMap<String, Hotel> HT = new ConcurrentHashMap<>();

    // metodo per leggere il contenuto del file hotels.json
    public void leggiHotelsFile() throws FileNotFoundException { 
        String filePath = "src/Data/Hotels.json";
        //String filePath = System.getProperty("user.dir") +  System.getProperty("file.separator") +  "Hotels.json";
        File dbfile = new File(filePath);
        try (FileReader fr = new FileReader(dbfile)) {
            JsonElement fileElement = JsonParser.parseReader(fr);
            JsonArray jsonUserArray = fileElement.getAsJsonArray();
            for (JsonElement userElement : jsonUserArray) {
                JsonObject itemJsonObject = userElement.getAsJsonObject();
                String id = itemJsonObject.get("id").getAsString();
                String name = itemJsonObject.get("name").getAsString();
                String description = itemJsonObject.get("description").getAsString();
                String city = itemJsonObject.get("city").getAsString();
                String phone = itemJsonObject.get("phone").getAsString();
                JsonArray JsonService = itemJsonObject.get("services").getAsJsonArray();
                List<String> service = new ArrayList<>();
                for (JsonElement services : JsonService) {
                    service.add(services.getAsString());
                }
                double rate = itemJsonObject.get("rate").getAsDouble();
                JsonObject ratingsObject = itemJsonObject.getAsJsonObject("ratings");
                int cleaning = ratingsObject.get("cleaning").getAsInt();
                int position = ratingsObject.get("position").getAsInt();
                int services = ratingsObject.get("services").getAsInt();
                int quality = ratingsObject.get("quality").getAsInt();
                Ratings r = new Ratings(cleaning, position, services, quality);

                //___________________________//
                ArrayList<Review> recensione = new ArrayList<>();
                if (itemJsonObject.has("reviews")) {
                    JsonArray jsonReviewsArray = itemJsonObject.getAsJsonArray("reviews");
                    for (JsonElement reviewElement : jsonReviewsArray) {
                        JsonObject reviewObject = reviewElement.getAsJsonObject();
                        //String reviewContent = reviewObject.get("content").getAsString();
                        int positionScore = reviewObject.get("positionScore").getAsInt();
                        int cleanlinessScore = reviewObject.get("cleanlinessScore").getAsInt();
                        int serviceScore = reviewObject.get("serviceScore").getAsInt();
                        int priceScore = reviewObject.get("priceScore").getAsInt();
                        Review rev = new Review( positionScore, cleanlinessScore, serviceScore, priceScore);
                        recensione.add(rev);
                    }
                }
                Hotel hotel = new Hotel(id, name, description, city, phone, service, rate, r, recensione);
                HT.put(name, hotel);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Hotel searchHotel(String hotelName, String cityName) {
        for (Hotel hotel : HT.values()) {
            if (hotel.getName().equalsIgnoreCase(hotelName) && hotel.getCity().equalsIgnoreCase(cityName)) {
                return hotel;
            }
        }
        return null; // hotel non trovato
    }


    public List<Hotel> searchHotelsByCity(String cityName) {
        List<Hotel> hotelsInCity = new ArrayList<>();
        boolean isCapoluogo = isCapoluogo(cityName);
        if (!isCapoluogo) {
            System.out.println("Attenzione: " + cityName + " non è un capoluogo di regione.");
            return hotelsInCity;
        }

        for (Hotel hotel : HT.values()) {
            if (hotel.getCity().equalsIgnoreCase(cityName)) {
                hotelsInCity.add(hotel);
            }
        }
        // calcola il ranking degli hotel
        Ranking.rankHotels(hotelsInCity);
        return hotelsInCity;
    }




    private boolean isCapoluogo(String city) {
        for (String capoluogo : CAPOLUOGHI_REGIONE) {
            if (capoluogo.equalsIgnoreCase(city)) {
                return true;
            }
        }
        return false;
    }

    public void addReviewToHotel(Hotel hotel, int cleanlinessScore, int positionScore, int serviceScore, int priceScore) {
        hotel.addReview(cleanlinessScore,positionScore, serviceScore, priceScore);
        // aggiorna la valutazione dell'hotel
        updateHotelRating(hotel);
    }


    public static void updateHotelRating(Hotel hotel) {
        if (hotel == null) {
            System.out.println("Hotel Non Valido.");
            return;
        }

        int totalCleaning = 0;
        int totalPosition = 0;
        int totalServices = 0;
        int totalQuality = 0;

        // calcola la somma totale dei punteggi per ciascun criterio di valutazione
        for (Review review : hotel.getReviews()) {
            Ratings reviewRatings = review.getRatings();
            totalCleaning += reviewRatings.getCleaning();
            totalPosition += reviewRatings.getPosition();
            totalServices += reviewRatings.getServices();
            totalQuality += reviewRatings.getQuality();
        }

        double avgCleaning = totalCleaning / (double) hotel.getReviews().size();
        double avgPosition = totalPosition / (double) hotel.getReviews().size();
        double avgServices = totalServices / (double) hotel.getReviews().size();
        double avgQuality = totalQuality / (double) hotel.getReviews().size();

        // calcola la media dei nuovi ratings aggiornati
        double avgRate = Math.round((avgCleaning + avgPosition + avgServices + avgQuality) / 4.0 * 10.0) / 10.0;
        System.out.println(avgRate);
        // aggiorna i ratings dell'hotel con le nuove medie
        Ratings newRatings = new Ratings((int) avgCleaning, (int) avgPosition, (int) avgServices, (int) avgQuality);
        hotel.setRatings(newRatings);

        hotel.setRate(avgRate);

        // scrivi il file JSON aggiornato
        jsonwrite(Collections.singletonMap(hotel.getName(), hotel));
    }

    public void updateRank() {
        // calcola il ranking per gli hotel caricati
        Ranking.rankHotels(new ArrayList<>(HT.values()));

    }

    // metodo per la scrittura del JSON su file
    public synchronized static void jsonwrite(Map<String, Hotel> hotelMap) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonArray jsonArray = new JsonArray();

        // leggere il contenuto attuale del file JSON e memorizzarlo in una struttura dati
        try (FileReader fileReader = new FileReader(System.getProperty("user.dir") +  System.getProperty("file.separator") + "Hotels.json")) {
            JsonElement fileElement = JsonParser.parseReader(fileReader);
            jsonArray = fileElement.getAsJsonArray();

            // aggiorna solo l'hotel specifico
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject hotelObject = jsonArray.get(i).getAsJsonObject();
                String hotelName = hotelObject.get("name").getAsString();
                String cityName = hotelObject.get("city").getAsString();
                Hotel hotelToUpdate = hotelMap.get(hotelName); // prendo l'hotel dalla mappa usando il nome come chiave
                if (hotelToUpdate != null && hotelToUpdate.getCity().equalsIgnoreCase(cityName)) {
                    // se troviamo un hotel con lo stesso nome e città, aggiorno le informazioni
                    hotelObject.addProperty("rate", hotelToUpdate.getRate());
                    // aggiorno i parametri interni del ratings
                    JsonObject ratingsObject = hotelObject.getAsJsonObject("ratings");
                    ratingsObject.addProperty("cleaning", hotelToUpdate.getRatings().getCleaning());
                    ratingsObject.addProperty("position", hotelToUpdate.getRatings().getPosition());
                    ratingsObject.addProperty("services", hotelToUpdate.getRatings().getServices());
                    ratingsObject.addProperty("quality", hotelToUpdate.getRatings().getQuality());
                    JsonArray jsonReviewsArray;
                    if (hotelObject.has("reviews")) {
                        jsonReviewsArray = hotelObject.get("reviews").getAsJsonArray();
                    } else {
                        //se non c'è reviews allora inizializzo un nuovo array JSON vuoto
                        jsonReviewsArray = new JsonArray();
                        hotelObject.add("reviews", jsonReviewsArray); // aggiungo l'array delle recensioni all'oggetto JSON dell'hotel
                    }

                    // aggiungo nuove recensioni all'array JSON delle recensioni
                    List<Review> reviews = hotelToUpdate.getReviews();
                    if (reviews != null && !reviews.isEmpty()) {
                        JsonObject reviewObject = new JsonObject();
                        for (Review review : reviews) {
                            reviewObject.addProperty("positionScore", review.getPositionScore());
                            reviewObject.addProperty("cleanlinessScore", review.getCleanlinessScore());
                            reviewObject.addProperty("serviceScore", review.getServiceScore());
                            reviewObject.addProperty("priceScore", review.getPriceScore());
                        }
                        jsonReviewsArray.add(reviewObject);
                    }

                    System.out.println("Hotel " + hotelName + " aggiornato nel file JSON.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // scrivo l'intera struttura dati aggiornata nel file JSON
        try (FileWriter fileWriter = new FileWriter(System.getProperty("user.dir") +  System.getProperty("file.separator") + "Hotels.json")) {
            gson.toJson(jsonArray, fileWriter);
            System.out.println("Scrittura su file JSON completata con successo.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}