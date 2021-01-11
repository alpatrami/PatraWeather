package ro.mta.facc.selab.tema2.model;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PatraWeatherModel {
    StringProperty lat;
    StringProperty lon;
    StringProperty cityName;
    StringProperty date;
    StringProperty info;

    StringProperty precipitation;
    StringProperty humidity;
    StringProperty wind;

    StringProperty countryCode;

    public PatraWeatherModel(String countryCode, String cityName, String lat, String lon)
    {
        this.countryCode= new SimpleStringProperty(countryCode);
        this.cityName= new SimpleStringProperty(cityName);
       // this.date= new SimpleStringProperty(date);
       // this.info= new SimpleStringProperty(info);

        this.lat= new SimpleStringProperty(lat);
        this.lon= new SimpleStringProperty(lon);

        //this.precipitation= new SimpleStringProperty(precipitation);
       // this.humidity= new SimpleStringProperty(humidity);
       // this.wind= new SimpleStringProperty(wind);
    }


    public String getLat() { return lat.get(); }
    public StringProperty latProperty() { return lat; }
    public void setLat(String lat) { this.lat.set(lat); }

    public String getLon() { return lon.get(); }
    public StringProperty lonProperty() { return lon; }
    public void setLon(String lon) { this.lon.set(lon); }

    public String getCityName() {
        return cityName.get();
    }
    public StringProperty getCityNameProperty() {
        return cityName;
    }
    public void setCityName(String cityName)
    {
        this.cityName.set(cityName);
    }


    public String getCountryCode() { return countryCode.get(); }
    public StringProperty getCountryCodeProperty() {
        return cityName;
    }
    public void setCountryCode(String countryCode)
    {
        this.countryCode.set(countryCode);
    }


    public String getDate() {
        return date.get();
    }
    public StringProperty getDateProperty() {
        return date;
    }
    public void setDate(String date)
    {
        this.cityName.set(date);
    }


    public String getInfo() {
        return info.get();
    }
    public StringProperty getInfoProperty() {
        return info;
    }
    public void setInfo(String info)
    {
        this.cityName.set(info);
    }


    public String getPrecipitation() {
        return precipitation.get();
    }
    public StringProperty getPrecipitationProperty() {
        return precipitation;
    }
    public void setPrecipitation(String precipitation)
    {
        this.cityName.set(precipitation);
    }


    public String getHumidity() {
        return humidity.get();
    }
    public StringProperty getHumidityProperty() {
        return humidity;
    }
    public void setHumidity(String humidity)
    {
        this.humidity.set(humidity);
    }


    public String getWind() {
        return wind.get();
    }
    public StringProperty getWindProperty() {
        return wind;
    }
    public void setWind(String wind)
    {
        this.wind.set(wind);
    }
}
