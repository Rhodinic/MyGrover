package htl.steyr.mygrover.controller;

import htl.steyr.mygrover.DTO.ModelDTO;
import htl.steyr.mygrover.TokenHandler;
import htl.steyr.mygrover.model.Model;
import htl.steyr.mygrover.repository.BrandRepository;
import htl.steyr.mygrover.repository.ModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@org.springframework.web.bind.annotation.RestController
@RequestMapping("/model")
public class ModelRestController {

    @Autowired
    ModelRepository modelRepository;

    @Autowired
    BrandRepository brandRepository;

    @Autowired
    TokenHandler tokenHandler;

    @PostMapping("/create")
    public ResponseEntity<Void> createModel(@RequestBody ModelDTO modelDTO, @RequestHeader("authorization") String token) {
        // If the brand does not exist
        if (brandRepository.findById(modelDTO.brand()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Model model = new Model();
        model.setBrand(brandRepository.findById(modelDTO.brand()).get());
        model.setName(modelDTO.name());
        model.setPrice(modelDTO.price());

        modelRepository.save(model);

        return ResponseEntity.ok(null);
    }

    @PostMapping("/update")
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Void> updateModel(@RequestBody ModelDTO modelDTO, @RequestHeader("authorization") String token) {
        // If the model does not exist
        if (modelRepository.findById(modelDTO.id()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // If the brand does not exist
        if (brandRepository.findById(modelDTO.brand()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Model updatedModel = modelRepository.findById(modelDTO.id()).get();
        updatedModel.setName(modelDTO.name());
        updatedModel.setPrice(modelDTO.price());
        updatedModel.setBrand(brandRepository.findById(modelDTO.brand()).get());

        modelRepository.save(updatedModel);

        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Void> deleteModel(@PathVariable Long id, @RequestHeader("authorization") String token) {
        // If the model does not exist
        if (modelRepository.findById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        modelRepository.deleteById(id);

        return ResponseEntity.ok(null);
    }

    @GetMapping(value = {"/load", "/load/{id}"})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<List<Model>> loadModel(@PathVariable(required = false) Long id, @RequestHeader("authorization") String token) {
        if (id != null) {
            // If the model does not exist
            if (modelRepository.findById(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            List<Model> result = new ArrayList<>(Collections.emptyList());

            result.add(modelRepository.findById(id).get());

            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.ok(modelRepository.findAll());
        }
    }

    @GetMapping("/brand/{id}")
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<List<Model>> loadModelForBrand(@PathVariable Long id, @RequestHeader("authorization") String token) {
        // If the brand does not exist
        if (brandRepository.findById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(modelRepository.findAllByBrand_Id(id));
    }
}
