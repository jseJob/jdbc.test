import java.sql.*;

public class AccountDao {

    static final String url="jdbc:postgresql://localhost:5432/postgres";
    static final String username="postgres";
    static final String password="123";

  public static Account findAccount(String nameParam) {            //метод, возвращающий найденный аккаунт
        Account account = new Account(null);

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM account WHERE name = ?")) {
            Class.forName("org.postgresql.Driver");
            preparedStatement.setString(1, nameParam);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.isBeforeFirst() || resultSet==null) {    //выводит "no data", если в базе не оказалось совпадений; будет возвращен пустой аккаунт
                    System.out.println("no data");
                } else {
                    while (resultSet.next()) {
                        account.setName(resultSet.getString(1));
                        account.setLastName(resultSet.getString(2));
                        account.setEmail(resultSet.getString(3));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return account;
    }

    public static void editAccount(String nameParam, String lastnameParam ){            //метод, меняющий фамилию у указанного аккаунта

        try (Connection connection= DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement =connection.prepareStatement("UPDATE account SET lastname=? WHERE name = ?")){
            Class.forName("org.postgresql.Driver");
            preparedStatement.setString(1,lastnameParam);
            preparedStatement.setString(2,nameParam);
            preparedStatement.executeUpdate();
            if(preparedStatement.executeUpdate()>0) System.out.println("account was updated");      //выводит сообщение, если операция прошла успешно

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


}
