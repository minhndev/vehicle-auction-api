package com.example.vehicle_auction.domain.model;

import com.example.vehicle_auction.domain.model.base.BaseIdModel;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageModel extends BaseIdModel {
    private String url;
    private boolean isMain;
    private Integer sortOrder;
}
