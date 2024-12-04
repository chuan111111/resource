package lab9;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import org.json.JSONObject;
import org.json.JSONArray;

public class PokemonServer {
    public static void main(String[] args) {
        int port = 1234; // 选择一个端口
        ExecutorService pool = Executors.newFixedThreadPool(10); // 线程池

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Waiting for clients to connect...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected.");
                pool.execute(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    if ("QUIT".equals(inputLine)) {
                        System.out.println("Client quits.");
                        break;
                    }
                    String pokemonInfo = getPokemonInfo(inputLine);

                    out.println(pokemonInfo);
                }
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private String getPokemonInfo(String pokemonName) {
            String url = "https://pokeapi.co/api/v2/pokemon/" + pokemonName.toLowerCase().replace(" ", "%20");
            try {
                URL urlObj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
                con.setConnectTimeout(10000); // 设置连接超时时间为10秒
                con.setReadTimeout(10000); // 设置读取超时时间为10秒
                con.setRequestMethod("GET");

                int responseCode = con.getResponseCode();
                if (responseCode != 200) {
                    return "Failed to get data from PokeAPI";
                }

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Parse JSON response using org.json library
                JSONObject obj = new JSONObject(response.toString());
                int height = obj.getInt("height");
                int weight = obj.getInt("weight");
                JSONArray abilities = obj.getJSONArray("abilities");

                StringBuilder abilitiesList = new StringBuilder();
                for (int i = 0; i < abilities.length(); i++) {
                    JSONObject abilityObj = abilities.getJSONObject(i);
                    String abilityName = abilityObj.getJSONObject("ability").getString("name");
                    abilitiesList.append(abilityName);
                    if (i < abilities.length() - 1) {
                        abilitiesList.append(", ");
                    }
                }

                return "Name: " + pokemonName + "\nHeight: " + height + "cm\nWeight: " + weight + "kg\nAbilities: " + abilitiesList.toString();
            } catch (IOException e) {
                e.printStackTrace();
                return "Error connecting to PokeAPI";
            }
        }
    }
}