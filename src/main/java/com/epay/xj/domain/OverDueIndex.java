package com.epay.xj.domain;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
@Entity
public class OverDueIndex {

	@Id
	private String CertNo;//身份证号
	private int YQ001;	//6个月	逾期一天以上	近6个月在贷款类机构逾期1天以上次数
	private int YQ002;	//6个月	逾期一天以上	近6个月在消费金融机构逾期1天以上次数
	private int YQ003;	//6个月	逾期一天以上	近6个月在银行类机构逾期1天以上次数
	private int YQ004;	//6个月	逾期一天以上	近6个月在小贷款类机构逾期1天以上次数
	private BigDecimal YQ005;	//6个月	平均逾期次数	近6个月逾期的平均每家机构逾期次数
	private BigDecimal YQ006;	//6个月	平均逾期次数	近6个月在消费金融机构逾期的平均每家机构逾期次数
	private BigDecimal YQ007;	//6个月	平均逾期次数	近6个月在银行类机构逾期的平均每家机构逾期次数
	private BigDecimal YQ008;	//6个月	平均逾期次数	近6个月在小贷贷款类机构逾期的平均每家机构逾期次数
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
	private BigDecimal YQ021;	//3个月	平均逾期次数	近3个月逾期的平均每家机构逾期次数
	private BigDecimal YQ022;	//3个月	平均逾期次数	近3个月在消费金融机构逾期的平均每家机构逾期次数
	private BigDecimal YQ023;	//3个月	平均逾期次数	近3个月在银行类机构逾期的平均每家机构逾期次数
	private BigDecimal YQ024;	//3个月	平均逾期次数	近3个月在小贷类机构逾期的平均每家机构逾期次数
	private BigDecimal YQ025;	//3个月	平均逾期次数	近3个月在贷款类机构逾期的平均每家机构逾期次数
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
	public String getCertNo() {
		return CertNo;
	}
	public void setCertNo(String certNo) {
		CertNo = certNo;
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
	public BigDecimal getYQ021() {
		return YQ021;
	}
	public void setYQ021(BigDecimal yQ021) {
		YQ021 = yQ021;
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
	
}
