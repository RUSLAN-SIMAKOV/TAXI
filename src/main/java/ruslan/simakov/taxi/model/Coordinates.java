package ruslan.simakov.taxi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Coordinates(

        @JsonProperty("x")
        Integer coordinateX,

        @JsonProperty("y")
        Integer coordinateY) {
}
