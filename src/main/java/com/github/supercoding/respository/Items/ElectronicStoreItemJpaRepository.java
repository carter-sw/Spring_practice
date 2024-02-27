package com.github.supercoding.respository.Items;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//실제 엔티티 , 아이템 엔티티의 타입을 적어줘야함
@Repository
public interface ElectronicStoreItemJpaRepository extends JpaRepository<ItemEntity,Integer> {
    List<ItemEntity> findItemEntitiesByTypeIn(List<String> types);

    List<ItemEntity> findItemEntitiesByPriceLessThanEqualOrderByPriceAsc(Integer maxValue);

    Page<ItemEntity> findAllByTypeIn(List<String> types, Pageable pageable);
}
