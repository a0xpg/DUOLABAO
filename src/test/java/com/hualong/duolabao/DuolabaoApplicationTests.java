package com.hualong.duolabao;

import com.alibaba.fastjson.JSON;
import com.hualong.duolabao.config.DlbConnfig;
import com.hualong.duolabao.dao.cluster.DlbDao;
import com.hualong.duolabao.dao.cluster.tDLBGoodsInfoMapper;
import com.hualong.duolabao.dao.pos.PosMain;
import com.hualong.duolabao.domin.BLBGoodsInfo;
import com.hualong.duolabao.domin.cStoreGoods;
import com.hualong.duolabao.domin.tDlbPosConfiguration;
import com.hualong.duolabao.exception.ApiSysException;
import com.hualong.duolabao.exception.ErrorEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DuolabaoApplicationTests {

	private static final Logger log= LoggerFactory.getLogger(DuolabaoApplicationTests.class);

	@Autowired
	private DlbDao dlbDao;

	@Autowired
	private DlbConnfig dlbConnfig;

	@Autowired
	private PosMain posMain;

	@Autowired
	private tDLBGoodsInfoMapper dlbGoodsInfoMapper;


	@Test
	public void getGoods() {
		List<String> stringList=new ArrayList<>();

		stringList.add("1010000001");
		List<cStoreGoods> list=null;
		try {
			if(!this.dlbConnfig.getIsdandian()){
				//单店的走这里
				list=this.posMain.GetcStoreGoods("0002", stringList);
			}else {
				//连锁的走这里
				list=this.posMain.GetcStoreGoodsDanDian("0002", stringList);
			}
			if(list!=null){
				log.info("获取到的结果是  {}", JSON.toJSON(list).toString());
			}else {
				log.info("结果集为空");
			}
		}catch (Exception e){
			e.printStackTrace();
			log.error(Thread.currentThread().getStackTrace()[1].getMethodName()+"获取本地商品数据出错了 {}",e.getMessage());
		}
	}

	@Test
	public void gettDlbPosConfiguration() {
		tDlbPosConfiguration tDlbPosConfiguration=posMain.GettDlbPosConfiguration("000281");
		log.info("获取到的结果是  {}", JSON.toJSON(tDlbPosConfiguration).toString());
	}

	@Test
	public void contextLoads() {
		Integer integer=dlbDao.Exec("delete");
		log.info("我是影响行数  {}",integer);
	}

	@Test
	public void getDlbConnfig() {
		log.info("得到哆啦宝的配置  {}",dlbConnfig.getMerchantno());
	}

}
