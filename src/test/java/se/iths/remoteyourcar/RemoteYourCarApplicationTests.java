package se.iths.remoteyourcar;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import se.iths.remoteyourcar.entities.VehicleState;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RemoteYourCarIT {
    @LocalServerPort
    private int port;
    @Test
    void smokeTestApiAvailable() {
        java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://h-178-174-162-51.a536.priv.bahnhof.se/api/1/vehicles/4545643/data_request/vehicle_state/"))
                .build();
        var response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).join();
        assertThat(response.statusCode()).isEqualTo(200);


    }


    @Test
    void smokeTestApiAVehicleClocked() {
        java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://h-178-174-162-51.a536.priv.bahnhof.se/api/1/vehicles/4545643/data_request/vehicle_state/"))
                .build();
        var response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).join();

        byte[] jsonData = response.body().toString().getBytes();

        ObjectMapper mapper = new ObjectMapper();
        try {
            VehicleState vehicle = mapper.readValue(jsonData, VehicleState.class);
            assertThat(vehicle.getDoorState().isDr_locked()).isEqualTo(true);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Test
    void smokeTestApiAVehicleUnlocked() {
        java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://h-178-174-162-51.a536.priv.bahnhof.se/api/1/vehicles/1/command/door_unlock/"))
                .build();
        var response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).join();

        byte[] jsonData = response.body().toString().getBytes();
        assertThat(response.statusCode()).isEqualTo(404);


    }

    @Test
    void smokeTestApiAVehicleUnauthorized() {
        java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://h-178-174-162-51.a536.priv.bahnhof.se/api/1/vehicles/4545643/data_request/drive_state/"))
                .build();
        var response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).join();

        byte[] jsonData = response.body().toString().getBytes();
        assertThat(response.statusCode()).isEqualTo(401);


    }
}