import org.junit.*;
import org.junit.runners.MethodSorters;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AgncyOrgWhTest {

    Connection conn = null;

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


    @Test

    /*
     * -- EXPECT to get 1 row where AGNCY_ORG_WID = 0; AGNCY_WID = 0; DPRTMNT_AGNCY_SHRT_CD = 'UN'; AGNCY_SHRT_CD = 'UN'; ORG_CD = 'UN'.
     */
    public void test1() {
    	System.out.println("Starting AgncyOrgWhTest.test1");
        String sql = "Select AGNCY_ORG_WID, \n" +
                "AGNCY_WID, \n" +
                "DPRTMNT_AGNCY_SHRT_CD, \n" +
                "AGNCY_SHRT_CD, \n" +
                "ORG_CD, \n" +
                "CURR_SW, \n" +
                "INSERT_DATE, \n" +
                "UPDATE_DATE, \n" +
                "SITE_WID, \n" +
                "PSEUDO_CITY_CODE, \n" +
                "DELETED_FLAG \n" +
                "from dtsdm.AGNCY_ORG_WH ao \n" +
                "where ao.agncy_org_wid = 0 \n";

        int rowCount = 0;
        int agncyOrgWid = 0;
        int agncyWid = 0;
        String dprtmntAgncyShrtCd = null;
        String agncyShrtCd = null;
        String orgCd = null;

        System.out.println("Starting AgncyOrgWhTest.test1,sql1");
        try {
            try (PreparedStatement ps = this.conn.prepareStatement(sql)) {
                //ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    //System.out.println("Size of results = " + rs.getInt(1));
                    while (rs.next()) {
                        agncyOrgWid = rs.getInt("AGNCY_ORG_WID");
                        agncyWid = rs.getInt("AGNCY_WID");
                        dprtmntAgncyShrtCd = rs.getString("DPRTMNT_AGNCY_SHRT_CD");
                        agncyShrtCd = rs.getString("AGNCY_SHRT_CD");
                        orgCd = rs.getString("ORG_CD");
                        rowCount++;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        assertEquals(1, rowCount);
        assertEquals(0, agncyOrgWid);
        assertEquals(0, agncyWid);
        assertEquals("UNK", dprtmntAgncyShrtCd);
        assertEquals("UNK", agncyShrtCd);
        assertEquals("UNK", orgCd);

        System.out.println("test1 rowCount= " + rowCount);
        System.out.println("test1 agncyOrgWid= " + agncyOrgWid);
        System.out.println("test1 agncyWid= " + agncyWid);
        System.out.println("test1 dprtmntAgncyShrtCd= " + dprtmntAgncyShrtCd);
        System.out.println("test1 agncyShrtCd= " + agncyShrtCd);
        System.out.println("test1 orgCd= " + orgCd);
        
        System.out.println("Finish AgncyOrgWhTest.test1");
        System.out.println();
    }

    @Test

    /*
     *
     */
    public void test2() {
    	System.out.println("Starting AgncyOrgWhTest.test2");
        String sql = "Select distinct ao.agncy_org_wid, count(*) \n" +
                "From dtsdm.agncy_org_wh ao \n" +
                "Group by ao.agncy_org_wid \n" +
                "order by ao.AGNCY_ORG_WID";

        String sql1 = "Select  count(*) \n" +
                "From dtsdm.agncy_org_wh ao " ;
        int rowCount = 0;
        int agncyOrgWid = 0;
        int count = 0;
        int totalCount = 0;

        System.out.println("Starting AgncyOrgWhTest.test2,sql");
        try {
            try (PreparedStatement ps = this.conn.prepareStatement(sql)) {
                //ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    //System.out.println("Size of results = " + rs.getInt(1));
                    while (rs.next()) {
                        agncyOrgWid = rs.getInt("AGNCY_ORG_WID");
                        count = rs.getInt("count(*)");

                        rowCount++;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        System.out.println("Starting AgncyOrgWhTest.test2,sql1");
        try {
            try (PreparedStatement ps = this.conn.prepareStatement(sql1)) {
                //ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    //System.out.println("Size of results = " + rs.getInt(1));
                    while (rs.next()) {
                        totalCount = rs.getInt("count(*)");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        assertEquals(rowCount, totalCount);

        System.out.println("test1 rowCount= " + rowCount + " total count = " + totalCount);
        
        System.out.println("Finish AgncyOrgWhTest.test2");
        System.out.println();

    }


    @Test
    /*
     * - Check the values of the AGNCY_ORG_WH.AGNCY_WID  column. Pass
     * '-- EXPECT to see the distinct values of AGNCY_WID from both below queries to be equal:
     */
    public void test3() {
    	System.out.println("Starting AgncyOrgWhTest.test3");
    	
        String sql = "select distinct agncy_org_wh.agncy_wid\n" +
                "from dtsdm.agncy_org_wh \n";

        String sql1 = "select distinct ao.agncy_wid, ao.agncy_shrt_cd, substr(ol.u##org,2,1) substr\n" +
                "from dtsdm.agncy_org_wh ao,DTSDM_SRC_STG.orglist ol\n" +
                "where ao.agncy_shrt_cd = substr(ol.u##org,2,1)\n" +
                "order by agncy_wid" ;

        ArrayList<Integer >sql1AgncyWid = new ArrayList<Integer>();
        ArrayList<Integer> sql2AgncyWid = new ArrayList<Integer>();
        ArrayList<String> sql2AgncyShortCd = new ArrayList<String>();
        ArrayList<String> sql2Substr = new ArrayList<String>();
        int distinctCount = 0;
        int agncyWid = 0;
        String agncyShrtCd = null;
        String substr = null;

        int rowCount = 0;

        System.out.println("Starting AgncyOrgWhTest.test3,sql");
        try {
            try (PreparedStatement ps = this.conn.prepareStatement(sql)) {
                //ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    //System.out.println("Size of results = " + rs.getInt(1));
                    while (rs.next()) {
                        agncyWid = rs.getInt("AGNCY_WID");
                        sql1AgncyWid.add(agncyWid);
                        distinctCount++;
                        System.out.println(distinctCount + " Sql 1  agncyWid = " + agncyWid );
                      }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Starting AgncyOrgWhTest.test3,sql1");
        try {
            try (PreparedStatement ps = this.conn.prepareStatement(sql1)) {
                //ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    //System.out.println("Size of results = " + rs.getInt(1));

                    while (rs.next()) {
                        rowCount++;
                        agncyWid = rs.getInt("AGNCY_WID");
                        sql2AgncyWid.add(agncyWid);

                        agncyShrtCd = rs.getString("AGNCY_SHRT_CD");
                        sql2AgncyShortCd.add(agncyShrtCd);

                        substr = rs.getString("SUBSTR");
                        sql2Substr.add(substr);

                        System.out.println(rowCount + " Sql 2  agncyWid = " + agncyWid + "  ; agncyShrtCd  = " + agncyShrtCd + " ; substr = " + substr);


                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("distinctCount = rowCount " + distinctCount + " = " + rowCount);
        assertEquals(distinctCount,rowCount);
        System.out.println("Finish AgncyOrgWhTest.test3");
        System.out.println();
    }

    @Test
        /*
         * Check the values of the AGNCY_ORG_WH.DPRTMNT_AGNCY_SHORT_CD column
         */
    public void test4() {
    	System.out.println("Starting AgncyOrgWhTest.test4");
    	
        String sql = "select distinct ao.DPRTMNT_AGNCY_SHRT_CD \n" + 
        		"from DTSDM.AGNCY_ORG_WH ao \n" + 
        		"";

        String sql1 = "Select distinct substr(ol.U##ORG,0,2) \n" +
                "From DTSDM_SRC_STG.ORGLIST ol" ;
                

        int distinctCount = 0;
        int rowCount = 0;

        System.out.println("Starting AgncyOrgWhTest.test4,sql");
        try {
            try (PreparedStatement ps = this.conn.prepareStatement(sql)) {
                //ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    //System.out.println("Size of results = " + rs.getInt(1));
                    while (rs.next()) {
                    	distinctCount++;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Starting AgncyOrgWhTest.test4,sql1");
        try {
            try (PreparedStatement ps = this.conn.prepareStatement(sql1)) {
                //ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    //System.out.println("Size of results = " + rs.getInt(1));
                    while (rs.next()) {
                    	rowCount++;

                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("distinctCount = rowCount " + distinctCount + " = " + rowCount);
        assertEquals(distinctCount,rowCount);
        
        System.out.println("Finish AgncyOrgWhTest.test4");
        System.out.println();
    }

    @Test
        /*
         * Check the values of the AGNCY_ORG_WH.AGNCY_SHRT_CD column.
         */
    public void test5() {
   	System.out.println("Starting AgncyOrgWhTest.test5");
    	
        String sql = "select distinct ao.DPRTMNT_AGNCY_SHRT_CD \n" + 
        		"from DTSDM.AGNCY_ORG_WH ao \n" + 
        		"";

        String sql1 = "Select distinct substr(ol.U##ORG,0,2) \n" +
                "From DTSDM_SRC_STG.ORGLIST ol" ;
                

        int distinctCount = 0;
        int rowCount = 0;

        System.out.println("Starting AgncyOrgWhTest.test5,sql");
        try {
            try (PreparedStatement ps = this.conn.prepareStatement(sql)) {
                //ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    //System.out.println("Size of results = " + rs.getInt(1));
                    while (rs.next()) {
                    	distinctCount++;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Starting AgncyOrgWhTest.test5,sql1");
        try {
            try (PreparedStatement ps = this.conn.prepareStatement(sql1)) {
                //ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    //System.out.println("Size of results = " + rs.getInt(1));
                    while (rs.next()) {
                    	rowCount++;

                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("distinctCount = rowCount " + distinctCount + " = " + rowCount);
        assertEquals(distinctCount,rowCount);
        
        System.out.println("Finish AgncyOrgWhTest.test5");
        System.out.println();
    }

    @Test
        /*
         * - Check the values of the ORG_CD column.
         */
    public void test6() {
    	System.out.println("Starting AgncyOrgWhTest.test6");
        String sql1 = "select distinct ao.DPRTMNT_AGNCY_SHRT_CD, ao.org_cd \n" + 
        		"from DTSDM.AGNCY_ORG_WH ao \n";

        String sql2 = "select distinct substr(u##org,2,3)  \n" +
                      "from DTSDM_SRC_STG.orglist ol \n" +
                      "where substr(u##org,0,2) in ('DA', 'DF')";

        String sql3 = "select distinct substr(u##org,2,2) \n" +
                      "from DTSDM_SRC_STG.orglist ol \n" +
                      "where substr(u##org,0,2) in ('DN', 'DD', 'DM', 'DJ')";



       int distinctCount = 0;
        int destCount = 0;
        int srcCount = 0;

        System.out.println("Starting AgncyOrgWhTest.test6,sql1");
        try {
            try (PreparedStatement ps = this.conn.prepareStatement(sql1)) {
                //ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    //System.out.println("Size of results = " + rs.getInt(1));
                    while (rs.next()) {
                    	distinctCount++;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        

        System.out.println("Starting AgncyOrgWhTest.test6,sql2");
        try {
            try (PreparedStatement ps = this.conn.prepareStatement(sql2)) {
                //ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    //System.out.println("Size of results = " + rs.getInt(1));
                    while (rs.next()) {
                        destCount++;

                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        
        System.out.println("Starting AgncyOrgWhTest.test6,sql3");
        try {
            try (PreparedStatement ps = this.conn.prepareStatement(sql3)) {
                //ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    //System.out.println("Size of results = " + rs.getInt(1));
                    while (rs.next()) {
                        srcCount++;

                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("Distinct Count  " + distinctCount );
        assertEquals(destCount,srcCount);
        
        System.out.println("Destination Count = Source wCount " + destCount + " = " + srcCount);
        assertEquals(destCount,srcCount);
        
        System.out.println("Finish AgncyOrgWhTest.test6");
        System.out.println();
    }

    @Test
        /*
         * Check the population of the DCMNT_WH.CURR_SW column
         */
    public void test7() {
    	System.out.println("Starting AgncyOrgWhTest.test7");
        String sql1 = "Select distinct AGNCY_ORG_WH.CURR_SW, count(*) \n" + 
        		"From DTSDM.AGNCY_ORG_WH \n" + 
        		"Group by AGNCY_ORG_WH.CURR_SW \n" ;;



        int distinctCount = 0;
        try {
            try (PreparedStatement ps = this.conn.prepareStatement(sql1)) {
                //ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    //System.out.println("Size of results = " + rs.getInt(1));
                    while (rs.next()) {
                        distinctCount++;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("distinctCount " + distinctCount );
        assertEquals(distinctCount,distinctCount);
        
        System.out.println("Finish AgncyOrgWhTest.test7");
        System.out.println();
    }

    @Test
        /*
         * - Check the values of the ORG_CD column.
         */
    public void test8() {
    	System.out.println("Starting AgncyOrgWhTest.test8");
    	
        String sql1 = "Select count (*)\n" +
                      "From DTSDM. AGNCY_ORG_WH \n";

        String sql2 = "Select distinct trunc(AGNCY_ORG_WH.INSERT_DATE) INSERT_DATE, count (*)\n" +
                      "From DTSDM. AGNCY_ORG_WH\n" +
                      "Group by trunc(AGNCY_ORG_WH.INSERT_DATE)";



        int distinctCount = 0;
        int count = 0;
        int totalCount = 0;

        System.out.println("Starting AgncyOrgWhTest.test8,sql1");
        try {
            try (PreparedStatement ps = this.conn.prepareStatement(sql1)) {
                //ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    //System.out.println("Size of results = " + rs.getInt(1));
                    while (rs.next()) {

                    	distinctCount = rs.getInt("count(*)");
               
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        System.out.println("Starting AgncyOrgWhTest.test8,sql2");
        try {
            try (PreparedStatement ps = this.conn.prepareStatement(sql2)) {
                //ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    //System.out.println("Size of results = " + rs.getInt(1));
                    while (rs.next()) {
                    	count = rs.getInt("count(*)");
                    	totalCount = count + totalCount;

                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("distinctCount = totalCount " + distinctCount + " = " + totalCount);
        assertEquals(distinctCount,totalCount);
        System.out.println("Finish AgncyOrgWhTest.test8");
        System.out.println();
    }

    @Test
        /*
         * - Check the values of the ORG_CD column.
         */
    public void test9() {
    	System.out.println("Starting AgncyOrgWhTest.test9");
    	
        String sql1 = "Select count (*)\n" +
                      "From DTSDM. AGNCY_ORG_WH \n";

        String sql2 = "Select distinct trunc(AGNCY_ORG_WH.UPDATE_DATE) UPDATE_DATE, count (*)\n" +
                      "From DTSDM. AGNCY_ORG_WH\n" +
                      "Group by trunc(AGNCY_ORG_WH.UPDATE_DATE)";



        int distinctCount = 0;
        int count = 0;
        int totalCount = 0;

        System.out.println("Starting AgncyOrgWhTest.test9,sql1");
        try {
            try (PreparedStatement ps = this.conn.prepareStatement(sql1)) {
                //ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    //System.out.println("Size of results = " + rs.getInt(1));
                    while (rs.next()) {

                        count = rs.getInt("count(*)");

                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        System.out.println("Starting AgncyOrgWhTest.test9,sql2");
        try {
            try (PreparedStatement ps = this.conn.prepareStatement(sql2)) {
                //ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    //System.out.println("Size of results = " + rs.getInt(1));
                    while (rs.next()) {
                     count = rs.getInt("count(*)");
                    	totalCount = count + totalCount;

                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("count = totalCount " + count + " = " + totalCount);
        assertEquals(count,totalCount);
        
        System.out.println("Finish AgncyOrgWhTest.test9");
        System.out.println();
    }

}