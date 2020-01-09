  -- ALTER TABLE `tdlbordermoneylog` ADD istest INT DEFAULT 1  0 测试的 1 正式的数
  --tDlbOrderMoneyLog  支付记录表
          IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = object_id(N'[dbo].[tDlbOrderMoneyLog]')
                      AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
          DROP TABLE [dbo].[tDlbOrderMoneyLog]
           CREATE TABLE tDlbOrderMoneyLog(
                  lineId   BIGINT IDENTITY(1,1),  --行号
                  bizType VARCHAR(30),            --支付类型
                  orderId VARCHAR(64),            --单号
                  tradeNo VARCHAR(64),            --外部商户号
                  tenant VARCHAR(20),             --外部商户号名称
                  amount Money DEFAULT 0,         --下单金额
                  currency VARCHAR(10),
                  authcode VARCHAR(64),           --微信 支付宝等授权码
                  orderIp VARCHAR(32),            --下单的IP地址
                  createTime DATETIME DEFAULT (GETDATE()),    --接收的时间
                  paycomplited bit DEFAULT 0,                --是否支付成功 1 成功 0 不成功
                  actualAmount Money DEFAULT 0,              --实际付款金额
                  isReturn int DEFAULT 0,                    --是否退款 0 没有 1 退款成功了
                  returnAmount Money DEFAULT 0,               --退款金额
                  updateKey int DEFAULT 0,                     --万能更改语句的中转站
                  storeId VARCHAR(32),                       --门店号 （对应我们的数据库）
                  sn       VARCHAR(32)                        --设备号
                  primary key(lineId)
          )

          --tDlpPayConfig  支付配置表
          IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = object_id(N'[dbo].[tDlpPayConfig]')
                      AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
          DROP TABLE [dbo].[tDlpPayConfig]
           CREATE TABLE tDlpPayConfig(
                  lineId   BIGINT IDENTITY(1,1),  --行号
                  tenant VARCHAR(64),             --外部商户号
                  tenantName VARCHAR(64),        --商户名称
                  accesskey VARCHAR(100),        --哆啦宝支付公钥
                  secretkey VARCHAR(100),        --哆啦宝支付私钥
                  agentnum VARCHAR(64),          --哆啦宝代理商编号
                  customernum VARCHAR(64),       --哆啦宝代理商编号下面对应的外部商户号
                  sn VARCHAR(64),                  --机器号（设备）
                  machinenum VARCHAR(64),        --哆啦宝的机器号
                  shopnum VARCHAR(64),           --哆啦宝提供的门店号
                  storeId VARCHAR(64),           --华隆的门店号
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


