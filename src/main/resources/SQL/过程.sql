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
END

GO

/*
	 才把数据插入到领导需要的表中
    exec p_Dataconversion_z '','','','','posstation101.dbo.pos_SaleSheetDetailTemp'
*/
IF EXISTS (SELECT * FROM DBO.SYSOBJECTS WHERE ID = OBJECT_ID(N'[dbo].[p_Dataconversion_z]') and OBJECTPROPERTY(ID, N'IsProcedure') = 1)
BEGIN
	DROP PROCEDURE [dbo].[p_Dataconversion_z]

END
GO
CREATE PROC [dbo].[p_Dataconversion_z]
 @cartId varchar(80),
 @storeId varchar(80),
 @cVipNo varchar(80),
 @cPosID VARCHAR(80),
 @tableName VARCHAR(80)
 AS
BEGIN

  DECLARE @SQL VARCHAR(8000)
  SET @SQL =  ' INSERT INTO '+@tableName+'
      (bWeight,cStoreNo,cPosID,cSaleSheetno_time,iSeed,cGoodsNo,cGoodsName,cBarcode,cOperatorno,cOperatorName,bAuditing,
       fPrice,fVipPrice,fQuantity,fLastSettle,fLastSettle0,dSaleDate,
       cSaleTime,dFinanceDate,cVipNo,fPrice_exe,bSettle,bVipPrice,fVipRate )

        SELECT isWeight,storeId,'''+@cPosID+''',merchantOrderId,lineId,id,name,barcode,sn,sn,bAuditing=0,
    basePrice,basePrice,fQuantity=(CASE  WHEN isWeight=0 THEN qty ELSE weight  END),
    (CASE  WHEN amount=0 THEN 0 ELSE amount/100  END) AS fLastSettle,(CASE  WHEN amount=0 THEN 0 ELSE amount/100  END) AS fLastSettle0,
    convert(varchar(10),getdate(),23),
    cSaleTime=convert(varchar(10),getdate(),108),convert(varchar(10),getdate(),23),'''+@cVipNo+''',
    basePrice,bSettle=0,bVipPrice=0,100  FROM tDlbGoodsInfo WHERE cartId='''+@cartId+''' AND storeId='''+@storeId+''' '

  PRINT(@SQL)
  EXEC(@SQL)
END

GO