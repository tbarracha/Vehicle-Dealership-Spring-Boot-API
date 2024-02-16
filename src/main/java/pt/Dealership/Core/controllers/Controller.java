package pt.Dealership.Core.controllers;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public abstract class Controller<EntityType extends RepresentationModel<EntityType>, Key> {

    // Public API
    // ==============================================================================================================
    public abstract ResponseEntity<EntityType> getById(Key id);

    public abstract CollectionModel<EntityType> getAll();

    public abstract ResponseEntity<EntityType> create(EntityType body);

    public abstract ResponseEntity<EntityType> update(Key id, EntityType body);

    public abstract ResponseEntity<EntityType> delete(Key id);



    // Internal
    // ==============================================================================================================

    // HTTP Response Entities
    // ------------------------------------------------------------------------------------------
    protected ResponseEntity<EntityType> httpCreatedOrNotAcceptable(EntityType model) {
        if (model != null)
            return new ResponseEntity<>(model, HttpStatus.CREATED);
        else
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

    protected ResponseEntity<EntityType> httpOkOrNotFound(EntityType model) {
        if (model != null)
            return new ResponseEntity<>(model, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    // Generated by ChatGPT
    // I wanted a generic method to add links so that I don't have to add these methods by hand
    protected EntityType addLinks(EntityType model, boolean addGetters, boolean addCreate, boolean addUpdate, boolean addDelete) {
        List<Link> links = new ArrayList<>();

        try {
            Class<? extends Controller> clazz = this.getClass();    // Get the class of the current controller
            Method[] methods = clazz.getDeclaredMethods();          // Get all methods of the class

            for (Method method : methods) {
                // Generate link for getter methods
                if (addGetters && method.getName().startsWith("get")) {
                    Link link = linkTo(methodOn(clazz).getById((Key) model.getClass().getMethod("getId").invoke(model))).withRel("getById");
                    links.add(link);

                // Generate link for getAll method
                } else if (addGetters && method.getName().equals("getAll")) {
                    Link link = linkTo(methodOn(clazz).getAll()).withRel("getAll");
                    links.add(link);

                // Generate link for create method
                } else if (addCreate && method.getName().equals("create")) {
                    Link link = linkTo(methodOn(clazz).create(model)).withRel("create");
                    links.add(link);

                // Generate link for update method
                } else if (addUpdate && method.getName().equals("update")) {
                    Link link = linkTo(methodOn(clazz).update((Key) model.getClass().getMethod("getId").invoke(model), model)).withRel("update");
                    links.add(link);

                // Generate link for delete method
                } else if (addDelete && method.getName().equals("delete")) {
                    Link link = linkTo(methodOn(clazz).delete((Key) model.getClass().getMethod("getId").invoke(model))).withRel("delete");
                    links.add(link);
                }
            }

            model.add(links); // Add all generated links to the model
        } catch (Exception e) {
            e.printStackTrace(); // Handle exception
        }

        return model;
    }

    protected CollectionModel<EntityType> addLinks(CollectionModel<EntityType> collectionModel, boolean addSelfLink, boolean addCreateLink) {
        List<Link> links = new ArrayList<>();

        try {
            Class<? extends Controller> clazz = this.getClass();    // Get the class of the current controller
            Method[] methods = clazz.getDeclaredMethods();          // Get all methods of the class

            for (Method method : methods) {
                // Add self link to the collection
                if (addSelfLink && method.getName().equals("getAll")) {
                    Link selfLink = linkTo(methodOn(clazz).getAll()).withSelfRel();
                    links.add(selfLink);
                }

                // Add create link
                if (addCreateLink && method.getName().equals("create")) {
                    Link createLink = linkTo(methodOn(clazz).create(null)).withRel("create");
                    links.add(createLink);
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
