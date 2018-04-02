package server;
import messages.*;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server implements Runnable {
    private List<Client> clients = new ArrayList<>();
    private ServerSocket serverSocket = null;
    private DatagramSocket datagramSocket;
    private Thread serverThread = null;
    private UdpThread udpThread;
    private int clientsCount = 0;
    private final int maxClients;


    public Server(int portNumber, int maxClients){
        this.maxClients = maxClients;
        try {
            serverSocket = new ServerSocket(portNumber);
            datagramSocket = new DatagramSocket(portNumber);
            udpThread = new UdpThread(this, datagramSocket);
            startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startServer(){
        udpThread.start();
        if(serverThread == null){
            serverThread = new Thread(this);
            serverThread.start();

        }
    }

    public void run() {
        while(serverThread != null){
            try {
                acceptClient(serverSocket.accept());
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public synchronized void spreadMessage(Client owner, Message message){
        for(Client client : clients){
            if(!client.equals(owner) && client.isLogged()){
                try {
                    client.getOos().writeObject(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void acceptClient(Socket socket){
        if(clientsCount < maxClients){
            try {
                ServerThread thread = new ServerThread(this, new ObjectInputStream(socket.getInputStream()));
                thread.start();
                Client client = new Client(thread, socket.getOutputStream(), socket);
                clients.add(client);
                thread.setClient(client);
            }
            catch (IOException e){
                e.printStackTrace();
            }
            clientsCount++;
        }
        else{
            try {
                (new ObjectOutputStream(socket.getOutputStream())).writeObject(new OverloadMessage());
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public synchronized void logoutClient(Client client){
        spreadMessage(client, new LogoutMessage(client.getUserName()));
        client.stopService();
        clients.remove(client);
        clientsCount--;
    }

    public synchronized void spreadUdpMessage(int ownerPort, String message){
        for(Client client : clients){
            if(client.getUdpPort()!=ownerPort){
                byte[] sendBuffer = (message).getBytes();
                DatagramPacket datagramPacket = new DatagramPacket(sendBuffer, sendBuffer.length, client.getUdpAddress(), client.getUdpPort());
                try {
                    datagramSocket.send(datagramPacket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        new Server(12346,5);
    }
}

