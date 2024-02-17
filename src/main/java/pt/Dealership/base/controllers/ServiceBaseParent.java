package pt.Dealership.base.controllers;

import org.springframework.hateoas.RepresentationModel;
import pt.Dealership.base.interfaces.ICrud;

/**
 * Parent class for ServiceBase, so we can reference this as type in the ControllerBase
 * @param <T> model type (ex: brand, car, etc)
 * @param <Key> primary key
 */
public abstract class ServiceBaseParent<T, Key>  implements ICrud<T, Key> {

}
