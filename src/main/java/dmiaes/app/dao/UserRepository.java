package dmiaes.app.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import dmiaes.app.model.User;

/** 
* @ClassName: IUserService
* @Description: 用户
* @author REEFE
* @date 2017-4-28 下午1:30:58
*  
*/
public interface UserRepository extends JpaRepository<User, Integer> {
	
    User queryByUserid(Long userid);

    User queryByUsername(String username);

    User queryByUsernameAndPassword(String username, String password);

    //注意有序的传递参数
    @Query("FROM User u WHERE u.username=?1 AND u.password IS NOT NULL")
    List<User> queryAll(String username);

    @Query("UPDATE User u SET u.password=?2 WHERE u.username=?1")
    //只要涉及到修改或删除就需要加下面两个注解
    @Modifying
    @Transactional
    void updatePwd(String username, String pwd);

    @Query("DELETE FROM User u WHERE u.username=?1")
    @Modifying
    @Transactional
    void deleteByUsername(String username);
    
}
