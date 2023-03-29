package htl.steyr.mygrover.controller;

import htl.steyr.mygrover.TokenHandler;
import htl.steyr.mygrover.model.Brand;
import htl.steyr.mygrover.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@CrossOrigin
@org.springframework.web.bind.annotation.RestController
@RequestMapping("/brand")
public class BrandRestController {

    @Autowired
    BrandRepository brandRepository;

    @Autowired
    TokenHandler tokenHandler;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Void> createBrand(@RequestBody Brand brand, @RequestHeader("authorization") String token){

        brandRepository.save(brand);

        return ResponseEntity.ok(null);
    }

    @PostMapping("/update")
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Void> updateBrand(@RequestBody Brand brand, @RequestHeader("authorization") String token){
        // If the brand does not exist
        if(brandRepository.findById(brand.getId()).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Brand updatedBrand = brandRepository.findById(brand.getId()).get();
        updatedBrand.setName(brand.getName());

        brandRepository.save(updatedBrand);

        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Void> deleteBrand(@PathVariable Long id, @RequestHeader("authorization") String token){
        // If the brand does not exist
        if(brandRepository.findById(id).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        brandRepository.deleteById(id);

        return ResponseEntity.ok(null);
    }

    @GetMapping(value = {"/load", "/load/{id}"})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<List<Brand>> loadBrand(@PathVariable(required = false) Long id, @RequestHeader("authorization") String token){
        if(id != null){
            if(brandRepository.findById(id).isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            List<Brand> result = new ArrayList<>(Collections.emptyList());

            result.add(brandRepository.findById(id).get());

            return ResponseEntity.ok(result);
        }else{
            return ResponseEntity.ok(brandRepository.findAll());
        }
    }
}
