import java.sql.*;

public class JdbcDao {

    // statements required for enabling Database interactions
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS caseworker_login\n" +
            "(\n" +
            " ID SERIAL not null, \n" +
            " un varchar(250) NOT null unique,\n" +
            " pwd varchar(250) not null unique,\n" +
            " PRIMARY KEY(ID)\n" +
            ");";

    private static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String DATABASE_USERNAME = "postgres";
    private static final String DATABASE_PASSWORD = "Ea$1ano01";
    private static final String SELECT_QUERY = "SELECT * FROM caseworker_login where un = ? and pwd = ?";
    private static final String INSERT_QUERY =  "INSERT INTO caseworker_login (un, pwd) VALUES (?, ?)";

    public boolean validate(String username, String password) {
        // Checks if the username and password entered match the record in the table
        try (Connection connection = DriverManager
                .getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                connection.close();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // print SQL exception information
        }
        return false;
    }

    public void createNewTable() {
        // Create the table if not already exists
        try {
            Connection connection = DriverManager
                    .getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
            Statement createTable = connection.createStatement();
            createTable.executeUpdate(CREATE_TABLE);
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertRecordIntoCreatedTable() {
        // Insert the username and password into the table if not already exists
        try {
            Connection connection = DriverManager
                    .getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
             PreparedStatement insertQuery = connection.prepareStatement(INSERT_QUERY);{
                insertQuery.setString(1, "govuk");
                insertQuery.setString(2, "Pa$$w0rd1234");
                // Execute the query or update query
                insertQuery.executeUpdate();
                connection.close();}
        } catch (SQLException e) {
//            e.printStackTrace();
            // print SQL exception information
        }
    }
}
