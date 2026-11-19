package config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Carga variables de configuración desde un archivo .env en la raíz del proyecto,
 * con respaldo a variables de entorno del sistema operativo.
 */
public class ConfigEnv {
    private static final Map<String, String> VARIABLES = new HashMap<>();
    private static boolean cargado = false;

    private ConfigEnv() {
    }

    public static synchronized void cargar() {
        if (cargado) {
            return;
        }
        File archivoEnv = new File(".env");
        if (archivoEnv.exists() && archivoEnv.isFile()) {
            try (BufferedReader br = new BufferedReader(new FileReader(archivoEnv))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    String limpia = linea.trim();
                    if (limpia.isEmpty() || limpia.startsWith("#")) {
                        continue;
                    }
                    int sep = limpia.indexOf('=');
                    if (sep > 0) {
                        String clave = limpia.substring(0, sep).trim();
                        String valor = limpia.substring(sep + 1).trim();
                        if ((valor.startsWith("\"") && valor.endsWith("\"")) ||
                            (valor.startsWith("'") && valor.endsWith("'"))) {
                            valor = valor.substring(1, valor.length() - 1);
                        }
                        VARIABLES.put(clave, valor);
                    }
                }
            } catch (IOException e) {
                System.err.println("Aviso al leer .env: " + e.getMessage());
            }
        }
        cargado = true;
    }

    public static String get(String clave, String valorPorDefecto) {
        if (!cargado) {
            cargar();
        }
        if (VARIABLES.containsKey(clave) && !VARIABLES.get(clave).isBlank()) {
            return VARIABLES.get(clave);
        }
        String envSistema = System.getenv(clave);
        if (envSistema != null && !envSistema.isBlank()) {
            return envSistema;
        }
        return valorPorDefecto;
    }
}