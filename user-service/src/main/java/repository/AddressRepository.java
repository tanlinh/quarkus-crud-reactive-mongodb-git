package repository;

import entity.Address;
import io.quarkus.mongodb.panache.PanacheMongoRepositoryBase;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AddressRepository implements PanacheMongoRepositoryBase<Address, String> {

    public Address findByProvinceAndDistrict(String province, String district) {
        return find("{$and: [{province: ?1, district: ?2}]}", province, district).firstResult();
    }
}
