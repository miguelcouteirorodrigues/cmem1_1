package pt.ipvc.miguels.cmmtrabalho1;

import com.google.gson.annotations.SerializedName;

public class Occurrence {

    @SerializedName("_id")
    private String id;
    @SerializedName("street")
    private String street;
    @SerializedName("city")
    private String city;
    @SerializedName("postal_code_1")
    private long postal_code_1;
    @SerializedName("postal_code_2")
    private long postal_code_2;
    @SerializedName("finished")
    private boolean finished;
    @SerializedName("description")
    private String description;
    @SerializedName("installation_type_id")
    private String installation_type_id;
    @SerializedName("installation_type_name")
    private String installation_type_name;
    @SerializedName("latitude")
    private double latitude;
    @SerializedName("longitude")
    private double longitude;

    public Occurrence(String id, String street, String city, long postal_code_1, long postal_code_2, boolean finished, String description, String installation_type_id, String installation_type_name, double latitude, double longitude)
    {
        this.id = id;
        this.street = street;
        this.city = city;
        this.postal_code_1 = postal_code_1;
        this.postal_code_2 = postal_code_2;
        this.finished = finished;
        this.description = description;
        this.installation_type_id = installation_type_id;
        this.installation_type_name = installation_type_name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId()
    {
        return this.id;
    }

    public String getStreet()
    {
        return this.street;
    }

    public String getCity()
    {
        return this.city;
    }

    public Long getPostalCode1()
    {
        return this.postal_code_1;
    }

    public Long getPostalCode2()
    {
        return this.postal_code_2;
    }

    public boolean getisFinished()
    {
        return finished;
    }

    public String getDescription()
    {
        return this.description;
    }

    public String getInstallationTypeId()
    {
        return this.installation_type_id;
    }

    public String getInstallationTypeName()
    {
        return this.installation_type_name;
    }

    public Double getLatitude()
    {
        return this.latitude;
    }

    public Double getLongitude()
    {
        return this.longitude;
    }
}
