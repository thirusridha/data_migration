package scrips.datamigration.common;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import scrips.datamigration.jpa.user.JpaUser;
import scrips.datamigration.jpa.user.UserDAO;
@Component
public class UserData {
	@Autowired
	private  UserDAO userDAO;
	public  String getSystemUserId() {
	try {
		List<JpaUser> userList = userDAO.findByName("System");
		if (userList != null && !userList.isEmpty())
			return userList.get(0).getId();
	} catch (Exception e) {
		e.printStackTrace();
	}
	return null;
	}
}
