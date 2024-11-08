package src.Server;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Condivisore implements Runnable{
    private MulticastSocket MCAST_SOCKET;
    private List<String> notificationList = new ArrayList<>();

    public Condivisore (int recPort, String mcastIp) throws IOException {
        this.MCAST_SOCKET = new MulticastSocket(recPort);
        InetAddress MCAST_ADDRESS = InetAddress.getByName(mcastIp);
        MCAST_SOCKET.joinGroup(MCAST_ADDRESS);
    }

    public void run() {
        // attende ripetutamente notifiche
        while (true) {
            byte[] buff = new byte[4096];
            DatagramPacket notification = new DatagramPacket(buff, buff.length);
            try {
                MCAST_SOCKET.receive(notification);

                // aggiunge la notifica nella lista
                notificationList.add(new String(notification.getData(), StandardCharsets.UTF_8));
            }
            catch (IOException e) {
                System.out.println("ERRORE");
            }
        }
    }

    public String print() {
        StringBuilder output = new StringBuilder();
        // stampa le notifiche degli altri utenti
        for (String notification : this.notificationList) {
            String[] payload = notification.split("&");
            for (int i = 0; i < payload.length; i++) {
                output.append("è diventato primo in classifica ").append(payload[i].substring(0, Math.min(payload[i].length(), 25)).trim()).append("\n");
            }
        }
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                notificationList.clear();
            }
        }, 5000);
        return output.toString();
    }

}
