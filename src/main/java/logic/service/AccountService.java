package logic.service;

import logic.database.IOMySQL;
import logic.database.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("AccountService")
public class AccountService {

    @Autowired
    IOMySQL mySql;

    public void addUserToMySQL(User user) {
        mySql.addAccount(user.getUserName(), user.getPassword(), false);
    }

    public boolean accountIsNotValid(User user) {
        if (mySql.validateUserName(user.getUserName())) {
            if (mySql.validateUserPassword(user.getUserName(), user.getPassword())) {
                return false;
            }
        }
        return true;
    }

    public boolean accountIsAdmin(User user) {
        return mySql.isAdmin(user.getUserName());
    }

    public boolean passwordIsEmpty(User user) {
        return user.getPassword().equals("");
    }

    public boolean userNameIsExist(User user) {
        return mySql.validateUserName(user.getUserName());
    }

    public boolean isNotAdmin(User user) {
        return !user.isAdminRights();
    }
}
