package Client;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonObject; // scaricare la libreria da internet e metterla dentro Libraries per un codice pulito

public class HOTELIERClientMain {
	private static final String SERVER_ADDRESS = "localhost";
	private static final int SERVER_PORT = 8080;
	private boolean loggedIn = false;
	private BufferedReader input;
	private PrintWriter output;
	private Socket socket;
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("1. Register");
		System.out.println("2. Login");
		System.out.println("3. Logout");
		System.out.println("4. SearchHotel");
		System.out.println("5. SearchAllHotels");
		System.out.println("6. InsertReview");
		System.out.println("7. ShowMyBadges");
		System.out.println();
		String user = scanner.nextLine();
		
	}

	private void handleCommand(String command, Scanner scanner ){
		switch (command) {
			case "register":
				register(scanner);
				break;
			case "login":
				login(scanner);
				break;	
			case "logout":
				logout();
				break;
			case "searchHotel":
				searchHotel(scanner);
				break;	
			case "insertReview":
				insertReview(scanner);
				break;
			case "searchAllHotels":
				searchAllHotels(scanner);
				break;
		
			default: System.out.println("Il comando " + command + " è sconosciuto, riprovare.");
				break;
		}
	}
	private void register(Scanner scanner) {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        
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
        if (!loggedIn) {
            System.out.println("Non sei loggato.");
            return;
        }
        sendMessage("logout");
        loggedIn = false;
    }   private void searchHotel(Scanner scanner) {
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
	private void sendMessage(String message) {
        if (output != null) {
            output.println(message);
            try {
                String response = input.readLine();
                System.out.println("Risposta dal server: " + response);
            } catch (IOException e) {
                System.out.println("Errore nella ricezione della risposta dal server.");
            }
        }
    }
	public class  ServerHandler {
		
		
	}

}
