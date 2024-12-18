package src.H_U_R;

import java.util.ArrayList;
import java.util.List;
public class User {
    private String username;
    private String password;
    private int badge;
    private int numberOfReviews;
    private List<Review> review;
    private boolean loggedIn;


// TODO PERCHÈ 2 COSTRUTTORI? NE SERVE SOLO 1 CHE INIZIALIZZA TUTTI I CAMPI

    //agiungere username e password
    //Deserializzazione di un utente da DB
    public User(String username, String password, int badge, int numberOfReviews, List<Review> reviews, boolean loggedIn) {
        this.username = username;
        this.password = password;
        this.badge = badge;
        this.numberOfReviews = numberOfReviews;
        this.review = reviews;
        this.loggedIn = loggedIn;
    }

    //SE UN UTENTE NON E' PRESENTE A DB ALLORA VIENE CREATO UN NUOVO UTENTE
    public User(String username, String password) {
         //prima inizializzazione
        this.username = username;
        this.password = password;
        this.numberOfReviews = 0;
        this.badge = 0;  //i nuovi utenti iniziano con livello 0
        this.review = new ArrayList<>();
        this.loggedIn = false;
    }

// FINO A QUI. METTERE I CAMPI GIUSTI ANCHE SE STANDARD 

    /*
     * Getters
     */
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public int getBadge() {
        return badge;
    }
    public int getNumberOfReviews() {
        return numberOfReviews;
    }
    public boolean getLoggedIn(){
        return this.loggedIn;
   }

    /*
     * Setters
     */

    public void isLoggedIn() {
        this.loggedIn = true;
    }

    public void isLoggedOut() {
        this.loggedIn = false;
    }

    public void setReviews(List<Review> reviews) {
        this.review = reviews;
    }

    public List<Review> getReview() {
        return review;
    }

    public int getNumvberReviews(){
         return this.numberOfReviews;
    }
    


    @Override
    public String toString() {
        return "User{" +
            "username='" + username + '\'' +
            ", password='" + password + '\'' +
            '}';
    }

    public void addReview(Review review) {
         this.review.add(review);
    }

    public void incrementTotalReviews() {
         numberOfReviews++;
    }

    public void updateBadge() {
        System.out.println(numberOfReviews);
        if (numberOfReviews >= 40) {

            badge = 4;  // Utente Super Contributore
        } else if (numberOfReviews >= 30) {
            badge = 3;  // Contributore Esperto
        } else if (numberOfReviews >= 20) {
            badge = 2;  // Contributore
        } else if (numberOfReviews >= 1) {
            badge = 1;  // Recensore Esperto
        } else {
            badge = 0;  // Recensore
        }
    }

    public String getBadgeName() {
        switch (badge) {
            case 4:
                return "Contributore Super";
            case 3:
                return "Contributore Esperto";
            case 2:
                return "Contributore";
            case 1:
                return "Recensore Esperto";
            default:
                return "Recensore";
        }
    }
}
