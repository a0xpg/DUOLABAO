package com.hualong.duolabao.service.impl;

import com.hualong.duolabao.config.DlbConnfig;
import com.hualong.duolabao.conntroller.DlbConntroller;
import com.hualong.duolabao.dao.cluster.DlbDao;
import com.hualong.duolabao.dao.pos.PosMain;
import com.hualong.duolabao.domin.cStoreGoods;
import com.hualong.duolabao.service.PosService;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2019-07-15.
 */
@Service
public class PosServiceImpl implements PosService {

    private static final Logger log= LoggerFactory.getLogger(PosServiceImpl.class);

    @Autowired
    private DlbDao dlbDao;

    @Autowired
    private DlbConnfig dlbConnfig;

    @Autowired
    private PosMain posMain;


    //获取商品基本信息
    List<cStoreGoods> GetcStoreGoodsS(String cStoreNo, List<String> barcodeList){

        List<cStoreGoods> list=null;

        try {
            if(!this.dlbConnfig.getIsdandian()){
                //单店的走这里
                list=this.posMain.GetcStoreGoods(cStoreNo, barcodeList);
            }else {
                //连锁的走这里
                list=this.posMain.GetcStoreGoodsDanDian(cStoreNo, barcodeList);
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error(Thread.currentThread().getStackTrace()[1].getMethodName()+"获取本地商品数据出错了 {}",e.getMessage());
            return list;
        }

        return list;
    }

}
