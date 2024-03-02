package com.github.supercoding.respository.storeSales;

import com.github.supercoding.respository.Items.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

//실제 엔티티 , 아이템 엔티티의 타입을 적어줘야함
@Repository
public interface StoreSalesJpaRepository extends JpaRepository<StoreSales, Integer> {

    @Query("SELECT s FROM StoreSales s JOIN FETCH s.itemEntities")
    List<StoreSales> findAllFetchJoin();

}
