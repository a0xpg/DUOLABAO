USE [posstation101]
GO
/****** Object:  StoredProcedure [dbo].[p_ProcessPosSheetDLB]    Script Date: 07/29/2019 09:54:43 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO



ALTER procedure [dbo].[p_ProcessPosSheetDLB]
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
	SET	E.discountAmount=(C.fLastSettle0-C.fLastSettle)*100,E.amount=C.fLastSettle0*100,E.price=C.fPrice*100
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