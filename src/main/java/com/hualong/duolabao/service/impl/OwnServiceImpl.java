package com.hualong.duolabao.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hualong.duolabao.config.DlbPayConnfig;
import com.hualong.duolabao.dao.cluster.tDlbSnConfigMapper;
import com.hualong.duolabao.domin.hldomin.tDlbSnConfig;
import com.hualong.duolabao.result.GlobalEumn;
import com.hualong.duolabao.result.ResultMsg;
import com.hualong.duolabao.service.OwnService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2019-11-18.
 */
@Service
public class OwnServiceImpl implements OwnService {

    private static final Logger log= LoggerFactory.getLogger(OwnServiceImpl.class);

    @Autowired
    private tDlbSnConfigMapper tDlbSnConfigMapper;

    @Override
    public String InsertSnConfig(String sn) {
        String result=JSON.toJSONString(new ResultMsg(
                false, GlobalEumn.SSCO001001.getCode(),GlobalEumn.SSCO001001.getMesssage(),null));;
        try{
            tDlbSnConfig tDlbSnConfig=tDlbSnConfigMapper.selectByPrimaryKey(sn);
            if(tDlbSnConfig!=null){
                result= JSON.toJSONString(tDlbSnConfig, SerializerFeature.WriteNullListAsEmpty,
                        SerializerFeature.WriteNullNumberAsZero,
                        SerializerFeature.WriteNullBooleanAsFalse,SerializerFeature.WriteMapNullValue);
                result=JSON.toJSONString(new ResultMsg(
                        true, GlobalEumn.SUCCESS.getCode(),GlobalEumn.SUCCESS.getMesssage(),(String)result));
            }else {
                if(tDlbSnConfigMapper.insertBySn(sn)>0){
                    result=JSON.toJSONString(new ResultMsg(
                            true, GlobalEumn.SUCCESS.getCode(),GlobalEumn.SUCCESS.getMesssage(),null));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("InsertSnConfig 出错了 {}",e.getMessage());
        }
        return result;
    }

    @Override
    public String SelectSnConfig(String sn) {
        String result=JSON.toJSONString(new ResultMsg(
                false, GlobalEumn.SSCO010003.getCode(),GlobalEumn.SSCO010003.getMesssage(),null));
        try{
            tDlbSnConfig tDlbSnConfig=tDlbSnConfigMapper.selectByPrimaryKey(sn);
            if(tDlbSnConfig!=null){
                result= JSON.toJSONString(tDlbSnConfig, SerializerFeature.WriteNullListAsEmpty,
                        SerializerFeature.WriteNullNumberAsZero,
                        SerializerFeature.WriteNullBooleanAsFalse,SerializerFeature.WriteMapNullValue);
                result=JSON.toJSONString(new ResultMsg(
                        true, GlobalEumn.SUCCESS.getCode(),GlobalEumn.SUCCESS.getMesssage(),(String)result));
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("SelectSnConfig 出错了 {}",e.getMessage());
        }
        return result;
    }
}
