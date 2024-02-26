package com.github.supercoding.web.dto;

import com.github.supercoding.respository.Items.ItemEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;

@Getter
@NoArgsConstructor
@ToString
public class Item {
    private String id;
    private String name;
    private String type;
    private Integer price;
    private Spec spec;


    public void setSpec(Spec spec) {
        this.spec = spec;
    }

}
