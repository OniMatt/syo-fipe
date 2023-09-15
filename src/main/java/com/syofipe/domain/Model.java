package com.syofipe.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

@Entity
public class Model extends PanacheEntity {
    @ManyToOne
    @JoinColumn( name = "brand", referencedColumnName = "name" )
    private Brand brand;
    private String name;

    public Model() {
    }

    public Model( Brand brand, String name ) {
        this.brand = brand;
        this.name = name;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand( Brand brand ) {
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }
}
