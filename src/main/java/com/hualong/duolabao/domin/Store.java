package com.hualong.duolabao.domin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 *@author zmy
 */
@SuppressWarnings("serial")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain=true)
public class Store implements Serializable {

    private String cStore;
    private String cStoreNo;
    private String cStoreName;

    public static void main(String[] args) {
        Store store=new Store("5","7","h");
        store.setCStore("咋合格明阳");
        System.out.println(store.toString());
    }


}
