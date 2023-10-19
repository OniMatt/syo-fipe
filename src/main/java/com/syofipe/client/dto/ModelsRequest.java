package com.syofipe.client.dto;

public class ModelsRequest {
    private int referenceTableCode;
    private int vehicleTypeCode;
    private int brandCode;

    public ModelsRequest() {
    }

    public ModelsRequest( int referenceTableCode, int vehicleTypeCode, int brandCode ) {
        this.referenceTableCode = referenceTableCode;
        this.vehicleTypeCode = vehicleTypeCode;
        this.brandCode = brandCode;
    }

    public int getReferenceTableCode() {
        return referenceTableCode;
    }

    public void setReferenceTableCode( int referenceTableCode ) {
        this.referenceTableCode = referenceTableCode;
    }

    public int getVehicleTypeCode() {
        return vehicleTypeCode;
    }

    public void setVehicleTypeCode( int vehicleTypeCode ) {
        this.vehicleTypeCode = vehicleTypeCode;
    }

    public int getBrandCode() {
        return brandCode;
    }

    public void setBrandCode( int brandCode ) {
        this.brandCode = brandCode;
    }
}