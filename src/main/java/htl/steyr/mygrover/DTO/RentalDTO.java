package htl.steyr.mygrover.DTO;

import java.util.Date;

public record RentalDTO(long id, Long modelId, Long customerId, Date from, Date to) {
}
