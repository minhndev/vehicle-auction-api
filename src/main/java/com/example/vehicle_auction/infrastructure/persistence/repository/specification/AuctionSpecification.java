package com.example.vehicle_auction.infrastructure.persistence.repository.specification;

import com.example.vehicle_auction.application.dto.auction.AuctionFilterRequest;
import com.example.vehicle_auction.infrastructure.persistence.entity.Auction;
import com.example.vehicle_auction.infrastructure.persistence.entity.Product;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class AuctionSpecification {

    public static Specification<Auction> filterBY(AuctionFilterRequest request){
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getStatus() != null){
                predicates.add(criteriaBuilder.equal(root.get("status"), request.getStatus()));
            }

            if (request.getMinPrice() != null){
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("currentPrice"), request.getMinPrice()));
            }

            if (request.getMaxPrice() != null){
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("currentPrice"), request.getMaxPrice()));
            }

            if (StringUtils.hasText(request.getKeyword()) || request.getCategoryId() != null){

                Join<Auction, Product> productJoin = root.join("product");

                if (StringUtils.hasText(request.getKeyword())) {
                    String likeKeyword = "%" + request.getKeyword().trim().toLowerCase() + "%";
                    predicates.add(criteriaBuilder.like(criteriaBuilder.lower(productJoin.get("name")), likeKeyword));
                }

                if (request.getCategoryId() != null) {
                    predicates.add(criteriaBuilder.equal(productJoin.get("categoryId"), request.getCategoryId()));
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

        };
    }

}
