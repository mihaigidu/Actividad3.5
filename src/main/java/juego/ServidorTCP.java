package juego;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

// Clase principal para el servidor del juego
public class ServidorTCP {
    public static void main(String[] args) {
        try {
            // Crear un socket de servidor en el puerto 5000
            // Al no especificar IP, Java escucha en 0.0.0.0 (todas las interfaces)
            ServerSocket serverSocket = new ServerSocket(5000);
            System.out.println("Servidor TCP activo en puerto 5000. Esperando conexiones...");

            // Bucle infinito para aceptar conexiones de clientes
            while (true) {
                // Esperar y aceptar una nueva conexion de cliente
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado desde: " + clientSocket.getInetAddress());

                // Crear un nuevo hilo para manejar la comunicacion con este cliente
                Thread clientThread = new Thread(new ClientHandler(clientSocket));
                clientThread.start(); // Iniciar el hilo del cliente
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}