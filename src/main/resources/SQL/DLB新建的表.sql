--临时商品表
          IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = object_id(N'[dbo].[tDlbGoodsInfo]')
                      AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
          DROP TABLE [dbo].[tDlbGoodsInfo]
           CREATE TABLE tDlbGoodsInfo(
                  --用作购物车的标识
                  cartId VARCHAR(64),
                  cartFlowNo VARCHAR(64),
                  storeId VARCHAR(64),
                  cashierNo VARCHAR(64),
                  sn VARCHAR(64),
                  merchantOrderId VARCHAR(64),--我们的单号

                  --下面是商品的基础字段
                  lineId   BIGINT IDENTITY(1,1),  --行号
                  id VARCHAR(30),
                  name VARCHAR(100),
                  amount MONEY,
                  discountAmount MONEY,
                  discountName VARCHAR(60),
                  basePrice MONEY DEFAULT 0,
                  price MONEY DEFAULT 0,
                  qty INT DEFAULT 1,
                  weight MONEY DEFAULT 0,
                  isWeight bit DEFAULT 0,
                  barcode VARCHAR(30),
                  unit VARCHAR(20)

                  primary key(lineId)
              )

          --会员表
          IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = object_id(N'[dbo].[tDlbVip]')
                      AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
          DROP TABLE [dbo].[tDlbVip]
          CREATE TABLE tDlbVip(
                  --用作购物车的标识
                  cartId VARCHAR(64),
                  storeId VARCHAR(64),
                  sn VARCHAR(64),
                  --下面是商品的基础字段
                  userId VARCHAR(30), --会员ID
                  userType VARCHAR(100), --会员类型默认传ISV:商家线下会员JDpin:京东会员
                  cardNum VARCHAR(60), --会员卡号
                  score   VARCHAR(20), --会员当前积分
                  fPFrate VARCHAR(10), --我们线下
                  bDiscount VARCHAR(2), --我们线下
                  addScore Money default 0 --应该增加的积分
                  primary key(cartId)
          )

          --配置表
          IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = object_id(N'[dbo].[tDlbPosConfiguration]')
                      AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
          DROP TABLE [dbo].[tDlbPosConfiguration]
           CREATE TABLE tDlbPosConfiguration(
                  --用作购物车的标识
                  cartId VARCHAR(64),
                  storeId VARCHAR(64),
                  sn VARCHAR(64),
                  --下面是商品的基础字段
                  posName VARCHAR(64), --库名
                  posid  VARCHAR(20), --对应我们的posid  前台收银编号
                  primary key(cartId,posid)
          )

          --tDlbOrderSysnLog 记录回传的订单表
          IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = object_id(N'[dbo].[tDlbOrderSysnLog]')
                      AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
          DROP TABLE [dbo].[tDlbOrderSysnLog]
           CREATE TABLE tDlbOrderSysnLog(

                  lineId   BIGINT IDENTITY(1,1),  --行号
                  merchantOrderId VARCHAR(64),
                  payTypeId VARCHAR(100),
                  payNo VARCHAR(100),
                  payAmount Money,
                  cartFlowNo VARCHAR(64),
                  items VARCHAR(8000), --商品信息
                  storeId VARCHAR(64),  --门店号
                  sn VARCHAR(64),
                  cardNum VARCHAR(32),
                  cashierNo VARCHAR(64),
                  createTime DATETIME DEFAULT (GETDATE())  --接收的时间
                  primary key(lineId)
          )

          --会员积分日志表
        if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[otherVipUpLog]') and OBJECTPROPERTY(id, N'IsUserTable') = 1)
        drop table [dbo].[otherVipUpLog]
        CREATE TABLE otherVipUpLog(
        createTime   DATETIME DEFAULT (GETDATE()),
        appId VARCHAR (64) DEFAULT  '',
        machineId VARCHAR (100) DEFAULT '',
        vipNo      VARCHAR(64)  DEFAULT '',
        addMoney   MONEY DEFAULT 0.0,
        oddMoney   MONEY DEFAULT 0.0,
        newMoney   MONEY DEFAULT 0.0
        )