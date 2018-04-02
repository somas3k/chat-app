package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;

public class UdpThread extends Thread {
    private DatagramSocket datagramSocket;
    private boolean on = true;

    public UdpThread(DatagramSocket datagramSocket) {
        this.datagramSocket = datagramSocket;
    }

    @Override
    public void run() {
        try {
            while(on){
                byte[] receiveBuffer = new byte[2048];
                Arrays.fill(receiveBuffer, (byte)0);
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                datagramSocket.receive(receivePacket);
                System.out.println(new String(receivePacket.getData(), 0, receivePacket.getLength()));
            }
        }
        catch (SocketException e){
            System.err.println("Stopped");
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }

    public synchronized void setOff(){
        on = false;
    }
}
