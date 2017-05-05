package dmiaes.app.util;

import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

public class StringUtil {

	/**
	 * 获取用户当前目录
	 * @Title: isEmpty
	 * @param str 
	 * @return 返回boolean对象
	 */
	public static boolean isEmpty(String str) {
		if(str == null|| "".equals(str.trim())){
			return true;
		}
		return false;
	}
	
	/**
	 * @description 生成包含字母与数字的随机字符串;
	 * @param str：要生成字符串的长度 
	 * @return
	 */
	public static String randomLowerAlphanumeric(int len) throws Exception {
		if (len == 0) {
			return null;
		}
		char[] returnStr = new char[len];
		String str = "0123456789abcdefghijklmnopqrstuvwxyz";
		char[] numbersAndLetters = str.toCharArray();
		Random random = new Random();
		for (int i = 0; i < returnStr.length; i++) {
			returnStr[i] = numbersAndLetters[random.nextInt(str.length())];
		}
		return new String(returnStr);
	}
	
	/**
	 * @description 生成UUID;
	 * @return
	 */
	public static String randomUUID() throws Exception {
		String uuid = UUID.randomUUID().toString().replace("-", "");
		return uuid;
	}

	/**
	 * @description 检测sql字符串中是否有非法字符串;
	 * @param str：SQL字符串
	 * @return	true：有；false：没有
	 */
	public static boolean sqlValidate(String str) throws Exception {
		str = str.toLowerCase();// 统一转为小写
		String badStr = "'|and|exec|execute|insert|select|delete|update|count|drop|*|%|chr|mid|master|truncate|"
				+ "char|declare|sitename|net user|xp_cmdshell|;|or|-|+|,|like'|and|exec|execute|insert|create|drop|" + "table|from|grant|use|group_concat|column_name|"
				+ "information_schema.columns|table_schema|union|where|select|delete|update|order|by|count|*|" + "chr|mid|master|truncate|char|declare|or|;|-|--|+|,|like|//|/|%|#";// 过滤掉的sql关键字，可以手动添加
		String[] badStrs = badStr.split("\\|");
		for (int i = 0; i < badStrs.length; i++) {
			if (str.indexOf(badStrs[i]) >= 0) {
				return true;
			}
		}
		return false;
	}


	/**
	 * @description 指定float类型保留几位小数（注：四舍五入）
	 * @param oldVal：指定double型数字
	 * @param precision：要保留小数点后几位
	 * @return 转换后的Float数字
	 */
	public static Float getNewFloat(double oldVal, int precision) throws Exception {
		String c = "10";
		for (int i = 1; i < precision; i++) {
			c += "0";
		}
		int d = Integer.parseInt(c);
		Float b = (float) (Math.round(oldVal * d)) / d;
		return b;
	}

	/**
	 * @description 指定double类型保留几位小数（注：四舍五入）
	 * @param oldVal：指定double型数字
	 * @param precision：要保留小数点后几位
	 * @return 转换后的double数字
	 */
	public static Double getNewDouble(double oldVal, int precision) throws Exception {
		Double ret = null;
		double factor = Math.pow(10, precision);
		ret = Math.floor(oldVal * factor + 0.5) / factor;
		return ret;
	}
	
	/**
	 * 
	 * @description 将字符串内部数字加上指定前缀
	 * 
	 * @param replaceMsg 待替换的字符串
	 * @param prefix 待加上的前缀
	 * @return
	 */
	public static String replaceMsgAddPx(String replaceMsg, String prefix) {
		Pattern pattern = Pattern.compile("[0-9]*");

		Matcher m = pattern.matcher(replaceMsg);

		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			String value = m.group();
			if (StringUtils.isNotBlank(value)) {
				m.appendReplacement(sb, prefix + value);
			}
		}
		m.appendTail(sb);
		return sb.toString();
	}

	/**
	 * 
	 * @description 校验数据库中的数据是否为空
	 * 
	 * @param checkValue
	 * @return
	 */
	public static boolean checkDataEmpty(Object checkValue) {
		boolean isEmpty = false;
		if (checkValue == null) {
			isEmpty = true;
		} else {
			String checkValueStr = checkValue.toString().trim();
			if (StringUtils.isEmpty(checkValueStr)) {
				isEmpty = true;
			} else {
				String type = checkValue.getClass().toString();
				if (type.endsWith("Character") || type.endsWith("String")) {
					if (StringUtils.isEmpty(checkValueStr)) {
						isEmpty = true;
					}
				} else if (type.endsWith("Date") || type.endsWith("Timestamp")) {
					if (checkValueStr.equals("1000-01-01") || checkValueStr.equals("1000-01-01 00:00:00.0")) {
						isEmpty = true;
					}
				} else if (type.endsWith("Double") || type.endsWith("Integer") || type.endsWith("Long") || type.endsWith("Float")) {
					if (checkValueStr.equals("-1") || checkValueStr.equals("-1.0")) {
						isEmpty = true;
					}
				}
			}

		}

		return isEmpty;
	}
	
	/**
	 * 
	 * @description  根据占位符替换数据
	 * 
	 * @param str 带替换的数据
	 * @param args 替换的数据值
	 * @return
	 */
	public static String fillStringByArgs(String str, String ... args){
        Matcher m=Pattern.compile("\\{(\\d)\\}").matcher(str);
        while(m.find()){
        	String arg = args[Integer.parseInt(m.group(1))];
        	if (arg == null) {
        		arg = "";
			}
            str=str.replace(m.group(), arg);
        }
        return str;
    }
	
	/**
	 * 
	 * @description 将查询的条件转义防止sql注入
	 * 
	 * @param obj
	 * @return
	 */
	public static String escape(Object obj){
		if(obj == null){
			return "";
		}else{
			return StringEscapeUtils.escapeSql(StringUtils.trim(obj.toString()));
		}
	}

}
