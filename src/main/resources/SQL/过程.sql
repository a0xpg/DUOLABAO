--EXEC p_createBLBGoodsInfo ''
IF EXISTS (SELECT * FROM DBO.SYSOBJECTS WHERE ID = OBJECT_ID(N'[dbo].[p_createBLBGoodsInfo]') and OBJECTPROPERTY(ID, N'IsProcedure') = 1)
BEGIN
	DROP PROCEDURE [dbo].[p_createBLBGoodsInfo]
END
GO
    CREATE procedure [dbo].[p_createBLBGoodsInfo]
    @extend varchar(32) --没用 （后期可以随意扩展 json xml 均可）
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
                  receivingCode  VARCHAR(30) --接收到的原始码

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
END

GO

/*
	 才把数据插入到领导需要的表中
    exec p_Dataconversion_z '0002','0002','123','81','posstation101.dbo.pos_SaleSheetDetailTemp'
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
       cSaleTime,dFinanceDate,cVipNo,fPrice_exe,bSettle,bVipPrice,fVipRate,fNormalSettle )

        SELECT isWeight,storeId,'''+@cPosID+''',merchantOrderId,lineId,id,name,barcode,'''+@cPosID+''','''+@cPosID+''',bAuditing=0,
    (CASE  WHEN basePrice=0 THEN 0 ELSE basePrice/100  END) AS  fPrice,(CASE  WHEN basePrice=0 THEN 0 ELSE basePrice/100  END) AS  fVipPrice,fQuantity=(CASE  WHEN isWeight=0 THEN qty ELSE weight  END),
    (CASE  WHEN amount=0 THEN 0 ELSE amount/100  END) AS fLastSettle,(CASE  WHEN amount=0 THEN 0 ELSE amount/100  END) AS fLastSettle0,
    convert(varchar(10),getdate(),23), cSaleTime=convert(varchar(10),getdate(),108),convert(varchar(10),getdate(),23),'''+@cVipNo+''',
   (CASE  WHEN basePrice=0 THEN 0 ELSE basePrice/100  END) AS  fPrice_exe,bSettle=0,bVipPrice=0,100,(CASE  WHEN basePrice=0 THEN 0 ELSE basePrice/100  END) AS  fNormalSettle  FROM tDlbGoodsInfo WHERE cartId='''+@cartId+''' AND storeId='''+@storeId+''' '

  PRINT(@SQL)
  EXEC(@SQL)
END

GO

/*
	  把数据插入到分库的结算表
    exec p_commitDataProcToPOS_SaleSheetDetailAndJiesuan_z '@sheeetno','@storeId','abc'
*/
IF EXISTS (SELECT * FROM DBO.SYSOBJECTS WHERE ID = OBJECT_ID(N'[dbo].[p_commitDataProcToPOS_SaleSheetDetailAndJiesuan_z]') and OBJECTPROPERTY(ID, N'IsProcedure') = 1)
BEGIN
	DROP PROCEDURE [dbo].[p_commitDataProcToPOS_SaleSheetDetailAndJiesuan_z]

END
GO
CREATE PROC [dbo].[p_commitDataProcToPOS_SaleSheetDetailAndJiesuan_z]
 @sheeetno varchar(80),
 @storeId varchar(16),
 @tableName VARCHAR(100)
 AS
BEGIN

  DECLARE @SQLWHERE VARCHAR(800)

  SET  @SQLWHERE = 'EXEC '+@tableName+' '
                          +''''+@storeId+''','
                          +''''+@sheeetno+''''

  PRINT(@SQLWHERE)

 EXEC (@SQLWHERE)
END

GO

IF EXISTS (SELECT * FROM DBO.SYSOBJECTS WHERE ID = OBJECT_ID(N'[dbo].[p_getPos_SerialNoSheetNo_Z]') and OBJECTPROPERTY(ID, N'IsProcedure') = 1)
BEGIN
	DROP PROCEDURE [dbo].[p_getPos_SerialNoSheetNo_Z]
