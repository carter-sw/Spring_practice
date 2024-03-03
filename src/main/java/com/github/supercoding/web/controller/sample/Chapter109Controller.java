package com.github.supercoding.web.controller.sample;

import com.github.supercoding.service.ElectronicStoreItemService;
import com.github.supercoding.web.dto.Item;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/sample")
@RequiredArgsConstructor
@Slf4j
public class Chapter109Controller {

    private final ElectronicStoreItemService electronicStoreItemService;

    @ApiOperation(value = "모든 Items id로 검색 (쿼리문)")
    @GetMapping("/items-prices")
    public List<Item> findItemByPrices(
            HttpServletRequest httpServletRequest
//            @RequestParam("max") Integer maxValue
    ) {
        Integer maxValue = Integer.valueOf(httpServletRequest.getParameter("max"));
        return electronicStoreItemService.findItemsOrderByPrices(maxValue);

    }



//    @ApiOperation(value = "모든 Item ids로 삭제")
//    @DeleteMapping("/items/{id}")
//    public void deleteItemByPathId(
//            @ApiParam(name = "id",value = "item ID",example = "1") @PathVariable String id
//            HttpServletResponse httpServletResponse) throws IOException {
//        electronicStoreItemService.deleteItem(id);
//        String responseMessage = "Object with id = " +id +"has been deleted";
//        httpServletResponse.getOutputStream().println(responseMessage);
//    }
}
