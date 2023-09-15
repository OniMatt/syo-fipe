package com.syofipe;

import java.io.StringReader;
import java.util.*;

import com.syofipe.client.FipeSchedule;
import com.syofipe.domain.Model;
import com.syofipe.repository.ModelRepository;

import jakarta.inject.Inject;
import jakarta.json.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path( "/api/fipe" )
@Produces( MediaType.APPLICATION_JSON )
public class FipeResource {
    @Inject
    ModelRepository modelRepository;

    @Inject
    FipeSchedule fipeSchedule;

    @GET
    public JsonObject getAllModels() {
        return formatModelJson( modelRepository.listAll() );
    }

    @GET
    @Path( "/marca/{brand}" )
    public JsonObject getModelsByBrand( @PathParam( "brand" ) String brand ) {
        return formatModelJson( modelRepository.getByBrand( brand ) );
    }

    @PATCH
    @Path( "/reprocessar" )
    public JsonObject reprocess( @QueryParam( "marca" ) String brand ) {
        try {
            fipeSchedule.persistFipeModels();
            String json = "{\"status\": \"Reprocessamento da tabela fipe realizado\"}";
            return Json.createReader( new StringReader( json ) ).readObject();

        } catch ( Exception e ) {
            e.printStackTrace();
            String json = "{\"status\": \"Ocorreu um erro ao reprocessar a tabela\"}";
            return Json.createReader( new StringReader( json ) ).readObject();

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
