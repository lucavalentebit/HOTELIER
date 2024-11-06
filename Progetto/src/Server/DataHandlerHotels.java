package src.Server;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import src.H_U_R.User;

import java.io.*;
import java.lang.reflect.Type;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DataHandlerHotels {
    private static final String HOTELS_FILE = "Data/Hotels.json";
    private List<Hotel> hotels;

    public DataHandlerHotels() {
        hotels = loadHotels();
    }

    private List<Hotel> loadHotels() {
        try {
            Gson gson = new Gson();
            BufferedReader br = new BufferedReader(new FileReader(HOTELS_FILE));
            Type listType = new TypeToken<List<Hotel>>() {}.getType();
            List<Hotel> hotelList = gson.fromJson(br, listType);
            br.close();
            return hotelList != null ? hotelList : new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Hotel> searchHotelByName(String name) {
        return hotels.stream()
            .filter(hotel -> hotel.getName().equalsIgnoreCase(name))
            .collect(Collectors.toList());
    }

    // Altri metodi...

}
