
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class HotelReservationSystem {
    public static final String url = "jdbc:mysql://localhost:3306/hotel_db";
    public static final String username = "root";
    public static final String password = "amitkumar@12";

    public HotelReservationSystem() {
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException var4) {
            System.out.println(var4.getMessage());
        }

        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_db", "root", "amitkumar@12");

            while(true) {
                System.out.println();
                System.out.println("HOTEL MANAGEMENT SYSTEM");
                Scanner sc = new Scanner(System.in);
                System.out.println("1. Reserve a Room");
                System.out.println("2. View Reservations");
                System.out.println("3. Get Room number");
                System.out.println("4. Update Reservation");
                System.out.println("5. Delete Entry");
                System.out.println("0. Exit");
                System.out.println("Choose an option : ");
                int choice = sc.nextInt();
                switch (choice) {
                    case 0:
                        exit();
                        sc.close();
                        return;
                    case 1:
                        Bookroom(con, sc);
                        break;
                    case 2:
                        ViewReservation(con);
                        break;
                    case 3:
                        SearchRoomNumber(con, sc);
                        break;
                    case 4:
                        updateReservation(con, sc);
                        break;
                    case 5:
                        deleteReservation(con, sc);
                        break;
                    default:
                        System.out.println("Invalid choice. try again.");
                }
            }
        } catch (SQLException var5) {
            System.out.println(var5.getMessage());
        } catch (InterruptedException var6) {
            System.out.println(var6.getMessage());
        }

    }

    private static void Bookroom(Connection con, Scanner sc) {
        try {
            System.out.println("Enter guest name: ");
            String guestName = sc.next();
            sc.nextLine();
            System.out.println("Enter contact number: ");
            String contactNumber = sc.next();
            System.out.println("Enter room no: ");
            int roomNumber = sc.nextInt();
            String sql = "INSERT INTO hotel_reservation(guest_name, room_number, contact_number) VALUES ('" + guestName + "', " + roomNumber + ", '" + contactNumber + "')";
            Statement stmt = con.createStatement();

            try {
                int affectedRow = stmt.executeUpdate(sql);
                if (affectedRow > 0) {
                    System.out.println("Reservation successful! and rows affected is: " + affectedRow);
                } else {
                    System.out.println("Reservation failed.");
                }
            } catch (Throwable var10) {
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (Throwable var9) {
                        var10.addSuppressed(var9);
                    }
                }

                throw var10;
            }

            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException var11) {
            var11.printStackTrace();
        }

    }

    private static void ViewReservation(Connection con) throws SQLException {
        String sql = "select * from hotel_db.hotel_reservation";
        Statement stmt = con.createStatement();

        try {
            ResultSet rs = stmt.executeQuery(sql);

            try {
                System.out.println("Current Reservation: ");
                System.out.println("+----------------+-----------------+---------------+----------------------+-----------------------+");
                System.out.println("| Reservation ID | Guest_name      | Room_number   | Contact_Number       | Reservation_Time      |");
                System.out.println("+----------------+-----------------+---------------+----------------------+-----------------------+");

                while(rs.next()) {
                    int reservationID = rs.getInt("reservation_id");
                    String guestName = rs.getString("guest_name");
                    int RoomNumber = rs.getInt("room_number");
                    String ContactNumber = rs.getString("contact_number");
                    String reservationDate = rs.getTimestamp("reservation_date").toString();
                    System.out.printf("| %-14d | %-15s | %-13d | %-20s | %-19s |\n", reservationID, guestName, RoomNumber, ContactNumber, reservationDate);
                }

                System.out.println("+----------------+-----------------+---------------+----------------------+-----------------------+");
            } catch (Throwable var11) {
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Throwable var10) {
                        var11.addSuppressed(var10);
                    }
                }

                throw var11;
            }

            if (rs != null) {
                rs.close();
            }
        } catch (Throwable var12) {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Throwable var9) {
                    var12.addSuppressed(var9);
                }
            }

            throw var12;
        }

        if (stmt != null) {
            stmt.close();
        }

    }

    private static void SearchRoomNumber(Connection con, Scanner sc) {
        try {
            System.out.println("Enter the Reservation ID: ");
            int reservationID = sc.nextInt();
            System.out.println("Enter the guest Name: ");
            String guestName = sc.next();
            String sql = "SELECT room_number FROM hotel_reservation WHERE reservation_id=" + reservationID + " AND guest_name ='" + guestName + "';";
            Statement stmt = con.createStatement();

            try {
                ResultSet rs = stmt.executeQuery(sql);

                try {
                    if (rs.next()) {
                        int RoomNumber = rs.getInt("room_number");
                        System.out.println("room number : " + RoomNumber);
                    } else {
                        System.out.println("Reservation not found for the Given ID and guest name.");
                    }
                } catch (Throwable var11) {
                    if (rs != null) {
                        try {
                            rs.close();
                        } catch (Throwable var10) {
                            var11.addSuppressed(var10);
                        }
                    }

                    throw var11;
                }

                if (rs != null) {
                    rs.close();
                }
            } catch (Throwable var12) {
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (Throwable var9) {
                        var12.addSuppressed(var9);
                    }
                }

                throw var12;
            }

            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException var13) {
            var13.printStackTrace();
        }

    }

    private static void updateReservation(Connection con, Scanner sc) throws SQLException {
        System.out.print("Enter reservation ID to update: ");
        int reservationID = sc.nextInt();
        sc.nextLine();
        if (!reservationExists(con, reservationID)) {
            System.out.println("Reservation not found for the given ID.");
        } else {
            System.out.print("Enter new guest name: ");
            String newGuestName = sc.nextLine();
            System.out.print("Enter new room number: ");
            int newRoomNumber = sc.nextInt();
            System.out.print("Enter new contact number: ");
            String newContactNumber = sc.next();
            String sql = "UPDATE hotel_reservation SET guest_name = '" + newGuestName + "', room_number = " + newRoomNumber + ", contact_number = '" + newContactNumber + "' WHERE reservation_id = " + reservationID;

            try {
                Statement stmt = con.createStatement();

                try {
                    int affected = stmt.executeUpdate(sql);
                    if (affected > 0) {
                        System.out.println("Guest Data has been UPDATED !!");
                    } else {
                        System.out.println("Guest Data Updation is Failed ");
                    }
                } catch (Throwable var11) {
                    if (stmt != null) {
                        try {
                            stmt.close();
                        } catch (Throwable var10) {
                            var11.addSuppressed(var10);
                        }
                    }

                    throw var11;
                }

                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException var12) {
                var12.printStackTrace();
            }

        }
    }

    private static void deleteReservation(Connection con, Scanner sc) {
        try {
            System.out.println("Enter the reservation ID to delete: ");
            int reservationID = sc.nextInt();
            if (!reservationExists(con, reservationID)) {
                System.out.println("Reservation not found for the given ID.");
                return;
            }

            String sql = "DELETE FROM hotel_reservation WHERE reservation_id = " + reservationID;
            Statement stmt = con.createStatement();

            try {
                int affectedRows = stmt.executeUpdate(sql);
                if (affectedRows > 0) {
                    System.out.println("Reservation data is deleted !");
                } else {
                    System.out.println("Reservation Deletion Failed !");
                }
            } catch (Throwable var8) {
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (Throwable var7) {
                        var8.addSuppressed(var7);
                    }
                }

                throw var8;
            }

            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException var9) {
            var9.printStackTrace();
        }

    }

    private static boolean reservationExists(Connection con, int reservationID) {
        try {
            String sql = "SELECT  reservation_id FROM hotel_reservation WHERE reservation_id=" + reservationID;
            Statement stmt = con.createStatement();

            boolean var5;
            try {
                ResultSet rs = stmt.executeQuery(sql);

                try {
                    var5 = rs.next();
                } catch (Throwable var9) {
                    if (rs != null) {
                        try {
                            rs.close();
                        } catch (Throwable var8) {
                            var9.addSuppressed(var8);
                        }
                    }

                    throw var9;
                }

                if (rs != null) {
                    rs.close();
                }
            } catch (Throwable var10) {
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (Throwable var7) {
                        var10.addSuppressed(var7);
                    }
                }

                throw var10;
            }

            if (stmt != null) {
                stmt.close();
            }

            return var5;
        } catch (SQLException var11) {
            var11.printStackTrace();
            return false;
        }
    }

    public static void exit() throws InterruptedException {
        System.out.print("exiting System");

        for(int i = 5; i != 0; --i) {
            System.out.print(".");
            Thread.sleep(400L);
        }

        System.out.println();
        System.out.println("Thank you for Visiting to our Hotel.");
    }
}
