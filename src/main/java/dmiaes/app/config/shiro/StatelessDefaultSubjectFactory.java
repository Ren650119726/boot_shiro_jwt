package dmiaes.app.config.shiro;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;

/** 
* @ClassName: StatelessDefaultSubjectFactory
* @Description:Subject 工厂管理器  
* 			<br>Shiro不创建会话 session 无状态
* @author REEFE
* @date 2017-4-25 上午10:53:38
*  
*/
public class StatelessDefaultSubjectFactory extends DefaultWebSubjectFactory{

	@Override
	public Subject createSubject(SubjectContext context) {
		// 不创建session.
		context.setSessionCreationEnabled(false);
		return super.createSubject(context);
	}
}
