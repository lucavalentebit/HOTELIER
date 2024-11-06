// package src.Server;
// /*import com.google.gson.Gson;
// import com.google.gson.JsonSyntaxException;
// import com.google.gson.stream.JsonReader;

// import src.H_U_R.Hotels;
// import src.H_U_R.User;

// import java.io.BufferedReader;
// import java.io.File;
// import java.io.FileReader;
// import java.io.FileWriter;
// import java.io.IOException;
// import java.lang.ProcessBuilder.Redirect.Type;
// import java.util.concurrent.ConcurrentHashMap;
// import java.util.ArrayList;
// import java.util.Map;*/

// import com.google.gson.Gson;
// import com.google.gson.reflect.TypeToken;

// import src.Data.Database;
// import src.H_U_R.User;

// import java.io.*;
// import java.lang.reflect.Type;
// import java.net.ServerSocket;
// import java.net.Socket;
// import java.util.concurrent.ConcurrentHashMap;
// import java.util.concurrent.ExecutorService;
// import java.util.concurrent.Executors;


// public class DataHandlerUsers {
//     private static final String USERS_FILE = "Progetto/src/Data/Users.json";
//         private static final DataHandlerUsers DB = null;
//         private ConcurrentHashMap<String, User> users;
//         private ExecutorService threadPool;
//         private volatile boolean shutdownRequested = false;
//         private ServerSocket serverSocket;
    
//         public DataHandlerUsers() {
//             users = loadUsers();
//             threadPool = Executors.newCachedThreadPool(); //inizializziazione TP
//         }
//         public void shutdown() {
//             threadPool.shutdown();
//             saveUsers(); //persistenza degli utenti a chiusura del server
//         }
//         // Gestisce la connessione con un client
//         public void handleClient(Socket clientSocket) {
    
//             DataHandlerHotels dataHandlerUsers;
//                     Database dataHandlerHotels;
//                     threadPool.execute(new ClientHandler(clientSocket, DB, dataHandlerUsers, dataHandlerHotels));
//     }

//     // Carica gli utenti dal file JSON
//     private ConcurrentHashMap<String, User> loadUsers() {
//         try {
//             File file = new File(USERS_FILE);
//             if (!file.exists()) {
//                 // Se il file non esiste, ritorna una nuova mappa vuota
//                 //ERRORE
//                 return new ConcurrentHashMap<>();
//             }
//             Gson gson = new Gson(); //rivedere questa chiamata
//             BufferedReader br = new BufferedReader(new FileReader(file));
//             Type type = new TypeToken<ConcurrentHashMap<String, User>>() {}.getType();
//             ConcurrentHashMap<String, User> loadedUsers = gson.fromJson(br, type);
//             br.close();
//             return loadedUsers != null ? loadedUsers : new ConcurrentHashMap<>();
//         } catch (IOException e) {
//             e.printStackTrace();
//             return new ConcurrentHashMap<>();
//         }
//     }

//     // Salva gli utenti su file JSON
//     public synchronized void saveUsers() {
//         try {
//             Gson gson = new Gson();
//             FileWriter writer = new FileWriter(USERS_FILE);
//             gson.toJson(users, writer);
//             writer.flush();
//             writer.close();
//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//     }

//     // Verifica se un utente esiste
//     public boolean userExists(String username) {
//         return users.containsKey(username);
//     }

//     // Aggiunge un nuovo utente
//     public synchronized boolean addUser(User user) {
//         if (users.containsKey(user.getUsername())) {
//             return false; // L'utente esiste già
//         }
//         users.put(user.getUsername(), user);
//         saveUsers();
//         return true;
//     }

//     // Ottiene un utente
//     public User getUser(String username) {
//         return users.get(username);
//     }

//     // Aggiorna un utente
//     public synchronized void updateUser(User user) {
//         users.put(user.getUsername(), user);
//         saveUsers();
//     }

//     // Rimuove un utente
//     public void setServerSocket(ServerSocket serverSocket) {
//         this.serverSocket = serverSocket;
//     }
//     // Richiesta di chiusura del server
//     public void requestShutdown() {
//         shutdownRequested = true;
//         try {
//             if (serverSocket != null && !serverSocket.isClosed()) {
//                 serverSocket.close();
//             }
//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//     }
//     // Verifica se è stata richiesta la chiusura del server
//     public boolean isShutdownRequested() {
//         return shutdownRequested;
//     }
// }