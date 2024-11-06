package src.Data;


import src.H_U_R.Hotel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class HotelFile {
    private String filePath;
    private List<Hotel> hotels;

    /**
     * Costruttore che imposta il percorso del file JSON contenente i dati degli hotel.
     * 
     * @param filePath Il percorso relativo al file JSON degli hotel.
     */
    public HotelFile(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Legge i dati degli hotel dal file JSON specificato e li restituisce come una lista di oggetti Hotel.
     * 
     * @return Una lista di oggetti Hotel caricati dal file JSON.
     * @throws IOException Se si verifica un errore durante la lettura del file.
     */
    public List<Hotel> loadHotels() throws IOException {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(this.filePath)) {
            Type hotelListType = new TypeToken<List<Hotel>>() {}.getType();
            hotels = gson.fromJson(reader, hotelListType);
        }
        return hotels;
    }

    /**
     * Restituisce la lista degli hotel caricati.
     * 
     * @return La lista degli hotel.
     */
    public List<Hotel> getHotels() {
        return hotels;
    }
}