package dmiaes.app.data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;


/**
 * @description 负责业务流程中的各种数据的传输
 */
@SuppressWarnings (value={"all"})
public class BeanData {

	/**
	 * 操作返回的结果
	 * 存储过程返回：-2——存储过程运行中发生完整性错误，-1——存储过程运行异常，0——运行业务失败，1——成功
	 */
	private String flag = null;
	
	/**
	 * 操作返回的信息
	 */
	private String msg = null;
	
	/**
	 * 操作返回的代码
	 */
	private String code = null;
	


	/**
	 * 操作的全部条件参数
	 */
	private Map<String, Object> mapParams = null;
	
	/**
	 * 操作返回的List
	 */
	private List<?> listData = null;
	
	/**
	 * 操作返回的Map
	 */
	private Map<String, Object> mapData = null;
	
	/**
	 * 操作返回的Integer 
	 */
	private Integer intData = null;
	
	/**
	 * 页面异步调用的返回结果
	 * json格式规范 eg：{"flag":"info","msg":"用户名或密码错误！"} 或 {"flag":"ok","msg":"操作成功！","code":"a","items":[{"name":"123"}]}
	 * 注：code与items中有数据则出现该节点，没有数据则不出现该节点。
	 */
	private Map<String, Object> mapAjaxData = null;
	
	
	
	public BeanData() {
		this.mapParams = new HashMap<String, Object>();
	}
	
	public BeanData(Map<String, Object> map) {
		this.mapParams = map;
	}
	
	
	
