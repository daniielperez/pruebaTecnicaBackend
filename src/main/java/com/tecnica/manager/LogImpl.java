package com.tecnica.manager;


import com.tecnica.utils.UtilConstantes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

@Service
public class LogImpl implements Log {

    private static final Logger log = LoggerFactory.getLogger(LogImpl.class);

    public void info(String mensaje) { log.info(mensaje); }
    public void info(String mensaje, String valorPropiedad) {
        MDC.put(UtilConstantes.LOG_VARIABLE_DINAMICA, valorPropiedad);
        log.info(mensaje);
    }
    public void error(String mensaje) {
        log.error(mensaje);
    }
    public void warm(String mensaje) {
        log.warn(mensaje);
    }
}