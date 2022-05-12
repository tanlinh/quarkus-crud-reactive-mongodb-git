package dto;

import lombok.Data;
import java.util.List;

@Data
public class ProductDTO {

    private String productName;
    private Double price;
    private Integer quantity;
    private List<OrderDTO> orderList;
}
