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
    static SqlUtils db_utils;

    static int nextFacilityId = 0;
    static int nextStaffId = 0;
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

            db_utils.sqlInsertQuery(ps);

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
            switch(selectedCol){
                case ADDRESS:
                    String sql = "UPDATE Facility";
                    break;
                case PHONE:
                    break;
                case MANAGER_NAME:
                    break;
                case ROBOT_CAPACITY:
                    break;
                case VEHICLE_CAPACITY:
                    break;
                case CITY:
                    break;
                default:

            }
            // String sql = "UPDATE Facility SET Fac_Address=?, Fac_Phone=?, Fac_Manager_Name=?, Fac_Robot_Capacity=?, Fac_Vehicle_Capacity=?, Fac_City=? WHERE Fac_ID=?;";
            // PreparedStatement stmt = db_utils.conn.prepareStatement(sql);
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }

        
        // Object[] entryToUpdate = null;
        
        // // Find the entry with the given ID
        // for (Object[] entry : facilityEntries) {
        //     if (((Integer) entry[0]).intValue() == facilityId) {
        //         entryToUpdate = entry;
        //         break;
        //     }
        // }
        
        // if (entryToUpdate != null) {
        //     // Remove the old entry
        //     facilityEntries.remove(entryToUpdate);
            
        //     // Update the values
        //     for (Map.Entry<FacilityCol, Object> update : updateValues.entrySet()) {
        //         int colIndex = update.getKey().ordinal();
        //         entryToUpdate[colIndex] = update.getValue();
        //     }
            
        //     // Add the updated entry back
        //     facilityEntries.add(entryToUpdate);
        //     System.out.println("Facility " + facilityId + " updated successfully.");
        // } else {
        //     System.err.println("Facility with ID " + facilityId + " not found.");
        // }
    }

    private static void deleteFacilityEntry(int id){
        Object[] objToRemove = null;
        for (Object[] objects : facilityEntries) {
            if(((Integer)objects[0]).intValue() == id){
                objToRemove = objects;
            }
        }

        if(objToRemove != null){
            facilityEntries.remove(objToRemove);
        } else {
            System.err.println("Unable to Remove specified Facility");
        }
    }

    private static List<Object[]> retrieveFacilityEntry(Map<FacilityCol, Object> searchCriteria){
        List<Object[]> results = new ArrayList<>();
        
        // If no criteria specified, return all entries
        if (searchCriteria == null || searchCriteria.isEmpty()) {
            results.addAll(facilityEntries);
            return results;
        }
        
        // Search through all entries and find matches
        for (Object[] entry : facilityEntries) {
            boolean matches = true;
            
            // Check each search criterion
            for (Map.Entry<FacilityCol, Object> criterion : searchCriteria.entrySet()) {
                int colIndex = criterion.getKey().ordinal();
                Object expectedValue = criterion.getValue();
                Object actualValue = entry[colIndex];
                
                // Handle different data types for comparison
                if (!actualValue.toString().equalsIgnoreCase(expectedValue.toString())) {
                    matches = false;
                    break;
                }
            }
            
            if (matches) {
                results.add(entry);
            }
        }
        
        return results;
    }

    private static void createStaffEntry(Scanner scanner){
        System.out.println("Enter Name, Phone, Email, and Type separated by commas: ");
        String[] res = scanner.nextLine().split(",");
        try {
            if (res.length != 4) {
                System.err.println("Argument error! Expected 4 fields, got " + res.length);
                return;
            }
            Object[] r = {nextStaffId++, res[0].trim(), res[1].trim(), res[2].trim(), res[3].trim()};
            staffEntries.add(r);
            System.out.println("Staff entry created successfully with ID: " + r);
        } catch (Exception e) {
            System.err.println("Argument error! " + e.getMessage());
        }
    }

    private static void updateStaffEntry(int staffId, Map<StaffCol, Object> updateValues){
        Object[] entryToUpdate = null;
        
        // Find the entry with the given ID
        for (Object[] entry : staffEntries) {
            if (((Integer) entry[0]).intValue() == staffId) {
                entryToUpdate = entry;
                break;
            }
        }
        
        if (entryToUpdate != null) {
            // Remove the old entry
            staffEntries.remove(entryToUpdate);
            
            // Update the values
            for (Map.Entry<StaffCol, Object> update : updateValues.entrySet()) {
                int colIndex = update.getKey().ordinal();
                entryToUpdate[colIndex] = update.getValue();
            }
            
            // Add the updated entry back
            staffEntries.add(entryToUpdate);
            System.out.println("Staff " + staffId + " updated successfully.");
        } else {
            System.err.println("Staff with ID " + staffId + " not found.");
        }
    }

    private static void deleteStaffEntry(int id){
        Object[] objToRemove = null;
        for (Object[] objects : staffEntries) {
            if(((Integer)objects[0]).intValue() == id){
                objToRemove = objects;
            }
        }

        if(objToRemove != null){
            staffEntries.remove(objToRemove);
            System.out.println("Staff " + id + " deleted successfully.");
        } else {
            System.err.println("Unable to Remove specified Staff");
        }
    }

    private static List<Object[]> retrieveStaffEntry(Map<StaffCol, Object> searchCriteria){
        List<Object[]> results = new ArrayList<>();
        
        // If no criteria specified, return all entries
        if (searchCriteria == null || searchCriteria.isEmpty()) {
            results.addAll(staffEntries);
            return results;
        }
        
        // Search through all entries and find matches
        for (Object[] entry : staffEntries) {
            boolean matches = true;
            
            // Check each search criterion
            for (Map.Entry<StaffCol, Object> criterion : searchCriteria.entrySet()) {
                int colIndex = criterion.getKey().ordinal();
                Object expectedValue = criterion.getValue();
                Object actualValue = entry[colIndex];
                
                // Handle different data types for comparison
                if (!actualValue.toString().equalsIgnoreCase(expectedValue.toString())) {
                    matches = false;
                    break;
                }
            }
            
            if (matches) {
                results.add(entry);
            }
        }
        
        return results;
    }

    public static void main(String[] args) {
        // Object[] ex = {1, "123 Main St.", "1234567890", "Steve", 10, 10, "Columbus"};
        // facilityEntries.add(ex);
        // Object[] exe = {1, "Craig", "9999999999", "craig@gmail.com", "Mechanic"};
        // staffEntries.add(exe);
        db_utils = new SqlUtils("RobotCompany.db");

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
                                facilityUpdates.put(FacilityCol.valueOf(facilityCol), facilityValue);
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
                                    facilitySearch.put(FacilityCol.ROBOT_CAPACITY, scanner.nextLine());
                                    break;

                                case 5:
                                    System.out.println("Enter Vehicle Capacity:");
                                    facilitySearch.put(FacilityCol.VEHICLE_CAPACITY, scanner.nextLine());
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

                            List<Object[]> facilityResults = retrieveFacilityEntry(facilitySearch);

                            if (facilityResults.isEmpty()) {
                                System.out.println("No facilities found.");
                            } else {
                                for (Object[] facility : facilityResults) {
                                    System.out.println("ID: " + facility[0] +
                                            ", Address: " + facility[1] +
                                            ", Phone: " + facility[2] +
                                            ", Manager: " + facility[3] +
                                            ", Robot Capacity: " + facility[4] +
                                            ", Vehicle Capacity: " + facility[5] +
                                            ", City: " + facility[6]);
                                }
                            }
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

                            List<Object[]> staffResults = retrieveStaffEntry(staffSearch);

                            if (staffResults.isEmpty()) {
                                System.out.println("No staff found.");
                            } else {
                                for (Object[] staff : staffResults) {
                                    System.out.println("ID: " + staff[0] +
                                            ", Name: " + staff[1] +
                                            ", Phone: " + staff[2] +
                                            ", Email: " + staff[3] +
                                            ", Type: " + staff[4]);
                                }
                            }
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

                            System.out.println("Robot rent successful.");
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

                            System.out.println("Equipment return registered.");
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

                            System.out.println("Robot delivery scheduled.");
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
