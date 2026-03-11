package com.example.vehicle_auction.infrastructure.persistence.mapper;

import com.example.vehicle_auction.domain.model.WatchlistModel;
import com.example.vehicle_auction.infrastructure.persistence.entity.Watchlist;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WatchlistEntityMapper {
    Watchlist toEntity(WatchlistModel model);
    WatchlistModel toDomain(Watchlist entity);
}
