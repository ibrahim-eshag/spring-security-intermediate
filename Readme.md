# Points learned

### continue-Basic Authentication

Besides the InMemoryUserDetailsManager, we often use other UserDetailManager
implementation, JdbcUserDetailsManager. The JdbcUserDetailsManager
interface manages users in an SQL database. It connects to the database directly through
JDBC.

* InMemoryUserDetailsManager, JdbcUserDetailsManager and LdapUserDetailsManager (not used much now days) interfaces all
  extends
  UserDetailsManager.
* UserDetailsManager is also extending UserDetailsService with additional features (create, update, delete a user and
  update the password, etc.) .
* The UserDetailsManager interface looks like following:

```java 
public interface UserDetailsManager extends UserDetailsService {
    void createUser(UserDetails user);

    void updateUser(UserDetails user);

    void deleteUser(String username);

    void changePassword(String oldPassword, String newPassword);

    boolean userExists(String username);
}
```

#### Following is Spring boot architecture , and where JSBCUserDetailsManager fits in the Architecture.

![JSBCUserDetailManager.png](src/main/resources/JSBCUserDetailManager.png)