package mapper;

import dto.ProductDTO;
import entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "cdi")
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {

    ProductDTO toDTO (Product user);
}
