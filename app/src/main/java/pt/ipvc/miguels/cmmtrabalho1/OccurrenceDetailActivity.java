package pt.ipvc.miguels.cmmtrabalho1;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OccurrenceDetailActivity extends AppCompatActivity implements OnMapReadyCallback
{
    private Occurrence occurrence;

    private Helpers.ViewMode viewMode;
    private boolean isViewMode;
    private boolean isFinishButtonClickable;
    private ArrayList<String> ids = new ArrayList<>();

    private EditText address;
    private EditText city;
    private EditText postalCode1;
    private EditText postalCode2;
    private EditText description;
    private Spinner installationType;
    private Double latitude = 0.0;
    private Double longitude = 0.0;

    private GoogleMap mMap;

    private Button saveRecord;
    private Button markAsFinished;

    private FusedLocationProviderClient mFusedLocationClient;
    private SupportMapFragment mapFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.occurrence_detail);

        address = findViewById(R.id.addressText);
        city = findViewById(R.id.cityText);
        postalCode1 = findViewById(R.id.postalCode1Text);
        postalCode2 = findViewById(R.id.postalCode2Text);
        description = findViewById(R.id.descriptionText);

        installationType = findViewById(R.id.installationTypeSpinner);

        saveRecord = findViewById(R.id.saveButton);
        markAsFinished = findViewById(R.id.markAsFinishedButton);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        //Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        Intent myIntent = getIntent(); // gets the previously created intent
        viewMode = (Helpers.ViewMode)myIntent.getSerializableExtra("View");

        try
        {
            latitude = myIntent.getDoubleExtra("latitude", 0);
            longitude = myIntent.getDoubleExtra("longitude", 0);
        }
        catch (Exception ex)
        {

        }

        switch (viewMode)
        {
            case CREATE:
                isViewMode = false;
                isFinishButtonClickable = false;
                break;
            case EDIT:
                isViewMode = false;
                isFinishButtonClickable = false;
                break;
            case VIEW_UNFINISHED:
                isViewMode = true;
                isFinishButtonClickable = true;
                break;
            case VIEW_FINISHED:
                isViewMode = true;
                isFinishButtonClickable = false;
                break;
            default:
                isViewMode = false;
                isFinishButtonClickable = false;
                break;
        }

        if (viewMode == Helpers.ViewMode.CREATE) {
            manageElements(viewMode);
            mapFragment.getMapAsync(OccurrenceDetailActivity.this);
        }
        else
        {
            String recordId = myIntent.getStringExtra("id");

            RestAPIService instance = RetrofitAdapter.getInstance();

            Call<Occurrence> occurrenceRecord = instance.getOccurrence(recordId);

            occurrenceRecord.enqueue(new Callback<Occurrence>() {
                @Override
                public void onResponse(Call<Occurrence> occurrenceRecord, Response<Occurrence> response) {
                    occurrence = response.body();
                    manageElements(viewMode);
                    mapFragment.getMapAsync(OccurrenceDetailActivity.this);
                }

                @Override
                public void onFailure(Call<Occurrence> listCall, Throwable t) {
                    Toast.makeText(OccurrenceDetailActivity.this, "Something went wrong... Please try again later!", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (viewMode == Helpers.ViewMode.CREATE && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }

        // Add a marker in Sydney and move the camera
        LatLng location = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(location));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 18.0f));
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setScrollGesturesEnabled(false);
        mMap.getUiSettings().setZoomGesturesEnabled(false);
        mMap.getUiSettings().setTiltGesturesEnabled(false);
        mMap.getUiSettings().setRotateGesturesEnabled(false);
    }

    private void manageElements(Helpers.ViewMode viewMode)
    {
        getInstallationTypes(installationType, occurrence);

        if (occurrence != null)
        {
            address.setText(occurrence.getStreet());
            address.setEnabled(!isViewMode);

            city.setText(occurrence.getCity());
            city.setEnabled(!isViewMode);

            postalCode1.setText(("0000" + occurrence.getPostalCode1().toString()).substring(occurrence.getPostalCode1().toString().length()));
            postalCode1.setEnabled(!isViewMode);

            postalCode2.setText(("000" + occurrence.getPostalCode2().toString()).substring(occurrence.getPostalCode2().toString().length()));
            postalCode2.setEnabled(!isViewMode);

            description.setText(occurrence.getDescription());
            description.setEnabled(!isViewMode);

            installationType.setSelection(ids.indexOf((occurrence.getInstallationTypeId())));
            installationType.setEnabled(!isViewMode);

            latitude = occurrence.getLatitude();
            longitude = occurrence.getLongitude();

            markAsFinished.setEnabled(isFinishButtonClickable);
        }

        if (viewMode == Helpers.ViewMode.VIEW_UNFINISHED || viewMode == Helpers.ViewMode.VIEW_FINISHED)
        {
            saveRecord.setVisibility(View.GONE);
            markAsFinished.setVisibility(View.VISIBLE);
        }
        else
        {
            saveRecord.setVisibility(View.VISIBLE);
            markAsFinished.setVisibility(View.GONE);
        }

        if (saveRecord.getVisibility() == View.VISIBLE)
        {
            saveRecord.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    RestAPIService instance = RetrofitAdapter.getInstance();

                    Occurrence occurrence = createOccurrence();

                    if (occurrence != null) {
                        Call<Occurrence> occurrenceRecord = instance.createOccurrence(occurrence.getStreet(),
                                occurrence.getCity(),
                                occurrence.getPostalCode1(),
                                occurrence.getPostalCode2(),
                                occurrence.getisFinished(),
                                occurrence.getDescription(),
                                occurrence.getInstallationTypeId(),
                                occurrence.getInstallationTypeName(),
                                occurrence.getLatitude(),
                                occurrence.getLongitude());

                        occurrenceRecord.enqueue(new Callback<Occurrence>() {
                            @Override
                            public void onResponse(Call<Occurrence> occurrenceRecord, Response<Occurrence> response) {
                                Toast.makeText(OccurrenceDetailActivity.this, "Record saved successfully", Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            }

                            @Override
                            public void onFailure(Call<Occurrence> listCall, Throwable t) {
                                Toast.makeText(OccurrenceDetailActivity.this, "Something went wrong... Please try again later!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }

        if (markAsFinished.getVisibility() == View.VISIBLE && markAsFinished.isEnabled() == true)
        {
            markAsFinished.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //DoIt(v);
                    RestAPIService instance = RetrofitAdapter.getInstance();

                    Call<Occurrence> occurrenceRecord = instance.updateOcccurence(occurrence.getId(), true);

                    occurrenceRecord.enqueue(new Callback<Occurrence>() {
                        @Override
                        public void onResponse(Call<Occurrence> occurrenceRecord, Response<Occurrence> response) {
                            Toast.makeText(OccurrenceDetailActivity.this, "Record finished successfully", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }

                        @Override
                        public void onFailure(Call<Occurrence> listCall, Throwable t) {
                            Toast.makeText(OccurrenceDetailActivity.this, "Something went wrong... Please try again later!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }

    private void getInstallationTypes(final Spinner installationType, final Occurrence occurrence)
    {
        RestAPIService instance = RetrofitAdapter.getInstance();

        Call<List<InstallationType>> listCall = instance.listInstallations();

        listCall.enqueue(new Callback<List<InstallationType>>() {
            @Override
            public void onResponse(Call<List<InstallationType>> listCall, Response<List<InstallationType>> response) {
                getInstallationTypesInternal(response, installationType, occurrence);
            }

            @Override
            public void onFailure(Call<List<InstallationType>> listCall, Throwable t) {
                Toast.makeText(OccurrenceDetailActivity.this, "Something went wrong... Please try again later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getInstallationTypesInternal(Response<List<InstallationType>> response, Spinner installationType, Occurrence occurrence)
    {
        List<InstallationType> result = response.body();

        String[] items = new String[response.body().size()];

        for (int i = 0; i < result.size(); i++)
        {
            InstallationType installType = result.get(i);
            items[i] = installType.getDescription();
            ids.add(installType.getId());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);

        installationType.setAdapter(adapter);
    }

    private Occurrence createOccurrence()
    {
        if (address.getText() == null)
        {
            Toast.makeText(OccurrenceDetailActivity.this, "Address cannot be empty.", Toast.LENGTH_SHORT).show();
            return null;
        }
        else if (city.getText() == null)
        {
            Toast.makeText(OccurrenceDetailActivity.this, "City cannot be empty.", Toast.LENGTH_SHORT).show();
            return null;
        }
        else if (postalCode1.getText() == null || postalCode2.getText() == null)
        {
            Toast.makeText(OccurrenceDetailActivity.this, "Postal code cannot be empty.", Toast.LENGTH_SHORT).show();
            return null;
        }
        else if (description.getText() == null)
        {
            Toast.makeText(OccurrenceDetailActivity.this, "Description cannot be empty.", Toast.LENGTH_SHORT).show();
            return null;
        }
        else if (installationType.getSelectedItem() == null)
        {
            Toast.makeText(OccurrenceDetailActivity.this, "Installation Type cannot be empty.", Toast.LENGTH_SHORT).show();
            return null;
        }

        Occurrence occurrence = new Occurrence(
                null,
                address.getText().toString(),
                city.getText().toString(),
                Long.parseLong(postalCode1.getText().toString()),
                Long.parseLong(postalCode2.getText().toString()),
                false,
                description.getText().toString(),
                ids.get(installationType.getSelectedItemPosition()),
                installationType.getSelectedItem().toString(),
                //ids.indexOf(installationType.getSelectedItemId()),
                latitude,
                longitude

        );

        return occurrence;
    }
}

