import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public abstract class TableTest {
    Connection conn = null;
    static WriteResults wr = null;

    @Before
    public void getConnection() {
        Connection con = null;
        try {
            Conf config = new Conf();

            Properties props = new Properties();
            props.put("myConnectionURL", config.getMyConnectionURL());
            props.put("user", config.getUser());
            props.put("password", config.getPassword());
            System.out.println("myConnectionURL " + props.getProperty("myConnectionURL"));
            System.out.println("user " + props.getProperty("user"));
            //System.out.println("password" + props.getProperty("password"));

            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection(props.getProperty("myConnectionURL"), props);
            System.out.println("Connection Successful");
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.conn = con;
    }


    @BeforeClass
    public static void openResults() {
        wr = new WriteResults("AgncyOrgWhTest.html");
        wr.pageHeader();
    }

    @AfterClass
    public static void closeResults() {
        wr.closePage();
        wr.printWriter.flush();
        wr.printWriter.close();
    }
}
