package juego;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

// Clase principal para el servidor del juego
public class ServidorTCP {
    public static void main(String[] args) {
        try {
            // Crear un socket de servidor en el puerto 12345
            ServerSocket serverSocket = new ServerSocket(12345);
            System.out.println("Servidor TCP esperando conexiones...");

            // Bucle infinito para aceptar conexiones de clientes
            while (true) {
                // Esperar y aceptar una nueva conexion de cliente
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado desde " + clientSocket.getInetAddress());

                // Crear un nuevo hilo para manejar la comunicacion con este cliente
                // Se le pasa el socket del cliente a la clase ClientHandler
                Thread clientThread = new Thread(new ClientHandler(clientSocket));
                clientThread.start(); // Iniciar el hilo del cliente
            }
        } catch (IOException e) {
            // Imprimir cualquier error de entrada/salida que ocurra
            e.printStackTrace();
        }
    }
}
