package com.example.vehicle_auction.domain.model;

import com.example.vehicle_auction.domain.model.base.FullModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
public class CategoryModel extends FullModel {
    private String name;
    private String slug;
    private String description;
    private boolean isActive;
    private boolean deleted;
    private LocalDateTime deletedAt;
}
