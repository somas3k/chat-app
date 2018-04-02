package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;

public class UdpThread extends Thread {
    private boolean on = true;
    private DatagramSocket datagramSocket;
    private Server server;

    public UdpThread(Server server, DatagramSocket datagramSocket) {
        this.datagramSocket = datagramSocket;
        this.server = server;
    }

    @Override
    public void run() {
        byte[] receiveBuffer = new byte[2048];
        while(on){
            Arrays.fill(receiveBuffer, (byte) 0);
            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
            try {
                datagramSocket.receive(receivePacket);
                String msg = new String(receivePacket.getData(), 0, receivePacket.getLength());

                server.spreadUdpMessage(receivePacket.getPort(), msg);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void setOff(){
        on = false;
    }
}
