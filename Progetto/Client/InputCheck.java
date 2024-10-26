package Client;

import java.util.Set;
import java.util.regex.Pattern;

public class InputCheck{
    private static final Pattern USER_PATTERN = Pattern.compile("^[a-zA-Z0-9]{3,20}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$");
    private static final Set<String> CITIES = Set.of(
        "Ancona", "Aosta", "Bari", "Bologna", "Cagliari", "Campobasso", 
        "Catanzaro", "Firenze", "Genova", "L'Aquila", "Milano", "Napoli", 
        "Palermo", "Perugia", "Potenza", "Roma", "Torino", "Trento", 
        "Trieste", "Venezia"
    );

    /*
     * Controllo dello username, seguendo la regex definita in USER_PATTERN
     */
    public static boolean isValidUsername(String username) {
        return username != null && !username.trim().isEmpty() && USER_PATTERN.matcher(username).matches();
    }
    /*
     * Controllo della password, seguendo la regex definita in PASSWORD_PATTERN
     */
    public static boolean isValidPassword(String password) {
        return password != null && !password.trim().isEmpty() && PASSWORD_PATTERN.matcher(password).matches();
    }
    /*
     * Controllo del nome dell'hotel, deve essere diverso da null e non vuoto
     */
    public static boolean isValidHotelName(String hotelName) {
        return hotelName != null && !hotelName.trim().isEmpty();
    }
    /*
     * Controllo del punteggio, deve essere compreso tra 0 e 5
     */
    public static boolean isValidScore(int score) {
        return score >= 0 && score <= 5;
    }
    /*
     * Controllo della città, deve essere presente nella lista delle città
     */
    public static boolean isValidCity(String city) {
        return city != null && CITIES.contains(city);
    }
}