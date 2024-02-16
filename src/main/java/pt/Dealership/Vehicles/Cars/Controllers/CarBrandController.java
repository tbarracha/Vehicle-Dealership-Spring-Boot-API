package pt.Dealership.Vehicles.Cars.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.Dealership.Vehicles.Cars.Services.CarBrandService;
import pt.Dealership.base.controllers.ControllerBase;
import pt.Dealership.Vehicles.Cars.Models.CarBrand;

@RestController
@RequestMapping("/api/brand")
public class CarBrandController extends ControllerBase<CarBrand, Long> {

    @Autowired
    private CarBrandService service;

    private boolean populated;

    @PostMapping(value = "/populate")
    public ResponseEntity<String> populate() {

        if (populated) {
            return httpOk();
        }

        service.create("Audi");
        service.create("BWM");
        service.create("Mercedes");
        service.create("Tesla");

        return httpOk("Populated brands!");
    }


    // Getters
    // -------------------------------
    @Override
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<CarBrand> getById(@PathVariable("id") Long id) {
        var entity = service.getById(id);
        entity = addLinks(entity, true, true, true, true);
        return httpOkOrNotFound(entity);
    }

    @GetMapping(value = "/name/{name}", produces = "application/json")
    public ResponseEntity<CarBrand> getByName(@PathVariable("name") String name) {
        var entity = service.getByName(name);
        entity = addLinks(entity, true, true, true, true);
        return httpOkOrNotFound(entity);
    }

    @Override
    @GetMapping(produces = "application/json")
    public CollectionModel<CarBrand> getAll() {
        var entityList = service.getAll();
        CollectionModel<CarBrand> collectionModel = CollectionModel.of(entityList);
        collectionModel = addLinks(collectionModel, true, true);
        return collectionModel;
    }


    // Create & Update
    // -------------------------------
    @Override
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<CarBrand> create(@RequestBody CarBrand body) {
        var entity = service.create(body);
        entity = addLinks(entity, true, true, true, true);
        return httpCreatedOrNotAcceptable(entity);
    }

    @Override
    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<CarBrand> update(@PathVariable("id") Long id, @RequestBody CarBrand body) {
        var entity = service.update(id, body);
        entity = addLinks(entity, true, true, true, true);
        return httpOkOrNotFound(entity);
    }


    // Delete
    // -------------------------------
    @Override
    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<CarBrand> delete(@PathVariable("id") Long id) {
        var entity = service.delete(id);
        entity = addLinks(entity, true, true, true, true);
        return httpOkOrNotFound(entity);
    }
}
