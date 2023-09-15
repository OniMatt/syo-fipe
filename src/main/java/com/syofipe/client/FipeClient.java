package com.syofipe.client;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import com.syofipe.client.dto.*;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

@Path( "/api/v2" )
@RegisterRestClient( configKey = "fipe-api" )
@Produces( MediaType.APPLICATION_JSON )
@ApplicationScoped
public interface FipeClient {

    @POST
    @Path( "/login" )
    Response login( LoginRequest requestBody );

    @POST
    @Path( "/vehicles/ConsultarTabelaDeReferencia" )
    Response getTables(
            @HeaderParam( "Authorization" ) String bearerToken,
            @HeaderParam( "DeviceToken" ) String DeviceToken );

    @POST
    @Path( "/vehicles/ConsultarMarcas" )
    Response getBrands(
            @HeaderParam( "Authorization" ) String bearerToken,
            @HeaderParam( "DeviceToken" ) String DeviceToken,
            BrandsRequest requestBody );

    @POST
    @Path( "/vehicles/ConsultarModelos" )
    Response getModels(
            @HeaderParam( "Authorization" ) String bearerToken,
            @HeaderParam( "DeviceToken" ) String DeviceToken,
            ModelsRequest requestBody );

}
