
  --tDlbOrderMoneyLog  支付记录表
          IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = object_id(N'[dbo].[tDlbOrderMoneyLog]')
                      AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
          DROP TABLE [dbo].[tDlbOrderMoneyLog]
           CREATE TABLE tDlbOrderMoneyLog(
                  lineId   BIGINT IDENTITY(1,1),  --行号
                  bizType VARCHAR(30),
                  orderId VARCHAR(64),
                  tradeNo VARCHAR(64),
                  tenant VARCHAR(20),
                  amount Money DEFAULT 0 ,
                  currency VARCHAR(10),
                  authcode VARCHAR(64),
                  orderIp VARCHAR(32),
                  createTime DATETIME DEFAULT (GETDATE()),  --接收的时间
                  paycomplited bit DEFAULT 0,               --是否支付成功
                  actualAmount Money DEFAULT 0,              --实际付款金额
                  isReturn int DEFAULT 0,                    --是否退款 0 没有 1 退款成功了
                  returnAmount Money DEFAULT 0,               --退款金额
                  updateKey int DEFAULT 0,                     --万能更改语句的中转站
                  storeId VARCHAR(32),
                  sn       VARCHAR(32)
                  primary key(lineId)
          )

          --tDlpPayConfig  支付配置表
          IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = object_id(N'[dbo].[tDlpPayConfig]')
                      AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
          DROP TABLE [dbo].[tDlpPayConfig]
           CREATE TABLE tDlpPayConfig(
                  lineId   BIGINT IDENTITY(1,1),  --行号
                  tenant VARCHAR(64),
                  tenantName VARCHAR(64),        --商户名称
                  accesskey VARCHAR(100),
                  secretkey VARCHAR(100),
                  agentnum VARCHAR(64),
                  customernum VARCHAR(64),
                  sn VARCHAR(64),                  --哆啦宝的机器号
                  machinenum VARCHAR(64),
                  shopnum VARCHAR(64),
                  storeId VARCHAR(64),
                  createTime DATETIME DEFAULT (GETDATE())
                  primary key(tenant,storeId)
          )



          CREATE TABLE tDlbOrderMoneyLog
          (
              lineId      INT AUTO_INCREMENT PRIMARY KEY NOT NULL ,
              bizType VARCHAR(30),
              orderId VARCHAR(64),
              tradeNo VARCHAR(64),
              tenant VARCHAR(20),
              amount FLOAT DEFAULT 0 ,
              currency VARCHAR(10),
              authcode VARCHAR(64),
              orderIp VARCHAR(32),
              createTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
              paycomplited BIT(1) DEFAULT 0,
              actualAmount FLOAT DEFAULT 0,
              isReturn INT DEFAULT 0,
              returnAmount FLOAT DEFAULT 0,
              updateKey INT DEFAULT 0,
              storeId VARCHAR(32),
              sn       VARCHAR(32)
          )ENGINE=INNODB  DEFAULT CHARSET=utf8;


          CREATE TABLE tDlpPayConfig
          (
              lineId      INT NOT NULL AUTO_INCREMENT PRIMARY KEY ,
              tenant VARCHAR(64),
              tenantName VARCHAR(64),
              accesskey VARCHAR(100),
              secretkey VARCHAR(100),
              agentnum VARCHAR(64),
              customernum VARCHAR(64),
              sn VARCHAR(64),
              machinenum VARCHAR(64),
              shopnum VARCHAR(64),
              storeId VARCHAR(64),
              createTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
          )ENGINE=INNODB  DEFAULT CHARSET=utf8;


