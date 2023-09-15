package com.syofipe.repository;

import java.util.Optional;

import com.syofipe.domain.Brand;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class BrandRepository implements PanacheRepository< Brand > {
    @Transactional
    public void saveBrand( Brand brand ) {
        Optional< Brand > brandOptional = this.findByNameOptional( brand );

        if ( brandOptional.isPresent() ) {
            return;
        }

        this.getEntityManager().merge( brand );
    }

    private Optional< Brand > findByNameOptional( Brand brand ) {
        return Brand.find( "name like ?1", "%" + brand.getName() + "%" )
                .firstResultOptional();
    }

    @Transactional
    public void delete() {
        this.deleteAll();
    }
}
