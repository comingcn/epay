-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: sh_epay
-- ------------------------------------------------------
-- Server version	5.5.56

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `cert_no`
--

DROP TABLE IF EXISTS `cert_no`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cert_no` (
  `id` varchar(32) NOT NULL COMMENT '身份证号主键',
  `updateTime` varchar(10) NOT NULL COMMENT '更新时间，如：2108-07-02；作为参与计算判断标识',
  `certNo` varchar(256) NOT NULL COMMENT '对应身份号，CERT_NO               VARCHAR(256),		身份证号',
  `cert_no` varchar(255) DEFAULT NULL,
  `update_time` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cert_no`
--

LOCK TABLES `cert_no` WRITE;
/*!40000 ALTER TABLE `cert_no` DISABLE KEYS */;
/*!40000 ALTER TABLE `cert_no` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `trade_detail`
--

DROP TABLE IF EXISTS `trade_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `trade_detail` (
  `txtSeqId` varchar(12) NOT NULL COMMENT '交易序列号',
  `certNo` varchar(256) DEFAULT NULL COMMENT '身份证号',
  `cardNo` varchar(45) DEFAULT NULL COMMENT '银行卡号',
  `merId` varchar(15) DEFAULT NULL COMMENT '商户ID	',
  `merType` int(1) DEFAULT NULL COMMENT '商户类型',
  `txtDate` varchar(8) DEFAULT NULL COMMENT '交易日期',
  `amout` decimal(15,2) DEFAULT NULL COMMENT '交易金额（单位元）',
  `returnCode` varchar(4) DEFAULT NULL COMMENT '交易状态码',
  PRIMARY KEY (`txtSeqId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `trade_detail`
--

LOCK TABLES `trade_detail` WRITE;
/*!40000 ALTER TABLE `trade_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `trade_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `variables`
--

DROP TABLE IF EXISTS `variables`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `variables` (
  `SQ001` int(3) NOT NULL COMMENT '近12个月申请认证的不同机构数',
  `SQ002` int(3) NOT NULL COMMENT '近12个月申请的不同贷款类机构数',
  `SQ003` int(3) NOT NULL COMMENT '近12个月申请的不同银行类机构数',
  `SQ004` int(3) NOT NULL COMMENT '近12个月用户用于申请认证的银行卡数',
  `SQ005` int(3) NOT NULL COMMENT '近12个月平均每张借记卡申请记录数',
  `SQ006` int(3) NOT NULL COMMENT '近12个月平均每张贷记卡申请记录数',
  `SQ007` int(3) NOT NULL COMMENT '近12个月平均每张卡在贷款类机构申请的记录数',
  `SQ008` int(3) NOT NULL COMMENT '近12个月平均每张卡在银行类机构申请的记录数',
  `SQ009` int(3) NOT NULL COMMENT '近6个月申请认证的不同机构数',
  `SQ010` int(3) NOT NULL COMMENT '近6个月申请认证的贷款类机构数',
  `SQ011` int(3) NOT NULL COMMENT '近6个月申请认证的银行类机构数',
  `SQ012` int(3) NOT NULL COMMENT '近6个月用户用于申请认证的银行卡数',
  `SQ013` int(3) NOT NULL COMMENT '近6个月平均每张借记卡申请记录数',
  `SQ014` int(3) NOT NULL COMMENT '近6个月平均每张贷记卡申请记录数',
  `SQ015` int(3) NOT NULL COMMENT '近6个月每张借记卡申请最小记录数',
  `SQ016` int(3) NOT NULL COMMENT '近6个月每张贷记卡申请最小记录数',
  `SQ017` int(3) NOT NULL COMMENT '近6个月平均每张卡在贷款类机构申请的记录数',
  `SQ018` int(3) NOT NULL COMMENT '近6个月平均每张卡在银行类机构申请的记录数',
  `SQ019` int(3) NOT NULL COMMENT '近3个月申请认证的不同机构数',
  `SQ020` int(3) NOT NULL COMMENT '近3个月申请的贷款类机构数',
  `SQ021` int(3) NOT NULL COMMENT '近3个月申请的银行类机构数',
  `SQ022` int(3) NOT NULL COMMENT '近3个月用户用于申请认证的银行卡数',
  `SQ023` int(3) NOT NULL COMMENT '近3个月平均每张借记卡申请记录数',
  `SQ024` int(3) NOT NULL COMMENT '近3个月平均每张贷记卡申请记录数',
  `SQ025` int(3) NOT NULL COMMENT '近3个月平均每张卡在贷款类机构申请的记录数',
  `SQ026` int(3) NOT NULL COMMENT '近3个月平均每张卡在银行类机构申请的记录数',
  `SQ027` int(3) NOT NULL COMMENT '近2个月申请认证的不同机构数',
  `SQ028` int(3) NOT NULL COMMENT '近2个月申请的贷款类机构数',
  `SQ029` int(3) NOT NULL COMMENT '近2个月申请的银行类机构数',
  `SQ030` int(3) NOT NULL COMMENT '近2个月申请的记录数',
  `SQ031` int(3) NOT NULL COMMENT '近1个月申请认证的不同机构数',
  `SQ032` int(3) NOT NULL COMMENT '近1个月申请的贷款类机构数',
  `SQ033` int(3) NOT NULL COMMENT '近1个月申请的银行类机构数',
  `SQ034` int(3) NOT NULL COMMENT '近1个月申请的记录数',
  `SQ035` int(3) NOT NULL COMMENT '近1个月用每张借记卡申请认证成功的平均记录数',
  `SQ036` int(3) NOT NULL COMMENT '近15天申请的不同机构数',
  `SQ037` int(3) NOT NULL COMMENT '近15天申请的贷款类机构数',
  `SQ038` int(3) NOT NULL COMMENT '近15天申请的银行类机构数',
  `SQ039` int(3) NOT NULL COMMENT '近15天申请的记录数',
  `SQ040` varchar(8) NOT NULL COMMENT '最近一次使用信用卡认证申请距今的时间',
  `SQ041` int(3) NOT NULL COMMENT '最近一次使用信用卡认证申请距今的天数',
  `SQ042` varchar(8) NOT NULL COMMENT '最早一次使用信用卡认证申请距今的时间',
  `SQ043` int(3) NOT NULL COMMENT '最早一次使用信用卡认证申请距今的天数',
  `SQ044` varchar(8) NOT NULL COMMENT '最近一次使用借记卡认证申请距离现在的时间',
  `SQ045` int(3) NOT NULL COMMENT '最近一次使用借记卡认证申请距离现在的天数',
  `SQ046` varchar(8) NOT NULL COMMENT '最早一次使用借记卡认证申请距离现在的时间',
  `SQ047` int(3) NOT NULL COMMENT '最早一次使用借记卡认证申请距离现在的天数',
  `HK001` int(3) NOT NULL COMMENT '近12个月成功还款的记录数',
  `HK002` varchar(32) NOT NULL COMMENT '近12个月成功还款的记录数占比',
  `HK003` int(3) NOT NULL COMMENT '近12个月成功还款的机构数',
  `HK004` int(3) NOT NULL COMMENT '近12个月成功还款的银行类机构数',
  `HK005` int(3) NOT NULL COMMENT '近12个月成功还款的消金类机构数',
  `HK006` int(3) NOT NULL COMMENT '近12个月成功还款的小贷类机构数',
  `HK007` int(3) NOT NULL COMMENT '近12个月余额不足的记录数',
  `HK008` varchar(32) NOT NULL COMMENT '近12个月失败还款记录数的占比',
  `HK009` varchar(32) NOT NULL COMMENT '近12个月余额不足的记录数占比',
  `HK010` varchar(32) NOT NULL COMMENT '近12个月余额不足的金额数占比',
  `HK011` int(3) NOT NULL COMMENT '近6个月成功还款的记录数',
  `HK012` int(3) NOT NULL COMMENT '近6个月在贷款类机构划扣成功的记录数',
  `HK013` varchar(32) NOT NULL COMMENT '近6个月成功还款的记录数占比',
  `HK014` int(3) NOT NULL COMMENT '近6个月成功还款的贷款类机构数',
  `HK015` int(3) NOT NULL COMMENT '近6个月成功还款的银行类机构数',
  `HK016` int(3) NOT NULL COMMENT '近6个月成功还款的消金类机构数',
  `HK017` int(3) NOT NULL COMMENT '近6个月成功还款的小贷类机构数',
  `HK018` int(3) NOT NULL COMMENT '近6个月余额不足的记录数',
  `HK019` varchar(32) NOT NULL COMMENT '近6个月划扣失败的记录数占比',
  `HK020` varchar(32) NOT NULL COMMENT '近6个月余额不足的记录数占比',
  `HK021` varchar(32) NOT NULL COMMENT '近6个月余额不足的金额数占比',
  `HK022` int(3) NOT NULL COMMENT '近3个月成功还款的记录数',
  `HK023` varchar(32) NOT NULL COMMENT '近3个月成功还款的记录数占比',
  `HK024` int(3) NOT NULL COMMENT '近3个月余额不足的记录数',
  `HK025` varchar(32) NOT NULL COMMENT '近3个月划扣失败的记录数占比',
  `HK026` int(3) NOT NULL COMMENT '近3个月成功还款的贷款类机构数',
  `HK027` int(3) NOT NULL COMMENT '近3个月成功还款的银行类机构数',
  `HK028` int(3) NOT NULL COMMENT '近3个月成功还款的消金类机构数',
  `HK029` int(3) NOT NULL COMMENT '近3个月成功还款的小贷类机构数',
  `HK030` varchar(32) NOT NULL COMMENT '近3个月余额不足的记录数占比',
  `HK031` varchar(32) NOT NULL COMMENT '近3个月余额不足的金额数占比',
  `HK032` varchar(32) NOT NULL COMMENT '近2个月代扣成功的记录数占比',
  `HK033` varchar(32) NOT NULL COMMENT '近2个月代扣成功的金额占比',
  `HK034` int(3) NOT NULL COMMENT '近1个月成功还款的记录数',
  `HK035` varchar(32) NOT NULL COMMENT '近1个月成功还款的记录数占比',
  `HK036` varchar(8) NOT NULL COMMENT '最早一次在贷款机构还款距今的时间',
  `HK037` int(3) NOT NULL COMMENT '最早一次在贷款机构还款距今的天数',
  `HK038` varchar(8) NOT NULL COMMENT '最近一次在贷款机构划扣距今的时间',
  `HK039` int(3) NOT NULL COMMENT '最近一次在贷款机构划扣距今的天数',
  `HK040` varchar(8) NOT NULL COMMENT '最近一次在贷款机构划扣成功距今的时间',
  `HK041` int(3) NOT NULL COMMENT '最近一次在贷款机构划扣成功距今的天数',
  `HK042` varchar(8) NOT NULL COMMENT '最近一次在银行机构划扣成功距今的时间',
  `HK043` int(3) NOT NULL COMMENT '最近一次在银行机构划扣成功距今的天数',
  `HK044` varchar(8) NOT NULL COMMENT '最近一次在银行机构划扣失败距今的时间',
  `HK045` int(3) NOT NULL COMMENT '最近一次在银行机构划扣失败距今的天数',
  `HK046` varchar(8) NOT NULL COMMENT '最近一次在消费金融机构划扣成功距今的时间',
  `HK047` int(3) NOT NULL COMMENT '最近一次在消费金融机构划扣失败距今的天数',
  `HK048` varchar(8) NOT NULL COMMENT '最近一次在小额贷款机构划扣成功距今的时间',
  `HK049` int(3) NOT NULL COMMENT '最近一次在小额贷款机构划扣失败距今的天数',
  `FK001` int(3) NOT NULL COMMENT '近12个月成功放款的记录数',
  `FK002` int(3) NOT NULL COMMENT '近12个月成功放款的不同机构数',
  `FK003` int(3) NOT NULL COMMENT '在贷款类机构放款成功的机构数',
  `FK004` decimal(15,2) NOT NULL COMMENT '近12个月在银行类机构放款的总金额',
  `FK005` decimal(15,2) NOT NULL COMMENT '近12个月在消费金融类机构放款的总金额',
  `FK006` decimal(15,2) NOT NULL COMMENT '近12个月在小额贷款类机构放款的总金额',
  `FK007` decimal(15,2) NOT NULL COMMENT '近12个月在贷款类机构放款的总金额',
  `FK008` int(3) NOT NULL COMMENT '近6个月成功放款的记录数',
  `FK009` int(3) NOT NULL COMMENT '近6个月成功放款的不同机构数',
  `FK010` decimal(15,2) NOT NULL COMMENT '近6个月在银行类机构放款的总金额',
  `FK011` decimal(15,2) NOT NULL COMMENT '近6个月在消费金融类机构放款的总金额',
  `FK012` decimal(15,2) NOT NULL COMMENT '近6个月在小贷类机构放款的总金额',
  `FK013` decimal(15,2) NOT NULL COMMENT '近6个月在贷款类机构放款的总金额',
  `FK014` varchar(8) NOT NULL COMMENT '最近一次在贷款机构放款的日期',
  `FK015` int(3) NOT NULL COMMENT '最近一次在贷款机构放款距今的天数',
  `FK016` varchar(8) NOT NULL COMMENT '最早一次在贷款机构放款时间',
  `FK017` int(3) NOT NULL COMMENT '最早一次在贷款机构放款距今的天数',
  `SX001` varchar(32) NOT NULL COMMENT '近12个月在贷款类机构授信的最大额度',
  `SX002` varchar(32) NOT NULL COMMENT '近12个月在银行类机构授信的最大额度',
  `SX003` varchar(32) NOT NULL COMMENT '近12个月在小额贷款类机构授信的最大额度',
  `SX004` varchar(32) NOT NULL COMMENT '近12个月在消费金融类机构授信的最大额度',
  `SX005` varchar(32) NOT NULL COMMENT '近6个月在贷款类机构授信的总额度',
  `SX006` varchar(32) NOT NULL COMMENT '近6个月在银行类机构授信的总额度',
  `SX007` varchar(32) NOT NULL COMMENT '近6个月在小额贷款类机构授信的最大额度',
  `SX008` varchar(32) NOT NULL COMMENT '近6个月在消费金融类机构授信的最大额度',
  `SX009` varchar(32) NOT NULL COMMENT '近3个月在贷款类机构授信的总额度',
  `SX010` varchar(32) NOT NULL COMMENT '近3个月在银行类机构授信的总额度',
  `SX011` varchar(32) NOT NULL COMMENT '近3个月在小额贷款类机构授信的最大额度',
  `SX012` varchar(32) NOT NULL COMMENT '近3个月在消费金融类机构授信的最大额度',
  `FX001` int(3) NOT NULL COMMENT '近12个月在小贷机构因账户原因划扣失败的次数',
  `FX002` int(3) NOT NULL COMMENT '近12个月在消费金融机构因账户原因划扣失败的次数',
  `FX003` int(3) NOT NULL COMMENT '近12个月在银行机构因账户原因划扣失败的次数',
  `FX004` int(3) NOT NULL COMMENT '近12个月在贷款类机构因账户原因划扣失败的次数',
  `FX005` int(3) NOT NULL COMMENT '近12个月因账户原因划扣失败的次数',
  `FX006` varchar(32) NOT NULL COMMENT '近12个月因账户原因划扣失败的记录数占比',
  `FX007` varchar(32) NOT NULL COMMENT '近12个月因账户原因划扣失败的金额占比',
  `FX008` int(3) NOT NULL COMMENT '近6个月在贷款类机构因账户原因划扣失败的次数',
  `FX009` int(3) NOT NULL COMMENT '近6个月在消费金融机构因账户原因划扣失败的次数',
  `FX010` int(3) NOT NULL COMMENT '近6个月在银行机构因账户原因划扣失败的次数',
  `FX011` int(3) NOT NULL COMMENT '近6个月在小贷机构因账户原因划扣失败的次数',
  `FX012` int(3) NOT NULL COMMENT '近6个月因账户原因划扣失败的次数',
  `FX013` varchar(32) NOT NULL COMMENT '近6个月因账户原因划扣失败的记录数占比',
  `FX014` varchar(32) NOT NULL COMMENT '近6个月因账户原因划扣失败的金额占比',
  `FX015` varchar(32) NOT NULL COMMENT '近6个月因超出限额划扣失败的金额占比',
  `FX016` varchar(32) NOT NULL COMMENT '近6个月因超出限额划扣失败的次数占比',
  `FX017` int(3) NOT NULL COMMENT '近3个月在贷款机构因账户原因划扣失败的次数',
  `FX018` int(3) NOT NULL COMMENT '近3个月在消费金融机构因账户原因划扣失败的次数',
  `FX019` int(3) NOT NULL COMMENT '近3个月在银行机构因账户原因划扣失败的次数',
  `FX020` int(3) NOT NULL COMMENT '近3个月在小贷类机构因账户原因划扣失败的次数',
  `FX021` int(3) NOT NULL COMMENT '近3个月因账户原因划扣失败的次数',
  `FX022` varchar(32) NOT NULL COMMENT '近3个月因账户原因划扣失败的次数占比',
  `FX023` varchar(32) NOT NULL COMMENT '近3个月因账户原因划扣失败的金额占比',
  `FX024` varchar(32) NOT NULL COMMENT '近3个月因超出限额划扣失败的金额占比',
  `FX025` varchar(32) NOT NULL COMMENT '近3个月因超出限额划扣失败的次数占比',
  `FX026` varchar(32) NOT NULL COMMENT '近7天余额不足的记录数占比',
  `FX027` int(3) NOT NULL COMMENT '近7天在消费金融行业余额不足的记录数',
  `FX028` int(3) NOT NULL COMMENT '近7天在贷款行业余额不足的记录数',
  `FX029` int(3) NOT NULL COMMENT '近7天在银行类机构余额不足的记录数',
  `FX030` int(3) NOT NULL COMMENT '近7天在小贷类机构余额不足的记录数',
  `FX031` varchar(32) NOT NULL COMMENT '近15天余额不足的记录数占比',
  `FX032` int(3) NOT NULL COMMENT '近15天在消费金融行业余额不足的记录数',
  `FX033` int(3) NOT NULL COMMENT '近15天在小贷行业余额不足的记录数',
  `FX034` int(3) NOT NULL COMMENT '近15天在银行类机构余额不足的记录数',
  `FX035` int(3) NOT NULL COMMENT '近15天在贷款类机构余额不足的记录数',
  `YQ001` int(3) NOT NULL COMMENT '近6个月在贷款类机构逾期1天以上次数',
  `YQ002` int(3) NOT NULL COMMENT '近6个月在消费金融机构逾期1天以上次数',
  `YQ003` int(3) NOT NULL COMMENT '近6个月在银行类机构逾期1天以上次数',
  `YQ004` int(3) NOT NULL COMMENT '近6个月在小贷款类机构逾期1天以上次数',
  `YQ005` int(3) NOT NULL COMMENT '近6个月逾期的平均每家机构逾期次数',
  `YQ006` int(3) NOT NULL COMMENT '近6个月在消费金融机构逾期的平均每家机构逾期次数',
  `YQ007` int(3) NOT NULL COMMENT '近6个月在银行类机构逾期的平均每家机构逾期次数',
  `YQ008` int(3) NOT NULL COMMENT '近6个月在小贷贷款类机构逾期的平均每家机构逾期次数',
  `YQ009` int(3) NOT NULL COMMENT '近6个月发生逾期的贷款类机构数',
  `YQ010` int(3) NOT NULL COMMENT '近6个月发生逾期的消费金融类机构数',
  `YQ011` int(3) NOT NULL COMMENT '近6个月发生逾期的银行类机构数',
  `YQ012` int(3) NOT NULL COMMENT '近6个月发生逾期的小贷贷款类机构数',
  `YQ013` int(3) NOT NULL COMMENT '近3个月在贷款类机构逾期1天以上次数',
  `YQ014` int(3) NOT NULL COMMENT '近3个月在消费金融机构逾期1天以上次数',
  `YQ015` int(3) NOT NULL COMMENT '近3个月在银行类机构逾期1天以上次数',
  `YQ016` int(3) NOT NULL COMMENT '近3个月在小贷贷款类机构逾期1天以上次数',
  `YQ017` int(3) NOT NULL COMMENT '近3个月发生逾期的贷款类机构数',
  `YQ018` int(3) NOT NULL COMMENT '近3个月发生逾期的消费金融类机构数',
  `YQ019` int(3) NOT NULL COMMENT '近3个月发生逾期的银行类机构数',
  `YQ020` int(3) NOT NULL COMMENT '近3个月发生逾期的小贷类机构数',
  `YQ021` int(3) NOT NULL COMMENT '近3个月逾期的平均每家机构逾期次数',
  `YQ022` int(3) NOT NULL COMMENT '近3个月在消费金融机构逾期的平均每家机构逾期次数',
  `YQ023` int(3) NOT NULL COMMENT '近3个月在银行类机构逾期的平均每家机构逾期次数',
  `YQ024` int(3) NOT NULL COMMENT '近3个月在小贷类机构逾期的平均每家机构逾期次数',
  `YQ025` int(3) NOT NULL COMMENT '近3个月在贷款类机构逾期的平均每家机构逾期次数',
  `YQ026` int(3) NOT NULL COMMENT '近6个月逾期1天以上天数总和',
  `YQ027` int(3) NOT NULL COMMENT '近3个月逾期1天以上天数总和',
  `YQ028` decimal(15,2) NOT NULL COMMENT '近6个月逾期30天以上金额总和',
  `YQ029` decimal(15,2) NOT NULL COMMENT '近6个月逾期7天以上金额总和',
  `YQ030` decimal(15,2) NOT NULL COMMENT '近6个月逾期1天以上金额总和',
  `YQ031` decimal(15,2) NOT NULL COMMENT '近3个月逾期30天以上金额总和',
  `YQ032` decimal(15,2) NOT NULL COMMENT '近3个月逾期7天以上金额总和',
  `YQ033` decimal(15,2) NOT NULL COMMENT '近3个月逾期1天以上金额总和',
  `YQ034` int(3) NOT NULL COMMENT '近12个月发生逾期的贷款类机构数',
  `YQ035` int(3) NOT NULL COMMENT '近12个月发生逾期的银行类机构数',
  `YQ036` int(3) NOT NULL COMMENT '近12个月发生逾期的消费金融类机构数',
  `YQ037` int(3) NOT NULL COMMENT '近12个月发生逾期的小贷贷款类机构数',
  `YQ038` int(3) NOT NULL COMMENT '近12个月在贷款类机构逾期1天以上的次数',
  `YQ039` int(3) NOT NULL COMMENT '近12个月在消费金融类机构逾期1天以上的次数',
  `YQ040` int(3) NOT NULL COMMENT '近12个月在银行类机构逾期1天以上的次数',
  `YQ041` int(3) NOT NULL COMMENT '近12个月在小额贷款行业逾期1天以上的次数',
  `YQ042` int(3) NOT NULL COMMENT '近12个月在消费金融机构逾期的最大每家机构逾期次数',
  `YQ043` int(3) NOT NULL COMMENT '近12个月在贷款机构逾期的最大每家机构逾期次数',
  `YQ044` int(3) NOT NULL COMMENT '近12个月在银行类机构逾期的最大每家机构逾期次数',
  `YQ045` int(3) NOT NULL COMMENT '近12个月在小贷类机构逾期的最大每家机构逾期次数'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `variables`
--

LOCK TABLES `variables` WRITE;
/*!40000 ALTER TABLE `variables` DISABLE KEYS */;
/*!40000 ALTER TABLE `variables` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-07-02 22:43:33
