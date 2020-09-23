import java.util.ArrayList;

public interface UserDao {
    ArrayList<User> getAllUsers();
    void save(ArrayList<User> users);
}