END
GO
CREATE procedure [dbo].[p_getPos_SerialNoSheetNo_Z]
@cStoreNo VARCHAR(32),
@cPosID VARCHAR(32),
@Zdriqi VARCHAR(32),
@callName VARCHAR(80)
AS
BEGIN
  DECLARE @SQLWHERE VARCHAR(8000)

  SET  @SQLWHERE = 'EXEC '+@callName+' '''+@cStoreNo+''','''
                          +@cPosID+''','''+@Zdriqi+''''

  PRINT(@SQLWHERE)

  EXEC (@SQLWHERE)
END

GO

IF EXISTS (SELECT * FROM DBO.SYSOBJECTS WHERE ID = OBJECT_ID(N'[dbo].[p_saveSheetNo_Z]') and OBJECTPROPERTY(ID, N'IsProcedure') = 1)
BEGIN
	DROP PROCEDURE [dbo].[p_saveSheetNo_Z]

END
GO
CREATE procedure [dbo].[p_saveSheetNo_Z]
@cStoreNo VARCHAR(32),
@cPosID VARCHAR(32),
@Zdriqi VARCHAR(32),
@SerNo VARCHAR(32),
@iSeed_Max VARCHAR(32),
@callName VARCHAR(80)
AS
BEGIN
  DECLARE @SQLWHERE VARCHAR(8000)

  SET  @SQLWHERE = 'EXEC '+@callName+' '''+@cStoreNo+''','''
                          +@cPosID+''','''+@Zdriqi+''','''
                          +@SerNo+''','+@iSeed_Max

  PRINT(@SQLWHERE)

  EXEC (@SQLWHERE)
END

GO

--动态调用过程获取数据的过程
--EXEC p_ProcessPosSheet_Z '0002','02','2019043012011993595','13628672210',100,0,'posstation101.dbo.p_ProcessPosSheet'
IF EXISTS (SELECT * FROM DBO.SYSOBJECTS WHERE ID = OBJECT_ID(N'[dbo].[p_ProcessPosSheet_Z]') and OBJECTPROPERTY(ID, N'IsProcedure') = 1)
BEGIN
	DROP PROCEDURE [dbo].[p_ProcessPosSheet_Z]

END
GO
CREATE procedure [dbo].[p_ProcessPosSheet_Z]
@cStoreNo varchar(32),
@cPosID varchar(32),
@cSaleSheetNo varchar(32),
@cVipNo varchar(32),
@fVipRate varchar(32),
@bDiscount varchar(32),
@callName VARCHAR(80)
AS
BEGIN
  DECLARE @SQLWHERE VARCHAR(800)

  SET  @SQLWHERE = 'EXEC '+@callName+' '''+@cStoreNo+''','''
                          +@cPosID+''','''+@cSaleSheetNo+''','''
                          +@cVipNo+''','''+@fVipRate+''','+@bDiscount

  PRINT(@SQLWHERE)

  EXEC (@SQLWHERE)
END

GO

--获取增加的积分的值
--EXEC p_getVipScoreAdd '2019043012011993595',100,'posstation006.dbo.p_CountVipScore_Online'
IF EXISTS (SELECT * FROM DBO.SYSOBJECTS WHERE ID = OBJECT_ID(N'[dbo].[p_getVipScoreAdd]') and OBJECTPROPERTY(ID, N'IsProcedure') = 1)
BEGIN
	DROP PROCEDURE [dbo].[p_getVipScoreAdd]

END
GO
CREATE procedure [dbo].[p_getVipScoreAdd]
@cSaleSheetNo varchar(32),
@fVipRate varchar(32),
@callName VARCHAR(80)
AS
BEGIN
  DECLARE @SQLWHERE VARCHAR(800)

  SET  @SQLWHERE = 'SELECT '+@callName+' ('
                          +''''+@cSaleSheetNo+''','
                          +@fVipRate+') AS vipAddScore '

  PRINT(@SQLWHERE)

 EXEC (@SQLWHERE)
END

GO

--积分增减的
IF EXISTS (SELECT * FROM DBO.SYSOBJECTS WHERE ID = OBJECT_ID(N'[dbo].[p_updateVipOther_z]') and OBJECTPROPERTY(ID, N'IsProcedure') = 1)
BEGIN
	DROP PROCEDURE [dbo].[p_updateVipOther_z]
END
GO
CREATE PROC [dbo].[p_updateVipOther_z]
 @appId varchar(64),
 @machineId varchar(64),
 @vipNo varchar(64),
 @addScore MONEY =0                --增加多少积分
 AS
BEGIN
  DECLARE @iFlag INT
  SET @iFlag = 0

  --
  DECLARE @fCurValue_Pos MONEY
  SET @fCurValue_Pos = (SELECT fCurValue_Pos FROM t_Vip WHERE cVipno=@vipNo)

  UPDATE t_vip SET fcurvalue=fcurvalue+@addScore,fCurValue_Pos=fCurValue_Pos+@addScore where cVipNo=@vipNo

  --判断影响函数
  SET @iFlag= @@rowcount

  PRINT(@iFlag)
  IF @iFlag > 0
  BEGIN
    INSERT INTO otherVipUpLog ( appId,machineId ,vipNo ,addMoney,oddMoney,newMoney) VALUES
     (@appId,@machineId,@vipNo,@addScore,@fCurValue_Pos,@fCurValue_Pos+@addScore)
  END


END

GO


