package primos;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class UdpClient {
    
    private static final String SERVER_IP = "44.197.206.164";
    private static final int SERVER_PORT = 6000;

    public static void main(String[] args) {
        try (DatagramSocket clientSocket = new DatagramSocket();
             Scanner scanner = new Scanner(System.in)) {

            InetAddress IPAddress = InetAddress.getByName(SERVER_IP);
            System.out.println("Cliente UDP iniciado. Conectando a " + SERVER_IP + ":" + SERVER_PORT);

            System.out.println("Introduce un numero positivo (o 'salir'):");
            while (scanner.hasNextLine()) {
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("salir")) break;

                // 1. Enviar datos
                byte[] sendData = input.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, SERVER_PORT);
                clientSocket.send(sendPacket);

                // 2. Esperar respuesta (UDP no garantiza entrega, si el firewall falla, esto se quedará esperando eternamente)
                byte[] receiveData = new byte[4096]; // Buffer grande por si hay muchos primos
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

                // Opcional: Timeout de 5 segundos para no quedarse colgado si falla la red
                clientSocket.setSoTimeout(5000);

                try {
                    clientSocket.receive(receivePacket);
                    String modifiedSentence = new String(receivePacket.getData(), 0, receivePacket.getLength());
                    System.out.println("Primos hasta " + input + ": " + modifiedSentence);
                } catch (java.net.SocketTimeoutException e) {
                    System.out.println("Error: El servidor no respondió. Verifica el Firewall UDP puerto 6000.");
                }

                System.out.println("\nIntroduce otro numero (o 'salir'):");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}