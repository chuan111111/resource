package lab9;

import java.io.*;
import java.net.*;

public class PokemonClient {
    public static void main(String[] args) {
        String host = "localhost";
        int port = 1234;

        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            Thread readerThread = new Thread(() -> {
                try {
                    String line;
                    while ((line = in.readLine()) != null) {
                        System.out.println(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            readerThread.start();

            System.out.println("Enter the pokemon name (or 'QUIT' to exit): ");
            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput);
                if ("QUIT".equals(userInput)) {
                    break;
                }
            }
            System.out.println("Disconnected from the server.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}