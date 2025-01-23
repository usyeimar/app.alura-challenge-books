package com.cariniana.appalurachallengebooks.services.api;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GutendexAPI {

    private final static String GUTENDEX_API_URL = "https://gutendex.com/books/";

    /**
     * Fetch data from the given path.
     *
     * @param url URL to fetch data from
     *            * @param clazz Class of the object to be returned
     *            * @param <T>   Type of the object to be returned
     *            * @return Object of type T
     *            * @throws IOException          if an I/O error occurs
     *            * @throws InterruptedException if the operation is interrupted
     */
    private static <T> T fetch(String url, Class<T> clazz) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(java.time.Duration.ofSeconds(60))
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Error al obtener datos de la GutendexAPI. Código de estado: "
                        + response.statusCode() + " Respuesta: " + response.body());
            }

            Gson gson = new Gson();


            return gson.fromJson(response.body(), clazz);

        } catch (JsonSyntaxException e) {
            throw new RuntimeException("Error al deserializar el JSON: " + e.getMessage(), e);
        } catch (IOException | InterruptedException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado: " + e.getMessage(), e);
        }
    }


    public static <T> T findBookByTitle(String title, Class<T> clazz) throws IOException, InterruptedException {
        return fetch(GUTENDEX_API_URL + "?search=" + title.replace(" ", "+"), clazz);
    }


}
