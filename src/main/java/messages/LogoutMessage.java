package messages;

import server.Client;
import server.Server;

public class LogoutMessage extends Message {
    public LogoutMessage(String userName) {
        super(userName);
    }

    @Override
    public void serverAction(Server server, Client client) {
        server.logoutClient(client);
    }

    @Override
    public void clientAction(client.Client client) {
        System.out.println(userName + " disconnected!");
    }
}
