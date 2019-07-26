
-- 销售表
if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[tPublicSale_JingDong_z]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
-- 销售表
drop table [dbo].[tPublicSale_JingDong_z]

CREATE TABLE tPublicSale_JingDong_z(
cSheetNo    VARCHAR(30),                             --我们数据库中的单号
appSheetNo  VARCHAR(30),                             --第三方数据库中的单号
saleTime   DATETIME DEFAULT (GETDATE()),              --单据销售时间（结算时间）
createTime   DATETIME DEFAULT (GETDATE()),            --单据创建时间
saleType     INT,                                      --支付方式状态
saleTypeName    VARCHAR(64),                           --支付方式名称
saleAllMoney    MONEY DEFAULT 0.0,                    --支付总金额  （备注：总金额=实付金额+折扣金额）
actualMoney     MONEY DEFAULT 0.0,                   --实付金额   正数 代表卖货  负数  代表退货    金钱单位全部是元
discountMoney     MONEY DEFAULT 0.0,                 --折扣金额
appId         VARCHAR(30) ,                          --第三方id
cStoreNo      VARCHAR(20) DEFAULT '',               --门店编号
cWHno          VARCHAR(20) DEFAULT '',               --仓库编号
returnSale     int DEFAULT 1,                         --是否是退货   1 正常销售   2 代表退货
returnTime     DATETIME DEFAULT (GETDATE()),           --退货时间
returncSheetNo VARCHAR(30) DEFAULT '',                          --如果是退货  退货以前的单号
beizhu          VARCHAR(100)  DEFAULT '正常销售',      --备注  正常销售和退货
is_tiQue        int DEFAULT 0,                           -- 0 没有提取   1 已经提取
cVipNo VARCHAR(20),                                   --会员卡号
isAccount        int DEFAULT 0 ,                          -- 0未支付单据  1已支付单子
machineIp      VARCHAR(100) DEFAULT  ''
Primary Key (cSheetNo)
)

CREATE NONCLUSTERED INDEX tPublicSale_JingDong_z_Idx ON tPublicSale_JingDong_z(cSheetNo)  --创建索引

-- 销售表详情
if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[tPublicSaleDetail_JingDong_z]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
-- 销售表详情
drop table [dbo].[tPublicSaleDetail_JingDong_z]

CREATE TABLE tPublicSaleDetail_JingDong_z(
cSheetNo    VARCHAR(30),
createTime   DATETIME DEFAULT (GETDATE()),
appId         VARCHAR(30) DEFAULT '',               --第三方id
cStoreNo      VARCHAR(20) DEFAULT '',               --门店编号
cWHno          VARCHAR(20) DEFAULT '',              --仓库编号
bAuditing      INT ,                                  --是否折扣 1 不折扣  0 折扣
cGoodsNo       VARCHAR(30) DEFAULT '',
cGoodsName     Varchar(64) DEFAULT '',
cBarcode       VARCHAR(30) DEFAULT '',
fNormalPrice   Money,                                    --正常售价
discountPrice  Money,                                    --折扣售价
discountMoney  Money,                                    --此商品如果参与折扣  优惠的金额
iSeed          INT,                                       --序号
fQuantity      Money,                                      --数量  称重表示g  其他表示个
cVipNo VARCHAR(20),                                   --会员卡号
isAccount        int DEFAULT 0,                          -- 0未支付单据  1已支付单子
machineIp      VARCHAR(100) DEFAULT  '',                --机器id
bWeight        BIT  DEFAULT  0,                           --是否称重
cFreshBarcode  VARCHAR(30) DEFAULT  ''                  --原始生鲜码  没有解析的
)
CREATE NONCLUSTERED INDEX tPublicSaleDetail_JingDong_z_Idx ON tPublicSaleDetail_JingDong_z(cSheetNo)  --创建索引


-- 对应的无人售货机器表
if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[posstation]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
-- 对应的无人售货机器表
drop table [dbo].[posstation]

CREATE TABLE posstation(
	appId  VARCHAR(32)   NOT NULL, --第三方账户id
	posname varchar(64) NOT NULL,  --
	posid varchar(2) NOT NULL,     --对应的机器编号（华隆分配）
	cWHno varchar(32) DEFAULT '',	--仓库编号
	Pos_Day varchar(64) NULL,       --数据库表名称 Posmanagement_main.dbo.test
	cStoreNo varchar(32) DEFAULT '', --门店编号
	cMachineID varchar(64) NOT NULL,  --机器id
	cMachineKey varchar(32) NOT NULL,  -- key  -- MD5(warelucent+cMachineID).toUpper MD5加密后 转大写
	cStoreName  VARCHAR(64) NOT NULL,
	appName  VARCHAR(100) NOT NULL,
)

--京东的测试数据  不要乱动
INSERT INTO posstation (appId, posname, posid, cWHno, Pos_Day, cStoreNo, cMachineID, cMachineKey, cStoreName, appName)
				VALUES
('20190417111411', 'ZZ', '02', '000', 'posstation101', '0002', 'SEWUFFNAJDK', 'F8AC81814103EE437D653266D049A5BF', '门店0002', '京东无人售货')