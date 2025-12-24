package lifemanagement;

import lifemanagement.auth.User;
import lifemanagement.auth.UserManager;

public class Main {
    public static void main(String[] args) {
        UserManager um = new UserManager();

        boolean ok = um.register(new User("testuser", "1234", "dark"));
        System.out.println("Register ok? " + ok);

        boolean loginOk = um.login("testuser", "1234");
        System.out.println ("Login ok? " + loginOk);

        }
    }
