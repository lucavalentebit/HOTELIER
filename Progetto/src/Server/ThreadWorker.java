package src.Server;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

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
    private final AtomicInteger activeConnections;
    private final HOTELIERServerMain server;
    // private static final String HOTELS_FILE = "src./Data/Hotels.json";

    public ThreadWorker(Socket socket, Database db, LeggiHotelsFile hf, AtomicInteger activeConnections,
            HOTELIERServerMain server) throws IOException {
        this.socket = socket;
        this.db = db;
        this.hf = hf;
        this.activeConnections = activeConnections;
        this.server = server;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new PrintWriter(socket.getOutputStream(), true);
    }

    @Override
    public void run() {
        try {
            while (!socket.isClosed() && !Thread.currentThread().isInterrupted()) {
                handleRequest(socket, db, hf);
            }
        } catch (Exception e) {
            System.err.println("Errore nell'handling della connessione: " + e.getMessage());
        } finally {
            activeConnections.decrementAndGet();
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("Errore durante la chiusura della socket: " + e.getMessage());
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
                    // scrivi sul buffer della socket così il client può leggere
                    break;

                case "searchhotel":
                    String hotelName = part[1];
                    String city = part[2];
                    handleSearchHotel(hotelName, city);
                    break;

                case "searchallhotels":
                    String generalCity = part[1];
                    handleSearchAllHotels(generalCity);
                    break;

                case "insertreview":
                    handleInsertReview(part);
                    break;

                case "showmybadges":
                    handleShowMyBadges();
                    break;

                case "logout":
                    handleLogout();
                    break;

                case "exit":
                    writer.println("Chiusura del server.");
                    handleExit();
                    break;

                default:
                    writer.println("Comando sconosciuto.");
                    break;

                }
            } catch (SocketException e) {
                System.out.println("Connessione reset dall'utente: " + e.getMessage());
                Thread.currentThread().interrupt(); // Signal thread to stop
            } catch (IOException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt(); // Signal thread to stop
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
        writer.flush();

    }

    private void handleSearchHotel(String hotelName, String city) {
        try {
            System.out.println("Ricerca Hotel: " + hotelName + " in " + city);
            LeggiHotelsFile leggiHotelsFile = new LeggiHotelsFile();
            leggiHotelsFile.leggiHotelsFile(); // Leggo hotel dal file
            System.out.println(leggiHotelsFile);
            Hotel hotelByName = leggiHotelsFile.searchHotel(hotelName, city);
            System.out.println("Hotel Trovato: " + hotelByName);

            if (hotelByName != null) {
                writer.println("Found hotel: " + hotelByName);
            } else {
                writer.println("HOTEL_NOT_FOUND_BY_NAME Hotel '" + hotelName + "' non trovato");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Errore: File non trovato - " + e.getMessage());
            writer.println("ERRORE: File degli hotel non trovato.");
        } catch (Exception e) {
            System.out.println("Errore durante la ricerca dell'hotel: " + e.getMessage());
            writer.println("ERRORE: Problema durante la ricerca dell'hotel.");
        } finally {
            writer.println("fine");
            writer.flush();
        }
    }

    private synchronized void handleSearchAllHotels(String city) {
        try {
            hf.leggiHotelsFile(); // Leggo hotel dal file
            List<Hotel> hotelsInCity = hf.searchHotelsByCity(city);

            if (!hotelsInCity.isEmpty()) {
                writer.println("Hotel Trovati:");
                for (Hotel hotel : hotelsInCity) {
                    writer.println(hotel.toString());
                }
                writer.println("fine");
            } else {
                writer.println("Nessun Hotel Trovato Nella Città Di '" + city + "'");
                writer.println("fine");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Errore: File non trovato - " + e.getMessage());
            writer.println("ERRORE: File degli hotel non trovato.");
            writer.println("fine");
        } catch (Exception e) {
            System.out.println("Errore durante la ricerca di tutti gli hotel: " + e.getMessage());
            writer.println("ERRORE: Problema durante la ricerca di tutti gli hotel.");
            writer.println("fine");
        }
    }

    private void handleInsertReview(String[] parts) {
        if (parts.length != 8) {
            writer.println("ERRORE: Parametri insufficienti per inserire una recensione.");
            return;
        }

        System.out.println("questo e' parts[0] \n" + parts[0] + "\n");
        System.out.println("questo e' parts[1] \n" + parts[1] + "\n");
        System.out.println("questo e' parts[2] \n" + parts[2] + "\n");
        System.out.println("questo e' parts[3] \n" + parts[3] + "\n");
        System.out.println("questo e' parts[4] \n" + parts[4] + "\n");
        System.out.println("questo e' parts[5] \n" + parts[5] + "\n");
        System.out.println("questo e' parts[6] \n" + parts[6] + "\n");
        System.out.println("questo e' parts[7] \n" + parts[7] + "\n");
        

        String hotelName = parts[1];
        String cityname  = parts[2];
        try {
            int overallRating = Integer.parseInt(parts[3]);
            int positionRating = Integer.parseInt(parts[4]);
            int cleanlinessRating = Integer.parseInt(parts[5]);
            int serviceRating = Integer.parseInt(parts[6]);
            int priceRating = Integer.parseInt(parts[7]);
    
            // Validazione dei punteggi
            if (!isValidRating(overallRating) || !isValidRating(positionRating) ||
                !isValidRating(cleanlinessRating) || !isValidRating(serviceRating) ||
                !isValidRating(priceRating)) {
                writer.println("ERRORE: Punteggi non validi. Devono essere tra 0 e 5.");
                return;
            }
    
            // Recupera l'hotel
            
            hf.leggiHotelsFile();
            Hotel hotelByName = hf.searchHotel(hotelName, cityname);

            System.out.println("Hotel Trovato: " + hotelByName);

            //Hotel hotel = hf.searchHotel(hotelName, cityname); // Aggiungi la logica per cercare per città se necessario
            if (hotelByName == null) {
                writer.println("ERRORE: Hotel non trovato.");
                return;
            }
    
            // Aggiungi la recensione all'hotel
            hf.addReviewToHotel(hotelByName, cleanlinessRating, positionRating, serviceRating, priceRating);
    
            // Aggiungi la recensione all'utente
            Review review = new Review(cleanlinessRating, positionRating, serviceRating, priceRating);
            db.addReviewToUser(clientUsername, review);
    
            writer.println("RECENSIONE_INSERITA_SUCCESSO");
        } catch (NumberFormatException e) {
            writer.println("ERRORE: Punteggi devono essere numeri interi.");
        } catch (Exception e) {
            writer.println("ERRORE: " + e.getMessage());
        }
    }

    private boolean isValidRating(int rating) {
        return rating >= 0 && rating <= 5;
    }

    private synchronized void handleLogout() {
        if (clientUsername != null) {
            User user = db.TrovaUser(clientUsername);
            if (user != null && user.getLoggedIn()) {
                user.isLoggedOut();
                clientUsername = null;
                writer.println("LOGOUT_SUCCESS");
            } else {
                writer.println("ERRORE: Utente non loggato.");
            }
        } else {
            writer.println("ERRORE: Nessun utente è loggato.");
        }
        writer.flush();
    }

    private synchronized void handleShowMyBadges() {
        User currentUser = db.TrovaUser(clientUsername);
        if (currentUser != null) {
            if (currentUser.getLoggedIn()) {
                // aggiorno il livello di esperienza dell'utente
                currentUser.updateBadge();
                // prendo il livello di esperienza dell'utente
                int experienceLevel = currentUser.getBadge();
                String experienceLevelName = currentUser.getBadgeName();
                writer.println("SHOW_BADGES_SUCCESS " + experienceLevel + " " + experienceLevelName);
            } else {
                writer.println("Utente Non Loggato");
            }
        } else {
            writer.println("Utente Non Trovato Nel Database");
        }

    }

    private void handleExit() {
        writer.println("Server in fase di shutdown.");
        server.initiateShutdown();
    }

    public void closeConnection() {
        try {
            socket.close();
        } catch (IOException e) {
            // Log se necessario
        }
    }
}
