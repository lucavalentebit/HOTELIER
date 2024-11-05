package src.H_U_R;

public class User {
    private String username;
    private String password;
    private String badge;
    private int numberOfReviews;
    private boolean loggedIn;

    public User(String username, String password, String badge, int numberOfReviews, boolean loggedIn) {
        this.username = username;
        this.password = password;
        this.badge = badge;
        this.numberOfReviews = numberOfReviews;
        this.loggedIn = loggedIn;
    }

    /*
     * Getters
     */
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getBadge() {
        return badge;
    }
    public int getNumberOfReviews() {
        return numberOfReviews;
    }
    public boolean isLoggedIn() {
        return loggedIn;
    }

    /*
     * Setters
     * vanno fatti con la deserializzazione
     */
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setBadge(String badge) {
        this.badge = badge;
    }
    public void setNumberOfReviews(int numberOfReviews) {
        this.numberOfReviews = numberOfReviews;
    }
    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }



    


    @Override
    public String toString() {
        return "User{" +
            "username='" + username + '\'' +
            ", password='" + password + '\'' +
            '}';
    }
}
