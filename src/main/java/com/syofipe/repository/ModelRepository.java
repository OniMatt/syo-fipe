package com.syofipe.repository;

import java.util.List;

import com.syofipe.domain.Model;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ModelRepository implements PanacheRepository< Model > {
    @Transactional
    public void saveModel( Model model ) {
        this.getEntityManager().merge( model );
    }

    public List< Model > getByBrand( String brand ) {
        return Model.find(
                "SELECT m FROM Model m WHERE brand like '" + brand + "'" )
                .list();
    }

    @Transactional
    public void delete() {
        this.deleteAll();
    }
}
