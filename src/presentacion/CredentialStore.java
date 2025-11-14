package presentacion;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Almacena credenciales simples en un archivo properties local (username=password:role)
 * Solución simple para el proyecto académico; no usar en producción.
 */
public class CredentialStore {
    private static final String CRED_FILE = "credentials.properties";
    private Properties props;

    public CredentialStore() {
        props = new Properties();
        load();
    }

    private void load() {
        File f = new File(CRED_FILE);
        if (!f.exists()) {
            return;
        }
        try (FileInputStream in = new FileInputStream(f)) {
            props.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void save() {
        try (FileOutputStream out = new FileOutputStream(CRED_FILE)) {
            props.store(out, "credentials: username=password:role");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean existsUser(String username) {
        return props.containsKey(username);
    }

    public boolean validate(String username, String password) {
        String v = props.getProperty(username);
        if (v == null) return false;
        String[] parts = v.split(":", 2);
        return parts.length >= 1 && parts[0].equals(password);
    }

    public String getRole(String username) {
        String v = props.getProperty(username);
        if (v == null) return null;
        String[] parts = v.split(":", 2);
        return parts.length == 2 ? parts[1] : null;
    }

    public void saveCredential(String username, String password, String role) {
        props.setProperty(username, password + ":" + role);
        save();
    }
}
