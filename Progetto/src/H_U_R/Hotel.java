package src.H_U_R;

import src.Server.Ratings;
import java.time.LocalDate;
import java.util.*;
import com.google.gson.Gson;

public class Hotel {
    private String id;
    private String name;
    private String description;
    private String city;
    private String phone;
    private List<String> services;
    private Double rate;
    private Ratings ratings;
    private ArrayList<Review>reviews;
    private int totalRating;
    private double localRank;
    private LocalDate lastReviewDate;



    public Hotel(String id, String name, String description,
                               String city, String phone, List<String> services,
                               Double rate, Ratings
                               ratings, ArrayList<Review>reviews ) 
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.city = city;
        this.phone = phone;
        this.services = services;
        this.rate = rate;
        this.ratings = ratings;
        this.reviews = reviews;
        
    }

    /*
     * getters
     */
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public String getCity() {
        return city;
    }
    public String getPhone() {
        return phone;
    }
    public List<String> getServices() {
        return services;
    }
    public Double getRate() {
        return rate;
    }
    public Ratings getRatings() {
        return ratings;
    }
    public List<Review> getReviews() {
        return reviews;
    }
    public int getTotalRating() {
        return totalRating;
    }


    /*
     * setters
     */

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setServices(List<String> services) {
        this.services = services;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public void setRatings(Ratings ratings) {
        this.ratings = ratings;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    public void setTotalRating(int totalRating) {
        this.totalRating = totalRating;
    }

    @Override
    public String toString() {
        String reviews = "";
        if(this.reviews != null) {
            for(int i=0; i<this.reviews.size(); i++) {
                reviews += this.reviews.get(i).toString();
                if(i != this.reviews.size() - 1) {
                    reviews += "\n";
                }
            }
        } else {
            reviews = "Nessuna recensione presente";
        }
        return "__________________________________________ \n" + 
                "Hotel: \n" +
                "--- ID: " + id + "\n" +
                "--- Nome: " + name + "\n" +
                "--- Descrizione: " + description + "\n" +
                "--- Città: " + city + "\n" +
                "--- Telefono: " + phone + "\n" +
                "--- Servizi: " + services + "\n" +
                "--- Valutazione Globale: " + rate + "\n" +
                "--- Valutazioni: \n" + reviews + "\n" +
                "__________________________________________";
    }

    public String toStringJSON() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public void addReview( int cleanlinessScore, int positionScore, int serviceScore, int priceScore) {

        this.lastReviewDate = LocalDate.now();
        Review newReview = new Review(cleanlinessScore, positionScore, serviceScore, priceScore);
        this.reviews.add(newReview);
        //System.out.println("Recensione aggiunta: " + newReview.getReviewContent());

    }


    public LocalDate getLastReviewDate() {
        return lastReviewDate;
    }

    public void setLocalRank(double localRank) {
        this.localRank = localRank;
    }

    public double getLocalRank() {
        return localRank;
    }

}
