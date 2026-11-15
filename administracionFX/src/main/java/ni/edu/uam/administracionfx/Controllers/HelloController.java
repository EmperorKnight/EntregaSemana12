package ni.edu.uam.administracionfx.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import ni.edu.uam.administracionfx.dao.IDao;
import ni.edu.uam.administracionfx.dao.ImplDao;
import ni.edu.uam.administracionfx.dao.ServicioUsuario;
import ni.edu.uam.administracionfx.model.Usuario;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML private TextField txtParametro;
    @FXML private TableView<Usuario> Usuarios;
    @FXML private TableColumn<Usuario, String> colID;
    @FXML private TableColumn<Usuario, String> colNombre;
    @FXML private TableColumn<Usuario, String> colSNombre;
    @FXML private TableColumn<Usuario, String> colApellido;
    @FXML private TableColumn<Usuario, String> colSApellido;
    @FXML private TableColumn<Usuario, String> colCorreo;
    @FXML private TableColumn<Usuario, String> colUsuario;
    @FXML private TableColumn<Usuario, String> colPassword;
    @FXML private TableColumn<Usuario, String> colRutaFoto;

    private final ServicioUsuario serviceUsuario;

    public HelloController(){
        IDao dao = new ImplDao();
        this.serviceUsuario = new ServicioUsuario(dao);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("primerNombre"));
        colSNombre.setCellValueFactory(new PropertyValueFactory<>("segundoNombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("primerApellido"));
        colSApellido.setCellValueFactory(new PropertyValueFactory<>("segundoApellido"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        colRutaFoto.setCellValueFactory(new PropertyValueFactory<>("rutaFoto"));

        try {
            Usuarios.setItems(getUsuarios());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        txtParametro.textProperty().addListener((observable, valorAnterior, valorNuevo) -> {
            if (valorNuevo.trim().isEmpty()) {
                try {
                    Usuarios.setItems(getUsuarios());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private ObservableList<Usuario> getUsuarios(){
        return FXCollections.observableArrayList(serviceUsuario.getAll());
    }

    @FXML
    private void registrar() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ni/edu/uam/administracionfx/registro.fxml"));
        Parent root = loader.load();

        Scene scene = Usuarios.getScene();
        scene.setRoot(root);

        Stage stage = (Stage) scene.getWindow();
        stage.sizeToScene();
        stage.centerOnScreen();
    }

    @FXML
    private void editar(){
        Usuario usuarioSeleccionado = Usuarios.getSelectionModel().getSelectedItem();

        if (usuarioSeleccionado == null) {
            UtilController.mostrarMensaje(
                    Alert.AlertType.WARNING,
                    "Advertencia",
                    "Por favor, seleccione un usuario de la tabla.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ni/edu/uam/administracionfx/editor.fxml"));
            Parent root = loader.load();

            EditorController controller = loader.getController();
            controller.prepararEditor(usuarioSeleccionado);

            Scene scene = Usuarios.getScene();
            scene.setRoot(root);

            Stage stage = (Stage) scene.getWindow();
            stage.sizeToScene();
            stage.centerOnScreen();

        } catch (Exception e) {
            UtilController.mostrarMensaje(
                    Alert.AlertType.ERROR,
                    "Validacion",
                    "Error: "+e.getMessage()
            );
        }
    }

    @FXML
    private void eliminar(){
        Usuario usuarioSeleccionado = Usuarios.getSelectionModel().getSelectedItem();

        if (usuarioSeleccionado == null) {
            UtilController.mostrarMensaje(
                    Alert.AlertType.WARNING,
                    "Advertencia",
                    "Por favor, seleccione un usuario de la tabla.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText(null);
        alert.setContentText("Eliminar Datos del usuario?");

        Optional<ButtonType> resultado = alert.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            serviceUsuario.delete(usuarioSeleccionado);
            UtilController.mostrarMensaje(Alert.AlertType.INFORMATION, "Confirmacion","Usuario Eliminado con exito");
            Usuarios.setItems(getUsuarios());
        }
        else{
            UtilController.mostrarMensaje(Alert.AlertType.INFORMATION, "Confirmacion","Eliminacion del usuario cancelada");
        }
    }

    @FXML
    private void buscar() {
        String parametro = txtParametro.getText().trim().toLowerCase();

        if (parametro.isEmpty()) {
            try {
                Usuarios.setItems(getUsuarios());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return;
        }

        ObservableList<Usuario> resultados = FXCollections.observableArrayList();

        for (Usuario usuario : Usuarios.getItems()) {
            for (TableColumn<Usuario, ?> columna : Usuarios.getColumns()) {
                Object valor = columna.getCellData(usuario);

                if (valor != null &&
                        valor.toString().toLowerCase().contains(parametro)) {
                    resultados.add(usuario);
                    break;
                }
            }
        }

        Usuarios.setItems(resultados);
    }
}