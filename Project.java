import java.util.NavigableSet;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.HashMap;
import java.util.Map;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

public class Project{
    static SqlUtils db_utils = new SqlUtils("RobotCompany.db");

    static int nextFacilityId = 0;
    static int nextStaffId = 0;
    static int nextRentalId = 0;
    static NavigableSet<Object[]> facilityEntries = new TreeSet<>(new Comparator<Object[]>() {
        @Override
        public int compare(Object[] a, Object[] b) {
            return Integer.compare((Integer) a[0], (Integer) b[0]);
        }
    });
    static NavigableSet<Object[]> staffEntries = new TreeSet<>(new Comparator<Object[]>() {
        @Override
        public int compare(Object[] a, Object[] b) {
            return Integer.compare((Integer) a[0], (Integer) b[0]);
        }
    });

    enum FacilityCol {FACILITY_ID, ADDRESS, PHONE, MANAGER_NAME, ROBOT_CAPACITY, VEHICLE_CAPACITY, CITY};
    enum StaffCol {EMPLOYEE_ID, NAME, PHONE, EMAIL, TYPE};

    private static void createFacilityEntry(Scanner scanner){
        System.out.println("Enter Address, Phone, Manager Name, Robot Capacity, Vehicle Capacity, and City separated by commas: ");
        String[] res = scanner.nextLine().split(",");
        try {
            if (res.length != 6) {
                System.err.println("Argument error! Expected 6 fields, got " + res.length);
                return;
            }
            // Object[] r = {nextFacilityId++, res[0].trim(), res[1].trim(), res[2].trim(), Integer.parseInt(res[3].trim()), Integer.parseInt(res[4].trim()), res[5].trim()};
            
            String sql = "INSERT INTO Facility VALUES (?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement ps = db_utils.conn.prepareStatement(sql);
            ps.setInt(1, nextFacilityId++);
            ps.setString(2, res[0].trim());
            ps.setString(3, res[1].trim());
            ps.setString(4, res[2].trim());
            ps.setInt(5, Integer.parseInt(res[3].trim()));
            ps.setInt(6, Integer.parseInt(res[4].trim()));
            ps.setString(7, res[5].trim());

            SqlUtils.sqlInsertQuery(ps);

            // facilityEntries.add(r);
            // System.out.println("Facility created successfully with ID: " + r[0]);
        } catch (NumberFormatException e) {
            System.err.println("Argument error! Robot Capacity and Vehicle Capacity must be integers.");
        } catch (Exception e) {
            System.err.println("Argument error! " + e.getMessage());
        }
    }