	/**
	 * @description 判断操作返回的结果是否为成功
	 */
	public Boolean isOperOk() {
		if(this.flag != null && this.flag.equals("ok")){
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * @description 判断操作返回的结果是否失败（业务级失败）
	 * 
	 */
	public Boolean isOperInfo() {
		if(this.flag != null && this.flag.equals("info")){
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * @description 判断操作返回的结果是否为失败（当且仅当 服务器及数据库运行发生异常）
	 * 
	 */
	public Boolean isOperError() {
		if(this.flag == null || this.flag.equals("error")){
			return true;
		} else {
			return false;
		}
	}


	
	/**
	 * @description 记录操作的错误日志（服务器级与数据库级错误）
	 * @param logger：org.apache.log4j.Logger
	 * @param methodName：出现错误的方法名
	 * @param e：Exception
	 */
	public void recordErrorLog(Logger logger, String methodName, Exception e) {
		try {
			
			StringBuffer sbError = new StringBuffer();
			
			sbError.append("\r\n发生错误的方法：").append(logger.getName()).append(".").append(methodName).append("\r\n");
			if(this.mapParams != null){
				sbError.append("发生错误时的各个参数值：\r\n");
				
				Set<String> set = this.mapParams.keySet();
				Iterator<String> iterator = set.iterator();
				while (iterator.hasNext()) {
					String key = iterator.next();
					sbError.append(key).append("：").append(this.mapParams.get(key)).append("\r\n");
				}
			}

			e.printStackTrace();
			logger.error(sbError.toString(), e);
	
		} catch (Exception ex) {
			
			ex.printStackTrace();
			logger.error(ex.getMessage());
			
		} finally {
			this.flag = "error";
			this.msg = "网络异常，请稍后再试！";
		}
	}
	
	/**
	 * @description 自动判断调用增删改操作的存储过程是否运行正常
	 * 				注：	增删改操作的存储过程如果没有返回结果，则不能使用queryForMap方法，否则程序抛出异常。
	 * 					有返回结果的存储过程格式为：
	 * 									flag（操作结果标示符1——执行成功，0——业务失败，-1——存储过程异常，-2——数据完整性异常；flag的值装载到this.flag）
	 *									customCode(自定操作代码；customCode的值装载到this.code)
	 *									result1 ... resultN(第一条语句影响的行数  ... 第N条语句影响的行数)
	 *									其它列 则装载到this.mapData中
	 * 					对于没有返回flag列的执行过程一律视为执行成功。
	 * 					系统错误日志只会自动记录 结果标示符 为-1，-2的执行过程。
	 * 
	 * @param logger：org.apache.log4j.Logger
	 * @param methodName：调用存储过程的方法名
	 * @param mapProResult：存储过程返回的结果Map
	 */
	public void judgeProResult(Logger logger, String methodName, Map<String, Object> mapProResult) {
		try {
			
			if (mapProResult.get("flag") != null) {
				
				Set<String> set = mapProResult.keySet();
				Iterator<String> iterator = set.iterator();
				String key;
				Object value;
				
				while (iterator.hasNext()) {
					
					key = iterator.next();
					value = mapProResult.get(key);
					
					if (key.equals("flag")) {
						
						String proFlag = value.toString().trim();
						
						if (proFlag.equals("1")) {
							this.flag = "ok";
						} else if (proFlag.equals("0")) {
							this.flag = "info";
						} else {
							
							StringBuffer sbError = new StringBuffer();

							sbError.append("\r\n存储过程运行中发生错误，调用该存储过程的方法为：").append(logger.getName()).append(".").append(methodName).append("\r\n");
							sbError.append("发生错误时mapParams中的各个参数值：\r\n");

							Set<String> setTmp0 = this.mapParams.keySet();
							Iterator<String> iteratorTmp0 = setTmp0.iterator();

							while (iteratorTmp0.hasNext()) {
								String keyTmp0 = iteratorTmp0.next();
								sbError.append(keyTmp0).append("：").append(this.mapParams.get(keyTmp0)).append("\r\n");
							}

							sbError.append("存储过程运行返回结果为：\r\n");

							Set<String> setTmp1 = mapProResult.keySet();
							Iterator<String> iteratorTmp1 = setTmp1.iterator();

							while (iteratorTmp1.hasNext()) {
								String keyTmp1 = iterator.next();
								sbError.append(keyTmp1).append("：").append(mapProResult.get(keyTmp1)).append("\r\n");
							}

							throw new Exception(sbError.toString());
						}
						
					} else if (key.equals("customCode")) {
						this.code = (value == null || value.toString().trim().equals("")) ? null : value.toString().trim();
					} else if (key.startsWith("result")) {
						// 不做操作
					} else {
						if (this.mapData == null) {
							this.mapData = new LinkedHashMap<String, Object>();
						}
						this.mapData.put(key, value);
					}
					
				}

			} else {
				this.flag = "ok";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			this.flag = "error";
			logger.error(e.getMessage());
		}
	}
	
	
		
	/**
	 * @description 设置条件参数与值
	 * @param key：条件参数的关键字
	 * @param value：条件参数的值
	 */
	public void putParam(String key, Object value) {
		this.mapParams.put(key, value);
	}
	
	/**
	 * @description 获取指定的条件参数的值
	 * @param key:条件参数的关键字
	 */
	public Object getParam(String key) {
		return this.mapParams.get(key);
	}

	/**
	 * @description 判断指定的条件参数的值是否为null或空
	 * @param key：条件参数的关键字
	 * @return null或空 返回true
	 */
	public Boolean isParamNullOrEmpty(String key) {
		if (this.mapParams.get(key) == null || this.mapParams.get(key).toString().trim().equals("")) {
			return true;
		} else {
			return false;
		}
	}

	
	
	/**
	 * @description 设置操作返回的结果为成功。
	 */
	public void setOperOK() {
		this.flag = "ok";
	}

	/**
	 * @description 设置操作返回的结果为失败（业务级失败）。
	 */
	public void setOperInfo() {
		this.flag = "info";
	}
	
	/**
	 * @description 设置操作返回的结果为失败（当且仅当 服务器及数据库运行发生异常）。
	 */
	public void setOperError() {
		this.flag = "error";
	}

	/**
	 * @description 设置操作返回的信息。
	 */
	public void setOperMsg(String msg) {
		this.msg = msg;
	}
	
	/**
	 * @description 设置操作返回的代码。
	 */
	public void setOperCode(String code) {
		this.code = code;
	}

	/**
	 * @description 设置操作返回的List。
	 */
	public void setListData(List<?> listData) {
		this.listData = listData;
	}
	
	/**
	 * @description 设置操作返回的Map。
	 */
	public void setMapData(Map<String, Object> mapData) {
		this.mapData = mapData;
	}
	
	/**
	 * @description 设置操作返回的Integer。
	 */
	public void setIntegerData(Integer intData) {
		this.intData = intData;
	}


	
	/**
	 * @description 获取操作结果（页面异步调用方式）。
	 * 注：当this.flag为error时，不返回系统任何错误信息给页面。
	 * 返回之前请检查code与list，否则会影响返回格式。
	 */
	public Map<String, Object> getMapAjaxData() {
		this.mapAjaxData = new LinkedHashMap<String, Object>();
		
		if (this.flag == null || this.flag.equals("error")) {
			this.mapAjaxData.put("flag", "error");
			this.mapAjaxData.put("msg", "网络异常，请稍后再试！");
			if (this.code != null && !this.code.trim().equals("")) {
				this.mapAjaxData.put("code", this.code);
			}
		} else {
			this.mapAjaxData.put("flag", this.flag);

			if (this.msg == null || this.msg.trim().equals("")) {
				this.mapAjaxData.put("msg", "");
			} else {
				this.mapAjaxData.put("msg", this.msg);
			}
			
			if (this.code != null && !this.code.trim().equals("")) {
				this.mapAjaxData.put("code", this.code);
			}
			
			if (this.listData != null && !this.listData.isEmpty()) {
				this.mapAjaxData.put("items", this.listData);
			}
		}
		
		return this.mapAjaxData;
	}

	
	
	/**
	 * 获取操作返回的结果
	 */
	public String getOperFlag() {
		return this.flag;
	}
	
	/**
	 * 获取操作返回的信息
	 */
	public String getOperMsg() {
		return this.msg;
	}
	
	/**
	 * 获取操作返回的代码
	 */
	public String getOperCode() {
		return code;
	}
	
	/**
	 * 获取操作中的全部参数
	 */
	public Map<String, Object> getMapParams() {
		return this.mapParams;
	}
	
	/**
	 * 获取操作返回的List
	 */
	public List<?> getListData() {
		return this.listData;
	}
	
	/**
	 * 获取操作返回的Map
	 */
	public Map<String, Object> getMapData() {
		return this.mapData;
	}
	
	/**
	 * 获取操作返回的Integer
	 */
	public Integer getIntegerData() {
		return this.intData;
	}
}