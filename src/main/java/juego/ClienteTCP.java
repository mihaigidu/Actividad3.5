package juego;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

// Clase principal para el cliente del juego
public class ClienteTCP {
    // Definimos la IP y el Puerto como constantes para facilitar cambios
    private static final String SERVER_IP = "44.197.206.164"; // TU IP DE AWS
    private static final int SERVER_PORT = 5000;

    public static void main(String[] args) {
        try {
            System.out.println("Conectando a " + SERVER_IP + " en el puerto " + SERVER_PORT + "...");

            // Conectar al servidor en la IP de AWS
            Socket socket = new Socket(SERVER_IP, SERVER_PORT);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);

            // Hilo para escuchar mensajes del servidor
            Thread serverListener = new Thread(() -> {
                try {
                    String serverResponse;
                    while ((serverResponse = in.readLine()) != null) {
                        System.out.println(serverResponse);
                    }
                } catch (IOException e) {
                    System.out.println("Conexión finalizada o interrumpida.");
                    System.exit(0); // Cerrar el programa si el servidor se desconecta
                }
            });
            serverListener.start();

            // Bucle principal para enviar datos
            while (true) {
                if (scanner.hasNextLine()) {
                    String userInput = scanner.nextLine();
                    out.println(userInput);
                    if (userInput.equalsIgnoreCase("no")) {
                        break;
                    }
                }
            }
            socket.close();
        } catch (IOException e) {
            System.err.println("Error: No se pudo conectar al servidor.");
            System.err.println("Verifica que la instancia EC2 esté encendida y el puerto 5000 abierto en el Security Group.");
        }
    }
}