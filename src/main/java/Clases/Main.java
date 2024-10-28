package Clases;

import Objetos.GestorDeArchivos;
import Objetos.idMarca;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        //Cargar datos
        GestorDeArchivos.cargarDatosDesdeJson();
        GestorDeArchivos.cargarLineasDesdeJson();
        GestorDeArchivos.CargarTiposDesdeJson();
        GestorDeArchivos.cargarVehiculosDesdeJson();


        //Una vez cargados los vehiculos, cargar sets
        GestorDeArchivos.cargarSets();

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/fxml/ventanaPrincipal.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("ProyectoFinal Gestion-De-Archivos!");
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args)
    {
        launch();
    }
}