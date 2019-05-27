import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.sql.*;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;

import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;

import org.mockito.Mockito;
import static org.mockito.Mockito.*;

import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.api.mockito.PowerMockito;



@RunWith(PowerMockRunner.class)
@PrepareForTest({AccountDao.class, DriverManager.class, Connection.class})
public class AccountDaoTest {

    Connection con = mock(Connection.class);
    PreparedStatement stmt =mock(PreparedStatement.class);
    ResultSet rs = mock(ResultSet.class);
    AccountDao dao = Mockito.mock(AccountDao.class);

    private Account p;

    @Before
    public void setUp() throws SQLException {

        p = new Account("name1");
        p.setLastName("lastname21");
        p.setEmail("e@mail1");

        Assert.assertNotNull(con);
        Assert.assertNotNull(stmt);
        Assert.assertNotNull(rs);

        when(con.prepareStatement(any(String.class))).thenReturn(stmt);             //общие правила поведения
        when(rs.first()).thenReturn(true);

        when(rs.getString(1)).thenReturn(p.getName());
        when(rs.getString(2)).thenReturn(p.getLastName());
        when(rs.getString(3)).thenReturn(p.getEmail());
        when(stmt.executeQuery()).thenReturn(rs);


    }

    @Test
    public void findAccountBehaviorTest() throws SQLException {                     //тест поведения метода findAccount

        PowerMockito.mockStatic(DriverManager.class);
        PowerMockito.when(DriverManager.getConnection(
                Mockito.eq("jdbc:postgresql://localhost:5432/postgres"),
                Mockito.eq("postgres"),
                Mockito.eq("123"))).thenReturn(con);

        Mockito.when(con.prepareStatement(Mockito.eq("SELECT * FROM account WHERE name = ?"))).thenReturn(stmt);
        Mockito.when(stmt.executeQuery()).thenReturn(rs);

        Account result = dao.findAccount("name1");
        Assert.assertNotNull(result);

        Mockito.verify(stmt).setString(Mockito.eq(1), Mockito.eq("name1"));

        Mockito.verify(stmt).executeQuery();
        Mockito.verify(con).prepareStatement(Mockito.eq("SELECT * FROM account WHERE name = ?"));

    }

    @Test
    public void findAccountTest() {                                                     //тест на корректный результат работы метода

        AccountDao.findAccount("name1");
        Account r = AccountDao.findAccount("name1");
        assertEquals(p, r);
    }

    @Test
    public void editAccountBehaviorTest()  throws SQLException{                          //тест поведения метода editAccount
        PowerMockito.mockStatic(DriverManager.class);
        PowerMockito.when(DriverManager.getConnection(
                Mockito.eq("jdbc:postgresql://localhost:5432/postgres"),
                Mockito.eq("postgres"),
                Mockito.eq("123"))).thenReturn(con);

        Mockito.when(con.prepareStatement(Mockito.eq("UPDATE account SET lastname=? WHERE name = ?"))).thenReturn(stmt);
        Mockito.when(stmt.executeUpdate()).thenReturn(1);

        dao.editAccount("name2", "lastname22");

        Mockito.verify(stmt).setString(Mockito.eq(1), Mockito.eq("lastname22"));
        Mockito.verify(stmt).setString(Mockito.eq(2), Mockito.eq("name2"));

        Mockito.verify(stmt,  times(2)).executeUpdate();
        Mockito.verify(con).prepareStatement(Mockito.eq("UPDATE account SET lastname=? WHERE name = ?"));

    }

    @Test
    public void editAccountTest() {                                                  //тест на корректный результат работы метода

        String editString = "lastname22";
        AccountDao.editAccount("name2", editString);
        Account r = AccountDao.findAccount("name2");

        assertEquals(editString, r.getLastName());

    }

}

