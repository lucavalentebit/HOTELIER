package src.Client;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class HOTELIERClientMain {
	private static final String SERVER_ADDRESS = "localhost";
	private static final int SERVER_PORT = 8080;
	private static boolean loggedIn = false;
	private BufferedReader input;
	private PrintWriter output;
	private Socket socket;
	public static void main(String[] args) {
        HOTELIERClientMain client = new HOTELIERClientMain();
        client.start();
    }

    private void start() {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("Connessione al server " + SERVER_ADDRESS + " sulla porta " + SERVER_PORT + " avvenuta con successo.");
            
            Scanner scanner = new Scanner(System.in);
            while (true) {
                showMenu();
                String command = scanner.nextLine();
                handleCommand(command, scanner);
            }
        } catch (IOException e) {
            System.out.println("Errore durante la connessione al server: " + e.getMessage());
        }
    }


        private void showMenu() {
        System.out.println("Benvenuto su HOTELIER! Seleziona un'opzione:");
        System.out.println("1. Register (Registra un nuovo utente)");
        System.out.println("2. Login (Effettua il login)");
        System.out.println("3. SearchHotel (Cerca un hotel)");
        System.out.println("4. SearchAllHotels (Cerca tutti gli hotel di una città)");
        System.out.println("5. InsertReview - Inserisci una recensione");
        System.out.println("6. ShowMyBadges - Mostra i tuoi badge");
        System.out.println("7. Logout - Effettua il logout");
        System.out.println("8. Exit - Esci dal programma");
        }

	private void handleCommand(String command, Scanner scanner ){
		switch (command.toLowerCase()) {
			case "1":
                register(scanner);
                break;

			case "2":
				login(scanner);
				break;

			case "3":
                searchHotel(scanner);
                break;

            case "4":
                searchAllHotels(scanner);
                break;

            case "5":
                if(loggedIn){
                    insertReview(scanner);
                } else {
                    System.out.println("Devi effettuare il login per poter inserire una recensione.");
                }
                break;

            case "6":
                if(loggedIn){
                    showMyBadges();
                } else {
                    System.out.println("Devi effettuare il login per poter visualizzare i tuoi badge.");
                }
                break;

            case "7":
                if(loggedIn){
                    sendMessage("logout");
                    loggedIn = false;
                    System.out.println("Logout effettuato con successo.");
                } else {
                    System.out.println("Non sei loggato.");
                }
                break;

            case "8":
                sendMessage("exit");
                System.out.println("Arrivederci!");
                System.exit(0);
                break;

            default:
                System.out.println("Comando sconosciuto. Riprova.");
                break;
		}
	}
	

	
    private void sendMessage(String message) {
        if (output != null) {
            output.println(message);
            try {
                String response = input.readLine();
                if (response != null) {
                    System.out.println("Risposta dal server: " + response);
                } else {
                    System.out.println("Nessuna risposta dal server.");
                }
            }
             catch (IOException e) {
                System.out.println("Il server non risponde.");
            }
        }
    }
    /*private void register(Scanner scanner) {
        try {
            // Invia il comando "register" al server
            output.println("register");

            // Attendi la richiesta di username
            String serverResponse = input.readLine();
            if (serverResponse != null && serverResponse.startsWith("Inserisci username:")) {
                System.out.print("Username: ");
                String username = scanner.nextLine();
                output.println(username);
            }

            // Attendi la risposta dopo l'inserimento dell'username
            serverResponse = input.readLine();
            if (serverResponse != null && serverResponse.startsWith("Registrazione fallita")) {
                System.out.println(serverResponse);
                return;
            }

            // Attendi la richiesta di password
            serverResponse = input.readLine();
            if (serverResponse != null && serverResponse.startsWith("Inserisci password:")) {
                System.out.print("Password: ");
                String password = scanner.nextLine();
                output.println(password);
            }

            // Attendi la conferma finale
            serverResponse = input.readLine();
            if (serverResponse != null) {
                System.out.println(serverResponse);
            }
        } catch (IOException e) {
            System.out.println("Errore durante la registrazione: " + e.getMessage());
        }
    }*/


    private void register(Scanner scanner){
        try {
            System.out.println("Inserisci username: ");
            String username = scanner.nextLine();
            if (InputCheck.isValidUsername(username)) {
                System.out.println("Inserisci password: ");
                String password = scanner.nextLine();
                if (InputCheck.isValidPassword(password)) {
                    sendMessage("register/" + username + "/" + password);
                } else {
                    System.out.println("Password non valida.");
                }
            } else {
                System.out.println("Username non valido.");
            }
        } catch (Exception e) { 
            System.out.println("Errore durante la registrazione: " + e.getMessage());
        }
    }
    

    private void login(Scanner scanner) {
        try {
            // Invia il comando "login" al server
            output.println("login");
    
            // Attende la richiesta di username
            String serverResponse = input.readLine();
            if (serverResponse != null && serverResponse.startsWith("Inserisci username:")) {
                System.out.print("Username: ");
                String username = scanner.nextLine();
                output.println(username);
            }
    
            // Attende la richiesta di password
            serverResponse = input.readLine();
            if (serverResponse != null && serverResponse.startsWith("Inserisci password:")) {
                System.out.print("Password: ");
                String password = scanner.nextLine();
                output.println(password);
            }
    
            // Attende la risposta finale
            serverResponse = input.readLine();
            if (serverResponse != null) {
                System.out.println(serverResponse);
                if (serverResponse.equals("Login effettuato con successo.")) {
                    loggedIn = true;
                }
            }
    
        } catch (IOException e) {
            System.out.println("Errore durante il login: " + e.getMessage());
        }
    }

    private void searchHotel(Scanner scanner) {
        try {
            // Invia il comando "searchhotel" al server
            output.println("searchhotel");
    
            // Richiedi il nome dell'hotel
            System.out.print("Inserisci il nome dell'hotel da cercare: ");
            String hotelName = scanner.nextLine();
            output.println(hotelName);
    
            // Ricevi la risposta dal server
            String serverResponse;
            while ((serverResponse = input.readLine()) != null && !serverResponse.equals("END")) {
                System.out.println(serverResponse);
            }
    
        } catch (IOException e) {
            System.out.println("Errore durante la ricerca dell'hotel: " + e.getMessage());
        }
    }

    private void searchAllHotels(Scanner scanner) {
        try {
            // Invia il comando "searchallhotels" al server
            output.println("searchallhotels");
    
            // Richiedi il nome della città
            System.out.print("Inserisci la città: ");
            String city = scanner.nextLine();
            output.println(city);
    
            // Ricevi la risposta dal server
            String serverResponse;
            while ((serverResponse = input.readLine()) != null && !serverResponse.equals("END")) {
                System.out.println(serverResponse);
            }
    
        } catch (IOException e) {
            System.out.println("Errore durante la ricerca degli hotel: " + e.getMessage());
        }
    }

    private void insertReview(Scanner scanner) {
        try {
            output.println("insertreview");
    
            System.out.print("Inserisci il nome dell'hotel: ");
            String hotelName = scanner.nextLine();
            output.println(hotelName);
    
            System.out.print("Punteggio generale (0-5): ");
            int overallRating = Integer.parseInt(scanner.nextLine());
            System.out.print("Punteggio Posizione (0-5): ");
            int positionRating = Integer.parseInt(scanner.nextLine());
            System.out.print("Punteggio Pulizia (0-5): ");
            int cleanlinessRating = Integer.parseInt(scanner.nextLine());
            System.out.print("Punteggio Servizio (0-5): ");
            int serviceRating = Integer.parseInt(scanner.nextLine());
            System.out.print("Punteggio Prezzo (0-5): ");
            int priceRating = Integer.parseInt(scanner.nextLine());
    
            output.println(overallRating);
            output.println(positionRating);
            output.println(cleanlinessRating);
            output.println(serviceRating);
            output.println(priceRating);
    
            String serverResponse = input.readLine();
            if (serverResponse != null) {
                System.out.println(serverResponse);
            }
    
        } catch (IOException e) {
            System.out.println("Errore durante l'inserimento della recensione: " + e.getMessage());
        }
    }

    private void showMyBadges() {
        try {
            // Invia il comando "showmybadges" al server
            output.println("showmybadges");
    
            // Ricevi la risposta dal server
            String serverResponse = input.readLine();
            if (serverResponse != null) {
                System.out.println("Il tuo badge: " + serverResponse);
            }
    
        } catch (IOException e) {
            System.out.println("Errore durante la visualizzazione dei badge: " + e.getMessage());
        }
    }
}
