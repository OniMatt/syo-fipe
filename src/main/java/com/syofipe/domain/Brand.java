package com.syofipe.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

@Entity
public class Brand extends PanacheEntity {
    @Column( unique = true )
    private String name;
    private int code;

    public Brand() {
    }

    public Brand( String name, int code ) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode( int code ) {
        this.code = code;
    }
}
