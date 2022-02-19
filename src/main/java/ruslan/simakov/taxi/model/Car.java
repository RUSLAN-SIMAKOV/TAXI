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
    private Long carId;

    @Column(name = "is_booked")
    private Boolean isBooked;

    @Column(name = "with_Passenger")
    private Boolean withPassenger;

    @Column(name = "position_x")
    private Long positionX;

    @Column(name = "position_y")
    private Long positionY;

    @Column(name = "source_x")
    private Long sourceX;

    @Column(name = "source_y")
    private Long sourceY;

    @Column(name = "destination_x")
    private Long destinationX;

    @Column(name = "destination_y")
    private Long destinationY;
}
