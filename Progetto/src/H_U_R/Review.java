package src.H_U_R;

import java.util.Date;
import java.text.SimpleDateFormat;

public class Review {
    private int overallRating;
    private int positionRating;
    private int cleanlinessRating;
    private int servicesRating;
    private int priceRating;
    private long timestamp;

    public Review(int positionRating, int cleanlinessRating, int servicesRating, int priceRating) {
        this.positionRating = positionRating;
        this.cleanlinessRating = cleanlinessRating;
        this.servicesRating = servicesRating;
        this.priceRating = priceRating;
        this.timestamp = System.currentTimeMillis();
    }

    /*
     
    public float getOverallRating() {
        return overallRating
    }

    public float getCleaningScore() {
        return cleaningScore;
    }

    public float getServicesScore() {
        return servicesScore;
    }

    public float getPositionScore() {
        return positionScore;
    }

    public float getQualityScore() {
        return qualityScore;
    }

    public long getDate() {
        return date;
    }

    
     * setters
     

    public void setGlobalScore(float globalScore) {
        this.globalScore = globalScore;
    }

    public void setCleaningScore(float cleaningScore) {
        this.cleaningScore = cleaningScore;
    }

    public void setServicesScore(float servicesScore) {
        this.servicesScore = servicesScore;
    }

    public void setPositionScore(float positionScore) {
        this.positionScore = positionScore;
    }

    public void setQualityScore(float qualityScore) {
        this.qualityScore = qualityScore;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getOverallRating() {
        return overallRating;
    }

    public long getTimestamp() {
        return timestamp;
    }
*/


public int getOverallRating() {
    return overallRating;
}

public int getPositionRating() {
    return positionRating;
}

public int getCleanlinessRating() {
    return cleanlinessRating;
}

public int getServicesRating() {
    return servicesRating;
}

public int getPriceRating() {
    return priceRating;
}

public long getTimestamp() {
    return timestamp;
}


public void setOverallRating(int overallRating) {
    this.overallRating = overallRating;
}

public void setPositionRating(int positionRating) {
    this.positionRating = positionRating;
}

public void setCleanlinessRating(int cleanlinessRating) {
    this.cleanlinessRating = cleanlinessRating;
}

public void setServicesRating(int servicesRating) {
    this.servicesRating = servicesRating;
}

public void setPriceRating(int priceRating) {
    this.priceRating = priceRating;
}

public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
}

 @Override
    public String toString() {
        Date dateObject = new Date(timestamp);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
        String formattedDate = formatter.format(dateObject);
    
        return  "[ Punteggio pulizia: " + cleanlinessRating + " ]\n" +
                "[ Punteggio servizi: " + servicesRating + " ]\n" +
                "[ Punteggio posizione: " + positionRating + " ]\n" +
                "[ Punteggio qualità: " + overallRating + " ]\n" +
                "[ Data: " + formattedDate + " ]";
    }
}


