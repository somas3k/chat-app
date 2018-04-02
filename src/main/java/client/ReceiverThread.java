package client;
import messages.*;
import java.io.IOException;
import java.io.ObjectInputStream;

public class ReceiverThread extends Thread {
    private ObjectInputStream ois;
    private Client client;
    private boolean on = true;

    public ReceiverThread(Client client, ObjectInputStream ois){
        this.client = client;
        this.ois = ois;
    }

    public void run() {
        while(on){
            try {
                Message msg = (Message) ois.readObject();
                msg.clientAction(client);
            } catch (IOException e) {
                client.stopConnection();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                System.err.println("Wrong message");
            }
        }
    }

    public synchronized void stopThread(){
        on = false;
    }
}
