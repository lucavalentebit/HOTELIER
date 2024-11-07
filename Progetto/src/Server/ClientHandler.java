package src.Server;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

import com.google.gson.stream.JsonReader;

import src.Client.InputCheck;
import src.H_U_R.*;
import src.Data.*;

class ClientHandler implements Runnable {
    private final Database DB;
    private final Socket socket;
    //private DataHandlerUsers dataHandler;
    private DataHandlerHotels dataHandlerHotels;
    private User currentUser;

    
    private final BufferedReader reader;
    private final PrintWriter writer;


    public ClientHandler(Socket socket,  Database DB) throws IOException{
        this.socket = socket;
        this.DB = DB;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new PrintWriter(socket.getOutputStream(), true);
    }

    @Override
    public void run() {
        try  {
            
            System.out.println("Gestione della connessione nel thread: " + Thread.currentThread().getName());
            String clientMessage;

            while ((clientMessage = reader.readLine()) != null) {
                
                handleCommands(clientMessage);
                
            }

        } catch (IOException e) {
            System.out.println("Errore durante la comunicazione con il client: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("Errore durante la chiusura del socket client: " + e.getMessage());
            }
        }
    }

    private void handleCommands(String message) throws IOException {
        String[] part = message.split("/");
        String operation = part[0];

            switch (operation) {
                case "register":                    
                    handleRegister(part[1], part[2]);
                    break;
                
                case "login":
                    handleLogin(part[1], part[2]);
                    break;

                case "searchhotel":
                   // handleSearchHotel(part[1], part[2]);
                     break;
                
                case "searchallhotels":
                    // handleSearchAllHotels(part[1]);
                     break;
                
                case "insertreview":
                //     handleInsertReview(input, output);
                //     break;
                
                case "showmybadges":
                //     handleShowMyBadges(output);
                     break;
                
                // case "logout":
                //     handleLogout(input, output);
                //     break;

                // case "exit":
                //     output.println("Chiusura del server.");
                //     dataHandler.requestShutdown();
                //     break;

                default:
                    writer.println("Comando sconosciuto.");
                    break;
            
            }
    }

    

    private void handleRegister(String usr, String psw) throws IOException {
        // Richiede l'username al client
        // Verifica dello username
        // if (!InputCheck.isValidUsername(usr)) {
        //     writer.println("Registrazione fallita. Username non valido.");
        //     return;
        // }

        // Verifica se l'utente esiste già
        // if (dataHandler.userExists(usr)) {
        //     writer.println("Registrazione fallita. Username già esistente.");
        //     return;
        // }

        // // Verifica della password
        // if (!InputCheck.isValidPassword(psw)) {
        //     writer.println("Registrazione fallita. Password non valida.");
        //     return;
        // }

        // Crea un nuovo utente e lo salva
        User newUser = new User(usr, psw);
        DB.inserisci(newUser);

        //dataHandler.addUser(newUser);

        writer.println("Registrazione effettuata con successo.");
    }

    private synchronized boolean handleLogin(String user, String psw) {
        // Richiedi username
        User usr = DB.TrovaUser(user);
        // Verifica se l'utente esiste
        if(usr == null){
            writer.println("Login fallito. Username non esistente.");
            return false;
        }
        else if (usr.getPassword().equals(psw)) {
            usr.isLoggedIn();
            
            writer.println("Login effettuato con successo.");
            return true;
        }
        else{
            writer.println("Login fallito. Password errata.");
            return false;
        }
    
        //dataHandler.updateUser(usr);
    
    }

//    private void handleSearchHotel(String hotelName, String city) {
//        try (JsonReader reader = new JsonReader(new FileReader(HOTELS_FILE))) {
//            reader.beginArray();
//            while (reader.hasNext()) {
//                Hotel h = gson.fromJson(reader, Hotel.class);
//                if (h.getCity().equals(city) && h.getName().equals(hotel)) {
//                    checkIfPresentAndAdd(hotels, h);
//                    return h.toString();
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return "Hotel not found";
//
//    }



    // private void handleShowMyBadges(PrintWriter output) {
    //     if (currentUser == null || !currentUser.isLoggedIn()) {
    //         output.println("Devi essere loggato per visualizzare i tuoi badge.");
    //         return;
    //     }
    //     // Ottieni il badge dell'utente
    //     String badge = currentUser.getBadge();
    //     output.println(badge != null ? badge : "Nessun badge ottenuto.");
    // }

    private void handleLogout(BufferedReader input, PrintWriter output) throws IOException {
        
    }
    private void handleSearchAllHotels(BufferedReader input, PrintWriter output) throws IOException {
    }

    private void handleInsertReview(BufferedReader input, PrintWriter output) throws IOException {
    }

}