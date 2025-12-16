package juego;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private int numeroSecreto;
    private boolean adivinado;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.numeroSecreto = generarNumeroSecreto();
        this.adivinado = false;
    }

    @Override
    public void run() {
        try {
            // Flujos de comunicacion con el cliente
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            // Mensajes de bienvenida
            out.println("¡Bienvenido al juego de adivinar el numero!");
            out.println("Adivina un numero entre 1 y 100.");

            String inputLine;
            // Bucle principal de juego
            while ((inputLine = in.readLine()) != null) {
                if (adivinado) {
                    // Si ya adivino, preguntar si quiere jugar de nuevo
                    if (inputLine.equalsIgnoreCase("si")) {
                        reiniciarJuego();
                        out.println("Nuevo juego. Adivina un numero entre 1 y 100.");
                    } else {
                        out.println("Gracias por jugar. ¡Hasta luego!");
                        break; // Terminar si no quiere seguir
                    }
                } else {
                    // Procesar el intento de adivinanza
                    try {
                        int intento = Integer.parseInt(inputLine);
                        evaluarIntento(intento, out);
                    } catch (NumberFormatException e) {
                        out.println("Por favor, introduce un numero valido.");
                    }
                }
            }
            clientSocket.close(); // Cerrar conexion al finalizar
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Genera un numero aleatorio entre 1 y 100
    private int generarNumeroSecreto() {
        return new Random().nextInt(100) + 1;
    }

    // Reinicia el estado del juego para una nueva partida
    private void reiniciarJuego() {
        this.numeroSecreto = generarNumeroSecreto();
        this.adivinado = false;
    }

    // Compara el intento con el numero secreto y da una pista
    private void evaluarIntento(int intento, PrintWriter out) {
        if (intento < numeroSecreto) {
            out.println("El numero es mayor.");
        } else if (intento > numeroSecreto) {
            out.println("El numero es menor.");
        } else {
            out.println("¡Numero correcto!");
            out.println("¿Quieres jugar de nuevo? (si/no)");
            adivinado = true; // Marcar como adivinado
        }
    }
}