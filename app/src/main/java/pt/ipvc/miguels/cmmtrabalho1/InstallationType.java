package pt.ipvc.miguels.cmmtrabalho1;

import com.google.gson.annotations.SerializedName;

public class InstallationType {

    @SerializedName("_id")
    private String id;
    @SerializedName("description")
    private String description;

    public InstallationType(String id, String description)
    {
        this.id = id;
        this.description = description;
    }

    public String getId()
    {
        return this.id;
    }

    public String getDescription()
    {
        return description;
    }
}
