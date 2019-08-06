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
                  merchantOrderId VARCHAR(64),--我们的订单号
                  payOrderId VARCHAR(64),      --小程序的支付单号（线下支付,微信支付,电子卡支付）
                  orderStatus int DEFAULT 0,   --订单状态  0 待支付订单 1 已经支付待出厂订单 2 已完成订单（经收银员检查订单无疑问）
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
                  amount MONEY,

                  createTime DATETIME DEFAULT (GETDATE())  --订单创建的时间
                  primary key(lineId)
              )