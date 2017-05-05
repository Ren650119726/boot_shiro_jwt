package dmiaes.app.data;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @description 页面传递过来的数据
 */
public class MapData {

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
			while (iterator.hasNext()) {
				key = iterator.next();
				value = this.map.get(key);
				
				if (value == null) {
					continue;
				} else if (value.toString().trim().equals("")) {
					value = null;
					this.map.put(key, value);
				} else {
					this.map.put(key, value);
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
