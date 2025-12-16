package primos;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Manejador de paquetes para el calculo de primos.
 */
public class PacketHandler implements Runnable {
    private DatagramSocket socket;
    private DatagramPacket packet;

    public PacketHandler(DatagramSocket socket, DatagramPacket packet) {
        this.socket = socket;
        this.packet = packet;
    }

    @Override
    public void run() {
        try {
            String received = new String(packet.getData(), 0, packet.getLength()).trim();
            System.out.println("Recibido de " + packet.getAddress() + ": " + received);

            String response;
            try {
                int number = Integer.parseInt(received);
                if (number < 0) {
                    response = "Error: El numero debe ser positivo.";
                } else {
                    List<Integer> primes = getPrimes(number);
                    response = String.join(",", primes.stream().map(String::valueOf).toArray(String[]::new));
                }
            } catch (NumberFormatException e) {
                response = "Error: Entrada invalida. Envia un numero entero.";
            }

            byte[] sendData = response.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, packet.getAddress(), packet.getPort());
            socket.send(sendPacket);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Calcula los primos hasta n.
     */
    private List<Integer> getPrimes(int n) {
        List<Integer> primes = new ArrayList<>();
        if (n >= 2) primes.add(2);
        for (int i = 3; i <= n; i += 2) {
            if (isPrime(i)) {
                primes.add(i);
            }
        }
        return primes;
    }

    /**
     * Comprueba si n es primo.
     */
    private boolean isPrime(int n) {
        if (n <= 1) return false;
        if (n == 2) return true;
        if (n % 2 == 0) return false;
        for (int i = 3; i <= Math.sqrt(n); i += 2) {
            if (n % i == 0) return false;
        }
        return true;
    }
}