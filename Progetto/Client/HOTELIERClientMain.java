package Client;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonObject; // scaricare la libreria da internet e metterla dentro Libraries per un codice pulito

public class HOTELIERClientMain {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Inserisci user: ");
		String user = scanner.nextLine();
		System.out.println("Inserisci password: ");
		String password = scanner.nextLine();
		if (user.contains("Leonardo Luperini") && password.equals("")) {
			System.out.println("Accesso come amministratore");
		} else {
			System.out.println("Accesso come utente");
		}
		int id = 1;
		System.out.format("user: %s, password: %s, id: %d%n", user, password, id);
		// TODO funzione che avvia il client
		scanner.close();
	}

}
