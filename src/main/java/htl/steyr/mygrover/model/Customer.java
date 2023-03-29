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
public class Customer {
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
    private String email;

    @JsonIgnore
    @OneToMany(mappedBy="customer")
    @Getter
    @Setter
    private Set<Rental> rentals;
}
