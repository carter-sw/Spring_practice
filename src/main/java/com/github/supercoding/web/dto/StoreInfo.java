package com.github.supercoding.web.dto;

import com.github.supercoding.respository.Items.ItemEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.github.supercoding.respository.storeSales.StoreSales;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class StoreInfo {
    private Integer id;
    private String storeName;
    private Integer amount;
    private List<String> itemNames;

    public StoreInfo(StoreSales stroreSales){
        this.id = stroreSales.getId();
        this.storeName = stroreSales.getStoreName();
        this.amount = stroreSales.getAmount();
        this.itemNames = stroreSales.getItemEntities().stream().map(ItemEntity::getName).collect(Collectors.toList());
    }

}
