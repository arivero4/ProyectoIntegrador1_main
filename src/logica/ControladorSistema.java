package logica;
import negocio.alertas.GestorAlertas;
import negocio.cultivos.GestorCultivos;
import negocio.departamentos.GestorDepartamentos;
import negocio.informes.GestorInformes;
import negocio.inspecciones.GestorInspecciones;
import negocio.municipios.GestorMunicipios;
import negocio.plagas.GestorPlagas;
import negocio.predios.GestorPredios;
import negocio.resultados.GestorResultados;
import negocio.veredas.GestorVeredas;
import negocio.usuarios.GestorUsuarios;

public class ControladorSistema {
    private GestorPredios gestorPredios;
    private GestorInspecciones gestorInspecciones;
    private GestorResultados gestorResultados;
    private GestorUsuarios gestorUsuarios;
    private GestorAlertas gestorAlertas;
    private GestorCultivos gestorCultivos;
    private GestorDepartamentos gestorDepartamentos;
    private GestorInformes gestorInformes;
    private GestorMunicipios gestorMunicipios;
    private GestorPlagas gestorPlagas;
    private GestorVeredas gestorVeredas;

    public ControladorSistema() {
        this.gestorPredios = new GestorPredios();
        this.gestorInspecciones = new GestorInspecciones();
        this.gestorResultados = new GestorResultados();
        this.gestorUsuarios = new GestorUsuarios();
        this.gestorAlertas = new GestorAlertas();
        this.gestorCultivos = new GestorCultivos();
        this.gestorDepartamentos = new GestorDepartamentos();
        this.gestorInformes = new GestorInformes();
        this.gestorMunicipios = new GestorMunicipios();
        this.gestorPlagas = new GestorPlagas();
        this.gestorVeredas = new GestorVeredas();
    }

    public GestorPredios getGestorPredios() {
        return gestorPredios;
    }

    public GestorInspecciones getGestorInspecciones() {
        return gestorInspecciones;
    }

    public GestorResultados getGestorResultados() {
        return gestorResultados;
    }

    public GestorUsuarios getGestorUsuarios() {
        return gestorUsuarios;
    }

    public GestorAlertas getGestorAlertas() {
        return gestorAlertas;
    }

    public GestorCultivos getGestorCultivos() {
        return gestorCultivos;
    }

    public GestorDepartamentos getGestorDepartamentos() {
        return gestorDepartamentos;
    }

    public GestorInformes getGestorInformes() {
        return gestorInformes;
    }

    public GestorMunicipios getGestorMunicipios() {
        return gestorMunicipios;
    }

    public GestorPlagas getGestorPlagas() {
        return gestorPlagas;
    }

    public GestorVeredas getGestorVeredas() {
        return gestorVeredas;
    }
    
    // Métodos para gestión de propietarios
    public boolean registrarPropietario(model.Propietario propietario) {
        return gestorUsuarios.registrarPropietario(propietario);
    }
    
    public model.Propietario consultarPropietario(String numeroIdentificacion) {
        return gestorUsuarios.consultarPropietario(numeroIdentificacion);
    }
    
    public boolean actualizarPropietario(model.Propietario propietario) {
        return gestorUsuarios.actualizarPropietario(propietario);
    }
    
    public java.util.List<model.Propietario> listarPropietarios() {
        return gestorUsuarios.listarPropietarios();
    }
}
