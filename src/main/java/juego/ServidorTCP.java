package juego;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorTCP {
    public static void main(String[] args) {
        try {
            // Escuchar en todas las interfaces de red en el puerto 5000
            ServerSocket serverSocket = new ServerSocket(5000);
            System.out.println("Servidor TCP activo en puerto 5000. Esperando conexiones...");

            // Bucle para aceptar clientes de forma continua
            while (true) {
                // Bloquea hasta que un cliente se conecta
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado desde: " + clientSocket.getInetAddress());

                // Crea y ejecuta un nuevo hilo para el cliente
                Thread clientThread = new Thread(new ClientHandler(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}