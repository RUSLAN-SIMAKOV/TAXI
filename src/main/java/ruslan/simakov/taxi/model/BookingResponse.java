package ruslan.simakov.taxi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BookingResponse(

        @JsonProperty("car_id")
        Integer carId,

        @JsonProperty("total_time")
        Integer totalTime) {
}
