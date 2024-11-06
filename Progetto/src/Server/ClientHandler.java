package src.Server;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

import src.Client.InputCheck;
import src.H_U_R.User;
import src.H_U_R.Hotel;
import src.H_U_R.Review;

/*class ClientHandler implements Runnable {
    private Socket clientSocket;
    private DataHandler dataHandler;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
        this.dataHandler = new DataHandler();
    }

    @Override
    public void run() {
        try (BufferedReader input = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true)) {

            System.out.println("Gestione della connessione nel thread: " + Thread.currentThread().getName());
            handleCommands(input, output);

        } catch (IOException e) {
            System.out.println("Errore durante la comunicazione con il client: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("Errore durante la chiusura del socket client: " + e.getMessage());
            }
        }
    }

    private void handleCommands(BufferedReader input, PrintWriter output) throws IOException {
        String command;
        while ((command = input.readLine()) != null) {
            System.out.println("Comando ricevuto: " + command);

            switch (command) {
                case "register":
                    handleRegister(input, output);
                    break;
                // Gestisci altri comandi...
                default:
                    output.println("Comando sconosciuto.");
                    break;
            }
        }
    }

    private void handleRegister(BufferedReader input, PrintWriter output) throws IOException {
        // Richiede l'username al client
        output.println("Inserisci username:");
        String username = input.readLine();

        // Verifica dello username
        if (!InputCheck.isValidUsername(username)) {
            output.println("Registrazione fallita. Username non valido.");
            return;
        }

        // Verifica se l'utente esiste già
        if (dataHandler.userExists(username)) {
            output.println("Registrazione fallita. Username già esistente.");
            return;
        }

        // Richiede la password al client
        output.println("Inserisci password:");
        String password = input.readLine();

        // Verifica della password
        if (!InputCheck.isValidPassword(password)) {
            output.println("Registrazione fallita. Password non valida.");
            return;
        }

        // Crea un nuovo utente e lo salva
        User newUser = new User(username, password, "", 0, false);
        dataHandler.addUser(newUser);

        output.println("Registrazione effettuata con successo.");
    }
}*/

class ClientHandler implements Runnable {
    private Socket clientSocket;
    private DataHandlerUsers dataHandler;
    private DataHandlerHotels dataHandlerHotels;
    private User currentUser;

    public ClientHandler(Socket socket, DataHandlerUsers dataHandler) {
        this.clientSocket = socket;
        this.dataHandler = dataHandler;
        this.dataHandlerHotels = dataHandlerHotels;

    }

    @Override
    public void run() {
        try (BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true)) {

            System.out.println("Gestione della connessione nel thread: " + Thread.currentThread().getName());
            handleCommands(input, output);

        } catch (IOException e) {
            System.out.println("Errore durante la comunicazione con il client: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("Errore durante la chiusura del socket client: " + e.getMessage());
            }
        }
    }

    private void handleCommands(BufferedReader input, PrintWriter output) throws IOException {
        String command;
        while ((command = input.readLine()) != null) {
            System.out.println("Comando ricevuto: " + command);

            switch (command.toLowerCase()) {
                case "register":
                    handleRegister(input, output);
                    break;
                
                case "login":
                    handleLogin(input, output);
                    break;

                case "searchhotel":
                    handleSearchHotel(input, output);
                    break;
                
                case "searchallhotels":
                    handleSearchAllHotels(input, output);
                    break;
                
                case "insertreview":
                    handleInsertReview(input, output);
                    break;
                
                case "showmybadges":
                    handleShowMyBadges(output);
                    break;
                
                case "logout":
                    handleLogout(input, output);
                    break;

                case "exit":
                    output.println("Chiusura del server.");
                    dataHandler.requestShutdown();
                    break;

                default:
                    output.println("Comando sconosciuto.");
                    break;
            }
        }
    }

    

    private void handleRegister(BufferedReader input, PrintWriter output) throws IOException {
        // Richiede l'username al client
        output.println("Inserisci username:");
        String username = input.readLine();

        // Verifica dello username
        if (!InputCheck.isValidUsername(username)) {
            output.println("Registrazione fallita. Username non valido.");
            return;
        }

        // Verifica se l'utente esiste già
        if (dataHandler.userExists(username)) {
            output.println("Registrazione fallita. Username già esistente.");
            return;
        }

        // Richiede la password al client
        output.println("Inserisci password:");
        String password = input.readLine();

        // Verifica della password
        if (!InputCheck.isValidPassword(password)) {
            output.println("Registrazione fallita. Password non valida.");
            return;
        }

        // Crea un nuovo utente e lo salva
        User newUser = new User(username, password, "", 0, false);
        dataHandler.addUser(newUser);

        output.println("Registrazione effettuata con successo.");
    }

    private void handleLogin(BufferedReader input, PrintWriter output) throws IOException {
        // Richiedi username
        output.println("Inserisci username:");
        String username = input.readLine();
    
        // Verifica se l'utente esiste
        if (!dataHandler.userExists(username)) {
            output.println("Login fallito. Username non esistente.");
            return;
        }
    
        // Richiedi password
        output.println("Inserisci password:");
        String password = input.readLine();
    
        User user = dataHandler.getUser(username);
    
        // Verifica password
        if (!user.getPassword().equals(password)) {
            output.println("Login fallito. Password errata.");
            return;
        }
    
        // Imposta l'utente come loggato
        user.setLoggedIn(true);
        dataHandler.updateUser(user);
    
        output.println("Login effettuato con successo.");
    }

    private void handleSearchHotel(BufferedReader input, PrintWriter output) throws IOException {
    // Richiedi il nome dell'hotel
    output.println("Inserisci il nome dell'hotel da cercare:");
    String hotelName = input.readLine();

    if (hotelName == null || hotelName.trim().isEmpty()) {
        output.println("Nome hotel non valido.");
        return;
    }

    // Carica gli hotel dal DataHandlerHotels
    List<Hotel> hotels = dataHandlerHotels.searchHotelByName(hotelName);

    // Invia i risultati al client
    if (hotels == null || hotels.isEmpty()) {
        output.println("Nessun hotel trovato con il nome specificato.");
    } else {
        for (Hotel hotel : hotels) {
            output.println(hotel.toString());
        }
    }
    output.println("END");
}

    private void handleShowMyBadges(PrintWriter output) {
        if (currentUser == null || !currentUser.isLoggedIn()) {
            output.println("Devi essere loggato per visualizzare i tuoi badge.");
            return;
        }
        // Ottieni il badge dell'utente
        String badge = currentUser.getBadge();
        output.println(badge != null ? badge : "Nessun badge ottenuto.");
    }

    private void handleLogout(BufferedReader input, PrintWriter output) throws IOException {
        
    }
    private void handleSearchAllHotels(BufferedReader input, PrintWriter output) throws IOException {
    }

    private void handleInsertReview(BufferedReader input, PrintWriter output) throws IOException {
    }

}