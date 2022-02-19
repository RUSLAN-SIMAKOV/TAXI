package ruslan.simakov.taxi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Coordinates(

        @JsonProperty("x")
        Long coordinateX,

        @JsonProperty("y")
        Long coordinateY) {
}
