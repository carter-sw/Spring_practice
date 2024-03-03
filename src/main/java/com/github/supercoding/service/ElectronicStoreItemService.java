package com.github.supercoding.service;

import com.github.supercoding.respository.Items.ElectronicStoreItemJpaRepository;
import com.github.supercoding.respository.Items.ElectronicStoreItemRepository;
import com.github.supercoding.respository.Items.ItemEntity;
import com.github.supercoding.respository.storeSales.StoreSales;
import com.github.supercoding.respository.storeSales.StoreSalesJpaRepository;
import com.github.supercoding.respository.storeSales.StoreSalesRepository;
import com.github.supercoding.service.exceptions.NotFoundException;
import com.github.supercoding.service.mapper.ItemMapper;
import com.github.supercoding.web.dto.BuyOrder;
import com.github.supercoding.web.dto.Item;
import com.github.supercoding.web.dto.ItemBody;
import com.github.supercoding.web.dto.StoreInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ElectronicStoreItemService {

    private final ElectronicStoreItemJpaRepository electronicStoreItemJpaRepository;
    private final StoreSalesJpaRepository storeSalesJpaRepository;


    @Cacheable(value = "items",key="#root.methodName")
    public List<Item> findAllItem() {
        List<ItemEntity> itemEntities = electronicStoreItemJpaRepository.findAll();

        if(itemEntities.isEmpty()) throw new NotFoundException("아무 Items 들을 찾을 수 없습니다.");

        return itemEntities.stream().map(ItemMapper.INSTANCE::itemEntityToItem).collect(Collectors.toList());
    }

    @CacheEvict(value = "items",allEntries = true)
    public Integer saveItem(ItemBody itemBody) {
        ItemEntity itemEntity = new ItemEntity(null,itemBody.getName(),itemBody.getType()
                ,itemBody.getPrice(),itemBody.getSpec().getCpu(),itemBody.getSpec().getCapacity());

        ItemEntity itemEntityCreated;

        try{
            itemEntityCreated = electronicStoreItemJpaRepository.save(itemEntity);
        } catch (RuntimeException e){
            throw new NotFoundException("Item을 저장하는 도중에 Error가 발생 하였습니다.");
        }

        return itemEntityCreated.getId();
    }


    @Cacheable(value = "items",key="#id")
    public Item findItemById(String id){
        Integer idInt = Integer.parseInt(id);
        ItemEntity itemEntity = electronicStoreItemJpaRepository.findById(idInt).orElseThrow(()-> new NotFoundException("해당 ID:"+idInt+"의 item을 찾을수없습니다."));
        Item item = ItemMapper.INSTANCE.itemEntityToItem(itemEntity);
        return item;

    }

    @Cacheable(value = "items",key="#ids")
    public List<Item> findItemByIds(List<String> ids) {
        List<ItemEntity> itemEntities = electronicStoreItemJpaRepository.findAll();
       return itemEntities.stream()
                .map(ItemMapper.INSTANCE::itemEntityToItem)
                .filter((item -> ids.contains(item.getId())))
                .collect(Collectors.toList());
    }
    @CacheEvict(value = "items",allEntries = true)
    public void deleteItem(String id) {
        Integer idInt = Integer.parseInt(id);
       electronicStoreItemJpaRepository.deleteById(idInt);
    }

    @Transactional(transactionManager = "tmJpa1")
    @CacheEvict(value = "items",allEntries = true)
    public Item updateItem(String id, ItemBody itemBody) {
        Integer idInt = Integer.valueOf(id);

        ItemEntity itemEntityUpdated = electronicStoreItemJpaRepository.findById(idInt)
                .orElseThrow(()-> new NotFoundException("해당 ID:"+idInt+"의 item을 찾을수없습니다."));

        itemEntityUpdated.setItemBody(itemBody);

       return ItemMapper.INSTANCE.itemEntityToItem(itemEntityUpdated);
    }

    @Transactional (transactionManager = "tmJpa1")
    public Integer buyItems(BuyOrder buyOrder) {
        // 1. BuyOrder 에서 상품 ID와 수량을 얻어낸다.
        // 2. 상품을 조회하여 수량이 얼마나 있는 지 확인한다.
        // 3. 상품의 수량과 가격을 가지고 계산하여 총 가격을 구한다.
        // 4. 상품의 재고에 기존 계산한 재고를 뺀다.
        // 5. 상품 구매하는 수량 * 가격 만큼 가계 매상으로 올린다
        // (단, 재고가 아예 없거나 매장을 찾을 수 없으면 살 수 없다.)

        Integer itemId = buyOrder.getItemId();
        Integer itemNums = buyOrder.getItemNums();

        ItemEntity itemEntity = electronicStoreItemJpaRepository.findById(itemId).orElseThrow(()-> new NotFoundException("해당 이름의 item을 찾을수 없습니다."));

        if(itemEntity.getStoreSales() == null) throw new RuntimeException("매장을 찾을 수 없습니다.");
        if(itemEntity.getStock() <= 0) throw  new RuntimeException("매장의 재고가 없습니다.");

        Integer successBuyItemNums;
        if(itemNums >= itemEntity.getStock() ) successBuyItemNums = itemEntity.getStock();
        else successBuyItemNums = itemNums;

        Integer totalPrice = successBuyItemNums * itemEntity.getPrice();

        //Item 재고 감소
        itemEntity.setStock(itemEntity.getStock()-successBuyItemNums);

        if(successBuyItemNums ==4) {
            log.error("4개를 구매하는건 허락하지않습니다.");
            throw new RuntimeException("4개를 구매하는건 허락하지않습니다.");
        }

        // 매장 매상 추가
        StoreSales storeSales = itemEntity.getStoreSales()
                        .orElseThrow(()-> new NotFoundException("해당 이름의 storeId을 찾을수 없습니다."));

        storeSales.setAmount(storeSales.getAmount()+totalPrice);
        return successBuyItemNums;
    }

    public List<Item> findItemByTypes(List<String> types) {
        List<ItemEntity> itemEntities =electronicStoreItemJpaRepository.findItemEntitiesByTypeIn(types);
        return itemEntities.stream().map(ItemMapper.INSTANCE::itemEntityToItem).collect(Collectors.toList());
    }

    public List<Item> findItemsOrderByPrices(Integer maxValue) {
        List<ItemEntity> itemEntities =electronicStoreItemJpaRepository.findItemEntitiesByPriceLessThanEqualOrderByPriceAsc(maxValue);
        return itemEntities.stream().map(ItemMapper.INSTANCE::itemEntityToItem).collect(Collectors.toList());
    }

    public Page<Item> findAllWithPageable(Pageable pageable) {
        Page<ItemEntity> itemEntities = electronicStoreItemJpaRepository.findAll(pageable);
        return itemEntities.map(ItemMapper.INSTANCE::itemEntityToItem);
    }

    public Page<Item> findAllWithPageable(List<String> types, Pageable pageable) {
        Page<ItemEntity> itemEntities = electronicStoreItemJpaRepository.findAllByTypeIn(types,pageable);
        return itemEntities.map(ItemMapper.INSTANCE::itemEntityToItem);
    }

    @Transactional(transactionManager = "tmJpa1")
    public List<StoreInfo> findAllStoreInfo() {
        List<StoreSales> storeSales =storeSalesJpaRepository.findAll();
        List<StoreInfo> storeInfos = storeSales.stream().map(StoreInfo::new).collect(Collectors.toList());
        return storeInfos;
    }
}
