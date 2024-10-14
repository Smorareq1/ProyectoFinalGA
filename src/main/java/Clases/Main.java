package Clases;

import Objetos.GestorDeArchivos;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        //Cargar datos
        GestorDeArchivos.cargarDatosDesdeJson();



        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/fxml/ventanaPrincipal.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("ProyectoFinal Gestion-De-Archivos!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}