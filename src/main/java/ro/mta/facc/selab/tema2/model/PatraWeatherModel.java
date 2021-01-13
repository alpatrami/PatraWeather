package ro.mta.facc.selab.tema2.model;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *      Clasa Model din Arhitectura MVC + settters + getters + constructor
 *
 *@author Alin Pătrașcu
 */


public class PatraWeatherModel {
    StringProperty lat;
    StringProperty lon;
    StringProperty cityName;
    StringProperty countryCode;

    public PatraWeatherModel(String countryCode, String cityName, String lat, String lon)
    {
        this.countryCode= new SimpleStringProperty(countryCode);
        this.cityName= new SimpleStringProperty(cityName);
        this.lat= new SimpleStringProperty(lat);
        this.lon= new SimpleStringProperty(lon);
    }


    public String getLat() { return lat.get(); }
    public StringProperty latProperty() { return lat; }


    public String getLon() { return lon.get(); }
    public StringProperty lonProperty() { return lon; }


    public String getCityName() {
        return cityName.get();
    }
    public StringProperty getCityNameProperty() {
        return cityName;
    }



    public String getCountryCode() { return countryCode.get(); }
    public StringProperty getCountryCodeProperty() {
        return countryCode;
    }


}
