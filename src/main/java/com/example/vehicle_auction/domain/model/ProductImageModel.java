package com.example.vehicle_auction.domain.model;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageModel {
    private UUID id;
    private String url;
    private boolean isMain;
    private Integer sortOrder;
}
