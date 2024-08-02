package com.tecnica.manager;

public interface Log {

    public void info(String mensaje);
    public void info(String mensaje, String valorPropiedad);
    public void error(String mensaje);
    public void warm(String mensaje);
}
