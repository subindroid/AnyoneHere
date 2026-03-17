package dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Car implements Serializable {
    private int carId;
    private String userId;
    private String carBrand;
    private String carModel;
    private int carYear;
    private String carImage;
    private LocalDateTime createdAt;

    public int getCarId() { return carId; }
    public void setCarId(int carId) { this.carId = carId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getCarBrand() { return carBrand; }
    public void setCarBrand(String carBrand) { this.carBrand = carBrand; }

    public String getCarModel() { return carModel; }
    public void setCarModel(String carModel) { this.carModel = carModel; }

    public int getCarYear() { return carYear; }
    public void setCarYear(int carYear) { this.carYear = carYear; }

    public String getCarImage() { return carImage; }
    public void setCarImage(String carImage) { this.carImage = carImage; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Car() {}
}
