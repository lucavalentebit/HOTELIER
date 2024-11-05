package src.Server;

import java.io.*;
import java.net.Socket;

import src.Client.InputCheck;
import src.H_U_R.User;


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

    public ClientHandler(Socket socket, DataHandlerUsers dataHandler) {
        this.clientSocket = socket;
        this.dataHandler = new DataHandlerUsers(); // Inizializza DataHandler
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

            switch (command.toLowerCase()) {
                case "register":
                    handleRegister(input, output);
                    break;

                // Altri comandi...

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
}