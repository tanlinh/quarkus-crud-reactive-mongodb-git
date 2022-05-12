package dto;

import lombok.Data;

@Data
public class OrderDTO {

    private String status;
    private Double total;
    private String name;
    private Integer quantity;
}
