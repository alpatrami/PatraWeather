package ro.mta.facc.selab.tema2.controller;

import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import ro.mta.facc.selab.tema2.model.PatraWeatherModel;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

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
    private CheckBox unitId;


    @FXML
    private void initialize(){
        try {
            readFile();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        loadData();
    }

    @FXML


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

        for (String s : OTC)
        {
            countryBox.getItems().addAll(s);
        }

    }

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


        String selected = countryBox.getValue();
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

    public void parsJson(Event actionEvent) throws IOException{

        String city=cityBox.getValue();
        String country=countryBox.getValue();
        URL url;
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

        String _info = items.get(0).asObject().getString("description", "Unknown Item");
        String _capInfo=_info.substring(0,1).toUpperCase(Locale.ROOT)+_info.substring(1);
        iconCode=items.get(0).asObject().getString("icon", "Unknown Item");
        info.setText("");
        info.setText(_capInfo);

        String url1="http://openweathermap.org/img/wn/"+iconCode+"@2x.png";
        Image img = new Image(url1, true);
        imageId.setImage(img);
        imageId.setFitHeight(100);
        imageId.setFitWidth(100);

        symbol.setText("");
        if(!unitId.isSelected()) {
            symbol.setText("°C");
        }
        else {
            symbol.setText("°F");
        }

        JsonObject tempObj=Json.parse(down).asObject().get("main").asObject();
        double _temp = tempObj.getDouble("temp", 0);
        temp.setText("");
        temp.setText(String.valueOf(_temp));

        double _humidity = tempObj.getDouble("humidity", 0);
        StringBuilder appendWind= new StringBuilder();
        appendWind.append("Umiditate: ");
        appendWind.append(_humidity);
        appendWind.append(" %");
        humidity.setText("");
        humidity.setText(appendWind.toString());

        JsonObject tempObj1=Json.parse(down).asObject().get("wind").asObject();
        double _wind=tempObj1.getDouble("speed",0);
        StringBuilder appendWind2= new StringBuilder();
        appendWind2.append("Vant: ");
        appendWind2.append(_wind);
        if(!unitId.isSelected()) {
            appendWind2.append(" metri/sec");
        }
        else {
            appendWind2.append(" mile/ora");
        }
        wind.setText("");
        wind.setText(appendWind2.toString());

        String _cityName = Json.parse(down).asObject().getString("name", "Unknown Item");
        cityName.setText("");
        cityName.setText(_cityName);

        JsonObject tempObj3=Json.parse(down).asObject().get("main").asObject();
        double _pressure=tempObj3.getDouble("pressure",0);
        StringBuilder appendWind3= new StringBuilder();
        appendWind3.append("Presiune: ");
        appendWind3.append(_pressure);
        appendWind3.append(" hPa");
        pressure.setText("");
        pressure.setText(appendWind3.toString());

        String pattern= "EEEEE MMMMM YYYY";
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat(pattern,new Locale("ro",countryBox.getValue()));
        String date=simpleDateFormat.format(new Date());
        String _capDate=date.substring(0,1).toUpperCase(Locale.ROOT)+date.substring(1);
        String[] firstCuv=_capDate.split(" ");
        int nr=firstCuv[0].length();
        _capDate=_capDate.substring(0,nr+2).toUpperCase(Locale.ROOT)+date.substring(nr+2);
        dateId.setText("");
        dateId.setText(_capDate);


    }

    public void clearCity(MouseEvent mouseEvent) {
        cityBox.getItems().clear();
    }
}
