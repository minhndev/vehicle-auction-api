package com.example.vehicle_auction.application.mapper;

import com.example.vehicle_auction.application.dto.order.OrderResponse;
import com.example.vehicle_auction.domain.model.OrderModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {

    @Mapping(target = "winningPrice", source = "model.totalAmount")
    @Mapping(target = "depositAmount", expression = "java(model.getTotalAmount().subtract(model.getRemainingAmount()))")
    OrderResponse toResponse(OrderModel model, String productName);
}
