package ro.mta.facc.selab.tema2.model;


import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.Assert.*;

public class PatraWeatherModelTest {

    String lat;
    String lon;
    String cityName;
    String countryCode;

    private PatraWeatherModel testModel;

    @Before
    public void setUp() throws Exception {
        countryCode="RU";
        cityName="Razvilka";
        lat="55.5917";
        lon="37.7408";


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

        String[] cuv;
        cuv = list.get(0).split("\t");

        testModel=new PatraWeatherModel(cuv[4],cuv[1],cuv[2],cuv[3]);


    }

    @Test
    public void getLat() {
        assertEquals(testModel.getLat(),this.lat);
    }

    @Test
    public void latProperty() {
        assertEquals(testModel.latProperty().getValue(),this.lat);
    }

    @Test
    public void getLon() {
        assertEquals(testModel.getLon(),this.lon);
    }

    @Test
    public void lonProperty() {
        assertEquals(testModel.lonProperty().getValue(),this.lon);
    }

    @Test
    public void getCityName() {
        assertEquals(testModel.getLon(),this.lon);
    }

    @Test
    public void getCityNameProperty() {
        assertEquals(testModel.getCityNameProperty().getValue(),this.cityName);
    }

    @Test
    public void getCountryCode() {
        assertEquals(testModel.getCountryCode(),this.countryCode);
    }

    @Test
    public void getCountryCodeProperty() {
        assertEquals(testModel.getCountryCodeProperty().getValue(),this.countryCode);
    }

}