package com.example.expracticointerfaces;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.*;
import net.sf.jasperreports.swing.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Objects;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    @FXML
    private Button btnGuardar;
    @FXML
    private Label lblInfo;
    @FXML
    private TextField txtNombre;
    @FXML
    private ChoiceBox<String> cbSexo;
    @FXML
    private TextField txtPeso;
    @FXML
    private TextField txtEdad;
    @FXML
    private TextField txtTalla;
    @FXML
    private ChoiceBox<String> cbActividad;
    @FXML
    private TextArea txtObservaciones;
    @FXML
    private Button btnDescargar;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbSexo.getItems().addAll("Masculino", "Femenino");
        cbActividad.getItems().addAll("Sedentario", "Moderado", "Activo", "Muy Activo");
        cbSexo.getSelectionModel().selectFirst();
        cbActividad.getSelectionModel().selectFirst();
    }

    @FXML
    public void save(ActionEvent actionEvent) {

        int errores = 0;

        if(Objects.equals(txtNombre.getText(), "") || Objects.equals(txtEdad.getText(), "") ||Objects.equals(txtTalla.getText(), "") ||Objects.equals(txtPeso.getText(), "")){
            lblInfo.setText("Error!! Introduce un valor");
            errores++;
        }

        int edad = -1;
        if (txtEdad.getText().matches("\\d+")) {
            edad = Integer.parseInt(txtEdad.getText());
        } else {
            lblInfo.setText("Error!! Formato de edad incorrecto");
            errores++;
        }

        if(!txtPeso.getText().matches("\\d+")){
            lblInfo.setText("Error!! Formato de peso incorrecto");
            errores++;
        }

        if(!txtTalla.getText().matches("\\d+")){
            lblInfo.setText("Error!! Formato de talla incorrecto");
            errores++;
        }

        if (!txtNombre.getText().matches("[a-zA-Z]+")) {
            lblInfo.setText("Error!! El nombre solo puede contener letras");
            errores++;
        }

        double actividad = getActividad();
        double formulaHB = 0;

        if(errores==0){
            if (edad>=0){
                if (Objects.equals(cbSexo.getValue(), "Masculino")){
                    formulaHB = 66.473+13.751*(Double.parseDouble(txtPeso.getText()))+5.0033*(Double.parseDouble(txtTalla.getText()))-6.755*(Double.parseDouble(txtEdad.getText()));
                }else{
                    formulaHB = 655.0955+9.463*(Double.parseDouble(txtPeso.getText()))+1.8496*(Double.parseDouble(txtTalla.getText()))-4.6756*(Double.parseDouble(txtEdad.getText()));
                }
            }

            double GET = formulaHB * actividad;

            formulaHB = Math.round(formulaHB * 100.0) / 100.0;
            GET = Math.round(GET * 100.0) / 100.0;

            lblInfo.setText("El cliente "+txtNombre.getText()+" tiene un GER de "+formulaHB+" y un GET de "+GET);
        }



    }

    private double getActividad() {
        double actividad = 0;

        switch (cbActividad.getValue()) {
            case "Sedentario" -> actividad = 1.3;
            case "Moderado" -> {
                if (Objects.equals(cbSexo.getValue(), "Masculino")) actividad = 1.6;
                else actividad = 1.5;
            }
            case "Activo" -> {
                if (Objects.equals(cbSexo.getValue(), "Masculino")) actividad = 1.7;
                else actividad = 1.6;
            }
            case "Muy Activo" -> {
                if (Objects.equals(cbSexo.getValue(), "Masculino")) actividad = 2.1;
                else actividad = 1.9;
            }
        }
        return actividad;
    }


    @FXML
    public void download(ActionEvent actionEvent) {

        Connection conexion = MySQLConnection.getConexion();
        try {
            JasperPrint jasperPrint = JasperFillManager.fillReport("clientes.jasper", new HashMap<>(), conexion);

            JasperExportManager.exportReportToPdfFile(jasperPrint, "clientes.pdf");

            System.out.println("Informe generado y descargado como 'clientes.pdf'");
        } catch (JRException e) {
            throw new RuntimeException(e);
        }


    }
}