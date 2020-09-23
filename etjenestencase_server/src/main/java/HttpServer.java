//import com.google.gson.JsonObject;

import javax.sql.DataSource;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class HttpServer {
    private int port;

    public HttpServer(int port) {
        this.port = port;
    }

    public void startServer() {
        try {
            ServerSocket server = new ServerSocket(port);
            System.out.println("Server running");
            Socket socket = server.accept();
            PrintWriter pw = new PrintWriter(socket.getOutputStream());

            while (true) {
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
                String headers = br.readLine();
                String requestType = headers.split(" ")[0];
                String response = readRequest(br, requestType);
                pw.println(response);
            }
        }catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public String readRequest(BufferedReader br, String requestType) throws IOException {
        StringBuilder payload = new StringBuilder();
        String response = "";
        if(requestType.equals("POST")) {
            try {
                while (br.ready()) {
                    payload.append((char) br.read());
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            int startOfLastHeader = payload.indexOf("Accept-Language: ") + 1;
            String lastOfReq = payload.substring(startOfLastHeader);
            String[] split = lastOfReq.split("\r\n\r\n");
            File file = new File("datasett.csv");
            if (file.createNewFile()) {
                System.out.println("File created " + file.getName());
            } else {
                file.delete();
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter("datasett.csv");
            fileWriter.write(split[1]);
            sendToFileReader(file);
            response = "HTTP/1/1 200 OK\r\n\r\n";
        } /*else if (requestType.equals("GET")) {
            DataSource ds = new PostgresqlDataSource().getDataSource();
            UserDaoImpl userDao = new UserDaoImpl(ds);
            JsonObject json = new JsonObject();

            ArrayList<User> users = userDao.getAllUsers();
            for(int i = 0; i < users.size(); i++) {
                JsonObject userObject = new JsonObject();
                User user = users.get(i);
                userObject.addProperty("seq", user.getSeq());
                userObject.addProperty("fname", user.getfName());
                userObject.addProperty("lname", user.getlName());
                userObject.addProperty("age", user.getAge());
                userObject.addProperty("street", user.getStreet());
                userObject.addProperty("city", user.getCity());
                userObject.addProperty("state", user.getState());
                userObject.addProperty("lat", user.getLat());
                userObject.addProperty("lng", user.getLng());
                userObject.addProperty("ccnumber", user.getCcNumber());
                json.add("user", userObject);
            }
            response = json.toString();
        }*/
        return response;
    }

    public void sendToFileReader(File file) throws IOException {
        CsvFileReader cfr = new CsvFileReader(new FileReader(file));
        ArrayList contents = cfr.readFile();
        DataSource ds = new PostgresqlDataSource().getDataSource();
        UserDaoImpl userDao = new UserDaoImpl(ds);
        userDao.createTable();
        userDao.save(contents);
    }

    public static void main(String[] args) {
        HttpServer server = new HttpServer(8080);
        server.startServer();
    }
}
