--临时商品表
if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[tDlbGoodsInfo]')
            and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [dbo].[tDlbGoodsInfo]
 CREATE TABLE tDlbGoodsInfo(
        --用作购物车的标识
        cartld VARCHAR(64),
        cartFlowNo VARCHAR(64),
        storeId VARCHAR(64),
        cashierNo VARCHAR(64),
        sn VARCHAR(64),

        --下面是商品的基础字段
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

        primary key(cartld,storeId)
    )

--临时会员表
if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[tDlbVip]')
            and OBJECTPROPERTY(id, N'IsUserTable') = 1)
drop table [dbo].[tDlbVip]
 CREATE TABLE tDlbVip(
        --用作购物车的标识
        cartld VARCHAR(64),
        storeId VARCHAR(64),
        sn VARCHAR(64),

        --下面是商品的基础字段
        userId VARCHAR(30), --会员ID
        userType VARCHAR(100), --会员类型默认传ISV:商家线下会员JDpin:京东会员
        cardNum VARCHAR(60), --会员卡号
        primary key(cartld,storeId)
)


