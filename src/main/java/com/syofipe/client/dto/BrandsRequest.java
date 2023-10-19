package com.syofipe.client.dto;

public class BrandsRequest {
    private int referenceTableCode;
    private int vehicleTypeCode;

    public BrandsRequest() {
    }

    public BrandsRequest( int referenceTableCode, int vehicleTypeCode ) {
        this.referenceTableCode = referenceTableCode;
        this.vehicleTypeCode = vehicleTypeCode;
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
}