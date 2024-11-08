package src.Server;

import src.H_U_R.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;

public class Ranking {

    public static void rankHotels(List<Hotel> hotelsInCity) {
        for (Hotel hotel : hotelsInCity) {
            double qualityScore = calculateQualityScore(hotel);
            int quantityScore = hotel.getReviews().size();
            double actualityScore = calculateActualityReview(hotel);

            // calcolo del punteggio complessivo
            double totalScore = calculateTotalScore(qualityScore, quantityScore, actualityScore);

            // impostazione del punteggio sull'hotel
            hotel.setLocalRank(totalScore);
        }

        // ordinamento degli hotel in base al punteggio complessivo in ordine decrescente
        Collections.sort(hotelsInCity, Comparator.comparingDouble(Hotel::getLocalRank).reversed());

        // assegno il rank locale ad ogni hotel in base all'ordine della lista
        int rank = 1;
        for (Hotel hotel : hotelsInCity) {
            hotel.setLocalRank(rank++);
        }
    }
    public static double calculateQualityScore(Hotel hotel) {
        Ratings ratings = hotel.getRatings();
        int quality = ratings.getQuality();
        int position = ratings.getPosition();
        int cleaning = ratings.getCleaning();
        int services = ratings.getServices();
        return (quality + position + cleaning + services) / 4.0; //  media dei punteggi di qualità, posizione, pulizia e servizi
    }

    public static double calculateActualityReview(Hotel hotel) {
        if (hotel.getLastReviewDate() != null) {
            LocalDate currentDate = LocalDate.now();
            long daysSinceLastReview = ChronoUnit.DAYS.between(hotel.getLastReviewDate(), currentDate);
            return 1.0 / (daysSinceLastReview + 1); // aggiungo 1 al denominatore per evitare la divisione per zero
        } else {
            return 0.0;
        }
    }

    //Questo metodo calcola il punteggio totale di un hotel in base ai punteggi di qualità, quantità e attualità delle recensioni
    public static double calculateTotalScore(double qualityScore, int quantityScore, double actualityScore) {
        // Vado a definire i pesi per ogni parametro
        double weightQuality = 0.5; // 50% per la qualità
        double weightQuantity = 0.35; // 35% per la quantità
        double weightActuality = 0.15; // 15% per l'attualità

        double totalScore = (qualityScore * weightQuality) + (quantityScore * weightQuantity) + (actualityScore * weightActuality);

        return totalScore;
    }

}
