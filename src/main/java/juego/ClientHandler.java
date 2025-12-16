package juego;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

// Clase para manejar la logica del juego para cada cliente conectado
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
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            out.println("¡Bienvenido al juego de adivinar el numero!");
            out.println("Adivina un numero entre 1 y 100.");

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (adivinado) {
                    if (inputLine.equalsIgnoreCase("si")) {
                        reiniciarJuego();
                        out.println("Nuevo juego. Adivina un numero entre 1 y 100.");
                    } else {
                        out.println("Gracias por jugar. ¡Hasta luego!");
                        break;
                    }
                } else {
                    try {
                        int intento = Integer.parseInt(inputLine);
                        evaluarIntento(intento, out);
                    } catch (NumberFormatException e) {
                        out.println("Por favor, introduce un numero valido.");
                    }
                }
            }
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int generarNumeroSecreto() {
        return new Random().nextInt(100) + 1;
    }

    private void reiniciarJuego() {
        this.numeroSecreto = generarNumeroSecreto();
        this.adivinado = false;
    }

    private void evaluarIntento(int intento, PrintWriter out) {
        if (intento < numeroSecreto) {
            out.println("El numero es mayor.");
        } else if (intento > numeroSecreto) {
            out.println("El numero es menor.");
        } else {
            out.println("¡Numero correcto!");
            out.println("¿Quieres jugar de nuevo? (si/no)");
            adivinado = true;
        }
    }
}