package dmiaes.app.config.druid;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
 
/** 
* @ClassName: DruidConfiguration
* @Description: druid 配置. 这样的方式不需要添加注解：@ServletComponentScan
* @author REEFE
* @date 2017-4-27 下午2:27:49
*  
*/
@Configuration
public class DruidConfiguration {
    @Autowired
    private DataCfg dataCfg;
    /**
     * 注册一个StatViewServlet
     * @return
     */
    @Bean
    public ServletRegistrationBean DruidStatViewServle2(){
       //org.springframework.boot.context.embedded.ServletRegistrationBean提供类的进行注册.
       ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(),"/druid/*");
      
       //添加初始化参数：initParams
      
       //白名单：
       servletRegistrationBean.addInitParameter("allow","127.0.0.1");
       //IP黑名单 (存在共同时，deny优先于allow) : 如果满足deny的话提示:Sorry, you are not permitted to view this page.
//       servletRegistrationBean.addInitParameter("deny","192.168.1.73");
       //登录查看信息的账号密码.
       servletRegistrationBean.addInitParameter("loginUsername","admin");
       servletRegistrationBean.addInitParameter("loginPassword","123456");
       //是否能够重置数据.
       servletRegistrationBean.addInitParameter("resetEnable","false");
       return servletRegistrationBean;
    }
   
    /**
     * 注册一个：filterRegistrationBean
     * @return
     */
    @Bean
    public FilterRegistrationBean druidStatFilter2(){
      
       FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
      
       //添加过滤规则.
       filterRegistrationBean.addUrlPatterns("/*");
      
       //添加不需要忽略的格式信息.
       filterRegistrationBean.addInitParameter("exclusions","*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
       return filterRegistrationBean;
    }
    /**
     * druid初始化
     * @return
     * @throws SQLException
     */
    @Primary //默认数据源
    @Bean(name = "dataSource",destroyMethod = "close")
    public DruidDataSource Construction() throws SQLException {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(dataCfg.getUrl());
        dataSource.setUsername(dataCfg.getUsername());
        dataSource.setPassword(dataCfg.getPassword());
        dataSource.setDriverClassName(dataCfg.getDriver());
        //配置最大连接
        dataSource.setMaxActive(dataCfg.getMaxActive());
        //配置初始连接
        dataSource.setInitialSize(dataCfg.getInitialSize());
        //配置最小连接
        dataSource.setMinIdle(dataCfg.getMinIdle());
        //连接等待超时时间
        dataSource.setMaxWait(dataCfg.getMaxWait());
        //间隔多久进行检测,关闭空闲连接
        dataSource.setTimeBetweenEvictionRunsMillis(dataCfg.getTimeBetweenEvictionRunsMillis());
        //一个连接最小生存时间
        dataSource.setMinEvictableIdleTimeMillis(dataCfg.getMinEvictableIdleTimeMillis());
        //用来检测是否有效的sql
        dataSource.setValidationQuery(dataCfg.getValidationQuery());
        dataSource.setTestWhileIdle(dataCfg.isTestWhileIdle());
        dataSource.setTestOnBorrow(dataCfg.isTestOnBorrow());
        dataSource.setTestOnReturn(dataCfg.isTestOnReturn());
        //打开PSCache,并指定每个连接的PSCache大小
        dataSource.setPoolPreparedStatements(dataCfg.isPoolPreparedStatements());
        dataSource.setMaxOpenPreparedStatements(dataCfg.getMaxPoolPreparedStatementPerConnectionSize());
        //配置sql监控的filter
        dataSource.setFilters(dataCfg.getFilters()); 
        dataSource.setConnectionProperties(dataCfg.getConnectionProperties()); 
        try {
            dataSource.init();
        } catch (SQLException e) {
            throw new RuntimeException("druid datasource init fail");
        }
        return dataSource;
    }
}