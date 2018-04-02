package server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    private String userName;
    private ServerThread thread;
    private UdpThread udpThread;
    private ObjectOutputStream oos;
    private Socket socket;
    private boolean logged = false;
    private InetAddress udpAddress;
    private int udpPort;

    public Client(ServerThread thread, OutputStream oos, Socket socket) throws IOException {
        this.thread = thread;
        this.oos = new ObjectOutputStream(oos);
        this.socket = socket;
    }

    public ObjectOutputStream getOos() {
        return oos;
    }

    public String getUserName() {
        return userName;
    }

    public void stopService(){
        thread.stopThread();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    public void setUserName(String userName) {

        this.userName = userName;
    }

    public boolean isLogged(){
        return logged;
    }

    public void setUdpAddress(InetAddress udpAddress) {
        this.udpAddress = udpAddress;
    }

    public void setUdpPort(int udpPort) {
        this.udpPort = udpPort;
    }

    public InetAddress getUdpAddress() {
        return udpAddress;
    }

    public int getUdpPort() {
        return udpPort;
    }

    public void setUdpThread(UdpThread udpThread) {
        this.udpThread = udpThread;
    }
}
