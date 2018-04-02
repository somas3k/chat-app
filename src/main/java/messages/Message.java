package messages;



import server.Server;

import java.io.Serializable;

public abstract class Message implements Serializable {
    String userName;

    Message() {
    }

    Message(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public abstract void serverAction(Server server, server.Client client);
    public abstract void clientAction(client.Client client);


}
