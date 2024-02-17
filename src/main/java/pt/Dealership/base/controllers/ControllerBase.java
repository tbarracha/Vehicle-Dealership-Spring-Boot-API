package pt.Dealership.base.controllers;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.Dealership.base.models.GenericDTO;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Base class for all Controller Classes with common CRUD operations
 * @param <T> model type (ex: brand, car, etc)
 * @param <Key> primary key
 * @param <S> service
 */
public abstract class ControllerBase<T, Key, S extends ServiceBaseParent<T, Key>> { //implements ICrud<T, Key> {

    protected abstract S getService();

    // Public API
    // ==============================================================================================================
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<GenericDTO<T>> getById(@PathVariable("id") Key id) {
        var entity = getService().getById(id);
        var dto = addLinks(entity, true, true, true, true);
        return httpOkOrNotFound(dto);
    }

    @GetMapping(produces = "application/json")
    public CollectionModel<T> getAll()  {
        var entityList = getService().getAll();
        CollectionModel<T> collectionModel = CollectionModel.of(entityList);
        collectionModel = addLinks(collectionModel, true, true);
        return collectionModel;
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<GenericDTO<T>> create(@RequestBody T body) {
        var entity = getService().create(body);
        var dto = addLinks(entity, true, true, true, true);
        return httpCreatedOrNotAcceptable(dto);
    }

    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<GenericDTO<T>> update(@PathVariable("id") Key id, @RequestBody T body) {
        var entity = getService().update(id, body);
        var dto = addLinks(entity, true, true, true, true);
        return httpOkOrNotFound(dto);
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<GenericDTO<T>> delete(@PathVariable("id") Key id) {
        var entity = getService().delete(id);
        var dto = addLinks(entity, true, true, false, false);
        return httpOkOrNotFound(dto);
    }


    // HTTP Populate
    // ------------------------------------------------------------------------------------------
    private boolean isPopulated;

    @PostMapping(value = "/populate", produces = "application/json")
    public ResponseEntity<String> tryPopulate() {
        if (isPopulated) {
            return new ResponseEntity<>("Already Populated!", HttpStatus.OK);
        }

        populate();
        isPopulated = true;

        return new ResponseEntity<>("Populated!", HttpStatus.CREATED);
    }

    protected abstract void populate();


    // HTTP Response Entities
    // ------------------------------------------------------------------------------------------
    protected ResponseEntity<GenericDTO<T>> httpCreatedOrNotAcceptable(GenericDTO<T> model) {
        if (model != null)
            return new ResponseEntity<>(model, HttpStatus.CREATED);
        else
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

    protected ResponseEntity<GenericDTO<T>> httpOkOrNotFound(GenericDTO<T> model) {
        if (model != null)
            return new ResponseEntity<>(model, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    protected <TT> ResponseEntity<TT> httpOk(TT payLoad) {
        return new ResponseEntity<>(payLoad, HttpStatus.OK);
    }


    // Generated by ChatGPT
    // I wanted a generic method to add links so that I don't have to add these methods by hand
    protected GenericDTO<T> addLinks(T model, boolean addGetters, boolean addCreate, boolean addUpdate, boolean addDelete) {
        List<Link> links = new ArrayList<>();
        GenericDTO<T> dto = new GenericDTO<>(model);

        try {
            Class<? extends ControllerBase> clazz = this.getClass();    // Get the class of the current controller
            Method[] methods = clazz.getDeclaredMethods();          // Get all methods of the class

            for (Method method : methods) {
                // Generate link for getter methods
                if (addGetters && method.getName().startsWith("get")) {
                    Link link = linkTo(methodOn(clazz).getById((Key) model.getClass().getMethod("getId").invoke(model))).withRel("getById");
                    if (!links.contains(link))
                        links.add(link);

                    // Generate link for getAll method
                } else if (addGetters && method.getName().equals("getAll")) {
                    Link link = linkTo(methodOn(clazz).getAll()).withRel("getAll");
                    if (!links.contains(link))
                        links.add(link);

                    // Generate link for create method
                } else if (addCreate && method.getName().equals("create")) {
                    Link link = linkTo(methodOn(clazz).create(model)).withRel("create");
                    if (!links.contains(link))
                        links.add(link);

                    // Generate link for update method
                } else if (addUpdate && method.getName().equals("update")) {
                    Link link = linkTo(methodOn(clazz).update((Key) model.getClass().getMethod("getId").invoke(model), model)).withRel("update");
                    if (!links.contains(link))
                        links.add(link);

                    // Generate link for delete method
                } else if (addDelete && method.getName().equals("delete")) {
                    Link link = linkTo(methodOn(clazz).delete((Key) model.getClass().getMethod("getId").invoke(model))).withRel("delete");
                    if (!links.contains(link))
                        links.add(link);
                }
            }

            dto.add(links); // Add all generated links to the model
        } catch (Exception e) {
            e.printStackTrace(); // Handle exception
        }

        return dto;
    }

    protected CollectionModel<T> addLinks(CollectionModel<T> collectionModel, boolean addSelfLink, boolean addCreateLink) {
        List<Link> links = new ArrayList<>();

        try {
            Class<? extends ControllerBase> clazz = this.getClass();    // Get the class of the current controller
            Method[] methods = clazz.getDeclaredMethods();          // Get all methods of the class

            for (Method method : methods) {
                // Add self link to the collection
                if (addSelfLink && method.getName().equals("getAll")) {
                    Link link = linkTo(methodOn(clazz).getAll()).withSelfRel();
                    if (!links.contains(link))
                        links.add(link);
                }

                // Add create link
                if (addCreateLink && method.getName().equals("create")) {
                    Link link = linkTo(methodOn(clazz).create(null)).withRel("create");
                    if (!links.contains(link))
                        links.add(link);
                }
            }

            collectionModel.add(links); // Add all generated links to the model
        } catch (Exception e) {
            e.printStackTrace(); // Handle exception
        }

        return collectionModel;
    }

    // < Previous try which was still repetitive as fck >
    /*
    protected abstract ModelType addLinks(ModelType model, boolean addGetters, boolean addCreate, boolean addUpdate, boolean addDelete);

    // => hated to do this!
    protected Brand addLinks(Brand model, boolean addGetById, boolean addGetAll, boolean addUpdate, boolean addDelete) {
        if (addGetById)
            model.add(linkTo(methodOn(BrandController.class).get(model.getId())).withSelfRel());            // link to Get

        if (addGetAll)
            model.add(linkTo(methodOn(BrandController.class).getAll()).withSelfRel());                      // link to Get All

        if (addUpdate)
            model.add(linkTo(methodOn(BrandController.class).update(model.getId(), model)).withSelfRel());  // link to Update

        if (addDelete) {
            model.add(linkTo(methodOn(BrandController.class).delete(model)).withSelfRel());                 // link to Delete
            model.add(linkTo(methodOn(BrandController.class).deleteById(model.getId())).withSelfRel());     // link to Delete by ID
        }return model;
    }
    */
}
