package com.syofipe.client;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.syofipe.repository.*;

import io.quarkus.scheduler.Scheduled;
import io.quarkus.scheduler.Scheduled.ConcurrentExecution;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class FipeSchedule {

    @Inject
    @RestClient
    FipeClient fipeClient;

    @Inject
    FipeService fipeService;

    @Inject
    BrandRepository brandRepository;

    @Inject
    ModelRepository modelRepository;

    @Scheduled( every = "24h", concurrentExecution = ConcurrentExecution.SKIP )
    public void persistFipeModels() {
        try {
            modelRepository.delete();
            brandRepository.delete();
            String bearerToken = "Bearer " + fipeService.login();
            int lastTableCode = fipeService.getTables( bearerToken );
            fipeService.getBrands( bearerToken, lastTableCode );
            fipeService.getModels( bearerToken, lastTableCode );
        } catch ( Exception e ) {
            e.printStackTrace();
        }

    }
}
