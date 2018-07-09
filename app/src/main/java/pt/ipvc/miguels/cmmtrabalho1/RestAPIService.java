package pt.ipvc.miguels.cmmtrabalho1;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface RestAPIService {

    //Occurrences
    @GET("/event")
    Call<List<Occurrence>> listOccurrences();

    @POST("/event")
    @FormUrlEncoded
    Call<Occurrence> createOccurrence(@Field("street") String street,
                                      @Field("city") String city,
                                      @Field("postal_code_1") Long postal_code_1,
                                      @Field("postal_code_2") Long postal_code_2,
                                      @Field("finished") boolean finished,
                                      @Field("description") String description,
                                      @Field("installation_type_id") String installation_type_id,
                                      @Field("installation_type_name") String installation_type_name,
                                      @Field("latitude") Double latitude,
                                      @Field("longitude") Double longitude);

    @GET("/event/{eventId}")
    Call<Occurrence> getOccurrence(@Path("eventId") String eventId);

    @PUT("/event/{eventId}")
    @FormUrlEncoded
    Call<Occurrence> updateOcccurence(@Path("eventId") String eventId,
                                      @Field("finished") boolean finished);

    @DELETE("/event/{evenId}")
    Call<Occurrence> deleteOccurence();

    //Installations
    @GET("/installation_type")
    Call<List<InstallationType>> listInstallations();
}
