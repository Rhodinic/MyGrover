package htl.steyr.mygrover.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    @Setter(AccessLevel.NONE)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="model_id", nullable=false)
    @NonNull
    private Model model;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="customer_id", nullable=false)
    @NonNull
    private Customer customer;

    @Column
    @NonNull
    private Date fromDate;

    @Column
    @NonNull
    private Date toDate;

    @Column
    @NonNull
    private Boolean active;

}
