package ruslan.simakov.taxi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BookingRequest (

        @JsonProperty("source")
        Coordinates source,

        @JsonProperty("destination")
        Coordinates destination) {
}
