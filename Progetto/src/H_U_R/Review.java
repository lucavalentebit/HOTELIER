package src.H_U_R;

import java.util.Date;
import java.text.SimpleDateFormat;

public class Review {
    private float globalScore;
    private float cleaningScore;
    private float servicesScore;
    private float positionScore;
    private float qualityScore;
    private long date;


    public Review(float globalScore, float cleaningScore, float servicesScore, float positionScore, float qualityScore, long date) {
        this.globalScore = globalScore;
        this.cleaningScore = cleaningScore;
        this.servicesScore = servicesScore;
        this.positionScore = positionScore;
        this.qualityScore = qualityScore;
        this.date = date;
    }

    /*
     * getters
     */
    public float getGlobalScore() {
        return globalScore;
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

    /*
     * setters
     */

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

 @Override
    public String toString() {
        Date dateObject = new Date(date);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
        String formattedDate = formatter.format(dateObject);
    
        return  "[ Punteggio pulizia: " + cleaningScore + " ]\n" +
                "[ Punteggio servizi: " + servicesScore + " ]\n" +
                "[ Punteggio posizione: " + positionScore + " ]\n" +
                "[ Punteggio qualità: " + qualityScore + " ]\n" +
                "[ Data: " + formattedDate + " ]";
    }
}


