package com.hualong.duolabao.tool;

import org.json.JSONArray;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class String_Tool {

	/**
	 * <pre>
	 *     得到当前时间的时间戳
	 * </pre>
	 * @return
	 */
	public static String getTimeUnix() {
		return String.valueOf((new Date()).getTime()).substring(0,10);
	}

	public static String String_IS_Four(double d) { // 精确到四位小说
		DecimalFormat df = new DecimalFormat("0.0000");
		return df.format(d);
	}

	public static String String_IS_Four(String d) { // 精确到四位小说
		return String_IS_Four(Double.parseDouble(d));
	}

	public static String String_IS_Two(double d) { // 精确到两位小数
		DecimalFormat df = new DecimalFormat("0.00");
		return df.format(d);
	}

	public static String String_IS_Two(String d) { // 精确到四位小说
		return String_IS_Two(Double.parseDouble(d));
	}

	public static String String_IS_Four(int d) { // 精确到四位小说
		DecimalFormat df = new DecimalFormat("0000");
		return df.format(d);
	}

	// 两数chu
	public static String multiply(double a, double b) {
		BigDecimal c = new BigDecimal(a);
		BigDecimal d = new BigDecimal(b);
		BigDecimal f1 = c.add(d);
		Double f2 = f1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		return "" + f2;
	}

	// 两数相加
	public static String isJia(double a, double b) {
		BigDecimal c = new BigDecimal(a);
		BigDecimal d = new BigDecimal(b);
		BigDecimal f1 = c.add(d);
		Double f2 = f1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		return "" + f2;
	}

	// 两数相乘
	private static String mulMoney(String m1, String m2) {
		BigDecimal c = new BigDecimal(m1);
		BigDecimal d = new BigDecimal(m2);
		BigDecimal result = c.multiply(d);
		return result.toString();
	}

	private static double keep_two(String m1) {
		BigDecimal bg = new BigDecimal(m1);
		double result = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue(); // 四色五入
		return result;
	}

	public static String DataBaseTime() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = simpleDateFormat.format(new Date());
		return time;
	}

	public static String Dataadd(int n)  {

		Format f = new SimpleDateFormat("yyyy-MM-dd");

		Date today = new Date();


		Calendar c = Calendar.getInstance();
		c.setTime(today);
		c.add(Calendar.DAY_OF_MONTH, n);// 今天+1天

		Date tomorrow = c.getTime();

		String time = f.format(tomorrow);


		return time;

	}

	public static String DataBaseTime_MM() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmm");
		String time = simpleDateFormat.format(new Date());
		return time;
	}

	public static String DataBaseYear() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
		String time = simpleDateFormat.format(new Date());
		return time;
	}

	public static String DataBaseYear_Month_Day() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String time = simpleDateFormat.format(new Date());
		return time;
	}

	public static String DataBaseH_M_S() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
		String time = simpleDateFormat.format(new Date());
		return time;
	}

	public static String DataBaseH_M_S_String() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HHmmss");
		String time = simpleDateFormat.format(new Date());
		return time;
	}

	public static String DataBaseM() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM");
		String time = simpleDateFormat.format(new Date());
		return time;
	}

	public static String reformat() {
		Date today = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			String dateString = formatter.format(today);
			return dateString;

		} catch (IllegalArgumentException iae) {

		}
		return null;
	}

	public static boolean isEmpty(String str) {
		if (str == null) {
			return true;
		} else if (str.equals("null")) {
			return true;
		} else if (str.equals("NULL")) {
			return true;
		} else if (str.equals("")) {
			return true;
		} else if (str.equals("''")) {
			return true;
		} else {
			return false;
		}
	}

	public static String sql(String sort, String sql_, int Number_of_pages) {

		String sql = "SELECT TOP 10000 *  FROM (SELECT ROW_NUMBER() OVER (ORDER BY  " + sort
				+ ") AS RowNumber,* FROM  (" + sql_ + " ) as  Z ) as A WHERE RowNumber > 10000*("
				+ (Number_of_pages - 1) + ")";
		return sql;

	}

	public static String get_output_str(JSONArray array) {
		String str = "{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":" + array.toString().replace(" ", "") + "}";
		if (array != null && array.length() > 0) {
			str = "{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":" + array.toString().replace(" ", "") + "}";
		} else {
			str = "{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":" + array.toString().replace(" ", "") + "}";
		}
		return str;
	}

	public static String get_zao_wan_ban() {
		GregorianCalendar c = new GregorianCalendar();
		int apm = c.get(GregorianCalendar.AM_PM);
		return apm == 0 ? "早班" : "晚班";
	}

	public static List search(String name, List<HashMap<String, String>> list) {
		List<HashMap<String, String>> results = new ArrayList<HashMap<String, String>>();
		Pattern pattern = Pattern.compile(name);
		for (int i = 0; i < list.size(); i++) {
			Matcher matcher = pattern.matcher(list.get(i).get("name"));
			if (matcher.matches()) {
				results.add(list.get(i));
			}
		}
		return results;
	}
	//根据总记录条数和每页记录数  返回总页数
	public static  int getAllPage(int allNumber,int num){
		//总记录数大于页数
		if(num<allNumber){
			//取整数
			int page=allNumber/num;
			//去余数
			int pageY=allNumber/num;
			//如果余数大于0  返回page+1   如果余数等于0  返回page
			if(pageY==0){
				return page;
			}else{
				return page+1;
			}
		}
		return 0;
	}

	public static boolean isContainChinese(String str) {
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
		Matcher m = p.matcher(str);
		if (m.find()) {
			return true;
		}
		return false;
	}

	public static void main(String[] args) {
		System.out.println(isContainChinese("456张"));

	}
 

}
