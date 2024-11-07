package src.Server;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

import src.Data.*;
import src.H_U_R.*;
import src.Client.InputCheck;

public class ThreadWorker implements Runnable {
    private final Socket socket;
    private Database db;
    private LeggiHotelsFile hf;
    private final BufferedReader reader;
    private final PrintWriter writer;
    private String clientUsername;
    // private static final String HOTELS_FILE = "src./Data/Hotels.json";

    public ThreadWorker(Socket socket, Database db, LeggiHotelsFile hf) throws IOException {
        this.socket = socket;
        this.db = db;
        this.hf = hf;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new PrintWriter(socket.getOutputStream(), true);
    }

    @Override
    public void run() {
        while (true) {
            if (socket.isClosed() || socket == null) {
                break;
            } else {
                handleRequest(socket, db, hf);
            }
        }
    }

    private void handleRequest(Socket socket, Database db, LeggiHotelsFile hf) {
        try {
            if (socket == null || socket.isClosed()) {
                return;
            }

            String clientMessage = reader.readLine();
            String[] part = clientMessage.split("/");
            String operation = part[0];

            switch (operation) {
                case "register":
                    String usernameRegister = part[1];
                    String passwordRegister = part[2];
                    handleRegister(usernameRegister, passwordRegister);
                    break;

                case "login":
                    String usernameLogin = part[1];
                    String passwordLogin = part[2];
                    handleLogin(usernameLogin, passwordLogin);
                    // scrivi sul bufer della socket così il client può leggere
                    break;

                case "searchhotel":
                    String hotelName = part[1];
                    String city = part[2];
                    handleSearchHotel(hotelName, city);
                    break;

                case "searchallhotels":
                    String generalCity = part[1];
                    //handleSearchAllHotels(generalCity);
                    break;

                case "insertreview":

                    // handleInsertReview(input, output);
                    // break;

                case "showmybadges":

                    // handleShowMyBadges();
                    break;

                case "logout":
                    handleLogout(part[1]);
                    break;

                // case "exit":
                // output.println("Chiusura del server.");
                // dataHandler.requestShutdown();
                // break;

                default:
                    writer.println("Comando sconosciuto.");
                    break;

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleRegister(String username, String password) {
        User existingUser = db.TrovaUser(username);
        if (existingUser != null) {
            System.out.println("Utente già registrato.");
            writer.println("Utente già registrato.");
        } else {
            User newUser = new User(username, password);
            db.inserisci(newUser);
            newUser.isLoggedIn();
            writer.println("Utente " + username + " registrato con successo.");
        }
    }

    private synchronized boolean loginUser(String username, String password) {
        // cerco l'utente nel database
        User user = db.TrovaUser(username);
        if (user != null) {
            if (user.getPassword().equals(password)) {
                user.isLoggedIn();
                return true;
            } else {
                System.out.println("Password Errata Per: " + user.getUsername());
                return false;
            }
        } else {
            System.out.println("Utente Non Trovato.");
            return false;
        }

    }

    private void handleLogin(String username, String password) {

        // verifico se un utente è già loggato
        if (clientUsername != null && clientUsername.equals(username)) {
            writer.println("Utente già loggato.");
        }

        boolean loginSuccess = loginUser(username, password);

        if (loginSuccess) {
            clientUsername = username;
            System.out.println("User '" + username + "' Loggato Con Successo.");
            writer.println("LOGIN_SUCCESS");
        } else {
            writer.println("LOGIN_FAILED");
        }
    }

    private void handleSearchHotel(String hotelName, String city) throws FileNotFoundException {
        System.out.println(hotelName);
        System.out.println(city);
        LeggiHotelsFile leggiHotelsFile = new LeggiHotelsFile();
        leggiHotelsFile.leggiHotelsFile(); // leggo hotel dal file
        System.out.println(leggiHotelsFile);
        Hotel hotelByName = leggiHotelsFile.searchHotel(hotelName, city);
        System.out.println(hotelByName);
        if (hotelByName != null) {
            writer.println("HOTEL_FOUND_BY_NAME " + hotelByName);
            System.out.println("Found hotel: " + hotelByName);
        } else {
            writer.println("HOTEL_NOT_FOUND_BY_NAME Hotel '" + hotelName + "' non trovato");
        }
    }

    // private synchronized void handleSearchAllHotels(String city) throws
    // FileNotFoundException {
    // LeggiHotelsFile leggiHotelsFile = new LeggiHotelsFile();
    // leggiHotelsFile.leggiHotelsFile(); // leggo hotel dal file
    // List<Hotel> hotelsInCity = leggiHotelsFile.searchHotelsByCity(city);
    // User user = db.TrovaUser(clientUsername);
    // if (user != null && user.getLogin()) {
    // for (Hotel hotel : hotelsInCity) {
    // if (hotel.getLocalRank() == 1) {
    // sharer.notify(hotel);
    // break;
    // }
    // }
    // }
    // if (!hotelsInCity.isEmpty()) {
    // sendMessage("Hotel Trovati \n");
    // for (Hotel hotel : hotelsInCity) {
    // sendMessage(hotel.toString());
    // }
    // sendMessage("fine");
    // } else {
    // sendMessage("Nessun Hotel Trovato Nella Città Di '" + city + "'");
    // sendMessage("fine");
    // }
    // }

    private synchronized void handleLogout(String username) {
        User user = db.TrovaUser(username);
        if (user != null) {
            if (user.getLoggedIn()) {
                user.isLoggedOut();
                writer.flush();
                clientUsername = null;
                writer.println("LOGOUT_SUCCESS");
            } else {
                writer.flush();
                writer.println("ERRORE Utente Non Loggato");
            }
        } else {
            writer.flush();
            writer.println("Utente Non Loggato");
        }
    }

    // private synchronized String handleShowMyBadges() {
    // User currentUser = db.TrovaUser(clientUsername);
    // if (currentUser != null) {
    // if (currentUser.getLoggedIn()) {
    // // aggiorno il livello di esperienza dell'utente
    // currentUser.updateBadge();
    // // prendo il livello di esperienza dell'utente
    // int experienceLevel = currentUser.getBadge();
    // String experienceLevelName = currentUser.getBadgeName();
    // writer.println("SHOW_BADGES_SUCCESS " + experienceLevel + " " +
    // experienceLevelName);
    // } else {
    // writer.println("Utente Non Loggato");
    // }
    // } else {
    // writer.println("Utente Non Trovato Nel Database");
    // }

    // }
}
