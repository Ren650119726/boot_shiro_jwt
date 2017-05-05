package dmiaes.app.util;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

/** 
* @ClassName: JacksonObjectMapper
* @Description: 解决Jackson 差8小时的问题
* @author REEFE
* @date 2017-4-12 下午2:50:09
*  
*/
@Component("jacksonObjectMapper")
public class JacksonObjectMapper extends ObjectMapper {

    private static final long serialVersionUID = 4288193147502386170L;

    private static final Locale CHINA = Locale.CHINA;
    
    public JacksonObjectMapper() {
        this.setLocale(CHINA);
        this.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", CHINA));
        this.setTimeZone(TimeZone.getTimeZone("GMT+8"));
    }

}
