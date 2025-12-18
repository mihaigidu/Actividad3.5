package juego;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClienteTCP {
    // Configuracion de conexion
    private static final String SERVER_IP = "3.237.45.23";
    private static final int SERVER_PORT = 5000;

    public static void main(String[] args) {
        try {
            System.out.println("Conectando a " + SERVER_IP + " en el puerto " + SERVER_PORT + "...");

            // Establecer conexion con el servidor
            Socket socket = new Socket(SERVER_IP, SERVER_PORT);

            // Configurar flujos de entrada y salida
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);

            // Hilo secundario para recibir mensajes del servidor asincronamente
            Thread serverListener = new Thread(() -> {
                try {
                    String serverResponse;
                    while ((serverResponse = in.readLine()) != null) {
                        System.out.println(serverResponse);
                    }
                } catch (IOException e) {
                    System.out.println("Conexion finalizada o interrumpida.");
                    System.exit(0);
                }
            });
            serverListener.start();

            // Bucle principal para leer consola y enviar al servidor
            while (true) {
                if (scanner.hasNextLine()) {
                    String userInput = scanner.nextLine();
                    out.println(userInput);
                    if (userInput.equalsIgnoreCase("no")) {
                        break; // Salir si el usuario dice "no"
                    }
                }
            }
            socket.close();
        } catch (IOException e) {
            System.err.println("Error: No se pudo conectar al servidor.");
            System.err.println("Verifica que la instancia EC2 este encendida y el puerto 5000 abierto en el Security Group.");
        }
    }
}