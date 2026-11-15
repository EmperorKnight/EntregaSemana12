package ni.edu.uam.administracionfx.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
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
import java.util.Optional;

public class EditorController {
    @FXML private TextField txtPNombre;
    @FXML private TextField txtSNombre;
    @FXML private TextField txtPApellido;
    @FXML private TextField txtSApellido;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtUsuario;
    @FXML private  PasswordField txtPassword;
    @FXML private PasswordField txtConfirmaPassword;
    @FXML private ImageView imageFoto;

    private String rutaFoto;
    private Usuario usuarioLocal;

    private final ServicioUsuario serviceUsuario;

    public EditorController(){
        IDao dao = new ImplDao();
        this.serviceUsuario = new ServicioUsuario(dao);
    }

    @FXML
    private void seleccionarFoto() {
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

    public void prepararEditor(Usuario usuario){
        txtPNombre.appendText(usuario.getPrimerNombre());
        txtSNombre.appendText(usuario.getSegundoNombre());
        txtPApellido.appendText(usuario.getPrimerApellido());
        txtSApellido.appendText(usuario.getSegundoApellido());

        if(!usuario.getCorreo().isEmpty()){
            txtCorreo.appendText(usuario.getCorreo());
        }

        String ruta = usuario.getRutaFoto();
        if(ruta == null){
            imageFoto.setImage(null);
        }
        else{
            rutaFoto=ruta;
            mostrarImagen(ruta);
        }

        txtUsuario.appendText(usuario.getUsuario());
        txtPassword.appendText(usuario.getPassword());
        txtConfirmaPassword.appendText(usuario.getPassword());

        usuarioLocal = usuario;
    }

    @FXML
    private void actualizar(){
        if (!validarFormulario()) {
            return;
        }

        usuarioLocal.setPrimerNombre(txtPNombre.getText());
        usuarioLocal.setSegundoNombre(txtSNombre.getText());
        usuarioLocal.setPrimerApellido(txtPApellido.getText());
        usuarioLocal.setSegundoApellido(txtSApellido.getText());
        usuarioLocal.setCorreo(txtCorreo.getText());
        usuarioLocal.setUsuario(txtUsuario.getText());
        usuarioLocal.setPassword(txtPassword.getText());
        usuarioLocal.setRutaFoto(rutaFoto);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText(null);
        alert.setContentText("Actualizar Datos del usuario?");

        Optional<ButtonType> resultado = alert.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            serviceUsuario.update(usuarioLocal);
            UtilController.mostrarMensaje(Alert.AlertType.INFORMATION, "Confirmacion","Usuario Actualizado con exito");
            regresar();
        }
        else{
            UtilController.mostrarMensaje(Alert.AlertType.INFORMATION, "Confirmacion","Actualizacion del usuario cancelada");
        }
    }

    private boolean validarFormulario() {
        if (txtPNombre.getText().isBlank()
                || txtPApellido.getText().isBlank()
                || txtUsuario.getText().isBlank()
                || txtPassword.getText().isBlank()
                || txtConfirmaPassword.getText().isBlank()
        ) {
            UtilController.mostrarMensaje(Alert.AlertType.WARNING,
                    "Validacion",
                    "Todos los campos son obligatorios");
            return false;
        }
        if (!txtPassword.getText().equals(txtConfirmaPassword.getText())) {
            UtilController.mostrarMensaje(
                    Alert.AlertType.WARNING,
                    "Contraseña",
                    "Contraseña no coincide"
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
}
