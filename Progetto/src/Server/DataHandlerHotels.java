package src.Server;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import src.H_U_R.User;
import src.H_U_R.Hotel;

import java.io.*;
import java.lang.reflect.Type;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class DataHandlerHotels {
//scisci na merda


//ma quindi dobbiamo mollare ThreadWorker?

    
    private static final String HOTELS_FILE = "src./Data/Hotels.json";
    private List<Hotel> hotels;

    public DataHandlerHotels() {
        //hotels = loadHotels();
   }

 //   private List<Hotel> loadHotels() {
 //       try {
 //           JsonReader reader = new JsonReader(new FileReader(HOTELS_FILE));
 //           reader.beginArray();
 //           }
//            Type listType = new TypeToken<List<Hotel>>() {}.getType();
//            List<Hotel> hotelList = gson.fromJson(br, listType);
//            br.close();
//            return hotelList != null ? hotelList : new ArrayList<>();
//        } catch (IOException e) {
//            e.printStackTrace();
//            return new ArrayList<>();
//        }
//    }
//
//    public List<Hotel> searchHotelByName(String name) {
//        return hotels.stream()
//            .filter(hotel -> hotel.getName().equalsIgnoreCase(name))
//            .collect(Collectors.toList());
//    }

    // Altri metodi...




}
