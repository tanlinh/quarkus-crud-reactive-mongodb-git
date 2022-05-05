package com.product.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDTO {

    private String name;
    private String email;
    private String address;
    private String phoneNumber;
    private Boolean status;

    private String userName;
    private String password;

    private String fileUpload;
    private List<ProductDTO> productList;
}
