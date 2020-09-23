import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserDaoImpl implements UserDao {
    DataSource dataSource;

    public UserDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Connection dataBaseConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS USERS (\n"
                + "     seq SERIAL PRIMARY KEY, \n"
                + "     fname text, \n"
                + "     lname text, \n"
                + "     age integer, \n"
                + "     street text, \n"
                + "     city text, \n"
                + "     state text, \n"
                + "     lat decimal, \n"
                + "     lng decimal, \n"
                + "     ccnumber bigint\n"
                + ");";
        try(Connection conn = dataBaseConnection()) {
            PreparedStatement stmnt = conn.prepareStatement(sql);
            stmnt.execute();
            System.out.println("Created table");
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public ArrayList<User> getAllUsers() {
        String sql = "SELECT * FROM users";
        ArrayList<User> users = new ArrayList<>();
        try(Connection conn = dataBaseConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int seq = rs.getInt("seq");
                String fname = rs.getString("fname");
                String lname = rs.getString("lname");
                int age = rs.getInt("age");
                String street = rs.getString("street");
                String city = rs.getString("city");
                String state = rs.getString("state");
                Float lat = rs.getFloat("lat");
                Float lng = rs.getFloat("lng");
                Float ccNumber = rs.getFloat("ccnumber");
                User user = new User(seq, fname, lname, age, street, city, state, lat, lng, ccNumber);
                users.add(user);
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return users;
    }

    @Override
    public void save(ArrayList<User> users) {
        String sql = "INSERT INTO users(seq, fname, lname, age, street, city, state, lat, lng, ccnumber) \n"
                    +"      VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dataBaseConnection();
             PreparedStatement stmnt = conn.prepareStatement(sql)) {
            ArrayList<User> currentUsers = getAllUsers();
            for(int i = 0; i < users.size(); i++) {
                User user = users.get(i);
                stmnt.setInt(1, user.getSeq());
                stmnt.setString(2, user.getfName());
                stmnt.setString(3, user.getlName());
                stmnt.setInt(4, user.getAge());
                stmnt.setString(5, user.getStreet());
                stmnt.setString(6, user.getCity());
                stmnt.setString(7, user.getStreet());
                stmnt.setFloat(8, user.getLat());
                stmnt.setFloat(9, user.getLng());
                stmnt.setFloat(10, user.getCcNumber());
                if(!users.contains(user)) {
                    stmnt.execute();
                }
            }
            System.out.println("Save successful");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
}
