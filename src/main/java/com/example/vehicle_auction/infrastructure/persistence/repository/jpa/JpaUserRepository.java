package com.example.vehicle_auction.infrastructure.persistence.repository.jpa;

import com.example.vehicle_auction.infrastructure.persistence.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface JpaUserRepository extends JpaRepository<User, UUID> {
    @EntityGraph(attributePaths = {"account", "account.roles"})
    Optional<User> findByAccountId(UUID accountId);

    @EntityGraph(attributePaths = {"account", "account.roles"})
    @Query("""
            SELECT u
            FROM User u
            JOIN u.account a
            WHERE (:keyword IS NULL OR :keyword = '' OR
                  LOWER(CONCAT(COALESCE(u.firstName, ''), ' ', COALESCE(u.lastName, ''))) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                  LOWER(COALESCE(a.email, '')) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                  LOWER(COALESCE(u.phoneNumber, '')) LIKE LOWER(CONCAT('%', :keyword, '%')))
              AND (:active IS NULL OR a.active = :active)
              AND (:verified IS NULL OR a.verified = :verified)
              AND (:deleted IS NULL OR u.deleted = :deleted)
            """)
    Page<User> searchUsers(Pageable pageable,
                           @Param("keyword") String keyword,
                           @Param("active") Boolean active,
                           @Param("verified") Boolean verified,
                           @Param("deleted") Boolean deleted);

    @EntityGraph(attributePaths = {"account", "account.roles"})
    @Query("SELECT u FROM User u WHERE u.id = :id")
    Optional<User> findDetailById(@Param("id") UUID id);
}
