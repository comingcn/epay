package com.epay.xj.domain;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @package: com.epay.xj.domain 
 * @Title: Variables 指标向类
 * @Description: 
 * @author yanghf
 * @date 2018年7月2日 下午4:27:37
 */
//@Entity
//@Table(name="variables")//设置数据库中表名字
public class Variables {
	@Id
	private String md5CertNo;
	private String certNo;
	private int SQ001;	//12个月	机构数	近12个月申请认证的不同机构数
	private int SQ002;	//12个月	机构数	近12个月申请的不同贷款类机构数
	private int SQ003;	//12个月	机构数	近12个月申请的不同银行类机构数
	private int SQ004;	//12个月	银行卡数	近12个月用户用于申请认证的银行卡数
	private int SQ005;	//12个月	申请记录数	近12个月平均每张借记卡申请记录数
	private int SQ006;	//12个月	申请记录数	近12个月平均每张贷记卡申请记录数
	private int SQ007;	//12个月	申请记录数	近12个月平均每张卡在贷款类机构申请的记录数
	private int SQ008;	//12个月	申请记录数	近12个月平均每张卡在银行类机构申请的记录数
	private int SQ009;	//6个月	机构数	近6个月申请认证的不同机构数
	private int SQ010;	//6个月	机构数	近6个月申请认证的贷款类机构数
	private int SQ011;	//6个月	机构数	近6个月申请认证的银行类机构数
	private int SQ012;	//6个月	银行卡数	近6个月用户用于申请认证的银行卡数
	private int SQ013;	//6个月	申请记录数	近6个月平均每张借记卡申请记录数
	private int SQ014;	//6个月	申请记录数	近6个月平均每张贷记卡申请记录数
	private int SQ015;	//6个月	申请记录数	近6个月每张借记卡申请最小记录数
	private int SQ016;	//6个月	申请记录数	近6个月每张贷记卡申请最小记录数
	private int SQ017;	//6个月	申请记录数	近6个月平均每张卡在贷款类机构申请的记录数
	private int SQ018;	//6个月	申请记录数	近6个月平均每张卡在银行类机构申请的记录数
	private int SQ019;	//3个月	机构数	近3个月申请认证的不同机构数
	private int SQ020;	//3个月	机构数	近3个月申请的贷款类机构数
	private int SQ021;	//3个月	机构数	近3个月申请的银行类机构数
	private int SQ022;	//3个月	银行卡数	近3个月用户用于申请认证的银行卡数
	private int SQ023;	//3个月	申请记录数	近3个月平均每张借记卡申请记录数
	private int SQ024;	//3个月	申请记录数	近3个月平均每张贷记卡申请记录数
	private int SQ025;	//3个月	申请记录数	近3个月平均每张卡在贷款类机构申请的记录数
	private int SQ026;	//3个月	申请记录数	近3个月平均每张卡在银行类机构申请的记录数
	private int SQ027;	//2个月	机构数	近2个月申请认证的不同机构数
	private int SQ028;	//2个月	机构数	近2个月申请的贷款类机构数
	private int SQ029;	//2个月	机构数	近2个月申请的银行类机构数
	private int SQ030;	//2个月	申请记录数	近2个月申请的记录数
	private int SQ031;	//1个月	机构数	近1个月申请认证的不同机构数
	private int SQ032;	//1个月	机构数	近1个月申请的贷款类机构数
	private int SQ033;	//1个月	机构数	近1个月申请的银行类机构数
	private int SQ034;	//1个月	申请记录数	近1个月申请的记录数
	private int SQ035;	//1个月	成功平均记录数	近1个月用每张借记卡申请认证成功的平均记录数
	private int SQ036;	//15天	机构数	近15天申请的不同机构数
	private int SQ037;	//15天	机构数	近15天申请的贷款类机构数
	private int SQ038;	//15天	机构数	近15天申请的银行类机构数
	private int SQ039;	//15天	申请记录数	近15天申请的记录数
	private String SQ040;	//最近一次	时间指标	最近一次使用信用卡认证申请距今的时间
	private int SQ041;	//最近一次	时间指标	最近一次使用信用卡认证申请距今的天数
	private String SQ042;	//最早一次	时间指标	最早一次使用信用卡认证申请距今的时间
	private int SQ043;	//最早一次	时间指标	最早一次使用信用卡认证申请距今的天数
	private String SQ044;	//最近一次	时间指标	最近一次使用借记卡认证申请距离现在的时间
	private int SQ045;	//最近一次	时间指标	最近一次使用借记卡认证申请距离现在的天数
	private String SQ046;	//最早一次	时间指标	最早一次使用借记卡认证申请距离现在的时间
	private int SQ047;	//最早一次	时间指标	最早一次使用借记卡认证申请距离现在的天数
	private int HK001;	//12个月	成功次数	近12个月成功还款的记录数
	private String HK002;	//12个月	成功占比	近12个月成功还款的记录数占比
	private int HK003;	//12个月	机构数	近12个月成功还款的机构数
	private int HK004;	//12个月	机构数	近12个月成功还款的银行类机构数
	private int HK005;	//12个月	机构数	近12个月成功还款的消金类机构数
	private int HK006;	//12个月	机构数	近12个月成功还款的小贷类机构数
	private int HK007;	//12个月	失败次数	近12个月余额不足的记录数
	private String HK008;	//12个月	失败占比	近12个月失败还款记录数的占比
	private String HK009;	//12个月	失败占比	近12个月余额不足的记录数占比
	private String HK010;	//12个月	失败占比	近12个月余额不足的金额数占比
	private int HK011;	//6个月	成功次数	近6个月成功还款的记录数
	private int HK012;	//6个月	成功次数	近6个月在贷款类机构划扣成功的记录数
	private String HK013;	//6个月	成功占比	近6个月成功还款的记录数占比
	private int HK014;	//6个月	机构数	近6个月成功还款的贷款类机构数
	private int HK015;	//6个月	机构数	近6个月成功还款的银行类机构数
	private int HK016;	//6个月	机构数	近6个月成功还款的消金类机构数
	private int HK017;	//6个月	机构数	近6个月成功还款的小贷类机构数
	private int HK018;	//6个月	失败次数	近6个月余额不足的记录数
	private String HK019;	//6个月	失败占比	近6个月划扣失败的记录数占比
	private String HK020;	//6个月	失败占比	近6个月余额不足的记录数占比
	private String HK021;	//6个月	失败占比	近6个月余额不足的金额数占比
	private int HK022;	//3个月	成功次数	近3个月成功还款的记录数
	private String HK023;	//3个月	成功占比	近3个月成功还款的记录数占比
	private int HK024;	//3个月	失败次数	近3个月余额不足的记录数
	private String HK025;	//3个月	失败占比	近3个月划扣失败的记录数占比
	private int HK026;	//3个月	机构数	近3个月成功还款的贷款类机构数
	private int HK027;	//3个月	机构数	近3个月成功还款的银行类机构数
	private int HK028;	//3个月	机构数	近3个月成功还款的消金类机构数
	private int HK029;	//3个月	机构数	近3个月成功还款的小贷类机构数
	private String HK030;	//3个月	失败占比	近3个月余额不足的记录数占比
	private String HK031;	//3个月	失败占比	近3个月余额不足的金额数占比
	private String HK032;	//2个月	成功占比	近2个月代扣成功的记录数占比 
	private String HK033;	//2个月	成功占比	近2个月代扣成功的金额占比 
	private int HK034;	//1个月	成功次数	近1个月成功还款的记录数
	private String HK035;	//1个月	成功占比	近1个月成功还款的记录数占比
	private String HK036;	//最早一次	时间指标	最早一次在贷款机构还款距今的时间
	private int HK037;	//最早一次	时间指标	最早一次在贷款机构还款距今的天数
	private String HK038;	//最近一次	时间指标	最近一次在贷款机构划扣距今的时间
	private int HK039;	//最近一次	时间指标	最近一次在贷款机构划扣距今的天数
	private String HK040;	//最近一次	时间指标	最近一次在贷款机构划扣成功距今的时间
	private int HK041;	//最近一次	时间指标	最近一次在贷款机构划扣成功距今的天数
	private String HK042;	//最近一次	时间指标	最近一次在银行机构划扣成功距今的时间
	private int HK043;	//最近一次	时间指标	最近一次在银行机构划扣成功距今的天数
	private String HK044;	//最近一次	时间指标	最近一次在银行机构划扣失败距今的时间
	private int HK045;	//最近一次	时间指标	最近一次在银行机构划扣失败距今的天数
	private String HK046;	//最近一次	时间指标	最近一次在消费金融机构划扣成功距今的时间
	private int HK047;	//最近一次	时间指标	最近一次在消费金融机构划扣失败距今的天数
	private String HK048;	//最近一次	时间指标	最近一次在小额贷款机构划扣成功距今的时间
	private int HK049;	//最近一次	时间指标	最近一次在小额贷款机构划扣失败距今的天数
	private int FK001;	//12个月	成功次数	近12个月成功放款的记录数
	private int FK002;	//12个月	机构数	近12个月成功放款的不同机构数
	private int FK003;	//12个月	机构数	在贷款类机构放款成功的机构数
	private BigDecimal FK004;	//12个月	总金额	近12个月在银行类机构放款的总金额
	private BigDecimal FK005;	//12个月	总金额	近12个月在消费金融类机构放款的总金额
	private BigDecimal FK006;	//12个月	总金额	近12个月在小额贷款类机构放款的总金额
	private BigDecimal FK007;	//12个月	总金额	近12个月在贷款类机构放款的总金额
	private int FK008;	//6个月	成功次数	近6个月成功放款的记录数
	private int FK009;	//6个月	机构数	近6个月成功放款的不同机构数
	private int FK010;	//6个月	总金额	近6个月在银行类机构放款的总金额
	private BigDecimal FK011;	//6个月	总金额	近6个月在消费金融类机构放款的总金额
	private BigDecimal FK012;	//6个月	总金额	近6个月在小贷类机构放款的总金额
	private BigDecimal FK013;	//6个月	总金额	近6个月在贷款类机构放款的总金额
	private String FK014;	//最近一次	时间指标	最近一次在贷款机构放款的日期
	private String FK015;	//最近一次	时间指标	最近一次在贷款机构放款距今的天数
	private String FK016;	//最早一次	时间指标	最早一次在贷款机构放款时间
	private int FK017;	//最早一次	时间指标	最早一次在贷款机构放款距今的天数
	private BigDecimal SX001;	//12个月	最大额度	近12个月在贷款类机构授信的最大额度
	private BigDecimal SX002;	//12个月	最大额度	近12个月在银行类机构授信的最大额度
	private BigDecimal SX003;	//12个月	最大额度	近12个月在小额贷款类机构授信的最大额度
	private BigDecimal SX004;	//12个月	最大额度	近12个月在消费金融类机构授信的最大额度
	private BigDecimal SX005;	//6个月	总额度	近6个月在贷款类机构授信的总额度
	private BigDecimal SX006;	//6个月	总额度	近6个月在银行类机构授信的总额度
	private BigDecimal SX007;	//6个月	最大额度	近6个月在小额贷款类机构授信的最大额度
	private BigDecimal SX008;	//6个月	最大额度	近6个月在消费金融类机构授信的最大额度
	private BigDecimal SX009;	//3个月	总额度	近3个月在贷款类机构授信的总额度
	private BigDecimal SX010;	//3个月	总额度	近3个月在银行类机构授信的总额度
	private BigDecimal SX011;	//3个月	最大额度	近3个月在小额贷款类机构授信的最大额度
	private BigDecimal SX012;	//3个月	最大额度	近3个月在消费金融类机构授信的最大额度
	private int FX001;	//12个月	失败次数	近12个月在小贷机构因账户原因划扣失败的次数
	private int FX002;	//12个月	失败次数	近12个月在消费金融机构因账户原因划扣失败的次数
	private int FX003;	//12个月	失败次数	近12个月在银行机构因账户原因划扣失败的次数
	private int FX004;	//12个月	失败次数	近12个月在贷款类机构因账户原因划扣失败的次数
	private int FX005;	//12个月	失败次数	近12个月因账户原因划扣失败的次数
	private String FX006;	//12个月	失败占比	近12个月因账户原因划扣失败的记录数占比
	private String FX007;	//12个月	失败占比	近12个月因账户原因划扣失败的金额占比
	private int FX008;	//6个月	失败次数	近6个月在贷款类机构因账户原因划扣失败的次数
	private int FX009;	//6个月	失败次数	近6个月在消费金融机构因账户原因划扣失败的次数
	private int FX010;	//6个月	失败次数	近6个月在银行机构因账户原因划扣失败的次数
	private int FX011;	//6个月	失败次数	近6个月在小贷机构因账户原因划扣失败的次数
	private int FX012;	//6个月	失败次数	近6个月因账户原因划扣失败的次数
	private String FX013;	//6个月	失败占比	近6个月因账户原因划扣失败的记录数占比
	private String FX014;	//6个月	失败占比	近6个月因账户原因划扣失败的金额占比
	private String FX015;	//6个月	失败占比	近6个月因超出限额划扣失败的金额占比
	private String FX016;	//6个月	失败占比	近6个月因超出限额划扣失败的次数占比
	private int FX017;	//3个月	失败次数	近3个月在贷款机构因账户原因划扣失败的次数
	private int FX018;	//3个月	失败次数	近3个月在消费金融机构因账户原因划扣失败的次数
	private int FX019;	//3个月	失败次数	近3个月在银行机构因账户原因划扣失败的次数
	private int FX020;	//3个月	失败次数	近3个月在小贷类机构因账户原因划扣失败的次数
	private String FX021;	//3个月	失败次数	近3个月因账户原因划扣失败的次数
	private String FX022;	//3个月	失败占比	近3个月因账户原因划扣失败的次数占比
	private String FX023;	//3个月	失败占比	近3个月因账户原因划扣失败的金额占比
	private String FX024;	//3个月	失败占比	近3个月因超出限额划扣失败的金额占比
	private String FX025;	//3个月	失败占比	近3个月因超出限额划扣失败的次数占比
	private String FX026;	//近7天	失败占比	近7天余额不足的记录数占比
	private int FX027;	//近7天	失败次数	近7天在消费金融行业余额不足的记录数
	private int FX028;	//近7天	失败次数	近7天在贷款行业余额不足的记录数
	private int FX029;	//近7天	失败次数	近7天在银行类机构余额不足的记录数
	private int FX030;	//近7天	失败次数	近7天在小贷类机构余额不足的记录数
	private String FX031;	//近15天	失败占比	近15天余额不足的记录数占比
	private int FX032;	//近15天	失败次数	近15天在消费金融行业余额不足的记录数
	private int FX033;	//近15天	失败次数	近15天在小贷行业余额不足的记录数
	private int FX034;	//近15天	失败次数	近15天在银行类机构余额不足的记录数
	private int FX035;	//近15天	失败次数	近15天在贷款类机构余额不足的记录数
	private int YQ001;	//6个月	逾期一天以上	近6个月在贷款类机构逾期1天以上次数
	private int YQ002;	//6个月	逾期一天以上	近6个月在消费金融机构逾期1天以上次数
	private int YQ003;	//6个月	逾期一天以上	近6个月在银行类机构逾期1天以上次数
	private int YQ004;	//6个月	逾期一天以上	近6个月在小贷款类机构逾期1天以上次数
	private int YQ005;	//6个月	平均逾期次数	近6个月逾期的平均每家机构逾期次数
	private int YQ006;	//6个月	平均逾期次数	近6个月在消费金融机构逾期的平均每家机构逾期次数
	private int YQ007;	//6个月	平均逾期次数	近6个月在银行类机构逾期的平均每家机构逾期次数
	private int YQ008;	//6个月	平均逾期次数	近6个月在小贷贷款类机构逾期的平均每家机构逾期次数
	private int YQ009;	//6个月	逾期机构数	近6个月发生逾期的贷款类机构数
	private int YQ010;	//6个月	逾期机构数	近6个月发生逾期的消费金融类机构数
	private int YQ011;	//6个月	逾期机构数	近6个月发生逾期的银行类机构数
	private int YQ012;	//6个月	逾期机构数	近6个月发生逾期的小贷贷款类机构数
	private int YQ013;	//3个月	逾期一天以上次数	近3个月在贷款类机构逾期1天以上次数
	private int YQ014;	//3个月	逾期一天以上次数	近3个月在消费金融机构逾期1天以上次数
	private int YQ015;	//3个月	逾期一天以上次数	近3个月在银行类机构逾期1天以上次数
	private int YQ016;	//3个月	逾期一天以上次数	近3个月在小贷贷款类机构逾期1天以上次数
	private int YQ017;	//3个月	逾期机构数	近3个月发生逾期的贷款类机构数
	private int YQ018;	//3个月	逾期机构数	近3个月发生逾期的消费金融类机构数
	private int YQ019;	//3个月	逾期机构数	近3个月发生逾期的银行类机构数
	private int YQ020;	//3个月	逾期机构数	近3个月发生逾期的小贷类机构数
	private int YQ021;	//3个月	平均逾期次数	近3个月逾期的平均每家机构逾期次数
	private int YQ022;	//3个月	平均逾期次数	近3个月在消费金融机构逾期的平均每家机构逾期次数
	private int YQ023;	//3个月	平均逾期次数	近3个月在银行类机构逾期的平均每家机构逾期次数
	private int YQ024;	//3个月	平均逾期次数	近3个月在小贷类机构逾期的平均每家机构逾期次数
	private int YQ025;	//3个月	平均逾期次数	近3个月在贷款类机构逾期的平均每家机构逾期次数
	private int YQ026;	//6个月	逾期天数总和	近6个月逾期1天以上天数总和
	private int YQ027;	//3个月	逾期天数总和	近3个月逾期1天以上天数总和
	private BigDecimal YQ028;	//6个月	逾期金额总和	近6个月逾期30天以上金额总和
	private BigDecimal YQ029;	//6个月	逾期金额总和	近6个月逾期7天以上金额总和
	private BigDecimal YQ030;	//6个月	逾期金额总和	近6个月逾期1天以上金额总和
	private BigDecimal YQ031;	//3个月	逾期金额总和	近3个月逾期30天以上金额总和
	private BigDecimal YQ032;	//3个月	逾期金额总和	近3个月逾期7天以上金额总和
	private BigDecimal YQ033;	//3个月	逾期金额总和	近3个月逾期1天以上金额总和
	private int YQ034;	//12个月	逾期机构数	近12个月发生逾期的贷款类机构数
	private int YQ035;	//12个月	逾期机构数	近12个月发生逾期的银行类机构数
	private int YQ036;	//12个月	逾期机构数	近12个月发生逾期的消费金融类机构数
	private int YQ037;	//12个月	逾期机构数	近12个月发生逾期的小贷贷款类机构数
	private int YQ038;	//12个月	逾期1天以上	近12个月在贷款类机构逾期1天以上的次数
	private int YQ039;	//12个月	逾期1天以上	近12个月在消费金融类机构逾期1天以上的次数
	private int YQ040;	//12个月	逾期1天以上	近12个月在银行类机构逾期1天以上的次数
	private int YQ041;	//12个月	逾期1天以上	近12个月在小额贷款行业逾期1天以上的次数
	private int YQ042;	//12个月	最大逾期次数	近12个月在消费金融机构逾期的最大每家机构逾期次数
	private int YQ043;	//12个月	最大逾期次数	近12个月在贷款机构逾期的最大每家机构逾期次数
	private int YQ044;	//12个月	最大逾期次数	近12个月在银行类机构逾期的最大每家机构逾期次数
	private int YQ045;	//12个月	最大逾期次数	近12个月在小贷类机构逾期的最大每家机构逾期次数
	public String getMd5CertNo() {
		return md5CertNo;
	}
	public void setMd5CertNo(String md5CertNo) {
		this.md5CertNo = md5CertNo;
	}
	public String getCertNo() {
		return certNo;
	}
	public void setCertNo(String certNo) {
		this.certNo = certNo;
	}
	public int getSQ001() {
		return SQ001;
	}
	public void setSQ001(int sQ001) {
		SQ001 = sQ001;
	}
	public int getSQ002() {
		return SQ002;
	}
	public void setSQ002(int sQ002) {
		SQ002 = sQ002;
	}
	public int getSQ003() {
		return SQ003;
	}
	public void setSQ003(int sQ003) {
		SQ003 = sQ003;
	}
	public int getSQ004() {
		return SQ004;
	}
	public void setSQ004(int sQ004) {
		SQ004 = sQ004;
	}
	public int getSQ005() {
		return SQ005;
	}
	public void setSQ005(int sQ005) {
		SQ005 = sQ005;
	}
	public int getSQ006() {
		return SQ006;
	}
	public void setSQ006(int sQ006) {
		SQ006 = sQ006;
	}
	public int getSQ007() {
		return SQ007;
	}
	public void setSQ007(int sQ007) {
		SQ007 = sQ007;
	}
	public int getSQ008() {
		return SQ008;
	}
	public void setSQ008(int sQ008) {
		SQ008 = sQ008;
	}
	public int getSQ009() {
		return SQ009;
	}
	public void setSQ009(int sQ009) {
		SQ009 = sQ009;
	}
	public int getSQ010() {
		return SQ010;
	}
	public void setSQ010(int sQ010) {
		SQ010 = sQ010;
	}
	public int getSQ011() {
		return SQ011;
	}
	public void setSQ011(int sQ011) {
		SQ011 = sQ011;
	}
	public int getSQ012() {
		return SQ012;
	}
	public void setSQ012(int sQ012) {
		SQ012 = sQ012;
	}
	public int getSQ013() {
		return SQ013;
	}
	public void setSQ013(int sQ013) {
		SQ013 = sQ013;
	}
	public int getSQ014() {
		return SQ014;
	}
	public void setSQ014(int sQ014) {
		SQ014 = sQ014;
	}
	public int getSQ015() {
		return SQ015;
	}
	public void setSQ015(int sQ015) {
		SQ015 = sQ015;
	}
	public int getSQ016() {
		return SQ016;
	}
	public void setSQ016(int sQ016) {
		SQ016 = sQ016;
	}
	public int getSQ017() {
		return SQ017;
	}
	public void setSQ017(int sQ017) {
		SQ017 = sQ017;
	}
	public int getSQ018() {
		return SQ018;
	}
	public void setSQ018(int sQ018) {
		SQ018 = sQ018;
	}
	public int getSQ019() {
		return SQ019;
	}
	public void setSQ019(int sQ019) {
		SQ019 = sQ019;
	}
	public int getSQ020() {
		return SQ020;
	}
	public void setSQ020(int sQ020) {
		SQ020 = sQ020;
	}
	public int getSQ021() {
		return SQ021;
	}
	public void setSQ021(int sQ021) {
		SQ021 = sQ021;
	}
	public int getSQ022() {
		return SQ022;
	}
	public void setSQ022(int sQ022) {
		SQ022 = sQ022;
	}
	public int getSQ023() {
		return SQ023;
	}
	public void setSQ023(int sQ023) {
		SQ023 = sQ023;
	}
	public int getSQ024() {
		return SQ024;
	}
	public void setSQ024(int sQ024) {
		SQ024 = sQ024;
	}
	public int getSQ025() {
		return SQ025;
	}
	public void setSQ025(int sQ025) {
		SQ025 = sQ025;
	}
	public int getSQ026() {
		return SQ026;
	}
	public void setSQ026(int sQ026) {
		SQ026 = sQ026;
	}
	public int getSQ027() {
		return SQ027;
	}
	public void setSQ027(int sQ027) {
		SQ027 = sQ027;
	}
	public int getSQ028() {
		return SQ028;
	}
	public void setSQ028(int sQ028) {
		SQ028 = sQ028;
	}
	public int getSQ029() {
		return SQ029;
	}
	public void setSQ029(int sQ029) {
		SQ029 = sQ029;
	}
	public int getSQ030() {
		return SQ030;
	}
	public void setSQ030(int sQ030) {
		SQ030 = sQ030;
	}
	public int getSQ031() {
		return SQ031;
	}
	public void setSQ031(int sQ031) {
		SQ031 = sQ031;
	}
	public int getSQ032() {
		return SQ032;
	}
	public void setSQ032(int sQ032) {
		SQ032 = sQ032;
	}
	public int getSQ033() {
		return SQ033;
	}
	public void setSQ033(int sQ033) {
		SQ033 = sQ033;
	}
	public int getSQ034() {
		return SQ034;
	}
	public void setSQ034(int sQ034) {
		SQ034 = sQ034;
	}
	public int getSQ035() {
		return SQ035;
	}
	public void setSQ035(int sQ035) {
		SQ035 = sQ035;
	}
	public int getSQ036() {
		return SQ036;
	}
	public void setSQ036(int sQ036) {
		SQ036 = sQ036;
	}
	public int getSQ037() {
		return SQ037;
	}
	public void setSQ037(int sQ037) {
		SQ037 = sQ037;
	}
	public int getSQ038() {
		return SQ038;
	}
	public void setSQ038(int sQ038) {
		SQ038 = sQ038;
	}
	public int getSQ039() {
		return SQ039;
	}
	public void setSQ039(int sQ039) {
		SQ039 = sQ039;
	}
	public String getSQ040() {
		return SQ040;
	}
	public void setSQ040(String sQ040) {
		SQ040 = sQ040;
	}
	public int getSQ041() {
		return SQ041;
	}
	public void setSQ041(int sQ041) {
		SQ041 = sQ041;
	}
	public String getSQ042() {
		return SQ042;
	}
	public void setSQ042(String sQ042) {
		SQ042 = sQ042;
	}
	public int getSQ043() {
		return SQ043;
	}
	public void setSQ043(int sQ043) {
		SQ043 = sQ043;
	}
	public String getSQ044() {
		return SQ044;
	}
	public void setSQ044(String sQ044) {
		SQ044 = sQ044;
	}
	public int getSQ045() {
		return SQ045;
	}
	public void setSQ045(int sQ045) {
		SQ045 = sQ045;
	}
	public String getSQ046() {
		return SQ046;
	}
	public void setSQ046(String sQ046) {
		SQ046 = sQ046;
	}
	public int getSQ047() {
		return SQ047;
	}
	public void setSQ047(int sQ047) {
		SQ047 = sQ047;
	}
	public int getHK001() {
		return HK001;
	}
	public void setHK001(int hK001) {
		HK001 = hK001;
	}
	public String getHK002() {
		return HK002;
	}
	public void setHK002(String hK002) {
		HK002 = hK002;
	}
	public int getHK003() {
		return HK003;
	}
	public void setHK003(int hK003) {
		HK003 = hK003;
	}
	public int getHK004() {
		return HK004;
	}
	public void setHK004(int hK004) {
		HK004 = hK004;
	}
	public int getHK005() {
		return HK005;
	}
	public void setHK005(int hK005) {
		HK005 = hK005;
	}
	public int getHK006() {
		return HK006;
	}
	public void setHK006(int hK006) {
		HK006 = hK006;
	}
	public int getHK007() {
		return HK007;
	}
	public void setHK007(int hK007) {
		HK007 = hK007;
	}
	public String getHK008() {
		return HK008;
	}
	public void setHK008(String hK008) {
		HK008 = hK008;
	}
	public String getHK009() {
		return HK009;
	}
	public void setHK009(String hK009) {
		HK009 = hK009;
	}
	public String getHK010() {
		return HK010;
	}
	public void setHK010(String hK010) {
		HK010 = hK010;
	}
	public int getHK011() {
		return HK011;
	}
	public void setHK011(int hK011) {
		HK011 = hK011;
	}
	public int getHK012() {
		return HK012;
	}
	public void setHK012(int hK012) {
		HK012 = hK012;
	}
	public String getHK013() {
		return HK013;
	}
	public void setHK013(String hK013) {
		HK013 = hK013;
	}
	public int getHK014() {
		return HK014;
	}
	public void setHK014(int hK014) {
		HK014 = hK014;
	}
	public int getHK015() {
		return HK015;
	}
	public void setHK015(int hK015) {
		HK015 = hK015;
	}
	public int getHK016() {
		return HK016;
	}
	public void setHK016(int hK016) {
		HK016 = hK016;
	}
	public int getHK017() {
		return HK017;
	}
	public void setHK017(int hK017) {
		HK017 = hK017;
	}
	public int getHK018() {
		return HK018;
	}
	public void setHK018(int hK018) {
		HK018 = hK018;
	}
	public String getHK019() {
		return HK019;
	}
	public void setHK019(String hK019) {
		HK019 = hK019;
	}
	public String getHK020() {
		return HK020;
	}
	public void setHK020(String hK020) {
		HK020 = hK020;
	}
	public String getHK021() {
		return HK021;
	}
	public void setHK021(String hK021) {
		HK021 = hK021;
	}
	public int getHK022() {
		return HK022;
	}
	public void setHK022(int hK022) {
		HK022 = hK022;
	}
	public String getHK023() {
		return HK023;
	}
	public void setHK023(String hK023) {
		HK023 = hK023;
	}
	public int getHK024() {
		return HK024;
	}
	public void setHK024(int hK024) {
		HK024 = hK024;
	}
	public String getHK025() {
		return HK025;
	}
	public void setHK025(String hK025) {
		HK025 = hK025;
	}
	public int getHK026() {
		return HK026;
	}
	public void setHK026(int hK026) {
		HK026 = hK026;
	}
	public int getHK027() {
		return HK027;
	}
	public void setHK027(int hK027) {
		HK027 = hK027;
	}
	public int getHK028() {
		return HK028;
	}
	public void setHK028(int hK028) {
		HK028 = hK028;
	}
	public int getHK029() {
		return HK029;
	}
	public void setHK029(int hK029) {
		HK029 = hK029;
	}
	public String getHK030() {
		return HK030;
	}
	public void setHK030(String hK030) {
		HK030 = hK030;
	}
	public String getHK031() {
		return HK031;
	}
	public void setHK031(String hK031) {
		HK031 = hK031;
	}
	public String getHK032() {
		return HK032;
	}
	public void setHK032(String hK032) {
		HK032 = hK032;
	}
	public String getHK033() {
		return HK033;
	}
	public void setHK033(String hK033) {
		HK033 = hK033;
	}
	public int getHK034() {
		return HK034;
	}
	public void setHK034(int hK034) {
		HK034 = hK034;
	}
	public String getHK035() {
		return HK035;
	}
	public void setHK035(String hK035) {
		HK035 = hK035;
	}
	public String getHK036() {
		return HK036;
	}
	public void setHK036(String hK036) {
		HK036 = hK036;
	}
	public int getHK037() {
		return HK037;
	}
	public void setHK037(int hK037) {
		HK037 = hK037;
	}
	public String getHK038() {
		return HK038;
	}
	public void setHK038(String hK038) {
		HK038 = hK038;
	}
	public int getHK039() {
		return HK039;
	}
	public void setHK039(int hK039) {
		HK039 = hK039;
	}
	public String getHK040() {
		return HK040;
	}
	public void setHK040(String hK040) {
		HK040 = hK040;
	}
	public int getHK041() {
		return HK041;
	}
	public void setHK041(int hK041) {
		HK041 = hK041;
	}
	public String getHK042() {
		return HK042;
	}
	public void setHK042(String hK042) {
		HK042 = hK042;
	}
	public int getHK043() {
		return HK043;
	}
	public void setHK043(int hK043) {
		HK043 = hK043;
	}
	public String getHK044() {
		return HK044;
	}
	public void setHK044(String hK044) {
		HK044 = hK044;
	}
	public int getHK045() {
		return HK045;
	}
	public void setHK045(int hK045) {
		HK045 = hK045;
	}
	public String getHK046() {
		return HK046;
	}
	public void setHK046(String hK046) {
		HK046 = hK046;
	}
	public int getHK047() {
		return HK047;
	}
	public void setHK047(int hK047) {
		HK047 = hK047;
	}
	public String getHK048() {
		return HK048;
	}
	public void setHK048(String hK048) {
		HK048 = hK048;
	}
	public int getHK049() {
		return HK049;
	}
	public void setHK049(int hK049) {
		HK049 = hK049;
	}
	public int getFK001() {
		return FK001;
	}
	public void setFK001(int fK001) {
		FK001 = fK001;
	}
	public int getFK002() {
		return FK002;
	}
	public void setFK002(int fK002) {
		FK002 = fK002;
	}
	public int getFK003() {
		return FK003;
	}
	public void setFK003(int fK003) {
		FK003 = fK003;
	}
	public BigDecimal getFK004() {
		return FK004;
	}
	public void setFK004(BigDecimal fK004) {
		FK004 = fK004;
	}
	public BigDecimal getFK005() {
		return FK005;
	}
	public void setFK005(BigDecimal fK005) {
		FK005 = fK005;
	}
	public BigDecimal getFK006() {
		return FK006;
	}
	public void setFK006(BigDecimal fK006) {
		FK006 = fK006;
	}
	public BigDecimal getFK007() {
		return FK007;
	}
	public void setFK007(BigDecimal fK007) {
		FK007 = fK007;
	}
	public int getFK008() {
		return FK008;
	}
	public void setFK008(int fK008) {
		FK008 = fK008;
	}
	public int getFK009() {
		return FK009;
	}
	public void setFK009(int fK009) {
		FK009 = fK009;
	}
	public int getFK010() {
		return FK010;
	}
	public void setFK010(int fK010) {
		FK010 = fK010;
	}
	public BigDecimal getFK011() {
		return FK011;
	}
	public void setFK011(BigDecimal fK011) {
		FK011 = fK011;
	}
	public BigDecimal getFK012() {
		return FK012;
	}
	public void setFK012(BigDecimal fK012) {
		FK012 = fK012;
	}
	public BigDecimal getFK013() {
		return FK013;
	}
	public void setFK013(BigDecimal fK013) {
		FK013 = fK013;
	}
	public String getFK014() {
		return FK014;
	}
	public void setFK014(String fK014) {
		FK014 = fK014;
	}
	public String getFK015() {
		return FK015;
	}
	public void setFK015(String fK015) {
		FK015 = fK015;
	}
	public String getFK016() {
		return FK016;
	}
	public void setFK016(String fK016) {
		FK016 = fK016;
	}
	public int getFK017() {
		return FK017;
	}
	public void setFK017(int fK017) {
		FK017 = fK017;
	}
	public BigDecimal getSX001() {
		return SX001;
	}
	public void setSX001(BigDecimal sX001) {
		SX001 = sX001;
	}
	public BigDecimal getSX002() {
		return SX002;
	}
	public void setSX002(BigDecimal sX002) {
		SX002 = sX002;
	}
	public BigDecimal getSX003() {
		return SX003;
	}
	public void setSX003(BigDecimal sX003) {
		SX003 = sX003;
	}
	public BigDecimal getSX004() {
		return SX004;
	}
	public void setSX004(BigDecimal sX004) {
		SX004 = sX004;
	}
	public BigDecimal getSX005() {
		return SX005;
	}
	public void setSX005(BigDecimal sX005) {
		SX005 = sX005;
	}
	public BigDecimal getSX006() {
		return SX006;
	}
	public void setSX006(BigDecimal sX006) {
		SX006 = sX006;
	}
	public BigDecimal getSX007() {
		return SX007;
	}
	public void setSX007(BigDecimal sX007) {
		SX007 = sX007;
	}
	public BigDecimal getSX008() {
		return SX008;
	}
	public void setSX008(BigDecimal sX008) {
		SX008 = sX008;
	}
	public BigDecimal getSX009() {
		return SX009;
	}
	public void setSX009(BigDecimal sX009) {
		SX009 = sX009;
	}
	public BigDecimal getSX010() {
		return SX010;
	}
	public void setSX010(BigDecimal sX010) {
		SX010 = sX010;
	}
	public BigDecimal getSX011() {
		return SX011;
	}
	public void setSX011(BigDecimal sX011) {
		SX011 = sX011;
	}
	public BigDecimal getSX012() {
		return SX012;
	}
	public void setSX012(BigDecimal sX012) {
		SX012 = sX012;
	}
	public int getFX001() {
		return FX001;
	}
	public void setFX001(int fX001) {
		FX001 = fX001;
	}
	public int getFX002() {
		return FX002;
	}
	public void setFX002(int fX002) {
		FX002 = fX002;
	}
	public int getFX003() {
		return FX003;
	}
	public void setFX003(int fX003) {
		FX003 = fX003;
	}
	public int getFX004() {
		return FX004;
	}
	public void setFX004(int fX004) {
		FX004 = fX004;
	}
	public int getFX005() {
		return FX005;
	}
	public void setFX005(int fX005) {
		FX005 = fX005;
	}
	public String getFX006() {
		return FX006;
	}
	public void setFX006(String fX006) {
		FX006 = fX006;
	}
	public String getFX007() {
		return FX007;
	}
	public void setFX007(String fX007) {
		FX007 = fX007;
	}
	public int getFX008() {
		return FX008;
	}
	public void setFX008(int fX008) {
		FX008 = fX008;
	}
	public int getFX009() {
		return FX009;
	}
	public void setFX009(int fX009) {
		FX009 = fX009;
	}
	public int getFX010() {
		return FX010;
	}
	public void setFX010(int fX010) {
		FX010 = fX010;
	}
	public int getFX011() {
		return FX011;
	}
	public void setFX011(int fX011) {
		FX011 = fX011;
	}
	public int getFX012() {
		return FX012;
	}
	public void setFX012(int fX012) {
		FX012 = fX012;
	}
	public String getFX013() {
		return FX013;
	}
	public void setFX013(String fX013) {
		FX013 = fX013;
	}
	public String getFX014() {
		return FX014;
	}
	public void setFX014(String fX014) {
		FX014 = fX014;
	}
	public String getFX015() {
		return FX015;
	}
	public void setFX015(String fX015) {
		FX015 = fX015;
	}
	public String getFX016() {
		return FX016;
	}
	public void setFX016(String fX016) {
		FX016 = fX016;
	}
	public int getFX017() {
		return FX017;
	}
	public void setFX017(int fX017) {
		FX017 = fX017;
	}
	public int getFX018() {
		return FX018;
	}
	public void setFX018(int fX018) {
		FX018 = fX018;
	}
	public int getFX019() {
		return FX019;
	}
	public void setFX019(int fX019) {
		FX019 = fX019;
	}
	public int getFX020() {
		return FX020;
	}
	public void setFX020(int fX020) {
		FX020 = fX020;
	}
	public String getFX021() {
		return FX021;
	}
	public void setFX021(String fX021) {
		FX021 = fX021;
	}
	public String getFX022() {
		return FX022;
	}
	public void setFX022(String fX022) {
		FX022 = fX022;
	}
	public String getFX023() {
		return FX023;
	}
	public void setFX023(String fX023) {
		FX023 = fX023;
	}
	public String getFX024() {
		return FX024;
	}
	public void setFX024(String fX024) {
		FX024 = fX024;
	}
	public String getFX025() {
		return FX025;
	}
	public void setFX025(String fX025) {
		FX025 = fX025;
	}
	public String getFX026() {
		return FX026;
	}
	public void setFX026(String fX026) {
		FX026 = fX026;
	}
	public int getFX027() {
		return FX027;
	}
	public void setFX027(int fX027) {
		FX027 = fX027;
	}
	public int getFX028() {
		return FX028;
	}
	public void setFX028(int fX028) {
		FX028 = fX028;
	}
	public int getFX029() {
		return FX029;
	}
	public void setFX029(int fX029) {
		FX029 = fX029;
	}
	public int getFX030() {
		return FX030;
	}
	public void setFX030(int fX030) {
		FX030 = fX030;
	}
	public String getFX031() {
		return FX031;
	}
	public void setFX031(String fX031) {
		FX031 = fX031;
	}
	public int getFX032() {
		return FX032;
	}
	public void setFX032(int fX032) {
		FX032 = fX032;
	}
	public int getFX033() {
		return FX033;
	}
	public void setFX033(int fX033) {
		FX033 = fX033;
	}
	public int getFX034() {
		return FX034;
	}
	public void setFX034(int fX034) {
		FX034 = fX034;
	}
	public int getFX035() {
		return FX035;
	}
	public void setFX035(int fX035) {
		FX035 = fX035;
	}
	public int getYQ001() {
		return YQ001;
	}
	public void setYQ001(int yQ001) {
		YQ001 = yQ001;
	}
	public int getYQ002() {
		return YQ002;
	}
	public void setYQ002(int yQ002) {
		YQ002 = yQ002;
	}
	public int getYQ003() {
		return YQ003;
	}
	public void setYQ003(int yQ003) {
		YQ003 = yQ003;
	}
	public int getYQ004() {
		return YQ004;
	}
	public void setYQ004(int yQ004) {
		YQ004 = yQ004;
	}
	public int getYQ005() {
		return YQ005;
	}
	public void setYQ005(int yQ005) {
		YQ005 = yQ005;
	}
	public int getYQ006() {
		return YQ006;
	}
	public void setYQ006(int yQ006) {
		YQ006 = yQ006;
	}
	public int getYQ007() {
		return YQ007;
	}
	public void setYQ007(int yQ007) {
		YQ007 = yQ007;
	}
	public int getYQ008() {
		return YQ008;
	}
	public void setYQ008(int yQ008) {
		YQ008 = yQ008;
	}
	public int getYQ009() {
		return YQ009;
	}
	public void setYQ009(int yQ009) {
		YQ009 = yQ009;
	}
	public int getYQ010() {
		return YQ010;
	}
	public void setYQ010(int yQ010) {
		YQ010 = yQ010;
	}
	public int getYQ011() {
		return YQ011;
	}
	public void setYQ011(int yQ011) {
		YQ011 = yQ011;
	}
	public int getYQ012() {
		return YQ012;
	}
	public void setYQ012(int yQ012) {
		YQ012 = yQ012;
	}
	public int getYQ013() {
		return YQ013;
	}
	public void setYQ013(int yQ013) {
		YQ013 = yQ013;
	}
	public int getYQ014() {
		return YQ014;
	}
	public void setYQ014(int yQ014) {
		YQ014 = yQ014;
	}
	public int getYQ015() {
		return YQ015;
	}
	public void setYQ015(int yQ015) {
		YQ015 = yQ015;
	}
	public int getYQ016() {
		return YQ016;
	}
	public void setYQ016(int yQ016) {
		YQ016 = yQ016;
	}
	public int getYQ017() {
		return YQ017;
	}
	public void setYQ017(int yQ017) {
		YQ017 = yQ017;
	}
	public int getYQ018() {
		return YQ018;
	}
	public void setYQ018(int yQ018) {
		YQ018 = yQ018;
	}
	public int getYQ019() {
		return YQ019;
	}
	public void setYQ019(int yQ019) {
		YQ019 = yQ019;
	}
	public int getYQ020() {
		return YQ020;
	}
	public void setYQ020(int yQ020) {
		YQ020 = yQ020;
	}
	public int getYQ021() {
		return YQ021;
	}
	public void setYQ021(int yQ021) {
		YQ021 = yQ021;
	}
	public int getYQ022() {
		return YQ022;
	}
	public void setYQ022(int yQ022) {
		YQ022 = yQ022;
	}
	public int getYQ023() {
		return YQ023;
	}
	public void setYQ023(int yQ023) {
		YQ023 = yQ023;
	}
	public int getYQ024() {
		return YQ024;
	}
	public void setYQ024(int yQ024) {
		YQ024 = yQ024;
	}
	public int getYQ025() {
		return YQ025;
	}
	public void setYQ025(int yQ025) {
		YQ025 = yQ025;
	}
	public int getYQ026() {
		return YQ026;
	}
	public void setYQ026(int yQ026) {
		YQ026 = yQ026;
	}
	public int getYQ027() {
		return YQ027;
	}
	public void setYQ027(int yQ027) {
		YQ027 = yQ027;
	}
	public BigDecimal getYQ028() {
		return YQ028;
	}
	public void setYQ028(BigDecimal yQ028) {
		YQ028 = yQ028;
	}
	public BigDecimal getYQ029() {
		return YQ029;
	}
	public void setYQ029(BigDecimal yQ029) {
		YQ029 = yQ029;
	}
	public BigDecimal getYQ030() {
		return YQ030;
	}
	public void setYQ030(BigDecimal yQ030) {
		YQ030 = yQ030;
	}
	public BigDecimal getYQ031() {
		return YQ031;
	}
	public void setYQ031(BigDecimal yQ031) {
		YQ031 = yQ031;
	}
	public BigDecimal getYQ032() {
		return YQ032;
	}
	public void setYQ032(BigDecimal yQ032) {
		YQ032 = yQ032;
	}
	public BigDecimal getYQ033() {
		return YQ033;
	}
	public void setYQ033(BigDecimal yQ033) {
		YQ033 = yQ033;
	}
	public int getYQ034() {
		return YQ034;
	}
	public void setYQ034(int yQ034) {
		YQ034 = yQ034;
	}
	public int getYQ035() {
		return YQ035;
	}
	public void setYQ035(int yQ035) {
		YQ035 = yQ035;
	}
	public int getYQ036() {
		return YQ036;
	}
	public void setYQ036(int yQ036) {
		YQ036 = yQ036;
	}
	public int getYQ037() {
		return YQ037;
	}
	public void setYQ037(int yQ037) {
		YQ037 = yQ037;
	}
	public int getYQ038() {
		return YQ038;
	}
	public void setYQ038(int yQ038) {
		YQ038 = yQ038;
	}
	public int getYQ039() {
		return YQ039;
	}
	public void setYQ039(int yQ039) {
		YQ039 = yQ039;
	}
	public int getYQ040() {
		return YQ040;
	}
	public void setYQ040(int yQ040) {
		YQ040 = yQ040;
	}
	public int getYQ041() {
		return YQ041;
	}
	public void setYQ041(int yQ041) {
		YQ041 = yQ041;
	}
	public int getYQ042() {
		return YQ042;
	}
	public void setYQ042(int yQ042) {
		YQ042 = yQ042;
	}
	public int getYQ043() {
		return YQ043;
	}
	public void setYQ043(int yQ043) {
		YQ043 = yQ043;
	}
	public int getYQ044() {
		return YQ044;
	}
	public void setYQ044(int yQ044) {
		YQ044 = yQ044;
	}
	public int getYQ045() {
		return YQ045;
	}
	public void setYQ045(int yQ045) {
		YQ045 = yQ045;
	}
	
	
	@Override
	public String toString() {
		return "Variables [md5CertNo=" + md5CertNo + ", certNo=" + certNo + ", SQ001=" + SQ001 + ", SQ002=" + SQ002
				+ ", SQ003=" + SQ003 + ", SQ004=" + SQ004 + ", SQ005=" + SQ005 + ", SQ006=" + SQ006 + ", SQ007=" + SQ007
				+ ", SQ008=" + SQ008 + ", SQ009=" + SQ009 + ", SQ010=" + SQ010 + ", SQ011=" + SQ011 + ", SQ012=" + SQ012
				+ ", SQ013=" + SQ013 + ", SQ014=" + SQ014 + ", SQ015=" + SQ015 + ", SQ016=" + SQ016 + ", SQ017=" + SQ017
				+ ", SQ018=" + SQ018 + ", SQ019=" + SQ019 + ", SQ020=" + SQ020 + ", SQ021=" + SQ021 + ", SQ022=" + SQ022
				+ ", SQ023=" + SQ023 + ", SQ024=" + SQ024 + ", SQ025=" + SQ025 + ", SQ026=" + SQ026 + ", SQ027=" + SQ027
				+ ", SQ028=" + SQ028 + ", SQ029=" + SQ029 + ", SQ030=" + SQ030 + ", SQ031=" + SQ031 + ", SQ032=" + SQ032
				+ ", SQ033=" + SQ033 + ", SQ034=" + SQ034 + ", SQ035=" + SQ035 + ", SQ036=" + SQ036 + ", SQ037=" + SQ037
				+ ", SQ038=" + SQ038 + ", SQ039=" + SQ039 + ", SQ040=" + SQ040 + ", SQ041=" + SQ041 + ", SQ042=" + SQ042
				+ ", SQ043=" + SQ043 + ", SQ044=" + SQ044 + ", SQ045=" + SQ045 + ", SQ046=" + SQ046 + ", SQ047=" + SQ047
				+ ", HK001=" + HK001 + ", HK002=" + HK002 + ", HK003=" + HK003 + ", HK004=" + HK004 + ", HK005=" + HK005
				+ ", HK006=" + HK006 + ", HK007=" + HK007 + ", HK008=" + HK008 + ", HK009=" + HK009 + ", HK010=" + HK010
				+ ", HK011=" + HK011 + ", HK012=" + HK012 + ", HK013=" + HK013 + ", HK014=" + HK014 + ", HK015=" + HK015
				+ ", HK016=" + HK016 + ", HK017=" + HK017 + ", HK018=" + HK018 + ", HK019=" + HK019 + ", HK020=" + HK020
				+ ", HK021=" + HK021 + ", HK022=" + HK022 + ", HK023=" + HK023 + ", HK024=" + HK024 + ", HK025=" + HK025
				+ ", HK026=" + HK026 + ", HK027=" + HK027 + ", HK028=" + HK028 + ", HK029=" + HK029 + ", HK030=" + HK030
				+ ", HK031=" + HK031 + ", HK032=" + HK032 + ", HK033=" + HK033 + ", HK034=" + HK034 + ", HK035=" + HK035
				+ ", HK036=" + HK036 + ", HK037=" + HK037 + ", HK038=" + HK038 + ", HK039=" + HK039 + ", HK040=" + HK040
				+ ", HK041=" + HK041 + ", HK042=" + HK042 + ", HK043=" + HK043 + ", HK044=" + HK044 + ", HK045=" + HK045
				+ ", HK046=" + HK046 + ", HK047=" + HK047 + ", HK048=" + HK048 + ", HK049=" + HK049 + ", FK001=" + FK001
				+ ", FK002=" + FK002 + ", FK003=" + FK003 + ", FK004=" + FK004 + ", FK005=" + FK005 + ", FK006=" + FK006
				+ ", FK007=" + FK007 + ", FK008=" + FK008 + ", FK009=" + FK009 + ", FK010=" + FK010 + ", FK011=" + FK011
				+ ", FK012=" + FK012 + ", FK013=" + FK013 + ", FK014=" + FK014 + ", FK015=" + FK015 + ", FK016=" + FK016
				+ ", FK017=" + FK017 + ", SX001=" + SX001 + ", SX002=" + SX002 + ", SX003=" + SX003 + ", SX004=" + SX004
				+ ", SX005=" + SX005 + ", SX006=" + SX006 + ", SX007=" + SX007 + ", SX008=" + SX008 + ", SX009=" + SX009
				+ ", SX010=" + SX010 + ", SX011=" + SX011 + ", SX012=" + SX012 + ", FX001=" + FX001 + ", FX002=" + FX002
				+ ", FX003=" + FX003 + ", FX004=" + FX004 + ", FX005=" + FX005 + ", FX006=" + FX006 + ", FX007=" + FX007
				+ ", FX008=" + FX008 + ", FX009=" + FX009 + ", FX010=" + FX010 + ", FX011=" + FX011 + ", FX012=" + FX012
				+ ", FX013=" + FX013 + ", FX014=" + FX014 + ", FX015=" + FX015 + ", FX016=" + FX016 + ", FX017=" + FX017
				+ ", FX018=" + FX018 + ", FX019=" + FX019 + ", FX020=" + FX020 + ", FX021=" + FX021 + ", FX022=" + FX022
				+ ", FX023=" + FX023 + ", FX024=" + FX024 + ", FX025=" + FX025 + ", FX026=" + FX026 + ", FX027=" + FX027
				+ ", FX028=" + FX028 + ", FX029=" + FX029 + ", FX030=" + FX030 + ", FX031=" + FX031 + ", FX032=" + FX032
				+ ", FX033=" + FX033 + ", FX034=" + FX034 + ", FX035=" + FX035 + ", YQ001=" + YQ001 + ", YQ002=" + YQ002
				+ ", YQ003=" + YQ003 + ", YQ004=" + YQ004 + ", YQ005=" + YQ005 + ", YQ006=" + YQ006 + ", YQ007=" + YQ007
				+ ", YQ008=" + YQ008 + ", YQ009=" + YQ009 + ", YQ010=" + YQ010 + ", YQ011=" + YQ011 + ", YQ012=" + YQ012
				+ ", YQ013=" + YQ013 + ", YQ014=" + YQ014 + ", YQ015=" + YQ015 + ", YQ016=" + YQ016 + ", YQ017=" + YQ017
				+ ", YQ018=" + YQ018 + ", YQ019=" + YQ019 + ", YQ020=" + YQ020 + ", YQ021=" + YQ021 + ", YQ022=" + YQ022
				+ ", YQ023=" + YQ023 + ", YQ024=" + YQ024 + ", YQ025=" + YQ025 + ", YQ026=" + YQ026 + ", YQ027=" + YQ027
				+ ", YQ028=" + YQ028 + ", YQ029=" + YQ029 + ", YQ030=" + YQ030 + ", YQ031=" + YQ031 + ", YQ032=" + YQ032
				+ ", YQ033=" + YQ033 + ", YQ034=" + YQ034 + ", YQ035=" + YQ035 + ", YQ036=" + YQ036 + ", YQ037=" + YQ037
				+ ", YQ038=" + YQ038 + ", YQ039=" + YQ039 + ", YQ040=" + YQ040 + ", YQ041=" + YQ041 + ", YQ042=" + YQ042
				+ ", YQ043=" + YQ043 + ", YQ044=" + YQ044 + ", YQ045=" + YQ045 + "]";
	}
	
}
