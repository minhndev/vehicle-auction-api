package com.example.vehicle_auction.infrastructure.persistence.entity;

import com.example.vehicle_auction.infrastructure.persistence.entity.base.FullEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categories")
@Getter
@Setter
public class Category extends FullEntity {

    @Column(nullable = false, unique = true, length = 150)
    private String name;

    @Column(nullable = false,unique = true, length = 150 )
    private String slug;

    @Column(length = 250)
    private String description;

    @Column(name = "is_active" , nullable = false)
    private boolean active = true;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private Set<Product> products = new HashSet<>();

}
