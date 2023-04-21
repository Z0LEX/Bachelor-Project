package main.application;

import org.jspace.SequentialSpace;
import org.jspace.SpaceRepository;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Server {
    private static final String PORT = ":9001";
    private static final String PROTOCOL = "tcp://";
    private String ip = "127.0.0.1";
    public static SequentialSpace space = new SequentialSpace();
    public Server() {
//        ip = getIp();
        SpaceRepository repository = new SpaceRepository();

        repository.add("space", space);

        String serverURI = PROTOCOL + ip + PORT + "/?keep";
        repository.addGate(serverURI);
    }

    private static String getIp() {
        try(final DatagramSocket socket = new DatagramSocket()){
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            return socket.getLocalAddress().getHostAddress();
        } catch (SocketException | UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}
