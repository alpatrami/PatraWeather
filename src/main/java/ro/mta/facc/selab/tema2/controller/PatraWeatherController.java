package ro.mta.facc.selab.tema2.controller;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import ro.mta.facc.selab.tema2.model.PatraWeatherModel;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 *      Clasa Controller din Arhitectura MVC
 *
 * @author Alin Pătrașcu
 */

public class PatraWeatherController {

    private ObservableList<PatraWeatherModel> weatherData=FXCollections.observableArrayList();



    @FXML
    private ChoiceBox<String> countryBox;
    @FXML
    private ChoiceBox<String> cityBox;
    @FXML
    private Label cityName;
    @FXML
    private Label dateId;
    @FXML
    private Label info;
    @FXML
    private Label temp;
    @FXML
    private Label pressure;
    @FXML
    private Label humidity;
    @FXML
    private Label symbol;
    @FXML
    private Label wind;
    @FXML
    private ImageView imageId;
    @FXML
    private CheckBox unitId=new CheckBox();

    public PatraWeatherController() throws IOException {
    }

    /**
     *      La initializare: - voi citi fisierul si crea ObservableList cu elemente de tip PatraWeatherModel prin functia readFile()
     *                       - incarca in ChoiceBox tarile din fisierul de intrare prin functia loadData()
     */

