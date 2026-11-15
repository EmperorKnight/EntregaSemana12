package ni.edu.uam.administracionfx.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ni.edu.uam.administracionfx.dao.IDao;
import ni.edu.uam.administracionfx.dao.ImplDao;
import ni.edu.uam.administracionfx.dao.ServicioUsuario;
import ni.edu.uam.administracionfx.model.Usuario;

import java.io.File;

public class RegistroController {
    @FXML private TextField txtPNombre;
    @FXML private TextField txtSNombre;
    @FXML private TextField txtPApellido;
    @FXML private TextField txtSApellido;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPassword;
    @FXML private PasswordField txtConfirmaPassword;
    @FXML private ImageView imageFoto;

    private String rutaFoto;

    private final ServicioUsuario serviceUsuario;

    public RegistroController(){
        IDao dao = new ImplDao();
        this.serviceUsuario = new ServicioUsuario(dao);
    }

    @FXML
    private void seleccionarFoto()
    {
        File archivo = seleccionarArchivo();
        if (archivo == null) {
            UtilController.mostrarMensaje(Alert.AlertType.WARNING, "No hay Archivo", "Debe de seleccionar una fotografia");
            return;
        }
        try {
            rutaFoto = archivo.getAbsolutePath();
            mostrarImagen(rutaFoto);
        }
        catch (Exception e) {
            UtilController.mostrarException(e);
        }
    }

    private void mostrarImagen(String rutaFoto) {
        Image image = new Image(new File(rutaFoto).toURI().toString());
        imageFoto.setImage(image);
    }

    private File seleccionarArchivo() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Seleccionar archivo");
        fc.getExtensionFilters()
                .add(new FileChooser
                        .ExtensionFilter("Imagenes"
                        , "*.jpg"
                        , "*.png", "*.jpeg"));
        return fc.showOpenDialog(
                imageFoto
                        .getScene()
                        .getWindow());
    }

    @FXML
    private void registrar(){
        if (!validarFormulario()) {
            return;
        }
        Usuario usuario = new Usuario();
        usuario.setPrimerNombre(txtPNombre.getText());
        usuario.setSegundoNombre(txtSNombre.getText());
        usuario.setPrimerApellido(txtPApellido.getText());
        usuario.setSegundoApellido(txtSApellido.getText());
        usuario.setCorreo(txtCorreo.getText());
        usuario.setPassword(txtPassword.getText());
        usuario.setUsuario(txtUsuario.getText());
        usuario.setRutaFoto(rutaFoto);

        serviceUsuario.save(usuario);

        UtilController.mostrarMensaje(
                Alert.AlertType.CONFIRMATION,
                "Registro con exito",
                "Usuario Registrado con exito"
        );
        limpiarCampos();
    }


    private boolean validarFormulario() {
        if (txtPNombre.getText().isBlank()
                || txtPApellido.getText().isBlank()
                || txtCorreo.getText().isBlank()
                || txtUsuario.getText().isBlank()
                || txtPassword.getText().isBlank()
                || txtConfirmaPassword.getText().isBlank()
        ) {
            UtilController.mostrarMensaje(
                    Alert.AlertType.WARNING,
                    "Validacion",
                    "Todos los campos son obligatorios"
            );
            return false;
        }
        if (!txtPassword.getText()
                .equals(txtConfirmaPassword.getText())) {
            UtilController.mostrarMensaje(
                    Alert.AlertType.WARNING,
                    "Contraseña",
                    "Contraseña no coincide"
            );
            return false;
        }
        if (rutaFoto == null) {
            UtilController.mostrarMensaje(
                    Alert.AlertType.WARNING
                    ,"Fotografia"
                    ,"Debe seleccionar una fotografia"
            );
            return false;
        }
        return true;
    }

    @FXML
    private void regresar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ni/edu/uam/administracionfx/hello-view.fxml"));
            Parent root = loader.load();

            Scene scene = txtPNombre.getScene();
            scene.setRoot(root);

            Stage stage = (Stage) scene.getWindow();
            stage.sizeToScene();
            stage.centerOnScreen();
        }catch (Exception e){
            UtilController.mostrarException(e);
        }
    }

    private void limpiarCampos(){
        txtPNombre.clear();
        txtSNombre.clear();
        txtPApellido.clear();
        txtSApellido.clear();
        txtCorreo.clear();
        txtUsuario.clear();
        txtPassword.clear();
        txtConfirmaPassword.clear();
        imageFoto.setImage(null);
    }
}
