import java.sql.*;


public class SqlUtils {
    public Connection conn;
    private static final String DRIVER = "org.sqlite.JDBC";

    public SqlUtils(String DBName){
        String url = "jdbc:sqlite:" + DBName;
        conn = null;
        try{
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(url);
            if(conn == null){
                System.err.println("Database connection could not be established, exiting...");
                System.exit(1);
            }
        } catch(SQLException e){
            System.err.println(e.getMessage());
            System.exit(1);
        } catch(Exception e){
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public static void sqlSelectQuery(PreparedStatement stmt){
        try{
            ResultSet rs = stmt.executeQuery();
            ResultSetMetaData meta = rs.getMetaData();
            int cols = meta.getColumnCount();
            for(int i = 1; i < cols; i++){
                String val = meta.getColumnName(i);
                System.out.println(val);
                if(i < cols)
                    System.out.print(",  ");
            }
            System.out.println();
            while(rs.next()){
                for (int i = 1; i <= cols; i++) {
        			String columnValue = rs.getString(i);
            		System.out.print(columnValue);
            		if (i < cols) System.out.print(",  ");
        		}
    			System.out.print("\n");
            }
            rs.close();
            stmt.close();
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public static void sqlInsertQuery(PreparedStatement stmt){
        try{
            int i = stmt.executeUpdate();
            if(i == 0){
                System.out.println("Insertion failed!");
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public static void sqlUpdateQuery(PreparedStatement stmt){
        try{
            int i = stmt.executeUpdate();
            if(i == 0){
                System.out.println("Update failed!");
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
