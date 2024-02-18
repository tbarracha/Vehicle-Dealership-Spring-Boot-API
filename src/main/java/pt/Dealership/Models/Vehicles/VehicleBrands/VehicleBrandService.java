package pt.Dealership.Models.Vehicles.VehicleBrands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.Common.controllers.ServiceBase;
import pt.Dealership.Models.Cars.Car;

import java.util.List;

@Service
public class VehicleBrandService extends ServiceBase<VehicleBrand, Long, VehicleBrandRepository> {

    @Autowired
    private VehicleBrandRepository repository;

    @Override
    protected VehicleBrandRepository getRepository() {
        return repository;
    }

    @Override
    protected void updateEntityProperties(VehicleBrand updatedBody, VehicleBrand entityToUpdate) {

    }

    @Override
    protected VehicleBrand tryGetEntity(VehicleBrand body) {
        return null;
    }

    public VehicleBrand getByName(String name) {
        return repository.findByName(name).orElse(null);
    }

    public List<VehicleBrand> getByLetterStart(String letter) {
        return repository.findByFirstLetter(letter);
    }
}
