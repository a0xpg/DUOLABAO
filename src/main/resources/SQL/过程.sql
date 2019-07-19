--EXEC p_createBLBGoodsInfo ''
IF EXISTS (SELECT * FROM DBO.SYSOBJECTS WHERE ID = OBJECT_ID(N'[dbo].[p_createBLBGoodsInfo]') and OBJECTPROPERTY(ID, N'IsProcedure') = 1)
BEGIN
	DROP PROCEDURE [dbo].[p_createBLBGoodsInfo]
END
GO
    CREATE procedure [dbo].[p_createBLBGoodsInfo]
    @extend varchar(32) --没用
    AS
    BEGIN
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
                  unit VARCHAR(20),

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
END