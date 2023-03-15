package application;

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
    public Server() {
//        ip = getIp();
        SpaceRepository repository = new SpaceRepository();
        SequentialSpace space = new SequentialSpace();

        repository.add("space", space);

        String serverURI = PROTOCOL + ip + PORT + "/?keep";
        System.out.println("Server uri: " + serverURI);
        repository.addGate(serverURI);

        try {
            System.out.println("Host server put 'test' in space");
            space.put("Test");
            System.out.println(space.size());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
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
