package messages;

import server.Client;
import server.Server;

public class TextMessage extends Message {
    private String messageContent;

    public TextMessage(String userName, String messageContent) {
        super(userName);
        this.messageContent = messageContent;
    }

    public String getMessageContent() {
        return messageContent;
    }

    @Override
    public void serverAction(Server server, Client client) {
        switch(messageContent) {
            case "logout":
                server.logoutClient(client);
                break;
            default:
                server.spreadMessage(client, this);
                break;
        }
    }

    @Override
    public void clientAction(client.Client client) {
        switch(messageContent){
            default:
                System.out.println(userName+": " + messageContent);
                break;
        }

    }
}
