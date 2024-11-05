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
        System.out.println("7. Exit - Esci dal programma");
        }

	private void handleCommand(String command, Scanner scanner ){
		switch (command.toLowerCase()) {
			case "1":
                register(scanner);
                break;

			case "2":
				sendMessage("login");;
				break;

			case "3":
                sendMessage("searchHotel");
                break;

            case "4":
                sendMessage("searchAllHotels");;
                break;

            case "5":
                if(loggedIn){
                    sendMessage("insertReview");
                } else {
                    System.out.println("Devi effettuare il login per poter inserire una recensione.");
                }
                break;

            case "6":
                if(loggedIn){
                    sendMessage("showMyBadges");;
                } else {
                    System.out.println("Devi effettuare il login per poter visualizzare i tuoi badge.");
                }
                break;

            case "7":
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
    private void register(Scanner scanner) {
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
    }
}
