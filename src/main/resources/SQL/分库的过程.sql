/*
分库的过程  订单回传  直接提取数据的
返回说明
1 成功
2 异常 并携带异常错误码输出
3 此单子在该门店不存在
SELECT TOP 1 sheetno  FROM dbo.pos_jiesuan WHERE sheetno='0002'
exec p_commitDataProcToJieSuanAndPOS_SaleSheetDetail_z '0002','0002'
*/
IF EXISTS (SELECT * FROM DBO.SYSOBJECTS WHERE ID = OBJECT_ID(N'[dbo].[p_commitDataProcToJieSuanAndPOS_SaleSheetDetail_z]') and OBJECTPROPERTY(ID, N'IsProcedure') = 1)
BEGIN
	DROP PROCEDURE [dbo].p_commitDataProcToJieSuanAndPOS_SaleSheetDetail_z

END
GO
CREATE PROC [dbo].[p_commitDataProcToJieSuanAndPOS_SaleSheetDetail_z]
 @storeId varchar(16),
 @sheetno varchar(80)
 AS
BEGIN

  IF exists(SELECT TOP 1 sheetno  FROM dbo.pos_jiesuan WHERE sheetno=@sheetno)
  BEGIN
		SELECT 1 AS resultCode;
  END
  ELSE
  BEGIN

			BEGIN TRY
				BEGIN TRAN
							--插入结束表
							INSERT INTO  dbo.pos_jiesuan
							(   sheetno, jstype, mianzhi,zhekou,
									zhaoling, shishou,jstime, zdriqi,
									jiaozhang,shouyinyuanno,shouyinyuanmc, netjiecun,
									orientmoney,leftmoney, storevalue, detail,
									bPost, cWHno,cBanci_ID, iLineNo_Banci,
									cBanci)
							SELECT cSaleSheetno_time AS sheetno,'哆啦宝支付' AS jstype,
								SUM(fLastSettle) AS mianzhi,100 AS zhekou,0 AS zhaoling,SUM(fLastSettle) AS shishou,
										CONVERT(VARCHAR(100),GETDATE(),20) AS jstime,CONVERT(VARCHAR(100),GETDATE(),23) AS zdriqi,
										0 AS jiaozhang,cOperatorno AS shouyinyuanno,cOperatorName AS shouyinyuanmc,
										COUNT(cGoodsName) AS netjiecun,0,0,0,'哆啦宝支付',0,cStoreNo AS cWHno,(CONVERT(VARCHAR(100),GETDATE(),23)+'_1' ) AS cBanci_ID,1,'早班'
							FROM dbo.pos_SaleSheetDetailTemp
							WHERE cStoreNo=@storeId AND cSaleSheetno_time=@sheetno
							GROUP BY cSaleSheetno_time,cOperatorno,cOperatorName,cStoreNo

							--插入结算详情表
							INSERT INTO 	dbo.POS_SaleSheetDetail (
									cSaleSheetno, iSeed, cGoodsNo, cGoodsName, cBarCode,
									cOperatorno, cOperatorName, cVipCardno,bAuditing, bSettle,
									fVipScore, fPrice,fNormalSettle, bVipPrice, fVipPrice, bVipRate,
									fVipRate, fQuantity,fAgio, fLastSettle0, fLastSettle,
									dSaleDate, cSaleTime, dFinanceDate, cWorkerno,bPost, bChecked,
									cVipNo, bHidePrice,bHideQty, bWeight, fNormalVipScore,
									cWHno,  fVipScore_cur, cBanci_ID,iLineNo_Banci,  cBanci
							)
							SELECT cSaleSheetno_time AS cSaleSheetno, iSeed, cGoodsNo, cGoodsName, cBarCode,
									cOperatorno, cOperatorName,'' AS cVipCardno,bAuditing, bSettle,
									fVipScore, fPrice,fNormalSettle, bVipPrice, fVipPrice, bVipRate,
									fVipRate, fQuantity,fAgio, fLastSettle0, fLastSettle,
									dSaleDate, cSaleTime, dFinanceDate, 1 AS cWorkerno,bPost, bChecked,
									cVipNo,0 AS bHidePrice,0 AS bHideQty, bWeight, 0 AS fNormalVipScore,
									cStoreNo AS cWHno, 0 as  fVipScore_cur,(CONVERT(VARCHAR(100),GETDATE(),23)+'_1' ) AS cBanci_ID,
									1 AS iLineNo_Banci, '早班' AS cBanci FROM dbo.pos_SaleSheetDetailTemp
							WHERE cStoreNo=@storeId AND cSaleSheetno_time=@sheetno

							IF @@rowcount>0
							BEGIN
								SELECT 1 AS resultCode; --单子存在并插入结算表成功
							END
							ELSE
							BEGIN
							  SELECT 3 AS resultCode; --单子不存在
							END


				COMMIT TRAN
			END TRY

			--异常回滚
			BEGIN CATCH
				ROLLBACK TRAN
				SELECT 2 AS resultCode,@@ERROR AS errorCode;
				RETURN
			END CATCH

  END

