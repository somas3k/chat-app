package messages;

import server.Client;
import server.Server;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class LoginMessage extends Message {
    private int udpPort;
    private InetAddress address;
    public LoginMessage(String userName) {
        super(userName);
    }

    public LoginMessage(String userName, int udpPort, InetAddress address) {
        super(userName);
        this.udpPort = udpPort;
        try {
            this.address = InetAddress.getByName("localhost");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void serverAction(Server server, Client client) {
        client.setUserName(userName);
        client.setUdpPort(udpPort);
        client.setUdpAddress(address);

        client.setLogged(true);
        server.spreadMessage(client, new HelloMessage(client.getUserName()));
    }

    @Override
    public void clientAction(client.Client client) {

    }
}
