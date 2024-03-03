package com.github.supercoding.web.controller;

import com.github.supercoding.service.ElectronicStoreItemService;
import com.github.supercoding.web.dto.BuyOrder;
import com.github.supercoding.web.dto.Item;
import com.github.supercoding.web.dto.ItemBody;
import com.github.supercoding.web.dto.StoreInfo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class ElectronicStroeController {

    private final ElectronicStoreItemService electronicStoreItemService;

    @ApiOperation(value = "모든 Items을 검색")
    @GetMapping("/items")
    public List<Item> findAllItem() {
        List<Item> items = electronicStoreItemService.findAllItem();
        return items;
    }

    @ApiOperation(value = "모든 Item 등록")
    @PostMapping("/items")
    public String registerItem(@RequestBody ItemBody itemBody) {
        Integer itemId = electronicStoreItemService.saveItem(itemBody);
        return "ID: " + itemId;
    }

    @ApiOperation(value = "모든 Item id로 검색")
    @GetMapping("/items/{id}")
    public Item findItemByPathId(
            @ApiParam(name = "id", value = "item ID", example = "1")
            @PathVariable String id) {
        return electronicStoreItemService.findItemById(id);
    }

    @ApiOperation(value = "모든 Items id로 검색 (쿼리문)")
    @GetMapping("/items-query")
    public Item findItemByQueryId(
            @ApiParam(name = "id", value = "item ID", example = "1")
            @RequestParam("id") String id) {
        return electronicStoreItemService.findItemById(id);

    }

    @ApiOperation(value = "모든 Item ids로 검색")
    @GetMapping("/items-queries")
    public List<Item> findItemByQueryIds(
            @ApiParam(name = "ids", value = "item IDs", example = "{1,2,3}")
            @RequestParam("id") List<String> ids) {
        List<Item> items = electronicStoreItemService.findItemByIds(ids);
        return items;
    }

    @ApiOperation(value = "모든 Item ids로 삭제")
    @DeleteMapping("/items/{id}")
    public String deleteItemByPathId(@PathVariable String id) {
        electronicStoreItemService.deleteItem(id);
        return "Object with id =" + id + "has been deleted";

    }

    @ApiOperation(value = "모든 Item ids 수정")
    @PutMapping("/items/{id}")
    public Item updateItem(@PathVariable String id, @RequestBody ItemBody itemBody) {
        return electronicStoreItemService.updateItem(id, itemBody);
    }

    @PostMapping("/items/buy")
    public String buyItem(@RequestBody BuyOrder buyOrder){
        Integer orderItemNums = electronicStoreItemService.buyItems(buyOrder);
        return "요청하신 Item 중 " + orderItemNums + "개를 구매 하였습니다.";

    }

    @ApiOperation(value = "모든 Item ids로 검색")
    @GetMapping("/items-types")
    public List<Item> findItemByTypes(
            @ApiParam(name = "ids", value = "item IDs", example = "{1,2,3}")
            @RequestParam("type") List<String> types) {
        List<Item> items = electronicStoreItemService.findItemByTypes(types);
        return items;
    }

    @ApiOperation(value = "모든 Items id로 검색 (쿼리문)")
    @GetMapping("/items-prices")
    public List<Item> findItemByPrices(
            @RequestParam("max") Integer maxValue) {
        return electronicStoreItemService.findItemsOrderByPrices(maxValue);

    }

    @ApiOperation("pagination 지원")
    @GetMapping("/items-page")
    public Page<Item> findItemsPagination(Pageable pageable){
        return electronicStoreItemService.findAllWithPageable(pageable);
    }

    @ApiOperation("pagination 지원 2")
    @GetMapping("/items-types-page")
    public Page<Item> findItemsPagination(@RequestParam("type")List<String> types, Pageable pageable){
        return electronicStoreItemService.findAllWithPageable(types,pageable);
    }

    @ApiOperation("전체 stores 정보 검색")
    @GetMapping("/stores")
    public List<StoreInfo> findAllStoreInfo(){
        return electronicStoreItemService.findAllStoreInfo();
    }
}