END

GO

/**
 这是丰总写的过程
 */
IF EXISTS (SELECT * FROM DBO.SYSOBJECTS WHERE ID = OBJECT_ID(N'[dbo].[p_ProcessPosSheetDLB]') and OBJECTPROPERTY(ID, N'IsProcedure') = 1)
BEGIN
	DROP PROCEDURE [dbo].p_ProcessPosSheetDLB

END
GO
CREATE procedure [dbo].[p_ProcessPosSheetDLB]
@cStoreNo varchar(32),
@cPosID varchar(32),
@cSaleSheetNo varchar(32),
@cVipNo varchar(32),
@fVipRate money,
@bDiscount bit
/*
@cStoreNo 门店编号
@cPosID 款机编号
@cSaleSheetNo 临时单据号
@cVipNo 会员卡No
@fVipRate 会员打折率
@bDiscount 是否允许会员打折？
*/
as
begin
  update a
  set a.fVipScore=b.fVipScore
  from pos_SaleSheetDetailTemp a,pos_Goods b
  where a.cStoreNo=@cStoreNo and a.cPosID=@cPosID and a.cSaleSheetno_time=@cSaleSheetNo and a.cGoodsNo=b.cGoodsNo

  SET NOCOUNT ON
print 'hello!'
  declare @cVipInf varchar(64)
  set @cVipInf=''
    exec p_QryPloyOnSalesheet  @cStoreNo,@cPosID,@cSaleSheetNo
