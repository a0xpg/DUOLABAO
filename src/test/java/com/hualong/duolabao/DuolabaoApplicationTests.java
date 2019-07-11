package com.hualong.duolabao;

import com.hualong.duolabao.config.DlbConnfig;
import com.hualong.duolabao.dao.cluster.DlbDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DuolabaoApplicationTests {

	private static final Logger log= LoggerFactory.getLogger(DuolabaoApplicationTests.class);

	@Autowired
	private DlbDao dlbDao;

	@Autowired
	private DlbConnfig dlbConnfig;
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
