package com.example.proyectofinal_mapa;

import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener{

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://com.example.proyectofinal_registro");
    private static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath("Ubicaciones").build();
    public static final String KEY_ROWID = "_id";
    public static final String KEY_NOMBRE = "Nombre";
    public static final String KEY_DESCRIPCION = "Descripcion";
    public static final String KEY_LATITUD = "Latitud";
    public static final String KEY_LONGITUD = "Longitud";

    private ArrayList<Ubicacion> listUbicacion;
    private HashMap<String, Integer> MarkMap;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
    protected void onResume()
    {
        super.onResume();
        if (this.mMap != null)
        {
            this.Ubicaciones();
            this.Marcadores();
        }
    }

    private void Ubicaciones() {
        this.listUbicacion = new ArrayList<>();
        listUbicacion.clear();
        this.MarkMap = new HashMap<>();
        Cursor cursor = getContentResolver().query(CONTENT_URI, null, null,null, null);
        if(cursor !=null && cursor.moveToFirst()){
            do{
                Ubicacion ubicacion = new Ubicacion();
                ubicacion.set_id(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ROWID)));
                ubicacion.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NOMBRE)));
                ubicacion.setDescripcion(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESCRIPCION)));
                ubicacion.setLatitud(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_LATITUD)));
                ubicacion.setLongitud(cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_LONGITUD)));
                listUbicacion.add(ubicacion);
            } while (cursor.moveToNext());
        }
        if(listUbicacion.isEmpty()){
            Toast.makeText(this, "Sin Ubicaciones", Toast.LENGTH_LONG).show();
        }
    }

    private void Marcadores() {
        mMap.clear();
        for (Ubicacion ubicacion: listUbicacion){
            LatLng latLng = new LatLng(ubicacion.getLatitud(), ubicacion.getLongitud());
            String marker = mMap.addMarker(new MarkerOptions().position(latLng).title(ubicacion.getNombre()).snippet(ubicacion.getDescripcion())).getId();
            this.MarkMap.put(marker, ubicacion.get_id());
        }
        if(!this.listUbicacion.isEmpty()){
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(listUbicacion.get(listUbicacion.size() - 1).getLatitud(), listUbicacion.get(listUbicacion.size() - 1).getLongitud()),15));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Ubicaciones();
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        Marcadores();
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true)
                .setMessage("Eliminar la Ubicacion: " + marker.getTitle())
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        eliminarMarker(marker.getId());
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return true;
    }

    public void eliminarMarker(String markerId){
        String idMarker = this.MarkMap.get(markerId).toString();
        if (getContentResolver().delete(Uri.parse(CONTENT_URI.toString() + "/" + idMarker), null, null)>0){
            this.Ubicaciones();
            this.Marcadores();
        }
        else {
            Toast.makeText(this, "Error, Intente Nuevamente", Toast.LENGTH_LONG).show();
        }
    }
}
