package ruslan.simakov.taxi.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import ruslan.simakov.taxi.repository.CarRepository;
import ruslan.simakov.taxi.service.CarCreationService;

import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ruslan.simakov.taxi.util.TestFactory.BOOKING_REQUEST;
import static ruslan.simakov.taxi.util.TestFactory.REQUEST_FOR_BOOKING_FIRST_CAR;
import static ruslan.simakov.taxi.util.TestFactory.REQUEST_FOR_ONE_TICK;
import static ruslan.simakov.taxi.util.TestFactory.RESPONSE_FOR_FIRST_CAR;
import static ruslan.simakov.taxi.util.TestFactory.RESPONSE_FOR_FIRST_CAR_IN_ONE_TICK;
import static ruslan.simakov.taxi.util.TestFactory.RESPONSE_FOR_SECOND_CAR;
import static ruslan.simakov.taxi.util.TestFactory.RESPONSE_FOR_THIRD_CAR;

@SpringBootTest
@AutoConfigureMockMvc
class TaxiControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    CarRepository carRepository;

    @Autowired
    CarCreationService carCreationService;

    @Value("${parameter.car-position.x}")
    private long positionX;

    @Value("${parameter.car-position.y}")
    private long positionY;

    @SneakyThrows
    @Test
    void bookCarTest() {
        String response = mockMvc.perform(post("/api/book")
                        .contentType(APPLICATION_JSON)
                        .content(BOOKING_REQUEST))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        assertThat(response).isEqualTo(RESPONSE_FOR_FIRST_CAR);

        response = mockMvc.perform(post("/api/book")
                        .contentType(APPLICATION_JSON)
                        .content(BOOKING_REQUEST))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        assertThat(response).isEqualTo(RESPONSE_FOR_SECOND_CAR);

        response = mockMvc.perform(post("/api/book")
                        .contentType(APPLICATION_JSON)
                        .content(BOOKING_REQUEST))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        assertThat(response).isEqualTo(RESPONSE_FOR_THIRD_CAR);

        response = mockMvc.perform(post("/api/book")
                        .contentType(APPLICATION_JSON)
                        .content(BOOKING_REQUEST))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        assertThat(response).isEqualTo("");
    }

    @SneakyThrows
    @Test
    void bookCarEmptyBodyTest() {
        String response = mockMvc.perform(post("/api/book")
                        .contentType(APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertThat(response).isEqualTo("");
    }

    @SneakyThrows
    @Test
    void driveCarTest() {
        mockMvc.perform(post("/api/book")
                        .contentType(APPLICATION_JSON)
                        .content(REQUEST_FOR_ONE_TICK))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        mockMvc.perform(post("/api/tick"))
                .andExpect(status().isOk());

        var response = mockMvc.perform(post("/api/book")
                        .contentType(APPLICATION_JSON)
                        .content(REQUEST_FOR_BOOKING_FIRST_CAR))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        assertThat(response).isEqualTo(RESPONSE_FOR_FIRST_CAR_IN_ONE_TICK);
    }

    @SneakyThrows
    @Test
    void resetAllCars() {
        String response = mockMvc.perform(post("/api/book")
                        .contentType(APPLICATION_JSON)
                        .content(BOOKING_REQUEST))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        assertThat(response).isEqualTo(RESPONSE_FOR_FIRST_CAR);

        response = mockMvc.perform(post("/api/book")
                        .contentType(APPLICATION_JSON)
                        .content(BOOKING_REQUEST))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        assertThat(response).isEqualTo(RESPONSE_FOR_SECOND_CAR);

        response = mockMvc.perform(post("/api/book")
                        .contentType(APPLICATION_JSON)
                        .content(BOOKING_REQUEST))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        assertThat(response).isEqualTo(RESPONSE_FOR_THIRD_CAR);

        mockMvc.perform(put("/api/reset"))
                .andExpect(status().isOk());

        response = mockMvc.perform(post("/api/book")
                        .contentType(APPLICATION_JSON)
                        .content(BOOKING_REQUEST))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        assertThat(response).isEqualTo(RESPONSE_FOR_FIRST_CAR);
    }

    @AfterEach
    void tearDown() {
        StreamSupport.stream(carRepository.findAll().spliterator(), false)
                .map(c -> c.setIsBooked(false)
                        .setWithPassenger(false)
                        .setPositionX(positionX)
                        .setPositionY(positionY)
                        .setDestinationX(null)
                        .setDestinationY(null)
                        .setSourceX(null)
                        .setSourceY(null))
                .forEach(carRepository::save);
    }
}
