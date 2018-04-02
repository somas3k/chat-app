package messages;

import server.Server;

public class OverloadMessage extends Message {
    public OverloadMessage() {
        super();
    }

    @Override
    public void serverAction(Server server, server.Client client) {

    }

    @Override
    public void clientAction(client.Client client) {
        client.stopConnection();
    }
}
