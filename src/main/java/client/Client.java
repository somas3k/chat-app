package client;

import messages.LoginMessage;
import messages.LogoutMessage;
import messages.Message;
import messages.TextMessage;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client extends Thread {
    private Socket socket;
    private DatagramSocket datagramSocket;
    private MulticastSocket multicastSocket;
    private ReceiverThread receiver = null;
    private ObjectOutputStream oos;
    private String userName;
    private boolean on = true;
    private Scanner in = new Scanner(System.in);
    private String address;
    private InetAddress udpAddress;
    private InetAddress groupAddress;
    private int port;
    private UdpThread udpThread;
    private UdpThread multicastThread;
    private AsciiArts asciiArts = new AsciiArts();

    public Client(String address, String groupAddress, int port, String userName) {
        this.userName = userName;
        this.address = address;
        try {
            udpAddress = InetAddress.getByName(address);
            this.groupAddress = InetAddress.getByName(groupAddress);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        this.port = port;
    }

    public void connect() throws IOException{
        socket = new Socket(address, port);
        datagramSocket = new DatagramSocket();
        multicastSocket = new MulticastSocket(port + 1);
        multicastSocket.joinGroup(groupAddress);

    }

    public synchronized void sendToServer(Message message){
        try {
            oos.writeObject(message);
        } catch (IOException e) {
            System.err.println("Server is dead.");
            stopConnection();
        }
    }

    @Override
    public void run() {
        while(on){
            try {
                String messageContent = in.nextLine();
                switch (messageContent){
                    case "logout":
                        sendToServer(new LogoutMessage(userName));
                        on = false;
                        break;
                    case "U":
                        sendByUdp(asciiArts.getRandomArt(), datagramSocket, udpAddress, port);
                        break;
                    case "M":
                        multicastSocket.leaveGroup(groupAddress);
                        sendByUdp(asciiArts.getRandomArt(), multicastSocket, groupAddress, port+1);
                        multicastSocket.joinGroup(groupAddress);
                        break;
                    default:
                        sendToServer(new TextMessage(userName, messageContent));
                        break;
                }
            }
            catch (IllegalStateException e){
                on = false;
                System.out.println("Too much clients now. Try again later.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendByUdp(String message, DatagramSocket socket, InetAddress address, int port){
        byte[] sendBuffer;

        sendBuffer = (userName + ":\n" + message).getBytes();

        DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, address, port);
        try {
            socket.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void init() throws IOException{
        oos = new ObjectOutputStream(socket.getOutputStream());
        receiver = new ReceiverThread(this, new ObjectInputStream(socket.getInputStream()));
        receiver.start();
        udpThread = new UdpThread(datagramSocket);
        udpThread.start();
        multicastThread = new UdpThread(multicastSocket);
        multicastThread.start();
        sendToServer(new LoginMessage(userName, datagramSocket.getLocalPort(), datagramSocket.getInetAddress()));
    }

    public void stopConnection(){
        receiver.stopThread();
        udpThread.setOff();
        multicastThread.setOff();
        in.close();
        try {
            socket.close();
            datagramSocket.close();
            multicastSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("Enter your user name:");
        Scanner in = new Scanner(System.in);
        String userName = in.nextLine();
        try {
            String address = "localhost";
            String groupAddress = "224.0.1.1";

            int port = 12346;
            Client client = new Client(address, groupAddress, port, userName);
            client.connect();
            client.init();
            client.start();
        } catch (IOException e) {
            System.err.println("Server is dead");
        }

    }
}
