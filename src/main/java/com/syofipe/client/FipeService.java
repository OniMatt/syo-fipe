package com.syofipe.client;

import java.io.StringReader;
import java.util.*;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.syofipe.client.dto.*;
import com.syofipe.domain.*;
import com.syofipe.repository.*;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.*;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class FipeService {
    private final int VEHICLE_TYPE_CODE_CAR = 1;
    private final String HONDA_LABEL = "Honda";
    private final String VW_LABEL = "VW - VolksWagen";
    private final String TOYOTA_LABEL = "Toyota";
    private final String[] BRANDS = { HONDA_LABEL, VW_LABEL, TOYOTA_LABEL };

    @Inject
    @RestClient
    FipeClient fipeClient;

    @Inject
    BrandRepository brandRepository;

    @Inject
    ModelRepository modelRepository;

    @ConfigProperty( name = "fipe-api.email" )
    String email;
    @ConfigProperty( name = "fipe-api.password" )
    String password;
    @ConfigProperty( name = "fipe-api.device-token" )
    String deviceToken;

    public String login() {
        try {
            LoginRequest loginRequest = new LoginRequest( email, password );
            Response response = fipeClient.login( loginRequest );
            return extractToken( response.readEntity( String.class ) );
        } catch ( Exception e ) {
            System.out.println( e );
            return e.toString();
        }
    }

    public int getTables( String bearerToken ) {
        try {
            Response response = fipeClient.getTables( bearerToken,
                    deviceToken );
            return extractLatestTableCode(
                    response.readEntity( String.class ) );
        } catch ( Exception e ) {
            System.out.println( e );
            return 0;
        }
    }

    public void getBrands( String bearerToken, int tableCode ) {
        BrandsRequest brandsRequest = new BrandsRequest( tableCode,
                VEHICLE_TYPE_CODE_CAR );
        try {
            Response response = fipeClient.getBrands( bearerToken, deviceToken,
                    brandsRequest );
            persistBrands( response.readEntity( String.class ) );
        } catch ( Exception e ) {
            System.out.println( e );
        }
    }

    public void getModels( String bearerToken, int tableCode ) {
        try {
            List< Brand > brands = brandRepository.listAll();

            brands.stream().forEach( brand -> {
                ModelsRequest modelsRequest = new ModelsRequest( tableCode,
                        VEHICLE_TYPE_CODE_CAR, brand.getCode() );
                Response response = fipeClient.getModels( bearerToken,
                        deviceToken, modelsRequest );
                persistModels( response.readEntity( String.class ), brand );
            } );
        } catch ( Exception e ) {
            System.out.println( e );
        }
    }

    private String extractToken( String responseBody ) {
        JsonObject jsonObject = Json.createReader( new StringReader( responseBody ) ).readObject();
        return jsonObject.getJsonObject( "authorization" ).getString( "token" );
    }

    private int extractLatestTableCode( String responseBody ) {
        JsonObject jsonObject = Json.createReader( new StringReader( responseBody ) ).readObject();
        JsonArray jsonArray = jsonObject.getJsonArray( "response" );
        JsonObject firstObject = jsonArray.getJsonObject( 0 );
        return firstObject.getInt( "Codigo" );
    }

    private void persistBrands( String responseBody ) {
        JsonObject jsonObject = Json.createReader( new StringReader( responseBody ) ).readObject();
        JsonArray jsonArray = jsonObject.getJsonArray( "response" );

        for ( int i = 0; i < jsonArray.size(); i++ ) {
            JsonObject brandJson = jsonArray.getJsonObject( i );
            String label = brandJson.getString( "Label" );
            int code = Integer.parseInt( brandJson.getString( "Value" ) );

            if ( Arrays.asList( BRANDS ).contains( label ) ) {
                Brand brand = new Brand( label, code );
                brandRepository.saveBrand( brand );
            }
        }
    }

    private void persistModels( String responseBody, Brand brand ) {
        JsonObject jsonObject = Json.createReader( new StringReader( responseBody ) ).readObject();
        JsonObject responseArray = jsonObject.getJsonObject( "response" );
        JsonArray modelsArray = responseArray.getJsonArray( "Modelos" );

        for ( int i = 0; i < modelsArray.size(); i++ ) {
            JsonObject modelJson = modelsArray.getJsonObject( i );
            String label = modelJson.getString( "Label" );

            Model model = new Model( brand, label );

            modelRepository.saveModel( model );
        }
    }

}
