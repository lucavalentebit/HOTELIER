package src.H_U_R;

import java.util.Date;

import src.Server.Ratings;

import java.text.SimpleDateFormat;

public class Review {
    private int cleanlinessScore;
    private int positionScore;
    private int serviceScore;
    private int priceScore;

    public Review(int cleanlinessScore, int positionScore, int serviceScore, int priceScore) {
        this.cleanlinessScore = cleanlinessScore;
        this.positionScore = positionScore;
        this.serviceScore = serviceScore;
        this.priceScore = priceScore;
    }

    public int getPositionScore() {
        return positionScore;
    }

    public int getCleanlinessScore() {
        return cleanlinessScore;
    }

    public int getServiceScore() {
        return serviceScore;
    }

    public int getPriceScore() {
        return priceScore;
    }

    public Ratings getRatings() {
        return new Ratings(cleanlinessScore, positionScore, serviceScore, priceScore);
    }


}
//  mettere chiocciola Override
//     public String toString() {
//         Date dateObject = new Date(timestamp);
//         SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
//         String formattedDate = formatter.format(dateObject);
    
//         return  "[ Punteggio pulizia: " + cleanlinessScore + " ]\n" +
//                 "[ Punteggio servizi: " + serviceScore + " ]\n" +
//                 "[ Punteggio posizione: " + positionScore + " ]\n" +
//                 "[ Punteggio qualità: " + overallScore + " ]\n" +
//                 "[ Data: " + formattedDate + " ]";
//     }
// }


