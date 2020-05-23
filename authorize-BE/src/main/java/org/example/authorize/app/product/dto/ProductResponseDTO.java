package org.example.authorize.app.product.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductResponseDTO {

    private String id;

    private String name;

    private String description;

    private String image;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;
}
