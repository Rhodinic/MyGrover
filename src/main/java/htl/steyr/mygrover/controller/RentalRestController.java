package htl.steyr.mygrover.controller;

import htl.steyr.mygrover.DTO.LoadingDTO;
import htl.steyr.mygrover.DTO.RentalDTO;
import htl.steyr.mygrover.TokenHandler;
import htl.steyr.mygrover.model.Model;
import htl.steyr.mygrover.model.Rental;
import htl.steyr.mygrover.repository.CustomerRepository;
import htl.steyr.mygrover.repository.ModelRepository;
import htl.steyr.mygrover.repository.RentalRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@CrossOrigin
@org.springframework.web.bind.annotation.RestController
@RequestMapping("/rental")
public class RentalRestController {

    @Autowired
    RentalRepository rentalRepository;

    @Autowired
    ModelRepository modelRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    TokenHandler tokenHandler;

    @GetMapping("/period/{from}/{to}")
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<List<Model>> loadAvailableModels(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date from, @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date to, @RequestHeader("authorization") String token) {
        return ResponseEntity.ok(modelRepository.findAvailableModels(from, to));
    }

    @PostMapping("/rent")
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Void> rentModel(@RequestBody RentalDTO rentalDTO, @RequestHeader("authorization") String token) {
        // If this model is already rented in this time-period
        if(modelRepository.findModelRentedInTimePeriod(rentalDTO.from(), rentalDTO.to(), rentalDTO.modelId()).isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        // If the model does not exist
        if(modelRepository.findById(rentalDTO.modelId()).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // If the customer does not exist
        if(customerRepository.findById(rentalDTO.customerId()).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Rental rental = new Rental();
        rental.setModel(modelRepository.findById(rentalDTO.modelId()).get());
        rental.setCustomer(customerRepository.findById(rentalDTO.customerId()).get());
        rental.setToDate(rentalDTO.to());
        rental.setFromDate(rentalDTO.from());
        rental.setActive(false);

        rentalRepository.save(rental);

        return ResponseEntity.ok(null);
    }

    @PutMapping("/activate/{id}")
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Void> activateRent(@PathVariable Long id, @RequestHeader("authorization") String token) {
        // If the rental does not exist
        if(rentalRepository.findById(id).isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Rental rental = rentalRepository.findById(id).get();
        rental.setActive(true);
        rentalRepository.save(rental);

        return ResponseEntity.ok(null);
    }

    @GetMapping("/customer")
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<List<RentalDTO>> loadAllRentals(@RequestHeader("authorization") String token) {
        List<RentalDTO> result = new ArrayList<>(Collections.emptyList());

        for(Rental r : rentalRepository.findAllByActiveIsTrue()){
            result.add(new RentalDTO(
                    r.getId(),
                    r.getModel().getId(),
                    r.getCustomer().getId(),
                    r.getFromDate(),
                    r.getToDate())
            );
        }

        return ResponseEntity.ok(result);
    }

    @PostMapping("/done/{rentalId}")
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> finishRent(@PathVariable Long rentalId, @RequestBody LoadingDTO loadingDTO, @RequestHeader("authorization") String token) {
        // If the rental does not exist
        if(rentalRepository.findById(rentalId).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Rental rental = rentalRepository.findById(rentalId).get();
        rental.setActive(false);
        rentalRepository.save(rental);

        try {
            JSONObject result = new JSONObject();
            result.put("totalPrice", loadingDTO.loadings() * rental.getModel().getPrice());

            return ResponseEntity.ok(result.toString());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
