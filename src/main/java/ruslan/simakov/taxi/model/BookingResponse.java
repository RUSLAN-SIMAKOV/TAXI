package ruslan.simakov.taxi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BookingResponse(

        @JsonProperty("car_id")
        Long carId,

        @JsonProperty("total_time")
        Long totalTime) {
}
