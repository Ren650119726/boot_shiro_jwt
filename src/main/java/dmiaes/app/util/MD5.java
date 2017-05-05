package dmiaes.app.util;

import java.security.MessageDigest;

/**
 * @description 提供MD5加密
 */
public class MD5 {
	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	/**
	 * 转换字节数组为16进制字串
	 * @param b：字节数组
	 * @return 16进制字串
	 */
	private static String byteToHex(byte[] b) throws Exception {
		StringBuffer sbResult = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			sbResult.append(byteToHex(b[i]));
		}
		return sbResult.toString();
	}

	private static String byteToHex(byte b) throws Exception {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	/**
	 * MD5加密
	 * @param strSource：需加密的字符串
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String strSource) throws Exception {
		String resultString = null;
		resultString = new String(strSource);
		MessageDigest md = MessageDigest.getInstance("MD5");
		resultString = byteToHex(md.digest(resultString.getBytes()));
		return resultString;
	}

}