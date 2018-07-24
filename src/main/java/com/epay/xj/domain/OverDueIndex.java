package com.epay.xj.domain;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="cp_kpi.P1055_YQ_JAVA")
public class OverDueIndex {

	@Id
	public String CERT_NO;//身份证号
//	public Integer SCORE;//信用分
	/************************ 逾期类变量***************************/
	public int YQ001;	//6个月	逾期一天以上	近6个月在贷款类机构逾期1天以上次数
	public int YQ002;	//6个月	逾期一天以上	近6个月在消费金融机构逾期1天以上次数
	public int YQ003;	//6个月	逾期一天以上	近6个月在银行类机构逾期1天以上次数
	public int YQ004;	//6个月	逾期一天以上	近6个月在小贷款类机构逾期1天以上次数
	public BigDecimal YQ005;	//6个月	平均逾期次数	近6个月逾期的平均每家机构逾期次数
	public BigDecimal YQ006;	//6个月	平均逾期次数	近6个月在消费金融机构逾期的平均每家机构逾期次数
	public BigDecimal YQ007;	//6个月	平均逾期次数	近6个月在银行类机构逾期的平均每家机构逾期次数
	public BigDecimal YQ008;	//6个月	平均逾期次数	近6个月在小贷贷款类机构逾期的平均每家机构逾期次数
	public int YQ009;	//6个月	逾期机构数	近6个月发生逾期的贷款类机构数
	public int YQ010;	//6个月	逾期机构数	近6个月发生逾期的消费金融类机构数
	public int YQ011;	//6个月	逾期机构数	近6个月发生逾期的银行类机构数
	public int YQ012;	//6个月	逾期机构数	近6个月发生逾期的小贷贷款类机构数
	public int YQ013;	//3个月	逾期一天以上次数	近3个月在贷款类机构逾期1天以上次数
	public int YQ014;	//3个月	逾期一天以上次数	近3个月在消费金融机构逾期1天以上次数
	public int YQ015;	//3个月	逾期一天以上次数	近3个月在银行类机构逾期1天以上次数
	public int YQ016;	//3个月	逾期一天以上次数	近3个月在小贷贷款类机构逾期1天以上次数
	public int YQ017;	//3个月	逾期机构数	近3个月发生逾期的贷款类机构数
	public int YQ018;	//3个月	逾期机构数	近3个月发生逾期的消费金融类机构数
	public int YQ019;	//3个月	逾期机构数	近3个月发生逾期的银行类机构数
	public int YQ020;	//3个月	逾期机构数	近3个月发生逾期的小贷类机构数
//	public BigDecimal YQ021;	//3个月	平均逾期次数	近3个月逾期的平均每家机构逾期次数
	public BigDecimal YQ022;	//3个月	平均逾期次数	近3个月在消费金融机构逾期的平均每家机构逾期次数
	public BigDecimal YQ023;	//3个月	平均逾期次数	近3个月在银行类机构逾期的平均每家机构逾期次数
	public BigDecimal YQ024;	//3个月	平均逾期次数	近3个月在小贷类机构逾期的平均每家机构逾期次数
	public BigDecimal YQ025;	//3个月	平均逾期次数	近3个月在贷款类机构逾期的平均每家机构逾期次数
	public int YQ026;	//6个月	逾期天数总和	近6个月逾期1天以上天数总和
	public int YQ027;	//3个月	逾期天数总和	近3个月逾期1天以上天数总和
	public BigDecimal YQ028;	//6个月	逾期金额总和	近6个月逾期30天以上金额总和
	public BigDecimal YQ029;	//6个月	逾期金额总和	近6个月逾期7天以上金额总和
	public BigDecimal YQ030;	//6个月	逾期金额总和	近6个月逾期1天以上金额总和
	public BigDecimal YQ031;	//3个月	逾期金额总和	近3个月逾期30天以上金额总和
	public BigDecimal YQ032;	//3个月	逾期金额总和	近3个月逾期7天以上金额总和
	public BigDecimal YQ033;	//3个月	逾期金额总和	近3个月逾期1天以上金额总和
	public int YQ034;	//12个月	逾期机构数	近12个月发生逾期的贷款类机构数
	public int YQ035;	//12个月	逾期机构数	近12个月发生逾期的银行类机构数
	public int YQ036;	//12个月	逾期机构数	近12个月发生逾期的消费金融类机构数
	public int YQ037;	//12个月	逾期机构数	近12个月发生逾期的小贷贷款类机构数
	public int YQ038;	//12个月	逾期1天以上	近12个月在贷款类机构逾期1天以上的次数
	public int YQ039;	//12个月	逾期1天以上	近12个月在消费金融类机构逾期1天以上的次数
	public int YQ040;	//12个月	逾期1天以上	近12个月在银行类机构逾期1天以上的次数
	public int YQ041;	//12个月	逾期1天以上	近12个月在小额贷款行业逾期1天以上的次数
	public int YQ042;	//12个月	最大逾期次数	近12个月在消费金融机构逾期的最大每家机构逾期次数
	public int YQ043;	//12个月	最大逾期次数	近12个月在贷款机构逾期的最大每家机构逾期次数
	public int YQ044;	//12个月	最大逾期次数	近12个月在银行类机构逾期的最大每家机构逾期次数
	public int YQ045;	//12个月	最大逾期次数	近12个月在小贷类机构逾期的最大每家机构逾期次数
	public int YQ046;//6个月	最大逾期天数	6月_小贷_单机构_最大逾期天数
	public int YQ047;//6个月	最大逾期天数	6月_消金_单机构_最大逾期天数
	public int YQ048;//6个月	最大逾期天数	6月_银行_单机构_最大逾期天数
	public int YQ049;//6个月	最大逾期天数	6月_贷款_单机构_最大逾期天数
	public int YQ050;//3个月	最大逾期天数	3月_小贷_单机构_最大逾期天数
	public int YQ051;//3个月	最大逾期天数	3月_消金_单机构_最大逾期天数
	public int YQ052;//3个月	最大逾期天数	3月_银行_单机构_最大逾期天数
	public int YQ053;//3个月	最大逾期天数	3月_贷款_单机构_最大逾期天数
	public int YQ054;//1个月	最大逾期天数	1月_小贷_单机构_最大逾期天数
	public int YQ055;//1个月	最大逾期天数	1月_消金_单机构_最大逾期天数
	public int YQ056;//1个月	最大逾期天数	1月_银行_单机构_最大逾期天数
	public int YQ057;//1个月	最大逾期天数	1月_贷款_单机构_最大逾期天数
	public int YQ058;//15天	最大逾期天数	15天_小贷_单机构_最大逾期天数
	public int YQ059;//15天	最大逾期天数	15天_消金_单机构_最大逾期天数
	public int YQ060;//15天	最大逾期天数	15天_银行_单机构_最大逾期天数
	public int YQ061;//15天	最大逾期天数	15天_贷款_单机构_最大逾期天数
	public int YQ062;//15天	最大逾期天数	7天_小贷_单机构_最大逾期天数
	public int YQ063;//15天	最大逾期天数	7天_消金_单机构_最大逾期天数
	public int YQ064;//15天	最大逾期天数	7天_银行_单机构_最大逾期天数
	public int YQ065;//15天	最大逾期天数	7天_贷款_单机构_最大逾期天数
	public int YQ066;//6月	逾期1天_机构数	6月_全卡_逾期1天以上_机构数
	public int YQ067;//6月	逾期7天_机构数	6月_全卡_逾期7天以上_机构数
	public int YQ068;//6月	逾期30天_机构数	6月_全卡_逾期30天以上_机构数
	public int YQ069;//3月	逾期1天_机构数	3月_全卡_逾期1天以上_机构数
	public int YQ070;//3月	逾期7天_机构数	3月_全卡_逾期7天以上_机构数
	public int YQ071;//3月	逾期30天_机构数	3月_全卡_逾期30天以上_机构数
	public int YQ072;//15天	逾期1天_机构数	15天_全卡_逾期1天以上_机构数
	public int YQ073;//15天	逾期7天_机构数	15天_全卡_逾期7天以上_机构数
	public int YQ074;//7天	逾期1天_机构数	7天_全卡_逾期1天以上_机构数
	public int YQ075;//7天	逾期7天_机构数	7天_全卡_逾期7天以上_机构数
	public String getCERT_NO() {
		return CERT_NO;
	}
	public void setCERT_NO(String cERT_NO) {
		CERT_NO = cERT_NO;
	}
//	public Integer getSCORE() {
//		return SCORE;
//	}
//	public void setSCORE(Integer sCORE) {
//		SCORE = sCORE;
//	}
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
	public BigDecimal getYQ005() {
		return YQ005;
	}
	public void setYQ005(BigDecimal yQ005) {
		YQ005 = yQ005;
	}
	public BigDecimal getYQ006() {
		return YQ006;
	}
	public void setYQ006(BigDecimal yQ006) {
		YQ006 = yQ006;
	}
	public BigDecimal getYQ007() {
		return YQ007;
	}
	public void setYQ007(BigDecimal yQ007) {
		YQ007 = yQ007;
	}
	public BigDecimal getYQ008() {
		return YQ008;
	}
	public void setYQ008(BigDecimal yQ008) {
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
	public BigDecimal getYQ022() {
		return YQ022;
	}
	public void setYQ022(BigDecimal yQ022) {
		YQ022 = yQ022;
	}
	public BigDecimal getYQ023() {
		return YQ023;
	}
	public void setYQ023(BigDecimal yQ023) {
		YQ023 = yQ023;
	}
	public BigDecimal getYQ024() {
		return YQ024;
	}
	public void setYQ024(BigDecimal yQ024) {
		YQ024 = yQ024;
	}
	public BigDecimal getYQ025() {
		return YQ025;
	}
	public void setYQ025(BigDecimal yQ025) {
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
	public int getYQ046() {
		return YQ046;
	}
	public void setYQ046(int yQ046) {
		YQ046 = yQ046;
	}
	public int getYQ047() {
		return YQ047;
	}
	public void setYQ047(int yQ047) {
		YQ047 = yQ047;
	}
	public int getYQ048() {
		return YQ048;
	}
	public void setYQ048(int yQ048) {
		YQ048 = yQ048;
	}
	public int getYQ049() {
		return YQ049;
	}
	public void setYQ049(int yQ049) {
		YQ049 = yQ049;
	}
	public int getYQ050() {
		return YQ050;
	}
	public void setYQ050(int yQ050) {
		YQ050 = yQ050;
	}
	public int getYQ051() {
		return YQ051;
	}
	public void setYQ051(int yQ051) {
		YQ051 = yQ051;
	}
	public int getYQ052() {
		return YQ052;
	}
	public void setYQ052(int yQ052) {
		YQ052 = yQ052;
	}
	public int getYQ053() {
		return YQ053;
	}
	public void setYQ053(int yQ053) {
		YQ053 = yQ053;
	}
	public int getYQ054() {
		return YQ054;
	}
	public void setYQ054(int yQ054) {
		YQ054 = yQ054;
	}
	public int getYQ055() {
		return YQ055;
	}
	public void setYQ055(int yQ055) {
		YQ055 = yQ055;
	}
	public int getYQ056() {
		return YQ056;
	}
	public void setYQ056(int yQ056) {
		YQ056 = yQ056;
	}
	public int getYQ057() {
		return YQ057;
	}
	public void setYQ057(int yQ057) {
		YQ057 = yQ057;
	}
	public int getYQ058() {
		return YQ058;
	}
	public void setYQ058(int yQ058) {
		YQ058 = yQ058;
	}
	public int getYQ059() {
		return YQ059;
	}
	public void setYQ059(int yQ059) {
		YQ059 = yQ059;
	}
	public int getYQ060() {
		return YQ060;
	}
	public void setYQ060(int yQ060) {
		YQ060 = yQ060;
	}
	public int getYQ061() {
		return YQ061;
	}
	public void setYQ061(int yQ061) {
		YQ061 = yQ061;
	}
	public int getYQ062() {
		return YQ062;
	}
	public void setYQ062(int yQ062) {
		YQ062 = yQ062;
	}
	public int getYQ063() {
		return YQ063;
	}
	public void setYQ063(int yQ063) {
		YQ063 = yQ063;
	}
	public int getYQ064() {
		return YQ064;
	}
	public void setYQ064(int yQ064) {
		YQ064 = yQ064;
	}
	public int getYQ065() {
		return YQ065;
	}
	public void setYQ065(int yQ065) {
		YQ065 = yQ065;
	}
	public int getYQ066() {
		return YQ066;
	}
	public void setYQ066(int yQ066) {
		YQ066 = yQ066;
	}
	public int getYQ067() {
		return YQ067;
	}
	public void setYQ067(int yQ067) {
		YQ067 = yQ067;
	}
	public int getYQ068() {
		return YQ068;
	}
	public void setYQ068(int yQ068) {
		YQ068 = yQ068;
	}
	public int getYQ069() {
		return YQ069;
	}
	public void setYQ069(int yQ069) {
		YQ069 = yQ069;
	}
	public int getYQ070() {
		return YQ070;
	}
	public void setYQ070(int yQ070) {
		YQ070 = yQ070;
	}
	public int getYQ071() {
		return YQ071;
	}
	public void setYQ071(int yQ071) {
		YQ071 = yQ071;
	}
	public int getYQ072() {
		return YQ072;
	}
	public void setYQ072(int yQ072) {
		YQ072 = yQ072;
	}
	public int getYQ073() {
		return YQ073;
	}
	public void setYQ073(int yQ073) {
		YQ073 = yQ073;
	}
	public int getYQ074() {
		return YQ074;
	}
	public void setYQ074(int yQ074) {
		YQ074 = yQ074;
	}
	public int getYQ075() {
		return YQ075;
	}
	public void setYQ075(int yQ075) {
		YQ075 = yQ075;
	}

//	/************************ 放款类变量***************************/
//	public int FK001;	//12个月	成功次数	近12个月成功放款的记录数
//	public int FK002;	//12个月	机构数	近12个月成功放款的不同机构数
////	public int FK003;	//12个月	机构数	在贷款类机构放款成功的机构数
//	public BigDecimal FK004;	//12个月	总金额	近12个月在银行类机构放款的总金额
//	public BigDecimal FK005;	//12个月	总金额	近12个月在消费金融类机构放款的总金额
//	public BigDecimal FK006;	//12个月	总金额	近12个月在小额贷款类机构放款的总金额
//	public BigDecimal FK007;	//12个月	总金额	近12个月在贷款类机构放款的总金额
//	public int FK008;	//6个月	成功次数	近6个月成功放款的记录数
//	public int FK009;	//6个月	机构数	近6个月成功放款的不同机构数
//	public BigDecimal FK010;	//6个月	总金额	近6个月在银行类机构放款的总金额
//	public BigDecimal FK011;	//6个月	总金额	近6个月在消费金融类机构放款的总金额
//	public BigDecimal FK012;	//6个月	总金额	近6个月在小贷类机构放款的总金额
//	public BigDecimal FK013;	//6个月	总金额	近6个月在贷款类机构放款的总金额
//	public String FK014;	//最近一次	时间指标	最近一次在贷款机构放款的日期
//	public int FK015;	//最近一次	时间指标	最近一次在贷款机构放款距今的天数
//	public String FK016;	//最早一次	时间指标	最早一次在贷款机构放款时间
//	public int FK017;	//最早一次	时间指标	最早一次在贷款机构放款距今的天数
//	
//	/************************ 风险类指标***************************/
//	public int FX001;	//12个月	失败次数	近12个月在小贷机构因账户原因划扣失败的次数
//	public int FX002;	//12个月	失败次数	近12个月在消费金融机构因账户原因划扣失败的次数
//	public int FX003;	//12个月	失败次数	近12个月在银行机构因账户原因划扣失败的次数
//	public int FX004;	//12个月	失败次数	近12个月在贷款类机构因账户原因划扣失败的次数
////	public int FX005;	//12个月	失败次数	近12个月因账户原因划扣失败的次数
//	public BigDecimal FX006;	//12个月	失败占比	近12个月因账户原因划扣失败的记录数占比
//	public BigDecimal FX007;	//12个月	失败占比	近12个月因账户原因划扣失败的金额占比
//	public int FX008;	//6个月	失败次数	近6个月在贷款类机构因账户原因划扣失败的次数
//	public int FX009;	//6个月	失败次数	近6个月在消费金融机构因账户原因划扣失败的次数
//	public int FX010;	//6个月	失败次数	近6个月在银行机构因账户原因划扣失败的次数
//	public int FX011;	//6个月	失败次数	近6个月在小贷机构因账户原因划扣失败的次数
////	public int FX012;	//6个月	失败次数	近6个月因账户原因划扣失败的次数
//	public BigDecimal FX013;	//6个月	失败占比	近6个月因账户原因划扣失败的记录数占比
//	public BigDecimal FX014;	//6个月	失败占比	近6个月因账户原因划扣失败的金额占比
//	public BigDecimal FX015;	//6个月	失败占比	近6个月因超出限额划扣失败的金额占比
//	public BigDecimal FX016;	//6个月	失败占比	近6个月因超出限额划扣失败的次数占比
//	public int FX017;	//3个月	失败次数	近3个月在贷款机构因账户原因划扣失败的次数
//	public int FX018;	//3个月	失败次数	近3个月在消费金融机构因账户原因划扣失败的次数
//	public int FX019;	//3个月	失败次数	近3个月在银行机构因账户原因划扣失败的次数
//	public int FX020;	//3个月	失败次数	近3个月在小贷类机构因账户原因划扣失败的次数
////	public int FX021;	//3个月	失败次数	近3个月因账户原因划扣失败的次数
//	public BigDecimal FX022;	//3个月	失败占比	近3个月因账户原因划扣失败的次数占比
//	public BigDecimal FX023;	//3个月	失败占比	近3个月因账户原因划扣失败的金额占比
//	public BigDecimal FX024;	//3个月	失败占比	近3个月因超出限额划扣失败的金额占比
//	public BigDecimal FX025;	//3个月	失败占比	近3个月因超出限额划扣失败的次数占比
//	public BigDecimal FX026;	//近7天	失败占比	近7天余额不足的记录数占比
//	public int FX027;	//近7天	失败次数	近7天在消费金融行业余额不足的记录数
//	public int FX028;	//近7天	失败次数	近7天在贷款行业余额不足的记录数
//	public int FX029;	//近7天	失败次数	近7天在银行类机构余额不足的记录数
//	public int FX030;	//近7天	失败次数	近7天在小贷类机构余额不足的记录数
//	public BigDecimal FX031;	//近15天	失败占比	近15天余额不足的记录数占比
//	public int FX032;	//近15天	失败次数	近15天在消费金融行业余额不足的记录数
//	public int FX033;	//近15天	失败次数	近15天在小贷行业余额不足的记录数
//	public int FX034;	//近15天	失败次数	近15天在银行类机构余额不足的记录数
//	public int FX035;	//近15天	失败次数	近15天在贷款类机构余额不足的记录数
//	public int FX036; //12月_全卡_余额不足_失败次数-------------------------------修改
//	public BigDecimal FX037; //12月_全卡_失败扣款_余额不足记录数_占比-------------------------------修改
//	public BigDecimal FX038; //12月_全卡_失败扣款_余额不足金额_占比-------------------------------修改
//	public int FX039; //6月_全卡_余额不足_扣款失败次数-------------------------------修改
//	public BigDecimal FX040; //6月_全卡_失败扣款_余额不足记录数_占比-------------------------------修改
//	public BigDecimal FX041; //6月_全卡_失败扣款_余额不足金额_占比-------------------------------修改
//	public int FX042; //3月_全卡_余额不足_失败扣款_次数-------------------------------修改
//	public BigDecimal FX043; //3月_全卡_失败扣款_余额不足记录数_占比-------------------------------修改
//	public BigDecimal FX044; //3月_全卡_失败扣款_余额不足金额_占比-------------------------------修改
//
//	/************************ 还款类变量 ***************************/
//	public int HK001;	//12个月	成功次数	近12个月成功还款的记录数
////	public BigDecimal HK002;	//12个月	成功占比	近12个月成功还款的记录数占比
//	public int HK003;	//12个月	机构数	近12个月成功还款的机构数
//	public int HK004;	//12个月	机构数	近12个月成功还款的银行类机构数
//	public int HK005;	//12个月	机构数	近12个月成功还款的消金类机构数
//	public int HK006;	//12个月	机构数	近12个月成功还款的小贷类机构数
////	public int HK007;	//12个月	失败次数	近12个月余额不足的记录数
////	public BigDecimal HK008;	//12个月	失败占比	近12个月失败还款记录数的占比
////	public BigDecimal HK009;	//12个月	失败占比	近12个月余额不足的记录数占比
////	public BigDecimal HK010;	//12个月	失败占比	近12个月余额不足的金额数占比
//////	public int HK011;	//6个月	成功次数	近6个月成功还款的记录数
//	public int HK012;	//6个月	成功次数	近6个月在贷款类机构划扣成功的记录数
////	public BigDecimal HK013;	//6个月	成功占比	近6个月成功还款的记录数占比
//	public int HK014;	//6个月	机构数	近6个月成功还款的贷款类机构数
//	public int HK015;	//6个月	机构数	近6个月成功还款的银行类机构数
//	public int HK016;	//6个月	机构数	近6个月成功还款的消金类机构数
//	public int HK017;	//6个月	机构数	近6个月成功还款的小贷类机构数
////	public int HK018;	//6个月	失败次数	近6个月余额不足的记录数
////	public BigDecimal HK019;	//6个月	失败占比	近6个月划扣失败的记录数占比
////	public BigDecimal HK020;	//6个月	失败占比	近6个月余额不足的记录数占比
////	public BigDecimal HK021;	//6个月	失败占比	近6个月余额不足的金额数占比
//	public int HK022;	//3个月	成功次数	近3个月成功还款的记录数
////	public BigDecimal HK023;	//3个月	成功占比	近3个月成功还款的记录数占比
////	public int HK024;	//3个月	失败次数	近3个月余额不足的记录数
////	public BigDecimal HK025;	//3个月	失败占比	近3个月划扣失败的记录数占比
//	public int HK026;	//3个月	机构数	近3个月成功还款的贷款类机构数
//	public int HK027;	//3个月	机构数	近3个月成功还款的银行类机构数
//	public int HK028;	//3个月	机构数	近3个月成功还款的消金类机构数
//	public int HK029;	//3个月	机构数	近3个月成功还款的小贷类机构数
////	public BigDecimal HK030;	//3个月	失败占比	近3个月余额不足的记录数占比
////	public BigDecimal HK031;	//3个月	失败占比	近3个月余额不足的金额数占比
////	public BigDecimal HK032;	//2个月	成功占比	近2个月代扣成功的记录数占比 
////	public BigDecimal HK033;	//2个月	成功占比	近2个月代扣成功的金额占比 
//	public int HK034;	//1个月	成功次数	近1个月成功还款的记录数
////	public BigDecimal HK035;	//1个月	成功占比	近1个月成功还款的记录数占比
//	public String HK036;	//最早一次	时间指标	最早一次在贷款机构还款距今的时间
//	public int HK037;	//最早一次	时间指标	最早一次在贷款机构还款距今的天数
//	public String HK038;	//最近一次	时间指标	最近一次在贷款机构划扣距今的时间
//	public int HK039;	//最近一次	时间指标	最近一次在贷款机构划扣距今的天数
//	public String HK040;	//最近一次	时间指标	最近一次在贷款机构划扣成功距今的时间
//	public int HK041;	//最近一次	时间指标	最近一次在贷款机构划扣成功距今的天数
//	public String HK042;	//最近一次	时间指标	最近一次在银行机构划扣成功距今的时间
//	public int HK043;	//最近一次	时间指标	最近一次在银行机构划扣成功距今的天数
//	public String HK044;	//最近一次	时间指标	最近一次在银行机构划扣失败距今的时间
//	public int HK045;	//最近一次	时间指标	最近一次在银行机构划扣失败距今的天数
//	public String HK046;	//最近一次	时间指标	最近一次在消费金融机构划扣成功距今的时间
//	public int HK047;	//最近一次	时间指标	最近一次在消费金融机构划扣失败距今的天数
//	public String HK048;	//最近一次	时间指标	最近一次在小额贷款机构划扣成功距今的时间
//	public int HK049;	//最近一次	时间指标	最近一次在小额贷款机构划扣失败距今的天数
//	public BigDecimal HK050;//	12个月	总金额	12月_全卡_银行_还款_总额			
//	public BigDecimal HK051;//	12个月	总金额	12月_全卡_消金_还款_总额			
//	public BigDecimal HK052;//	12个月	总金额	12月_全卡_小贷_还款_总额			
//	public BigDecimal HK053;//	12个月	总金额	12月_全卡_贷款_还款_总额			
//	public BigDecimal HK054;//	6个月	总金额	6月_全卡_银行_还款_总额			
//	public BigDecimal HK055;//	6个月	总金额	6月_全卡_消金_还款_总额			
//	public BigDecimal HK056;//	6个月	总金额	6月_全卡_小贷_还款_总额			
//	public BigDecimal HK057;//	6个月	总金额	6月_全卡_贷款_还款_总额
//	/************************ 客户申请行为 ***************************/
//	public int SQ001;	//12个月	机构数	近12个月申请认证的不同机构数
//	public int SQ002;	//12个月	机构数	近12个月申请的不同贷款类机构数
//	public int SQ003;	//12个月	机构数	近12个月申请的不同银行类机构数
//	public int SQ004;	//12个月	银行卡数	近12个月用户用于申请认证的银行卡数
//	public BigDecimal SQ005;	//12个月	申请记录数	近12个月平均每张借记卡申请记录数
//	public BigDecimal SQ006;	//12个月	申请记录数	近12个月平均每张贷记卡申请记录数
//	public BigDecimal SQ007;	//12个月	申请记录数	近12个月平均每张卡在贷款类机构申请的记录数
//	public BigDecimal SQ008;	//12个月	申请记录数	近12个月平均每张卡在银行类机构申请的记录数
//	public int SQ009;	//6个月	机构数	近6个月申请认证的不同机构数
//	public int SQ010;	//6个月	机构数	近6个月申请认证的贷款类机构数
//	public int SQ011;	//6个月	机构数	近6个月申请认证的银行类机构数
//	public int SQ012;	//6个月	银行卡数	近6个月用户用于申请认证的银行卡数
//	public BigDecimal SQ013;	//6个月	申请记录数	近6个月平均每张借记卡申请记录数
//	public BigDecimal SQ014;	//6个月	申请记录数	近6个月平均每张贷记卡申请记录数
//	public int SQ015;	//6个月	申请记录数	近6个月每张借记卡申请最小记录数
//	public int SQ016;	//6个月	申请记录数	近6个月每张贷记卡申请最小记录数
//	public BigDecimal SQ017;	//6个月	申请记录数	近6个月平均每张卡在贷款类机构申请的记录数
//	public BigDecimal SQ018;	//6个月	申请记录数	近6个月平均每张卡在银行类机构申请的记录数
//	public int SQ019;	//3个月	机构数	近3个月申请认证的不同机构数
//	public int SQ020;	//3个月	机构数	近3个月申请的贷款类机构数
//	public int SQ021;	//3个月	机构数	近3个月申请的银行类机构数
//	public int SQ022;	//3个月	银行卡数	近3个月用户用于申请认证的银行卡数
//	public BigDecimal SQ023;	//3个月	申请记录数	近3个月平均每张借记卡申请记录数
//	public BigDecimal SQ024;	//3个月	申请记录数	近3个月平均每张贷记卡申请记录数
//	public BigDecimal SQ025;	//3个月	申请记录数	近3个月平均每张卡在贷款类机构申请的记录数
//	public BigDecimal SQ026;	//3个月	申请记录数	近3个月平均每张卡在银行类机构申请的记录数
//	public int SQ027;	//2个月	机构数	近2个月申请认证的不同机构数
//	public int SQ028;	//2个月	机构数	近2个月申请的贷款类机构数
//	public int SQ029;	//2个月	机构数	近2个月申请的银行类机构数
//	public int SQ030;	//2个月	申请记录数	近2个月申请的记录数
//	public int SQ031;	//1个月	机构数	近1个月申请认证的不同机构数
//	public int SQ032;	//1个月	机构数	近1个月申请的贷款类机构数
//	public int SQ033;	//1个月	机构数	近1个月申请的银行类机构数
//	public int SQ034;	//1个月	申请记录数	近1个月申请的记录数
//	public BigDecimal SQ035;	//1个月	成功平均记录数	近1个月用每张借记卡申请认证成功的平均记录数
//	public int SQ036;	//15天	机构数	近15天申请的不同机构数
//	public int SQ037;	//15天	机构数	近15天申请的贷款类机构数
//	public int SQ038;	//15天	机构数	近15天申请的银行类机构数
//	public int SQ039;	//15天	申请记录数	近15天申请的记录数
//	public String SQ040;	//最近一次	时间指标	最近一次使用信用卡认证申请距今的时间
//	public int SQ041;	//最近一次	时间指标	最近一次使用信用卡认证申请距今的天数
//	public String SQ042;	//最早一次	时间指标	最早一次使用信用卡认证申请距今的时间
//	public int SQ043;	//最早一次	时间指标	最早一次使用信用卡认证申请距今的天数
//	public String SQ044;	//最近一次	时间指标	最近一次使用借记卡认证申请距离现在的时间
//	public int SQ045;	//最近一次	时间指标	最近一次使用借记卡认证申请距离现在的天数
//	public String SQ046;	//最早一次	时间指标	最早一次使用借记卡认证申请距离现在的时间
//	public int SQ047;	//最早一次	时间指标	最早一次使用借记卡认证申请距离现在的天数
//	public BigDecimal SQ048;	//近12个月平均每张卡申请记录数
//	public BigDecimal SQ049;	//近6个月平均每张卡申请记录数
//	public int SQ050;	//近6个月每张借记卡申请最小记录数
//	public BigDecimal SQ051;	//近3个月平均每张卡申请记录数
//	/************************ 授信类变量 ***************************/
//	public BigDecimal SX001;	//12个月	最大额度	近12个月在贷款类机构授信的最大额度
//	public BigDecimal SX002;	//12个月	最大额度	近12个月在银行类机构授信的最大额度
//	public BigDecimal SX003;	//12个月	最大额度	近12个月在小额贷款类机构授信的最大额度
//	public BigDecimal SX004;	//12个月	最大额度	近12个月在消费金融类机构授信的最大额度
//	public BigDecimal SX005;	//6个月	总额度	近6个月在贷款类机构授信的总额度
//	public BigDecimal SX006;	//6个月	总额度	近6个月在银行类机构授信的总额度
//	public BigDecimal SX007;	//6个月	最大额度	近6个月在小额贷款类机构授信的最大额度
//	public BigDecimal SX008;	//6个月	最大额度	近6个月在消费金融类机构授信的最大额度
//	public BigDecimal SX009;	//3个月	总额度	近3个月在贷款类机构授信的总额度
//	public BigDecimal SX010;	//3个月	总额度	近3个月在银行类机构授信的总额度
//	public BigDecimal SX011;	//3个月	最大额度	近3个月在小额贷款类机构授信的最大额度
//	public BigDecimal SX012;	//3个月	最大额度	近3个月在消费金融类机构授信的最大额度
//	/************************ 扣款类指标 ***************************/
//	public BigDecimal KK001;//12个月	还款成功记录数占比
//	public BigDecimal KK002;//12个月	失败占比
//	public BigDecimal KK003;//6个月	还款成功记录数占比
//	public BigDecimal KK004;//6个月	失败占比
//	public BigDecimal KK005;//3个月	还款成功记录数占比
//	public BigDecimal KK006;//3个月	失败占比
//	public BigDecimal KK007;//2个月	还款成功记录数占比
//	public BigDecimal KK008;//2个月	还款成功金额占比
//	public BigDecimal KK009;//1个月	还款成功记录数占比
//	public BigDecimal KK010;//12个月	还款总额
//	public BigDecimal KK011;//6个月	还款总额
//	public BigDecimal KK012;//3个月	还款总额
//	public int KK013;//12个月	还款成功次数
//	public int KK014;//6个月	还款成功次数
//	public int KK015;//3个月	还款成功次数
//	public int KK016;//12个月	还款成功机构数
//	public int KK017;//12个月	还款成功机构数
//	public int KK018;//12个月	还款成功机构数
	
	
}
