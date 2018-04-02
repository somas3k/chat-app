package messages;

import server.Client;
import server.Server;

public class HelloMessage extends Message {

    public HelloMessage(String userName) {
        super(userName);
    }

    @Override
    public void serverAction(Server server, Client client) {

    }

    @Override
    public void clientAction(client.Client client) {
        System.out.println(userName + " has already connected!");
    }
}
