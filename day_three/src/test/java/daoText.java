import com.itheima.dao.UserDao;
import com.itheima.model.User;
import org.junit.Test;

import java.net.URLEncoder;

public class daoText {
    private UserDao userDao = new UserDao();

    @Test
    public void testGetUserByUserName() {
        System.out.println(userDao.getUserByUserName("11"));
    }

    @Test
    public void testregister() {
        try {
            User testUser = new User(0, "miko", "333473693557131227847482670787409432147", "rose", null, null, null, null);
            System.out.println(userDao.register(testUser));
        } catch (Exception e) {

        }
    }

    @Test
    public void text001() {
        String str = URLEncoder.encode("window.onload = function () {alert(\"nihao\");};</script>");
        System.out.println(str);
    }
}
