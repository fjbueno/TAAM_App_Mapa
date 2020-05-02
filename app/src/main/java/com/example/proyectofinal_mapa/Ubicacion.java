package com.example.proyectofinal_mapa;

public class Ubicacion {
    public Integer _id;
    public String Nombre, Descripcion;
    public Double Latitud, Longitud;

    public Integer get_id(){
        return _id;
    }

    public void set_id(Integer _id){
        this._id = _id;
    }

    public String getNombre(){
        return Nombre;
    }

    public void setNombre(String Nombre){
        this.Nombre = Nombre;
    }

    public String getDescripcion(){
        return Descripcion;
    }

    public void setDescripcion(String Descripcion){
        this.Descripcion = Descripcion;
    }

    public Double getLatitud(){
        return Latitud;
    }

    public void setLatitud(Double Latitud){
        this.Latitud = Latitud;
    }

    public Double getLongitud(){
        return Longitud;
    }

    public void setLongitud(Double Longitud){
        this.Longitud = Longitud;
    }
}
