package src.Client;

import java.io.*;

import java.net.Socket;
import java.util.Scanner;
import src.Server.Condivisore;



public class HOTELIERClientMain {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 8080;
    private static boolean loggedIn = false;
    private BufferedReader input;
    private PrintWriter output;
    private Socket socket;
    private static Condivisore condividi;
    private static Thread condividiThread;

    public static void main(String[] args) {
        HOTELIERClientMain client = new HOTELIERClientMain();
        client.start();
    }

    private void start() {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
            condividi = new Condivisore(8080, "225.225.225.225");
            condividiThread = new Thread(condividi);
            condividiThread.start();
            System.out.println("Connessione al server " + SERVER_ADDRESS + " sulla porta " + SERVER_PORT
                    + " avvenuta con successo.");

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

    private void handleCommand(String command, Scanner scanner) {
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
                if (loggedIn) {
                    insertReview(scanner);
                } else {
                    System.out.println("Devi effettuare il login per poter inserire una recensione.");
                }
                break;

            case "6":
                if (loggedIn) {
                    showMyBadges();
                } else {
                    System.out.println("Devi effettuare il login per poter visualizzare i tuoi badge.");
                }
                break;

                case "7":
                if (loggedIn) {
                    String response = sendMessage("logout");
                    if (response != null && response.equals("LOGOUT_SUCCESS")) {
                        loggedIn = false;
                        System.out.println("Logout effettuato con successo.");
                    } else {
                        System.out.println("Errore durante il logout: " + response);
                    }
                } else {
                    System.out.println("Non sei loggato.");
                }
                break;

                case "8":
                sendMessage("exit");
                System.out.println("Arrivederci!");
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println("Errore durante la chiusura del socket: " + e.getMessage());
                }
                System.exit(0);
                break;

            default:
                System.out.println("Comando sconosciuto. Riprova.");
                break;
        }
    }

    private String sendMessage(String message) {
        if (output != null) {
            output.println(message);
            try {
                String response = input.readLine();
                if (response != null) {
                    System.out.println("Risposta dal server: " + response);
                    return response;
                } else {
                    System.out.println("Nessuna risposta dal server.");
                    return null;
                }
            } catch (IOException e) {
                System.out.println("Il server non risponde.");
                return null;
            }
        }
        return null;
    }

    private void register(Scanner scanner) {
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
            System.out.print("Inserisci username: ");
            String username = scanner.nextLine();
            System.out.print("Inserisci password: ");
            String password = scanner.nextLine();

            String response = sendMessage("login/" + username + "/" + password);

            if (response == null) {
                System.out.println("Nessuna risposta dal server.");
                return;
            }

            if (response.equals("LOGIN_SUCCESS")) {
                loggedIn = true;
                System.out.println("Login effettuato con successo.");
            } else if (response.equals("LOGIN_FAILED")) {
                System.out.println("Login fallito. Controlla le tue credenziali.");
            } else if (response.equals("Utente già loggato.")) {
                loggedIn = true;
                System.out.println("Sei già loggato.");
            } else {
                System.out.println("Risposta inaspettata dal server: " + response);
            }
        } catch (Exception e) {
            System.out.println("Errore durante il login: " + e.getMessage());
        }
    }

    private void searchHotel(Scanner scanner) {
        try {
            System.out.print("Inserisci il nome dell'hotel da cercare: ");
            String hotelName = scanner.nextLine();
            System.out.print("Inserisci la città: ");
            String city = scanner.nextLine();
            output.println("searchhotel/" + hotelName + "/" + city);

            String serverResponse;
            while ((serverResponse = input.readLine()) != null) {
                System.out.println(serverResponse);
                if (serverResponse.equals("fine")) {
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Errore durante la ricerca dell'hotel: " + e.getMessage());
        }
    }

    private void searchAllHotels(Scanner scanner) {
        System.out.print("Inserisci il nome della città: ");
        String city = scanner.nextLine();
        output.println("searchallhotels/" + city);

        try {
            String serverResponse;
            while ((serverResponse = input.readLine()) != null) {
                if (serverResponse.equals("fine")) {
                    break;
                }
                System.out.println(serverResponse);
            }
        } catch (IOException e) {
            System.out.println("Errore durante la ricerca di tutti gli hotel: " + e.getMessage());
        }
    }


    private void insertReview(Scanner scanner) {
        try {
            System.out.print("Inserisci il nome dell'hotel: ");
            String hotelName = scanner.nextLine();
            System.out.print("Inserisci il nome della città: ");
            String cityName = scanner.nextLine();
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
    
            output.println("insertreview/" + hotelName +"/"+cityName+ "/" + overallRating + "/" + positionRating + "/"
                    + cleanlinessRating + "/" + serviceRating + "/" + priceRating);
            String serverResponse = input.readLine();
            if (serverResponse != null) {
                if (serverResponse.equals("RECENSIONE_INSERITA_SUCCESSO")) {
                    System.out.println("Recensione inserita con successo.");
                } else {
                    System.out.println("Errore: " + serverResponse);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Errore: I punteggi devono essere numeri interi.");
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
