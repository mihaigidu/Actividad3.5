package juego;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

// Clase para manejar la logica del juego para cada cliente conectado
public class ClientHandler implements Runnable {
    private final Socket clientSocket; // Socket para la comunicacion con el cliente
    private int numeroSecreto; // El numero que el cliente debe adivinar
    private boolean adivinado; // Bandera para saber si el numero ya fue adivinado

    // Constructor que recibe el socket del cliente
    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.numeroSecreto = generarNumeroSecreto(); // Generar el numero secreto inicial
        this.adivinado = false; // Inicialmente, el numero no ha sido adivinado
    }

    // Metodo principal que se ejecuta en un hilo separado para cada cliente
    @Override
    public void run() {
        try {
            // Preparar para leer datos del cliente
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            // Preparar para enviar datos al cliente
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            // Mensajes de bienvenida para el cliente
            out.println("¡Bienvenido al juego de adivinar el numero!");
            out.println("Adivina un numero entre 1 y 100.");

            String inputLine;
            // Bucle para leer los intentos del cliente
            while ((inputLine = in.readLine()) != null) {
                if (adivinado) {
                    // Si el numero ya fue adivinado, preguntar si quiere jugar de nuevo
                    if (inputLine.equalsIgnoreCase("si")) {
                        reiniciarJuego();
                        out.println("Nuevo juego. Adivina un numero entre 1 y 100.");
                    } else {
                        out.println("Gracias por jugar. ¡Hasta luego!");
                        break; // Terminar el bucle si no quiere jugar mas
                    }
                } else {
                    // Si el numero no ha sido adivinado, procesar el intento
                    try {
                        int intento = Integer.parseInt(inputLine); // Convertir la entrada a numero
                        evaluarIntento(intento, out); // Evaluar el numero ingresado
                    } catch (NumberFormatException e) {
                        // Si la entrada no es un numero valido
                        out.println("Por favor, introduce un numero valido.");
                    }
                }
            }

            // Cerrar la conexion con el cliente
            clientSocket.close();
        } catch (IOException e) {
            // Imprimir cualquier error de entrada/salida
            e.printStackTrace();
        }
    }

    // Genera un numero aleatorio entre 1 y 100
    private int generarNumeroSecreto() {
        return new Random().nextInt(100) + 1;
    }

    // Reinicia el juego con un nuevo numero secreto
    private void reiniciarJuego() {
        this.numeroSecreto = generarNumeroSecreto();
        this.adivinado = false;
    }

    // Evalua el intento del jugador y le da pistas
    private void evaluarIntento(int intento, PrintWriter out) {
        if (intento < numeroSecreto) {
            out.println("El numero es mayor.");
        } else if (intento > numeroSecreto) {
            out.println("El numero es menor.");
        } else {
            // Si el jugador adivina el numero
            out.println("¡Numero correcto!");
            out.println("¿Quieres jugar de nuevo? (si/no)");
            adivinado = true; // Marcar como adivinado
        }
    }
}
