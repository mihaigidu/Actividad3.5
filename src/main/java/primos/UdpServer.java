package primos;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UdpServer {
    public static void main(String[] args) {
        int port = 6000;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Puerto invalido, usando " + port);
            }
        }

        System.out.println("Servidor UDP escuchando en el puerto " + port);

        try (DatagramSocket serverSocket = new DatagramSocket(port)) {
            while (true) {
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);

                // Utiliza un hilo para manejar la respuesta
                PacketHandler handler = new PacketHandler(serverSocket, receivePacket);
                new Thread(handler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}