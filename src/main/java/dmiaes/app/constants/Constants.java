package dmiaes.app.constants;

public interface Constants {

	/**
	 * 数据请求返回码
	 */
	public static final int RESCODE_SUCCESS = 1000;				//成功
	public static final int RESCODE_SUCCESS_MSG = 1001;			//成功(有返回信息)
	public static final int RESCODE_EXCEPTION = 1002;			//请求抛出异常
	public static final int RESCODE_NOLOGIN = 1003;				//未登陆状态
	public static final int RESCODE_NOEXIST = 1004;				//查询结果为空
	public static final int RESCODE_NOAUTH = 1005;				//无操作权限
	
	/**
	 * jwt
	 */
	public static final String JWT_ID = "jwt";
	public static final String JWT_SECRET = "7786df7fc3a34e26a61c034d5ec8245d";
	public static final String JWT_TYPE = "bearer";
	public static final int JWT_TTL = 60*60*1000;  //millisecond
	public static final int JWT_REFRESH_INTERVAL = 55*60*1000;  //millisecond
	public static final int JWT_REFRESH_TTL = 12*60*60*1000;  //millisecond
	
	/**
	 * 角色
	 */
	int SUPER_ADMINISTRATOR = 1; //超级管理员

	/**
	 * 菜单类型
	 */
	int MENU_PARENT_ID = 1;//父菜单Id
	int MENU_FUNCTION= 1; //功能
	int MENU_BUTTON = 2 ; //按钮



	/**
	 * 缓存Key
	 */
	String CACHE_TOKNID = "app:tokenId_";         //tokenId
	String CACHE_USER_ID = "app:userId_";         //用户Id
	long CACHE_TOKENID_TIME = 30*60;                       //tokenId有效时间


}
