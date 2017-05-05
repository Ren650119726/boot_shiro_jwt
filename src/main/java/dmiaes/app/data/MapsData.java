package dmiaes.app.data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description 页面传递过来的数据
 */
public class MapsData {

	/**
	 * 页面的map数据
	 */
	private Map<String, Object> map = null;

	/**
	 * 页面的复选框数据
	 */
	private Object[] objarray = null;

	/**
	 * @description 获取页面传入数据，并将数据为""的转换为null
	 * 
	 * @return 以Map方式返回页面传入数据
	 */
	public Map<String, Object> getMap() {
		if (this.map != null) {
			Set<String> set = this.map.keySet();
			Iterator<String> iterator = set.iterator();
			String key;
			Object value;

			Pattern pattern = Pattern.compile("^map\\[.*\\]\\=.*");

			while (iterator.hasNext()) {
				key = iterator.next();
				value = this.map.get(key);
				if (value == null) {
					continue;
				} else if (value.toString().trim().equals("") || value.toString().trim().equals("undefined")) {
					value = null;
					this.map.put(key, value);
				} else {
					boolean isMaps = pattern.matcher(value.toString()).matches();
					if (isMaps) {
						Map<String, Object> mapTwo = new HashMap<String, Object>();
						for (String mapOne : value.toString().split("\\_\\^\\_")) {
							// 查找规则公式中[]以内的字符和=后面的内容
							Pattern keyValue = Pattern.compile("(\\[(\\'{0,1}|\\\"{0,1}).*?(\\\"{0,1}|\\'{0,1})\\])(\\=.*)");
							Matcher kvMatcher = keyValue.matcher(mapOne);
							while (kvMatcher.find()) {
								// 去掉[]和里面的单引号和双引号
								String key_1 = kvMatcher.group(1).replaceAll("\\[(\\'{0,1}\\\"{0,1})|(\\\"{0,1}\\'{0,1})\\]", "");
								// 去掉=和后面的单引号和双引号   
								String value_1 = kvMatcher.group(4).replaceAll("\\=(\\'{0,1}|\\\"{0,1})|(\\\"|\\')", "");
								mapTwo.put(key_1, value_1);
							}

						}
						this.map.put(key, mapTwo);
					} else {
						this.map.put(key, value);
					}
				}
			}
		}
		return map;
	}

	/**
	 * 设置页面数据
	 */
	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

	public Object[] getObjarray() {
		return this.objarray;
	}

	public void setObjarray(Object[] objarray) {
		this.objarray = objarray;
	}

}
