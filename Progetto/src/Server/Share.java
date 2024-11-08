package src.Server;
import java.net.*;
import src.H_U_R.*;

public class Share {
    private static InetAddress MCAST;
    private static DatagramSocket SOCKET;
    private static int CLIENTPORT;

    public Share (String mcastip, int clientPort) throws SocketException, UnknownHostException {
        this.CLIENTPORT = clientPort;
        this.MCAST = InetAddress.getByName(mcastip);
        this.SOCKET = new DatagramSocket(10000);
    }

    public void notify(Hotel hotel) {
        // manda in multicast le notifiche
        String msg = hotel.getName();
        System.out.println("Notifying: " + msg);
        System.out.println(msg.getBytes() + " " + msg.length());

        try {
            this.SOCKET.send(new DatagramPacket(msg.getBytes(), msg.length(), MCAST, CLIENTPORT));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
