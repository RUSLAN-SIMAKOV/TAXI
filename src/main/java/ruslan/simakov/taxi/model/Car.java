package ruslan.simakov.taxi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Entity
@NoArgsConstructor
@Data
@Table(name = "car")
@Accessors(chain = true)
@EqualsAndHashCode(of = "carId")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "car_id")
    private Integer carId;

    @Column(name = "is_booked")
    private Boolean isBooked;

    @Column(name = "position_x")
    private Integer positionX;

    @Column(name = "position_y")
    private Integer positionY;

    @Column(name = "destination_x")
    private Integer destinationX;

    @Column(name = "destination_y")
    private Integer destinationY;
}
