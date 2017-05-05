package dmiaes.app.filter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/** 
* @ClassName: SimpleCorsFilter
* @Description: 解决跨域
* @author REEFE
* @date 2017-4-26 下午12:52:32
*  
*/
@Configuration
public class SimpleCorsFilter extends WebMvcConfigurerAdapter {
	  private CorsConfiguration buildConfig() {
	        CorsConfiguration corsConfiguration = new CorsConfiguration();
	        corsConfiguration.addAllowedOrigin("*"); // 允许任何域名使用
	        corsConfiguration.addAllowedHeader("*"); // 允许任何头
	        corsConfiguration.addAllowedMethod("*"); // 允许任何方法（post、get等）
	        corsConfiguration.setAllowCredentials(true);

	        return corsConfiguration;
	    }

	    @Bean
	    public CorsFilter corsFilter() {
	        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	        source.registerCorsConfiguration("/**", buildConfig()); // 4
	        return new CorsFilter(source);
	    }

	    @Override
	    public void addCorsMappings(CorsRegistry registry) {
	        registry.addMapping("/**")
	                .allowedOrigins("*")
	                .allowCredentials(true)
	                .allowedMethods("GET", "POST", "DELETE", "PUT")
	                .maxAge(3600);
	    }
}
