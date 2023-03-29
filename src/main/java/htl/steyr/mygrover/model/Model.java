package htl.steyr.mygrover.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column
    @NonNull
    private String name;

    @Column
    @NonNull
    private Double price;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="brand_id", nullable=false)
    @NonNull
    private Brand brand;

    @JsonIgnore
    @OneToMany(mappedBy="model")
    @Getter
    @Setter
    private Set<Rental> rentals;

}
