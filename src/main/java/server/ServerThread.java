package server;

import messages.*;

import java.io.*;
import java.net.Socket;

class ServerThread extends Thread{
    private Server server;
    private ObjectInputStream ois;
    private Client client;
    private boolean on = true;

    public ServerThread(Server server, ObjectInputStream ois){
        this.server = server;
        this.ois = ois;
    }

    public void run() {
        while(on){
            Message msg;
            try {
                msg = (Message) ois.readObject();
                msg.serverAction(server, client);
            }
            catch (IOException e){
                //e.printStackTrace();
                server.logoutClient(client);
            }
            catch (ClassNotFoundException e){
                e.printStackTrace();
                System.err.println("Wrong Message");
            }
        }
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public synchronized void stopThread(){
        on = false;
    }
}
