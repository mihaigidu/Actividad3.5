package juego;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

// Clase principal para el cliente del juego
public class ClienteTCP {
    public static void main(String[] args) {
        try {
            // Conectar al servidor en localhost y puerto 12345
            Socket socket = new Socket("localhost", 12345);
            // Preparar para leer datos del servidor
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // Preparar para enviar datos al servidor
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            // Escaner para leer la entrada del usuario desde la consola
            Scanner scanner = new Scanner(System.in);

            // Hilo para escuchar los mensajes del servidor de forma continua
            Thread serverListener = new Thread(() -> {
                try {
                    String serverResponse;
                    // Leer y mostrar mensajes del servidor mientras la conexion este activa
                    while ((serverResponse = in.readLine()) != null) {
                        System.out.println(serverResponse);
                    }
                } catch (IOException e) {
                    // Informar al usuario si se pierde la conexion con el servidor
                    System.out.println("Desconectado del servidor.");
                }
            });
            serverListener.start(); // Iniciar el hilo de escucha

            // Bucle principal para leer la entrada del usuario y enviarla al servidor
            while (true) {
                String userInput = scanner.nextLine(); // Leer linea de la consola
                out.println(userInput); // Enviar la linea al servidor
                // Si el usuario escribe "no", terminar el bucle y cerrar el cliente
                if (userInput.equalsIgnoreCase("no")) {
                    break;
                }
            }

            // Cerrar la conexion del socket
            socket.close();
        } catch (IOException e) {
            // Imprimir cualquier error de entrada/salida que ocurra
            e.printStackTrace();
        }
    }
}
