package Controllers;

import Objetos.*;
import com.itextpdf.layout.properties.HorizontalAlignment;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.image.ImageView;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.AlignmentPropertyValue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import java.util.Map;

public class InicioController {

 private static final String MARCAS_JSON = "src/main/resources/datos/marcas.json";
    private static final String TIPOS_JSON = "src/main/resources/datos/tipos.json";
    private static final String LINEAS_JSON = "src/main/resources/datos/lineas.json";
    private static final String VEHICULOS_JSON = "src/main/resources/datos/vehiculos.json";
    private static final String pdfPath = "src/main/resources/datos/Reporte/reporte.pdf";

    @FXML
    private Text marcasRegistradasText;

    @FXML
    private Text tiposRegistradosText;

    @FXML
    private Text lineasRegistradasText;

    @FXML
    private Text vehiculosRegistradosText;

    @FXML
    private ImageView saveIcon;

    @FXML
    public void initialize() {
        // Obtén el tamaño de los diccionarios
        int marcasSize = GestorDeArchivos.diccionarioNombreMarcas.size();
        int tiposSize = GestorDeArchivos.diccionarioNombreTipos.size();
        int lineasSize = GestorDeArchivos.diccionarioNombreLineas.size();
        int vehiculosSize = GestorDeArchivos.diccionarioNombreVehiculos.size();

        // Actualiza el valor del Text en la interfaz
        marcasRegistradasText.setText(String.valueOf(marcasSize));
        tiposRegistradosText.setText(String.valueOf(tiposSize));
        lineasRegistradasText.setText(String.valueOf(lineasSize));
        vehiculosRegistradosText.setText(String.valueOf(vehiculosSize));

        // Acción al hacer clic en el icono de guardar
        saveIcon.setOnMouseClicked(e -> generarReporte());
    }

   public void generarReporte() {
       try {
           PdfWriter writer = new PdfWriter(pdfPath);
           PdfDocument pdfDoc = new PdfDocument(writer);
           Document document = new Document(pdfDoc);

           document.add(new Paragraph("Reporte Completo de Vehículos"));

           // Cargar y añadir Marcas
           document.add(new Paragraph("Marcas Registradas:"));
           Map<String, Marca> marcas = GestorDeArchivos.cargarMarcas(MARCAS_JSON);
           Table marcasTable = new Table(new float[]{2, 2, 2});
           marcasTable.addHeaderCell(new Paragraph("Nombre").setBold());
           marcasTable.addHeaderCell(new Paragraph("Año de Creación").setBold());
           marcasTable.addHeaderCell(new Paragraph("Fundador").setBold());

           for (Marca marca : marcas.values()) {
               marcasTable.addCell(marca.getNombre());
               marcasTable.addCell(String.valueOf(marca.getAnioDeCreacion()));
               marcasTable.addCell(marca.getFundador());
           }

           // Centrar la tabla en el documento
           marcasTable.setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER);
           marcasTable.setHorizontalAlignment(HorizontalAlignment.CENTER);
           document.add(marcasTable);

           // Cargar y añadir Tipos
           document.add(new Paragraph("Tipos Registrados:"));
           Map<String, Tipo> tipos = GestorDeArchivos.cargarTipos(TIPOS_JSON);
           Table tiposTable = new Table(new float[]{2, 2});
           tiposTable.addHeaderCell(new Paragraph("Tipo").setBold());
           tiposTable.addHeaderCell(new Paragraph("Año de Creación").setBold());

           for (Tipo tipo : tipos.values()) {
               tiposTable.addCell(tipo.getNombreTipo());
               tiposTable.addCell(String.valueOf(tipo.getAnioTipo()));
           }

           // Centrar la tabla en el documento
           tiposTable.setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER);
              tiposTable.setHorizontalAlignment(HorizontalAlignment.CENTER);
           document.add(tiposTable);

           // Cargar y añadir Líneas
           document.add(new Paragraph("Líneas Registradas:"));
           Map<String, Linea> lineas = GestorDeArchivos.cargarLineas(LINEAS_JSON);
           Table lineasTable = new Table(new float[]{2, 2, 2});
           lineasTable.addHeaderCell(new Paragraph("Marca").setBold());
           lineasTable.addHeaderCell(new Paragraph("Línea").setBold());
           lineasTable.addHeaderCell(new Paragraph("Año de Creación").setBold());

           for (Linea linea : lineas.values()) {
               lineasTable.addCell(linea.getNombreMarcaDeLinea());
               lineasTable.addCell(linea.getNombreLinea());
               lineasTable.addCell(String.valueOf(linea.getAnioLinea()));
           }

           // Centrar la tabla en el documento
           lineasTable.setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER);
           lineasTable.setHorizontalAlignment(HorizontalAlignment.CENTER);
           document.add(lineasTable);

           // Cargar y añadir Vehículos
           document.add(new Paragraph("Vehículos Registrados:"));
           Map<String, Vehiculo> vehiculos = GestorDeArchivos.cargarVehiculos(VEHICULOS_JSON);
           Table vehiculosTable = new Table(new float[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
           vehiculosTable.addHeaderCell(new Paragraph("Marca").setBold());
           vehiculosTable.addHeaderCell(new Paragraph("Tipo").setBold());
           vehiculosTable.addHeaderCell(new Paragraph("Línea").setBold());
           vehiculosTable.addHeaderCell(new Paragraph("Modelo").setBold());
           vehiculosTable.addHeaderCell(new Paragraph("Color").setBold());
           vehiculosTable.addHeaderCell(new Paragraph("Asientos").setBold());
           vehiculosTable.addHeaderCell(new Paragraph("Placa").setBold());
           vehiculosTable.addHeaderCell(new Paragraph("Chasis").setBold());
           vehiculosTable.addHeaderCell(new Paragraph("Motor").setBold());
           vehiculosTable.addHeaderCell(new Paragraph("Vin").setBold());

           for (Vehiculo vehiculo : vehiculos.values()) {
               vehiculosTable.addCell(vehiculo.getMarcaNombreVehiculo());
               vehiculosTable.addCell(vehiculo.getTipoNombreVehiculo());
               vehiculosTable.addCell(vehiculo.getLineaNombreVehiculo());
               vehiculosTable.addCell(vehiculo.getModelo());
               vehiculosTable.addCell(vehiculo.getColor());
               vehiculosTable.addCell(String.valueOf(vehiculo.getNumeroDeAsientos()));
               vehiculosTable.addCell(vehiculo.getPlaca());
               vehiculosTable.addCell(vehiculo.getChasis());
               vehiculosTable.addCell(vehiculo.getMotor());
               vehiculosTable.addCell(vehiculo.getVin());
           }

           // Centrar la tabla en el documento
           vehiculosTable.setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER);
           vehiculosTable.setHorizontalAlignment(HorizontalAlignment.CENTER);
           document.add(vehiculosTable);

           document.close();
           System.out.println("Reporte generado en " + pdfPath);

       } catch (FileNotFoundException e) {
           System.err.println("No se pudo encontrar el archivo PDF de destino.");
           e.printStackTrace();
       } catch (IOException e) {
           System.err.println("Error al leer los archivos JSON.");
           e.printStackTrace();
       }
   }


}
