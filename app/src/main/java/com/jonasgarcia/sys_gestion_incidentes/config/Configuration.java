package com.jonasgarcia.sys_gestion_incidentes.config;

public class Configuration {
    public String urlUsuarios, urlAuth, urlIncidentes;

    public Configuration(){
        this.urlUsuarios = "https://sysgi-jonasg4.000webhostapp.com/api/v1/usuarios";
        this.urlAuth = "https://sysgi-jonasg4.000webhostapp.com/api/v1/auth";
        this.urlIncidentes = "https://sysgi-jonasg4.000webhostapp.com/api/v1/incidentes";
    }
}
