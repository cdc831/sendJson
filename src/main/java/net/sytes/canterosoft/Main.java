package net.sytes.canterosoft;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import javax.swing.*;
import java.io.Closeable;
import java.util.Date;

public class Main {

    private String url;
    private String idRh;
    private String empresa;
    private String fechaDesde;
    private String fechaHasta;
    private String calendario;
    public final String USER_AGENT = "Mozilla/5.0";
    public JsonObject ObjetoPrincipal;

    public static void main(String[] args) {
        // Creamos el metodo Main por si necesitamos ejecutar el JAR por Consola
        Main m = new Main();
        try {
            m.runSendJson(args[0],args[1],args[2],args[3],args[4],args[5],Integer.parseInt(args[6]));
            System.out.println("Ejecutado con Exito !!!");
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public void runSendJson(String url,String idRh,String empresa,String fechaDesde,String fechaHasta,String calendario, Integer setLog){
        // Este metodo lo creamos para ejecutarlo desde meta4 y le agregamos un pequeño LOG para ver si los datos que enviamos
        // son recibidos por el JAR

        if (setLog == 1){
            JOptionPane.showMessageDialog(null,url);
            JOptionPane.showMessageDialog(null,idRh);
            JOptionPane.showMessageDialog(null,empresa);
            JOptionPane.showMessageDialog(null,fechaDesde);
            JOptionPane.showMessageDialog(null,fechaHasta);
            JOptionPane.showMessageDialog(null,calendario);
        }

        if (url != null){
            setUrl(url);
        }else{
            JOptionPane.showMessageDialog(null,"Por favor ingrese la url.");
            System.exit(0);
        }

        if (idRh != null){
            setIdRh(idRh);
        }else{
            JOptionPane.showMessageDialog(null,"Por favor ingrese ID HR.");
            System.exit(0);
        }

        if (empresa != null){
            setEmpresa(empresa);
        }else{
            JOptionPane.showMessageDialog(null,"Por favor ingrese la empresa.");
            System.exit(0);
        }

        if (fechaDesde != null){
            setFechaDesde(fechaDesde);
        }else{
            JOptionPane.showMessageDialog(null,"Por favor ingrese la fecha desde.");
            System.exit(0);
        }

        if (fechaHasta != null){
            setFechaHasta(fechaHasta);
        }else{
            JOptionPane.showMessageDialog(null,"Por favor ingrese la fecha hasta.");
            System.exit(0);
        }

        if (calendario != null){
            setCalendario(calendario);
        }else{
            JOptionPane.showMessageDialog(null,"Por favor ingrese la fecha hasta.");
            System.exit(0);
        }

        // Invocamos al manejador JSON
        ManejadorJson();
    }

    public void ManejadorJson() {
        String informacion = "{pre-envio}";

        HttpClient httpClient = HttpClientBuilder.create().build();
        try {
            HttpPost request = new HttpPost(getUrl());
            StringEntity params = new StringEntity(devuelveJson(), ContentType.APPLICATION_JSON);
            request.setEntity(params);

            HttpResponse response = httpClient.execute(request);

            HttpEntity responseEntity = response.getEntity();
            informacion = "{post-envio}";
            if(responseEntity!=null) {
                informacion = EntityUtils.toString(responseEntity);
            }
            EntityUtils.consume(responseEntity);
            ((Closeable) response).close();
        } catch (Exception ex) {
            informacion = "{error-envio post sin proxy:" + ex.getMessage() + "}";
            ex.printStackTrace();
        }
    }

    public String devuelveJson() {
        ObjetoPrincipal = new JsonObject();
        ObjetoPrincipal.addProperty("idRh",getIdRh());
        ObjetoPrincipal.addProperty("empresa",getEmpresa());
        ObjetoPrincipal.addProperty("fechaDesde",getFechaDesde());
        ObjetoPrincipal.addProperty("fechaHasta",getFechaHasta());
        ObjetoPrincipal.addProperty("calendario",getCalendario());

        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        return gson.toJson(ObjetoPrincipal);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIdRh() {
        return idRh;
    }

    public void setIdRh(String idRh) {
        this.idRh = idRh;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(String fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public String getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(String fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public String getCalendario() {
        return calendario;
    }

    public void setCalendario(String calendario) {
        this.calendario = calendario;
    }
}
