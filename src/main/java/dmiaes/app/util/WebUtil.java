package dmiaes.app.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

import dmiaes.app.base.ResponseBase;

/**
 * @ClassName: WebUtil
 * @Description:
 * @author REEFE
 * @date 2017-4-26 下午3:04:10
 * 
 */
public class WebUtil {
	public static HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
			.getRequestAttributes()).getRequest();

	public static void responseWrit(ResponseBase<?> responseBase) {
		ServletWebRequest servletWebRequest = new ServletWebRequest(request);
		HttpServletResponse response = servletWebRequest.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.append(JsonUtils.toJson(responseBase));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	public static void responseWrit(HttpServletResponse response,
			ResponseBase<?> responseBase) {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.append(JsonUtils.toJson(responseBase));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}
	public static void responseWrit(HttpServletResponse response,
			ResponseEntity<String> responseEntity) {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.append(JsonUtils.toJson(responseEntity));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}
}
