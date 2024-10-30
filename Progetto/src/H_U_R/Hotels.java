package src.H_U_R;

import java.util.*;

public class Hotels {
    private int id;
    private String name;
    private String description;
    private String city;
    private int phone;
    private List<String> services;
    private int rate;
    private Map<String, Integer> ratings;
    private List<Review> reviews;
    private int totalRating;



    public Hotels(int id, String name, String description,
                               String city, int phone, List<String> services,
                               int rate, Map<String, Integer>
                               ratings, List<Review> reviews, int totalRating) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.city = city;
        this.phone = phone;
        this.services = services;
        this.rate = rate;
        this.ratings = ratings;
        this.reviews = reviews;
        this.totalRating = totalRating;
    }

    /*
     * getters
     */
    public int getId() {
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
    public int getPhone() {
        return phone;
    }
    public List<String> getServices() {
        return services;
    }
    public int getRate() {
        return rate;
    }
    public Map<String, Integer> getRatings() {
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

    public void setId(int id) {
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

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public void setServices(List<String> services) {
        this.services = services;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public void setRatings(Map<String, Integer> ratings) {
        this.ratings = ratings;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }


}
