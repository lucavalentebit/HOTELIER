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
            sendMessage(command);
                logout();
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
        String username;
        String password;
        while(true){
            System.out.print("Username: ");
            username = scanner.nextLine();
            if(InputCheck.isValidUsername(username)){
                break;
            } else {
                System.out.println("Lo username deve contenere solo lettere e numeri e deve essere lungo tra 3 e 20 caratteri.");
            }
        }
        while(true){
            System.out.print("Password: ");
            password = scanner.nextLine();
            if(InputCheck.isValidPassword(password)){
                break;
            } else {
                System.out.println("La password deve contenere almeno 8 caratteri, di cui almeno una lettera maiuscola, una lettera minuscola e un numero.");
            }
        }
        
        String message = "register " + username + " " + password;
        sendMessage(message);
    }

    private void login(Scanner scanner) {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        String message = "login " + username + " " + password;
        sendMessage(message);
        loggedIn = true; // Assumiamo login riuscito per semplicità, ma dovrebbe essere confermato dal server
    }

    private void logout() {
            try {
                if(socket != null) {
                    socket.close();
                }
                if(input != null) {
                    input.close();
                }
                if (output != null) {
                    output.close();
                }
            } catch (IOException e) {
                System.out.println("Errore durante la chiusura delle risorse: " + e.getMessage());
            } finally {
                sendMessage("exit");
                System.out.println("Arrivederci!");
                System.exit(0);
            }
        }
       
    
    private void searchHotel(Scanner scanner) {
        System.out.print("Nome dell'hotel: ");
        String hotelName = scanner.nextLine();
        System.out.print("Città: ");
        String city = scanner.nextLine();

        String message = "searchHotel " + hotelName + " " + city;
        sendMessage(message);
    }

    private void searchAllHotels(Scanner scanner) {
        System.out.print("Città: ");
        String city = scanner.nextLine();

        String message = "searchAllHotels " + city;
        sendMessage(message);
    }

    private void insertReview(Scanner scanner) {
        System.out.print("Nome dell'hotel: ");
        String hotelName = scanner.nextLine();    System.out.print("Città: ");
        String city = scanner.nextLine();
        System.out.print("Punteggio globale (0-5): ");
        int globalScore = scanner.nextInt();
        scanner.nextLine(); // Consuma il newline rimasto

        String message = "insertReview " + hotelName + " " + city + " " + globalScore;
        sendMessage(message);
    }

    private void showMyBadges() {
        sendMessage("showMyBadges");
    }


}
