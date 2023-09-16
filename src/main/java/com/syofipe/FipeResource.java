package com.syofipe;

import java.io.StringReader;
import java.util.*;

import com.syofipe.domain.Model;
import com.syofipe.repository.ModelRepository;

import jakarta.inject.Inject;
import jakarta.json.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path( "/api/fipe" )
@Produces( MediaType.APPLICATION_JSON )
public class FipeResource {
    @Inject
    ModelRepository modelRepository;

    @Inject
    FipeSchedule fipeSchedule;

    @GET
    public Response getAllModels() {
        return Response.ok( formatModelJson( modelRepository.listAll() ) ).build();
    }

    @GET
    @Path( "/marca/{brand}" )
    public Response getModelsByBrand( @PathParam( "brand" ) String brand ) {
        return Response.ok( formatModelJson( modelRepository.getByBrand( brand ) ) ).build();
    }

    @PATCH
    @Path( "/reprocessar" )
    public Response reprocess( @QueryParam( "marca" ) String brand ) {
        try {
            fipeSchedule.persistFipeModels();
            String json = "{\"status\": \"Reprocessamento da tabela fipe realizado\"}";
            return Response.ok( Json.createReader( new StringReader( json ) ).readObject() ).build();

        } catch ( Exception e ) {
            e.printStackTrace();
            String json = "{\"status\": \"Ocorreu um erro ao reprocessar a tabela\"}";
            return Response.ok( Json.createReader( new StringReader( json ) ).readObject() ).build();
        }
    }

    private JsonObject formatModelJson( List< Model > models ) {
        Map< String, List< Map< String, String > > > brandModelsMap = new HashMap<>();

        for ( Model model : models ) {
            String brandName = model.getBrand().getName();
            String modelName = model.getName();

            brandModelsMap
                    .computeIfAbsent( brandName, k -> new ArrayList<>() )
                    .add( Collections.singletonMap( "modelo", modelName ) );
        }

        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();

        brandModelsMap.forEach( ( brandName, modelsList ) -> {
            JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();

            modelsList.forEach( model -> {
                JsonObjectBuilder modelObjectBuilder = Json
                        .createObjectBuilder();
                modelObjectBuilder.add( "modelo", model.get( "modelo" ) );
                jsonArrayBuilder.add( modelObjectBuilder );
            } );

            jsonObjectBuilder.add( brandName, jsonArrayBuilder );
        } );

        return jsonObjectBuilder.build();
    }

}
