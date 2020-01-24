package com.example.nrg_monitor.main.app;

public class Device {

    private int device_id;
    private String device_name;
    private String device_type;
    private String device_brand;
    private Integer device_wattage;
    private Double device_runtime;
    private Integer device_activity_status;

    public Device(Integer device_id,String device_name, String device_type, String device_brand, Integer device_wattage, Double device_runtime, Integer device_activity_status) {

        this.device_id = device_id;
        this.device_name = device_name;
        this.device_type = device_type;
        this.device_brand = device_brand;
        this.device_wattage = device_wattage;
        this.device_runtime = device_runtime;
        this.device_activity_status = device_activity_status;
    }

    public Device(String device_name, String device_type, String device_brand, Integer device_wattage, Double device_runtime, Integer device_activity_status) {

        this.device_name = device_name;
        this.device_type = device_type;
        this.device_brand = device_brand;
        this.device_wattage = device_wattage;
        this.device_runtime = device_runtime;
        this.device_activity_status = device_activity_status;
    }


    public int getDevice_id() {
        return device_id;
    }

    public void setDevice_id(int device_id) {
        this.device_id = device_id;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public String getDevice_brand() {
        return device_brand;
    }

    public void setDevice_brand(String device_brand) {
        this.device_brand = device_brand;
    }

    public Integer getDevice_wattage() {
        return device_wattage;
    }

    public void setDevice_wattage(Integer device_wattage) {
        this.device_wattage = device_wattage;
    }

    public Double getDevice_runtime() {
        return device_runtime;
    }

    public void setDevice_runtime(Double device_runtime) {
        this.device_runtime = device_runtime;
    }

    public Integer getDevice_activity_status() {
        return device_activity_status;
    }

    public void setDevice_activity_status(Integer device_activity_status) {
        this.device_activity_status = device_activity_status;
    }
}
