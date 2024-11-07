package src.Server;

public class Ratings {
    private int cleaning;
    private int position;
    private int services;
    private int quality;

    public Ratings(int cleaning, int position, int services, int quality) {
        this.cleaning = cleaning;
        this.position = position;
        this.services = services;
        this.quality = quality;
    }

    public int getCleaning() {
        return cleaning;
    }

    public int getPosition() {
        return position;
    }

    public int getServices() {
        return services;
    }

    public int getQuality() {
        return quality;
    }

    @Override
    public String toString() {
        return "Ratings{" +
                "position=" + position +
                ", cleaning=" + cleaning +
                ", services=" + services +
                ", quality=" + quality +
                '}';
    }
}