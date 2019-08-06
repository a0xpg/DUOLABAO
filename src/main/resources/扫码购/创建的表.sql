/*
     购物车  订单表
 */
IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = object_id(N'[dbo].[tSMGGoodsInfo]')
                      AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
          DROP TABLE [dbo].[tSMGGoodsInfo]
           CREATE TABLE tSMGGoodsInfo(
                  --下面是商品的基础字段
                  lineId   BIGINT IDENTITY(1,1),  --行号
                  openId VARCHAR(64),
                  unionId VARCHAR(64),
                  storeId VARCHAR(64),
                  merchantOrderId VARCHAR(64), --我们的订单号
                  payOrderId VARCHAR(64),       --小程序的支付单号（线下支付,微信支付,电子卡支付）
                  orderStatus int DEFAULT 0,    --订单状态  0 待支付订单 1 已经支付待出厂订单 2 已完成订单（经收银员检查订单无疑问）
                  orderType   int DEFAULT 0,    --结算方式 0 微信支付 1 前台支付 2 微信钱包支付
                  cGoodsNo VARCHAR(30),
                  cGoodsName VARCHAR(100),
                  amount MONEY,
                  discountAmount MONEY,
                  basePrice MONEY DEFAULT 0,
                  price MONEY DEFAULT 0,
                  qty INT DEFAULT 1,
                  weight MONEY DEFAULT 0,
                  isWeight bit DEFAULT 0,      --是否称重商品 0 非称重  1 称重
                  barcode VARCHAR(30),
                  unit VARCHAR(20),
                  createTime DATETIME DEFAULT (GETDATE()),  --订单创建的时间
                  payedTime DATETIME,                        --支付时间
                  checkUpNo VARCHAR(64) DEFAULT  '',       --记录核销人员编号
                  checkUpName VARCHAR(64) DEFAULT  ''      --记录核销人员名称
                  primary key(lineId)
              )
/*
     超市门店位置表（主要保存门店编号 门店名称 门店地理位置）
 */
IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = object_id(N'[dbo].[tSMGStoreLocation]')
                      AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
          DROP TABLE [dbo].[tSMGStoreLocation]
           CREATE TABLE tSMGStoreLocation(
                  --下面是商品的基础字段
                  lineId   BIGINT IDENTITY(1,1),  --行号
                  openId VARCHAR(64),
                  unionId VARCHAR(64),
                  province    VARCHAR(64),
                  city    VARCHAR(64),
                  location    VARCHAR(100),          --集体位置
                  storeId VARCHAR(64),
                  storeName VARCHAR(64),
                  longitude VARCHAR(64),         --经度
                  latitude VARCHAR(64),          --维度
                  createTime DATETIME DEFAULT (GETDATE()),
                  updateTime DATETIME DEFAULT (GETDATE())  --订单创建的时间
                  primary key(lineId)
              )

/*
     线上钱包表
 */
IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = object_id(N'[dbo].[tSMGMoneyOnline]')
                      AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
          DROP TABLE [dbo].[tSMGMoneyOnline]
           CREATE TABLE tSMGMoneyOnline(
                  --下面是商品的基础字段
                  lineId   BIGINT IDENTITY(1,1),  --行号
                  openId VARCHAR(64),
                  unionId VARCHAR(64),
                  storeId VARCHAR(64),
                  amount MONEY,                   --当前金额
                  malt   VARCHAR(64),             --随机生成的盐
                  moneySign VARCHAR(100)          --金额对比加密的字符串是否一致
                  createTime DATETIME DEFAULT (GETDATE())  --
                  primary key(lineId)
              )

/*
     线上钱包消费记录表
 */
IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = object_id(N'[dbo].[tSMGMoneySaleLog]')
                      AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
          DROP TABLE [dbo].[tSMGMoneySaleLog]
           CREATE TABLE tSMGMoneySaleLog(
                  --下面是商品的基础字段
                  lineId   BIGINT IDENTITY(1,1),  --行号
                  openId VARCHAR(64),
                  unionId VARCHAR(64),
                  storeId VARCHAR(64),
                  merchantOrderId VARCHAR(64),  --我们的订单号(消费)  如果是充值(充值的单号)
                  amount MONEY,                   --消费（或）的金额
                  saleStatus INT  DEFAULT 0,     --默认0 消费 1 充值
                  saleTime DATETIME DEFAULT (GETDATE())   --消费（或充值）的时间
                  primary key(lineId)
              )


/*
     微信支付订单表
 */
IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = object_id(N'[dbo].[tSMGMoneyOnline]')
                      AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
          DROP TABLE [dbo].[tSMGMoneyOnline]
           CREATE TABLE tSMGMoneyOnline(
                  --下面是商品的基础字段
                  lineId   BIGINT IDENTITY(1,1),  --行号
                  openId VARCHAR(64),
                  unionId VARCHAR(64),
                  storeId VARCHAR(64),
                  merchantOrderId VARCHAR(64),  --我们的订单号（结算）
                  payOrderId VARCHAR(64),        --哆啦宝支付单据
                  amount MONEY,                   --消费的金额
                  payStatus INT  DEFAULT 0,     --默认0 发起支付 1 支付成功
                  createTime DATETIME DEFAULT (GETDATE())
                  primary key(lineId)
              )