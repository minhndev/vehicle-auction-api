package com.example.vehicle_auction.domain.model;

import com.example.vehicle_auction.domain.model.base.FullModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class CategoryModel extends FullModel {
    private UUID id;
    private String name;
    private String slug;
    private String description;
    private boolean isActive;
    private boolean deleted;
    private LocalDateTime deletedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