    @FXML
    private void initialize() {
        try {
            readFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadData();


    }


    private void readFile() throws Exception {
        ArrayList<String> list=new ArrayList<String>();
        File inputFile=new File("src/main/resources/input.txt");
        Scanner myReader = new Scanner(inputFile);
        while(myReader.hasNextLine())
        {
            String data;
            data = myReader.nextLine();
            list.add(data);
        }

        myReader.close();
        for (String s : list) {
            String[] cuv = s.split("\t");
            if (cuv.length != 5)
                throw new Exception("Fisierul nu are formatul corespunzator");

            PatraWeatherModel aux=new PatraWeatherModel(cuv[4],cuv[1],cuv[2],cuv[3]);
            weatherData.add(aux);
        }
    }

    private void loadData()
    {
        int sw=0;
        ArrayList<String> OTC = new ArrayList<String>();
        for(int i=0;i<weatherData.size();i++)
        {
            if(i==0) {
                OTC.add(weatherData.get(i).getCountryCode());
                sw=1;
            }
            else
                for (String s : OTC)
                    if (s.equals(weatherData.get(i).getCountryCode())) {
                        sw = 1;
                        break;
                    }
            if(sw==0)
                OTC.add(weatherData.get(i).getCountryCode());
            else
                sw=0;
        }


        /**
         *      Transform Codul ISO in numele complet al tarii
         */
        ArrayList<String> realName=new ArrayList<String>();
        for(int i=0; i< OTC.size(); i++)
        {
            Locale l=new Locale("ro",OTC.get(i));
            realName.add(l.getDisplayCountry());
        }

        for (String s : realName)
        {
            countryBox.getItems().addAll(s);
        }

        /**
         *      Am adaugat un listener pentru a se putea schimba automat unitatea de masura la bifarea si debifarea CheckBoxului
         */
        unitId.selectedProperty().addListener(
                (ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {
                    if(cityBox.getValue()!=null && countryBox.getValue()!=null) {
                        String city = cityBox.getValue();
                        String country = countryBox.getValue();
                        URL url = null;

                        /**
                         *      Pentru a putea afisa atat in format metric cat si in format imperial am implementat urmatorul if
                         */

                        if (!unitId.isSelected()) {
                            try {
                                url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + city + "," + country + ",&appid=594d4c00d8a8bdeb6b836dc8ad9d6c4c&lang=ro&units=metric");
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + city + "," + country + ",&appid=594d4c00d8a8bdeb6b836dc8ad9d6c4c&lang=ro&units=imperial");
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }
                        }

                        URLConnection conn = null;
                        try {
                            conn = url.openConnection();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        BufferedReader reader = null;
                        try {
                            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String down = null;
                        try {
                            down = org.apache.commons.io.IOUtils.toString(reader);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String iconCode = "";

                        JsonArray items = Json.parse(down).asObject().get("weather").asArray();

                        /**
                         *      Obtin descrierea
                         */
                        String _info = items.get(0).asObject().getString("description", "Unknown Item");
                        String _capInfo = _info.substring(0, 1).toUpperCase(Locale.ROOT) + _info.substring(1);
                        iconCode = items.get(0).asObject().getString("icon", "Unknown Item");
                        info.setText("");
                        info.setText(_capInfo);

                        /**
                         *      Formez linkul in care se poate gasi icon ul specific vremii si il incarc
                         */
                        String url1 = "http://openweathermap.org/img/wn/" + iconCode + "@2x.png";
                        Image img = new Image(url1, true);
                        imageId.setImage(img);
                        imageId.setFitHeight(100);
                        imageId.setFitWidth(100);

                        /**
                         *      Afisez unitatea
                         */
                        symbol.setText("");
                        if (!unitId.isSelected()) {
                            symbol.setText("°C");
                        } else {
                            symbol.setText("°F");
                        }

                        /**
                         *      Obtin temperatura
                         */
                        JsonObject tempObj = Json.parse(down).asObject().get("main").asObject();
                        double _temp = tempObj.getDouble("temp", 0);
                        temp.setText("");
                        temp.setText(String.valueOf(_temp));

                        /**
                         *      Obtin umiditatea
                         */
                        double _humidity = tempObj.getDouble("humidity", 0);
                        StringBuilder appendHumidity = new StringBuilder();
                        appendHumidity.append("Umiditate: ");
                        appendHumidity.append(_humidity);
                        appendHumidity.append(" %");
                        humidity.setText("");
                        humidity.setText(appendHumidity.toString());

                        /**
                         *      Obtin detaliile despre vant
                         */
                        JsonObject tempObj1 = Json.parse(down).asObject().get("wind").asObject();
                        double _wind = tempObj1.getDouble("speed", 0);
                        StringBuilder appendWind = new StringBuilder();
                        appendWind.append("Vant: ");
                        appendWind.append(_wind);
                        if (!unitId.isSelected()) {
                            appendWind.append(" metri/sec");
                        } else {
                            appendWind.append(" mile/ora");
                        }
                        wind.setText("");
                        wind.setText(appendWind.toString());

                        /**
                         *      Obtin numele orasului
                         */
                        String _cityName = Json.parse(down).asObject().getString("name", "Unknown Item");
                        cityName.setText("");
                        cityName.setText(_cityName);

                        /**
                         *      Obtin presiunea
                         */
                        JsonObject tempObj3 = Json.parse(down).asObject().get("main").asObject();
                        double _pressure = tempObj3.getDouble("pressure", 0);
                        StringBuilder appendPressure = new StringBuilder();
                        appendPressure.append("Presiune: ");
                        appendPressure.append(_pressure);
                        appendPressure.append(" hPa");
                        pressure.setText("");
                        pressure.setText(appendPressure.toString());

                        /**
                         *      Am trnasformat din valoarea primita in dt in formatul afisat in interfata grafica
                         */
                        double dt = Json.parse(down).asObject().getDouble("dt", 0);
                        LocalDateTime ldt = Instant.ofEpochSecond((long) dt).atZone(ZoneId.systemDefault()).toLocalDateTime();
                        String pattern = "EEEE MMMM YYYY HH:mm:ss";
                        Map<String, String> countries = new HashMap<>();
                        for (String iso : Locale.getISOCountries()) {
                            Locale l = new Locale("ro", iso);
                            countries.put(l.getDisplayCountry(), iso);
                        }
                        String selected = countries.get(countryBox.getValue());
                        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern, new Locale("ro", selected));

                        String date = ldt.format(dateTimeFormatter);
                        String _capDate = date.substring(0, 1).toUpperCase(Locale.ROOT) + date.substring(1);
                        String[] firstCuv = _capDate.split(" ");
                        int nr = firstCuv[0].length();
                        _capDate = _capDate.substring(0, nr + 2).toUpperCase(Locale.ROOT) + date.substring(nr + 2);
                        dateId.setText("");
                        dateId.setText(_capDate);

                        try {
                            FileWriter fileWriter= new FileWriter("src/main/resources/Logs.txt",true);
                            fileWriter.write( "<Oras: "+_cityName+ "> <Tara: " + countryBox.getValue() + "> <Data/Ora request: " + _capDate +"> <Informatii: " + _capInfo +"> <Temperatura: " +_temp + symbol.getText()+"> <IconCode: "+ iconCode +"> <"+appendPressure.toString()+"> <"+appendHumidity.toString()+"> <" + appendWind.toString()+">\n\n");
                            fileWriter.close();
                        } catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     *      La inchiderea choicebox-ului se ia din ObservableList orasele care apartin tarii alese
     */
    public void setOnHidden(Event event) {
        cityBox.getItems().clear();
        cityName.setText("");
        info.setText("");
        temp.setText("");
        pressure.setText("");
        dateId.setText("");
        symbol.setText("");
        humidity.setText("");
        wind.setText("");
        imageId.setImage(null);


        /**
         *      In prima faza trebuie ca numele tarii sa fie transformat inapoi in Codul ISO
         */
        Map<String, String> countries = new HashMap<>();
        for (String iso : Locale.getISOCountries()) {
            Locale l = new Locale("ro", iso);
            countries.put(l.getDisplayCountry(), iso);
        }
        String selected = countries.get(countryBox.getValue());

        /**
         *      De aici incepe cautarea si adaugarea in ChoiceBox
         */
        ArrayList<String> cities = new ArrayList<String>();
        int sw = 0;
        if (selected != null) {
            for (int i = 0; i < weatherData.size(); i++) {
                if (selected.equals(weatherData.get(i).getCountryCode())) {
                    for (String s : cities)
                        if (s.equals(weatherData.get(i).getCountryCode())) {
                            sw = 1;
                            break;
                        }
                    if (sw == 0)
                        cities.add(weatherData.get(i).getCityName());
                    else
                        sw = 0;
                }
            }

            cityBox.getItems().addAll(cities);

        }
    }


    /**
     *  Aici se efectueaza parsarea Jsonului
     *
     */
    public void parsJson(Event actionEvent) throws IOException{

        String city=cityBox.getValue();
        String country=countryBox.getValue();
        URL url;

        /**
         *      Pentru a putea afisa atat in format metric cat si in format imperial am implementat urmatorul if
         */
        if(!unitId.isSelected()) {
            url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + city + "," + country + ",&appid=594d4c00d8a8bdeb6b836dc8ad9d6c4c&lang=ro&units=metric");
        }
        else {
            url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + city + "," + country + ",&appid=594d4c00d8a8bdeb6b836dc8ad9d6c4c&lang=ro&units=imperial");
        }

        URLConnection conn = url.openConnection();
        BufferedReader reader = new BufferedReader( new InputStreamReader( conn.getInputStream() ) );
        String down = org.apache.commons.io.IOUtils.toString(reader);
        String iconCode= "";

        JsonArray items = Json.parse(down).asObject().get("weather").asArray();

        /**
         *      Obtin descrierea
         */
        String _info = items.get(0).asObject().getString("description", "Unknown Item");
        String _capInfo=_info.substring(0,1).toUpperCase(Locale.ROOT)+_info.substring(1);
        iconCode=items.get(0).asObject().getString("icon", "Unknown Item");
        info.setText("");
        info.setText(_capInfo);

        /**
         *      Formez linkul in care se poate gasi icon ul specific vremii si il incarc
         */
        String url1="http://openweathermap.org/img/wn/"+iconCode+"@2x.png";
        Image img = new Image(url1, true);
        imageId.setImage(img);
        imageId.setFitHeight(100);
        imageId.setFitWidth(100);

        /**
         *      Afisez unitatea
         */
        symbol.setText("");
        if(!unitId.isSelected()) {
            symbol.setText("°C");
        }
        else {
            symbol.setText("°F");
        }

        /**
         *      Obtin temperatura
         */
        JsonObject tempObj=Json.parse(down).asObject().get("main").asObject();
        double _temp = tempObj.getDouble("temp", 0);
        temp.setText("");
        temp.setText(String.valueOf(_temp));

        /**
         *      Obtin umiditatea
         */
        double _humidity = tempObj.getDouble("humidity", 0);
        StringBuilder appendHumidity= new StringBuilder();
        appendHumidity.append("Umiditate: ");
        appendHumidity.append(_humidity);
        appendHumidity.append(" %");
        humidity.setText("");
        humidity.setText(appendHumidity.toString());

        /**
         *      Obtin detaliile despre vant
         */
        JsonObject tempObj1=Json.parse(down).asObject().get("wind").asObject();
        double _wind=tempObj1.getDouble("speed",0);
        StringBuilder appendWind= new StringBuilder();
        appendWind.append("Vant: ");
        appendWind.append(_wind);
        if(!unitId.isSelected()) {
            appendWind.append(" metri/sec");
        }
        else {
            appendWind.append(" mile/ora");
        }
        wind.setText("");
        wind.setText(appendWind.toString());

        /**
         *      Obtin numele orasului
         */
        String _cityName = Json.parse(down).asObject().getString("name", "Unknown Item");
        cityName.setText("");
        cityName.setText(_cityName);

        /**
         *      Obtin presiunea
         */
        JsonObject tempObj3=Json.parse(down).asObject().get("main").asObject();
        double _pressure=tempObj3.getDouble("pressure",0);
        StringBuilder appendPressure = new StringBuilder();
        appendPressure.append("Presiune: ");
        appendPressure.append(_pressure);
        appendPressure.append(" hPa");
        pressure.setText("");
        pressure.setText(appendPressure.toString());

        /**
         *      Am trnasformat din valoarea primita in dt in formatul afisat in interfata grafica
         */
        double dt=Json.parse(down).asObject().getDouble("dt", 0);
        LocalDateTime ldt= Instant.ofEpochSecond((long)dt).atZone(ZoneId.systemDefault()).toLocalDateTime();
        String pattern= "EEEE MMMM YYYY HH:mm:ss";
        Map<String, String> countries = new HashMap<>();
        for (String iso : Locale.getISOCountries()) {
            Locale l = new Locale("ro", iso);
            countries.put(l.getDisplayCountry(), iso);
        }
        String selected = countries.get(countryBox.getValue());
        DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern(pattern,new Locale("ro",selected));

        String date=ldt.format(dateTimeFormatter);
        String _capDate=date.substring(0,1).toUpperCase(Locale.ROOT)+date.substring(1);
        String[] firstCuv=_capDate.split(" ");
        int nr=firstCuv[0].length();
        _capDate=_capDate.substring(0,nr+2).toUpperCase(Locale.ROOT)+date.substring(nr+2);
        dateId.setText("");
        dateId.setText(_capDate);

        try {
            FileWriter fileWriter= new FileWriter("src/main/resources/Logs.txt",true);
            fileWriter.write( "<Oras: "+_cityName+ "> <Tara: " + countryBox.getValue() + "> <Data/Ora request: " + _capDate +"> <Informatii: " + _capInfo +"> <Temperatura: " +_temp + symbol.getText()+"> <IconCode: "+ iconCode +"> <"+appendPressure.toString()+"> <"+appendHumidity.toString()+"> <" + appendWind.toString()+">\n\n");
            fileWriter.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }





}