print '1001'



  declare Goods_cursor cursor
  for
  select cGoodsNo from pos_SaleSheetDetailTemp
  where cStoreNo=@cStoreNo and cPosID=@cPosID and cSaleSheetno_time=@cSaleSheetNo
  group by cGoodsNo

  declare @cGoodsNo varchar(32)

  open Goods_cursor
  fetch next from Goods_cursor
  into @cGoodsNo

  while @@fetch_status=0
  begin
    exec p_UpdateSaleDetail @cStoreNo,@cPosID,@cGoodsNo,@cSaleSheetNo
		fetch next from Goods_cursor
		into @cGoodsNo
  end


  close Goods_cursor
  deallocate Goods_cursor


      if @bDiscount=1
      begin
        set @cVipInf='1&'+@cVipNo
      end else
      begin
        set @cVipInf=@cVipNo
      end;

  if dbo.trim(@cVipNo)<>''
  begin
		exec p_VipPrice_Plan @cStoreNo,@cSaleSheetNo
    exec p_PerformVipPrice @cStoreNo,@cPosID,@cSaleSheetNo,0

    exec p_PerformDiscount @cStoreNo,@cPosID,@cSaleSheetNo,@cVipInf

    exec p_PerformVipRate @cStoreNo,@cPosID,@cSaleSheetNo,@fVipRate

  end else
  begin
    exec p_PerformDiscount @cStoreNo,@cPosID,@cSaleSheetNo,@cVipInf
  end

  update pos_SaleSheetDetailTemp
  set fLastSettle=round(fLastSettle0*fVipRate/100,2)-fAgio-isnull(fAgio_Diff,0),fRate_Exe=fVipRate
  where cStoreNo=@cStoreNo and cPosID=@cPosID and cSaleSheetno_time=@cSaleSheetNo
  and isnull(bUpdate,0)=0

  update a
  set a.fLastSettle=round(a.fLastSettle0*a.fVipRate/100,2)-a.fAgio-isnull(a.fAgio_Diff,0),a.fRate_Exe=a.fVipRate
  ,a.fPrice_Exe=round(a.fPrice_Exe,2)
  ,a.fPrice=round(a.fPrice,2)
  ,a.fVipPrice=round(a.fVipPrice,2)
  ,a.fVipScore=case when isnull(a.bWeight,0)=1 then (case when a.fVipScore_left<0 then 0 else a.fVipScore end ) else a.fVipScore end
  from pos_SaleSheetDetailTemp a ,Pos_Goods b

  where cStoreNo=@cStoreNo and cPosID=@cPosID and cSaleSheetno_time=@cSaleSheetNo and a.cGoodsNo=b.cGoodsNo

  update pos_SaleSheetDetailTemp
  set fLastSettle=
  case when isnull(bWeight,0)=0 then round((fPrice*(isnull(fDiscount,100)/100.00))*fQuantity,2)
   else round((fLastSettle0*(isnull(fDiscount,100)/100.00)),2) end-isnull(fAgio_Diff,0),
   fRate_Exe=isnull(fDiscount,100)

  where  cStoreNo=@cStoreNo and cPosID=@cPosID and cSaleSheetno_time=@cSaleSheetNo
  and isnull(bUpdate,0)=0  and fLastSettle>round((fPrice*(fDiscount/100.00))*fQuantity,2)

  update pos_SaleSheetDetailTemp

  set fLastSettle=case when isnull(fAgio_Diff,0)<=0
  then round((fLastSettle0*(isnull(fDiscount,100)/100.00)),2)
  else fLastSettle0-isnull(fAgio_Diff,0) end,
  fRate_Exe=isnull(fDiscount,100)
  where cStoreNo=@cStoreNo and cPosID=@cPosID and cSaleSheetno_time=@cSaleSheetNo

  update pos_SaleSheetDetailTemp
  set fMoneyValue=0
  where cStoreNo=@cStoreNo and cPosID=@cPosID and cSaleSheetno_time=@cSaleSheetNo
  and isnull(bUpdate,0)=1
  if dbo.trim(@cVipNo)<>''
  begin
    exec p_PerformVipPrice_Weight @cStoreNo,@cPosID,@cSaleSheetNo,0
  end

 --以下是zmy增加
	UPDATE E
	SET	E.discountAmount=case when floor((C.fNormalSettle-C.fLastSettle)*100)>0 then floor((C.fNormalSettle-C.fLastSettle)*100) else 0 end,
	E.amount=case when floor(C.fNormalSettle*100)>0 then  floor(C.fNormalSettle*100) else 0 end,
	E.price=floor(C.fPrice*100)
	FROM Posmanagement_main.dbo.tDlbGoodsInfo E,
			 (SELECT
				a.cStoreNo,
				b.cUnit,--单位
				b.cSpec,--规格
				a.cSaleSheetno_time,--临时单据号
				a.cGoodsNo,--商品编码
				a.cGoodsName,
				a.cBarcode,
				a.fQuantity,
				a.fPrice,--正价金额
				fNormalSettle=round(a.fPrice*a.fQuantity,2),--正常金额
				a.fVipPrice,--会员金额
				a.fLastSettle0,--折前金额
				a.fAgio,--分差金额
				a.fPrice_exe,
				a.fRate_Exe,--折扣率
				a.fVipRate,--会员打折率
				a.fDiscount,--折扣金额
				a.fLastSettle,--执行金额
				a.iSeed,
				bAuditing,--是否特价
				a.bHidePrice,--小票隐藏单价
				a.bHideQty,--小票隐藏数量
				a.bExchange,--是否超值换购
				a.bWeight--是否称重
			  FROM  pos_SaleSheetDetailTemp a left join pos_Goods b ON a.cGoodsNo=b.cGoodsNo
				WHERE a.cStoreNo=@cStoreNo and a.cPosID=@cPosID and a.cSaleSheetno_time=@cSaleSheetNo ) AS C
	WHERE  E.storeId=C.cStoreNo  AND E.merchantOrderId=C.cSaleSheetno_time AND E.lineId=C.iSeed

  --以上是zmy增加的

  select b.cUnit,--单位
  b.cSpec,--规格

  a.cSaleSheetno_time,--临时单据号
  a.cGoodsNo,--商品编码
  a.cGoodsName,
  a.cBarcode,
  a.fQuantity,
         a.fPrice,--正价金额
         fNormalSettle=round(a.fPrice*a.fQuantity,2),--正常金额
         a.fVipPrice,--会员金额
         a.fLastSettle0,--折前金额
         a.fAgio,--分差金额
   a.fPrice_exe,
  a.fRate_Exe,--折扣率
  a.fVipRate,--会员打折率
  a.fDiscount,--折扣金额
  a.fLastSettle,--执行金额
  a.iSeed,
  bAuditing,--是否特价
  a.bHidePrice,--小票隐藏单价
  a.bHideQty,--小票隐藏数量
  a.bExchange,--是否超值换购
  a.bWeight--是否称重
  from pos_SaleSheetDetailTemp a left join pos_Goods b on a.cGoodsNo=b.cGoodsNo
  where a.cStoreNo=@cStoreNo and a.cPosID=@cPosID and a.cSaleSheetno_time=@cSaleSheetNo
end
