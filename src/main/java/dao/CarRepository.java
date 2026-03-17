package dao;

import dto.Car;
import util.DBUtil;

import java.sql.*;
import java.util.ArrayList;

public class CarRepository {

    public static ArrayList<Car> getCarsByUserId(String userId) {
        ArrayList<Car> cars = new ArrayList<>();
        String sql = "SELECT * FROM user_cars WHERE user_id = ? ORDER BY created_at DESC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Car car = new Car();
                    car.setCarId(rs.getInt("car_id"));
                    car.setUserId(rs.getString("user_id"));
                    car.setCarBrand(rs.getString("car_brand"));
                    car.setCarModel(rs.getString("car_model"));
                    car.setCarYear(rs.getInt("car_year"));
                    car.setCarImage(rs.getString("car_image"));
                    Timestamp ts = rs.getTimestamp("created_at");
                    if (ts != null) car.setCreatedAt(ts.toLocalDateTime());
                    cars.add(car);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cars;
    }

    public static void insertCar(Car car) {
        String sql = """
            INSERT INTO user_cars (user_id, car_brand, car_model, car_year, car_image)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, car.getUserId());
            pstmt.setString(2, car.getCarBrand());
            pstmt.setString(3, car.getCarModel());
            pstmt.setInt(4, car.getCarYear());
            pstmt.setString(5, car.getCarImage());
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // userId 검증 포함 (본인 차만 삭제 가능)
    public static void deleteCar(int carId, String userId) {
        String sql = "DELETE FROM user_cars WHERE car_id = ? AND user_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, carId);
            pstmt.setString(2, userId);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
