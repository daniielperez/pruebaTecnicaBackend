package com.tecnica.messages;

public enum ExceptionMessages {

    ERROR201("el sharedKey %s ya está registrado en el sistema"),
    ERROR202("el email %s ya está registrado en el sistema"),
    ERROR203("el cliente a actualizar no se encuentra en el sistema"),
    ERROR204("el cliente con sharedKey %s no se encuentra en el sistema");

    private String description;

    private ExceptionMessages(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getMensajeConParametros(String... params) {
        String mensaje = String.format(description, (Object[]) params);
        return mensaje;
    }
}
