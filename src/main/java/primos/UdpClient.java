package primos;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

/**
 * Cliente UDP para solicitar el calculo de numeros primos.
 */
public class UdpClient {
    public static void main(String[] args) {
        String host = "localhost";
        int port = 6000;

        if (args.length >= 2) {
            host = args[0];
            port = Integer.parseInt(args[1]);
        }

        try (DatagramSocket clientSocket = new DatagramSocket();
             Scanner scanner = new Scanner(System.in)) {

            InetAddress IPAddress = InetAddress.getByName(host);

            System.out.println("Introduce un numero para obtener los primos hasta el (o 'salir'):");
            while (scanner.hasNextLine()) {
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("salir")) break;

                byte[] sendData = input.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                clientSocket.send(sendPacket);

                byte[] receiveData = new byte[4096];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                clientSocket.receive(receivePacket);

                String modifiedSentence = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Primos: " + modifiedSentence);
                System.out.println("Introduce otro numero (o 'salir'):");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}