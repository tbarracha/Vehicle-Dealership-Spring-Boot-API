package pt.Dealership.Vehicles.VehicleModels;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.Common.controllers.ServiceBase;
import pt.Dealership.Vehicles.VehicleBrands.VehicleBrand;
import pt.Dealership.Vehicles.VehicleBrands.VehicleBrandService;

import java.util.List;

@Service
public class VehicleModelService extends ServiceBase<VehicleModel, Long, VehicleModelRepository> {

    @Autowired
    private VehicleModelRepository repository;

    @Autowired
    private VehicleBrandService brandService;

    @Override
    protected VehicleModelRepository getRepository() {
        return repository;
    }

    @Override
    protected void updateEntityProperties(VehicleModel updatedBody, VehicleModel entityToUpdate) {

    }

    public VehicleModel create(long brandId, String name) {
        var brand = brandService.getById(brandId);
        var model = new VehicleModel(brand, name);

        return create(model);
    }

    public VehicleModel getByName(String name) {
        return repository.findByName(name).orElse(null);
    }

    public List<VehicleModel> getByFirstLetter(String firstLetter) {
        return repository.findByFirstLetter(firstLetter);
    }

    public List<VehicleModel> getByBrandId(long brandId) {
        return repository.findByBrandId(brandId);
    }

    public List<VehicleModel> getByBrandName(String brandName) {
        return repository.findByBrandName(brandName);
    }
}