    private static void updateFacilityEntry(int facilityId, Map<FacilityCol, Object> updateValues){
        // Object[] vals = updateValues.values().toArray(new Object[updateValues.values().size()]);
        FacilityCol selectedCol = updateValues.keySet().toArray(new FacilityCol[1])[0];
        Object val = updateValues.values().toArray(new Object[1])[0];

        try{
            String sql = "";
            boolean isInt = false;
            switch(selectedCol){
                case ADDRESS:
                    sql = "UPDATE Facility SET Fac_Address=? WHERE Fac_ID=?";
                    break;
                case PHONE:
                    sql = "UPDATE Facility SET Fac_Phone=? WHERE Fac_ID=?";
                    break;
                case MANAGER_NAME:
                    sql = "UPDATE Facility SET Fac_Manager_Name=? WHERE Fac_ID=?";
                    break;
                case ROBOT_CAPACITY:
                    sql = "UPDATE Facility SET Fac_Robot_Capacity=? WHERE Fac_ID=?";
                    isInt = true;
                    break;
                case VEHICLE_CAPACITY:
                    sql = "UPDATE Facility SET Fac_Vehicle_Capacity=? WHERE Fac_ID=?";
                    isInt = true;
                    break;
                case CITY:
                    sql = "UPDATE Facility SET Fac_City=? WHERE Fac_ID=?";
                    break;
            }
            PreparedStatement stmt = db_utils.conn.prepareStatement(sql);
            if(isInt){
                stmt.setInt(1, (Integer) val);
            } else {
                stmt.setString(1, (String)val);
            }
            stmt.setInt(2, facilityId);

            SqlUtils.sqlUpdateQuery(stmt);
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private static void deleteFacilityEntry(int id){
        try{
            String sql = "DELETE FROM Facility WHERE Fac_ID=?";
            PreparedStatement stmt = db_utils.conn.prepareStatement(sql);
            stmt.setInt(1, id);
            SqlUtils.sqlUpdateQuery(stmt);
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private static void retrieveFacilityEntry(Map<FacilityCol, Object> searchCriteria){
        // assert(db_utils.conn != null);
        FacilityCol selectedCol = searchCriteria.keySet().toArray(new FacilityCol[1])[0];
        Object val = searchCriteria.values().toArray(new Object[1])[0];

        if (searchCriteria.isEmpty()) {
            try {
                String sql = "SELECT * FROM Facility;";
                PreparedStatement stmt = db_utils.conn.prepareStatement(sql);
                SqlUtils.sqlSelectQuery(stmt);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            return;
        }

        try{
            String sql = "";
            boolean isInt = false;
            switch(selectedCol){
                case ADDRESS:
                    sql = "SELECT * FROM Facility WHERE Fac_Address=?;";
                    break;
                case PHONE:
                    sql = "SELECT * FROM Facility WHERE Fac_Phone=?;";
                    break;
                case MANAGER_NAME:
                    sql = "SELECT * FROM Facility WHERE Fac_Manager_Name=?;";
                    break;
                case ROBOT_CAPACITY:
                    sql = "SELECT * FROM Facility WHERE Fac_Robot_Capacity=?;";
                    isInt = true;
                    break;
                case VEHICLE_CAPACITY:
                    sql = "SELECT * FROM Facility WHERE Fac_Vehicle_Capacity=?;";
                    isInt = true;
                    break;
                case CITY:
                    sql = "SELECT * FROM Facility WHERE Fac_City=?;";
                    break;
            }
            PreparedStatement stmt = db_utils.conn.prepareStatement(sql);
            if(isInt){
                stmt.setInt(1, (Integer) val);
            } else {
                stmt.setString(1, (String)val);
            }
            SqlUtils.sqlSelectQuery(stmt);
            
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private static void createStaffEntry(Scanner scanner){
        System.out.println("Enter Name, Phone, Email, and Type separated by commas: ");
        String[] res = scanner.nextLine().split(",");
        try {
            if (res.length != 4) {
                System.err.println("Argument error! Expected 4 fields, got " + res.length);
                return;
            }
            // Object[] r = {nextStaffId++, res[0].trim(), res[1].trim(), res[2].trim(), res[3].trim()};
            // staffEntries.add(r);
            // System.out.println("Staff entry created successfully with ID: " + r);
            String sql = "INSERT INTO Staff_Member VALUES (?, ?, ?, ?, ?);";
            PreparedStatement ps = db_utils.conn.prepareStatement(sql);
            ps.setInt(1, nextStaffId++);
            ps.setString(2, res[0].trim());
            ps.setString(3, res[1].trim());
            ps.setString(4, res[2].trim());
            ps.setString(5, res[3].trim());
            SqlUtils.sqlInsertQuery(ps);
        } catch (Exception e) {
            System.err.println("Argument error! " + e.getMessage());
        }
    }

    private static void updateStaffEntry(int staffId, Map<StaffCol, Object> updateValues){
        StaffCol selectedCol = updateValues.keySet().toArray(new StaffCol[1])[0];
        Object val = updateValues.values().toArray(new Object[1])[0];

        try {
            String sql = "";
            switch(selectedCol){
                case NAME:
                    sql = "UPDATE Staff_Member SET Emp_Name=? WHERE Emp_ID=?";
                    break;
                case PHONE:
                    sql = "UPDATE Staff_Member SET Emp_Phone=? WHERE Emp_ID=?";
                    break;
                case EMAIL:
                    sql = "UPDATE Staff_Member SET Emp_Email=? WHERE Emp_ID=?";
                    break;
                case TYPE:
                    sql = "UPDATE Staff_Member SET Emp_Type=? WHERE Emp_ID=?";
                    break;
            }
            PreparedStatement stmt = db_utils.conn.prepareStatement(sql);
            stmt.setString(1, (String)val);
            stmt.setInt(2, staffId);

            SqlUtils.sqlUpdateQuery(stmt);
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private static void deleteStaffEntry(int id){
        try{
            String sql = "DELETE FROM Staff_Member WHERE Emp_ID=?";
            PreparedStatement stmt = db_utils.conn.prepareStatement(sql);
            stmt.setInt(1, id);
            SqlUtils.sqlUpdateQuery(stmt);
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private static void retrieveStaffEntry(Map<StaffCol, Object> searchCriteria){
        StaffCol selectedCol = searchCriteria.keySet().toArray(new StaffCol[1])[0];
        Object val = searchCriteria.values().toArray(new Object[1])[0];

        if (searchCriteria.isEmpty()) {
            try {
                String sql = "SELECT * FROM Staff_Member;";
                PreparedStatement stmt = db_utils.conn.prepareStatement(sql);
                SqlUtils.sqlSelectQuery(stmt);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            return;
        }

        try {
            String sql = "";
            switch(selectedCol){
                case NAME:
                    sql = "SELECT * FROM Staff_Member WHERE Emp_Name=?;";
                    break;
                case PHONE:
                    sql = "SELECT * FROM Staff_Member WHERE Emp_Phone=?;";
                    break;
                case EMAIL:
                    sql = "SELECT * FROM Staff_Member WHERE Emp_Email=?;";
                    break;
                case TYPE:
                    sql = "SELECT * FROM Staff_Member WHERE Emp_Type=?;";
                    break;
            }
            PreparedStatement stmt = db_utils.conn.prepareStatement(sql);
            stmt.setString(1, (String)val);
            SqlUtils.sqlSelectQuery(stmt);
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        // db_utils = new SqlUtils("RobotCompany.db"); assert(db_utils != null); assert(db_utils.conn != null);

        Boolean prompting = true;
        Scanner scanner = new Scanner(System.in);
        while(prompting){

            System.out.println("\n=== Rental Home Robot DBMS ===");
            System.out.println("Select a Resource to Manage:");
            System.out.println("1. Facilities\n2. Staff\n3. Robots\n0. Exit");
            int selection = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            switch (selection) {
                case 1:
                    System.out.println("\n--- Facility Management ---");
                    System.out.println("1. Create\n2. Update\n3. Delete\n4. Retrieve");
                    int facilitySelection = scanner.nextInt();
                    scanner.nextLine();
                    switch (facilitySelection) {
                        case 1:
                            createFacilityEntry(scanner);
                            break;
                        case 2:
                            System.out.println("Enter Facility ID to update:");
                            int facilityId = scanner.nextInt();
                            scanner.nextLine();
                            System.out.println("Enter column to update (ADDRESS, PHONE, MANAGER_NAME, ROBOT_CAPACITY, VEHICLE_CAPACITY, CITY):");
                            String facilityCol = scanner.nextLine().toUpperCase();
                            System.out.println("Enter new value:");
                            String facilityValue = scanner.nextLine();
                            Map<FacilityCol, Object> facilityUpdates = new HashMap<>();
                            try {
                                FacilityCol colEnum = FacilityCol.valueOf(facilityCol);
                                if (colEnum == FacilityCol.ROBOT_CAPACITY || colEnum == FacilityCol.VEHICLE_CAPACITY) {
                                    facilityUpdates.put(colEnum, Integer.parseInt(facilityValue));
                                } else {
                                    facilityUpdates.put(colEnum, facilityValue);
                                }
                                updateFacilityEntry(facilityId, facilityUpdates);
                            } catch (IllegalArgumentException e) {
                                System.err.println("Invalid column name!");
                            }
                            break;
                        case 3:
                            System.out.println("Enter Facility ID to delete:");
                            int delFacilityId = scanner.nextInt();
                            deleteFacilityEntry(delFacilityId);
                            break;
                        case 4:
                            
                            System.out.println("\nSearch Facilities By:");
                            System.out.println("1. Address");
                            System.out.println("2. Phone");
                            System.out.println("3. Manager Name");
                            System.out.println("4. Robot Capacity");
                            System.out.println("5. Vehicle Capacity");
                            System.out.println("6. City");
                            System.out.println("7. Show All");

                            int facilitySearchOption = scanner.nextInt();
                            scanner.nextLine();

                            Map<FacilityCol, Object> facilitySearch = new HashMap<>();

                            switch (facilitySearchOption) {
                                case 1:
                                    System.out.println("Enter Address:");
                                    facilitySearch.put(FacilityCol.ADDRESS, scanner.nextLine());
                                    break;

                                case 2:
                                    System.out.println("Enter Phone:");
                                    facilitySearch.put(FacilityCol.PHONE, scanner.nextLine());
                                    break;

                                case 3:
                                    System.out.println("Enter Manager Name:");
                                    facilitySearch.put(FacilityCol.MANAGER_NAME, scanner.nextLine());
                                    break;

                                case 4:
                                    System.out.println("Enter Robot Capacity:");
                                    facilitySearch.put(FacilityCol.ROBOT_CAPACITY, Integer.parseInt(scanner.nextLine()));
                                    break;

                                case 5:
                                    System.out.println("Enter Vehicle Capacity:");
                                    facilitySearch.put(FacilityCol.VEHICLE_CAPACITY, Integer.parseInt(scanner.nextLine()));
                                    break;

                                case 6:
                                    System.out.println("Enter City:");
                                    facilitySearch.put(FacilityCol.CITY, scanner.nextLine());
                                    break;

                                case 7:
                                    break;

                                default:
                                    System.err.println("Invalid selection.");
                                    return;
                            }

                            retrieveFacilityEntry(facilitySearch);
                            break;
                        default:
                            System.err.println("Invalid Selection");
                            break;
                    }
                    break;
                case 2:
                    System.out.println("\n--- Staff Management ---");
                    System.out.println("1. Create\n2. Update\n3. Delete\n4. Retrieve");
                    int staffSelection = scanner.nextInt();
                    scanner.nextLine();
                    switch (staffSelection) {
                        case 1:
                            createStaffEntry(scanner);
                            break;
                        case 2:
                            System.out.println("Enter Staff ID to update:");
                            int staffId = scanner.nextInt();
                            scanner.nextLine();
                            System.out.println("Enter column to update (NAME, PHONE, EMAIL, TYPE):");
                            String staffCol = scanner.nextLine().toUpperCase();
                            System.out.println("Enter new value:");
                            String staffValue = scanner.nextLine();
                            Map<StaffCol, Object> staffUpdates = new HashMap<>();
                            try {
                                staffUpdates.put(StaffCol.valueOf(staffCol), staffValue);
                                updateStaffEntry(staffId, staffUpdates);
                            } catch (IllegalArgumentException e) {
                                System.err.println("Invalid column name!");
                            }
                            break;
                        case 3:
                            System.out.println("Enter Staff ID to delete:");
                            int delStaffId = scanner.nextInt();
                            deleteStaffEntry(delStaffId);
                            break;
                        case 4:
                            System.out.println("\nSearch Staff By:");
                            System.out.println("1. Name");
                            System.out.println("2. Phone");
                            System.out.println("3. Email");
                            System.out.println("4. Type");
                            System.out.println("5. Show All");

                            int staffSearchOption = scanner.nextInt();
                            scanner.nextLine();

                            Map<StaffCol, Object> staffSearch = new HashMap<>();

                            switch (staffSearchOption) {
                                case 1:
                                    System.out.println("Enter Name:");
                                    staffSearch.put(StaffCol.NAME, scanner.nextLine());
                                    break;

                                case 2:
                                    System.out.println("Enter Phone:");
                                    staffSearch.put(StaffCol.PHONE, scanner.nextLine());
                                    break;

                                case 3:
                                    System.out.println("Enter Email:");
                                    staffSearch.put(StaffCol.EMAIL, scanner.nextLine());
                                    break;

                                case 4:
                                    System.out.println("Enter Type:");
                                    staffSearch.put(StaffCol.TYPE, scanner.nextLine());
                                    break;

                                case 5:
                                    break;

                                default:
                                    System.err.println("Invalid selection.");
                                    return;
                            }

                            retrieveStaffEntry(staffSearch);
                            break;
                        default:
                            System.err.println("Invalid Selection");
                            break;
                    }
                    break;
                case 3:
                    System.out.println("\n--- Robot Management ---");
                    System.out.println("1. Rent Robot");
                    System.out.println("2. Return Equipment");
                    System.out.println("3. Delivery of Robots");
                    System.out.println("4. Pickup of Robots");
                    int robotSelection = scanner.nextInt();
                    scanner.nextLine();

                    switch (robotSelection) {

                        case 1:
                            System.out.println("\n--- Rent Robot ---");
                            System.out.println("Enter Customer ID:");
                            int rentCustomerId = scanner.nextInt();
                            scanner.nextLine();

                            System.out.println("Enter Robot Serial Number:");
                            String rentRobotSerial = scanner.nextLine();

                            System.out.println("Enter Start Date:");
                            String rentStartDate = scanner.nextLine();

                            System.out.println("Enter End Date:");
                            String rentEndDate = scanner.nextLine();

                            System.out.println("Enter Rental Fee:");
                            double rentFee = scanner.nextDouble();
                            scanner.nextLine();

                            try{
                                String sql = "INSERT INTO Rental VALUES(?, ?, ?, ?, ?, ?)"
                                PreparedStatement ps = db_utils.conn.prepareStatement(sql);
                                ps.setInt(1, nextRentalId++);
                                ps.setString(2, rentStartDate);
                                ps.setString(3, rentEndDate);
                                ps.setDouble(4, rentFee);
                                ps.setInt(5, rentRobotSerial);
                                ps.setInt(6, rentCustomerId);
                                SqlUtils.sqlInsertQuery(ps);
                                System.out.println("Robot rent successful.");
                            }
                            catch{
                                System.err.println("Error renting robot: " + e.getMessage());
                            }
                            break;

                        case 2:
                            System.out.println("\n--- Return Equipment ---");
                            System.out.println("Enter Customer ID:");
                            int returnCustomerId = scanner.nextInt();
                            scanner.nextLine();

                            System.out.println("Enter Robot Serial Number:");
                            String returnRobotSerial = scanner.nextLine();

                            System.out.println("Enter Return Date:");
                            String returnDate = scanner.nextLine();
                            try{
                                String sql = "UPDATE Rental SET Rent_End_Date = ? WHERE Rent_Item_SN = ? AND Rent_Cust_Ref = ?"
                                PreparedStatement ps = db_utils.conn.prepareStatement(sql);
                                ps.setString(1, returnDate);
                                ps.setInt(2, returnRobotSerial);
                                ps.setInt(3, returnCustomerId);
                                SqlUtils.sqlInsertQuery(ps);
                                System.out.println("Equipment return registered.");
                            }
                            catch{
                                System.err.println("Error returning robot: " + e.getMessage());
                            }
                            break;

                        case 3:
                            System.out.println("\n--- Delivery of Robots ---");
                            System.out.println("Enter Customer ID:");
                            int deliveryCustomerId = scanner.nextInt();
                            scanner.nextLine();

                            System.out.println("Enter Robot Serial Number:");
                            String deliveryRobotSerial = scanner.nextLine();

                            System.out.println("Enter Driverless Vehicle Serial Number:");
                            String deliveryVehicle = scanner.nextLine();

                            System.out.println("Enter Delivery Date:");
                            String deliveryDate = scanner.nextLine();
                            
                            try{
                                System.out.println("Robot delivery scheduled.");
                            }
                            break;

                        case 4:
                            System.out.println("\n--- Pickup of Robots ---");
                            System.out.println("Enter Customer ID:");
                            int pickupCustomerId = scanner.nextInt();
                            scanner.nextLine();

                            System.out.println("Enter Robot Serial Number:");
                            String pickupRobotSerial = scanner.nextLine();

                            System.out.println("Enter Driverless Vehicle Serial Number:");
                            String pickupVehicle = scanner.nextLine();

                            System.out.println("Enter Pickup Date:");
                            String pickupDate = scanner.nextLine();

                            System.out.println("Robot pickup scheduled.");
                            break;

                        default:
                            System.err.println("Invalid Selection");
                            break;
                    }
                    break;

                case 0:
                    prompting = false;
                    System.out.println("Exiting...");
                    break;
                default:
                    System.err.println("Invalid Selection");
                    break;
            }
        }
        scanner.close();
    }
}
