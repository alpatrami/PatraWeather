package ro.mta.facc.selab.tema2.model;

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *      Efectuarea testului mockito
 *
 *@author Alin Pătrașcu
 */

public class PatraWeatherModelTestMockito {

    String lat;
    String lon;
    String cityName;
    String countryCode;

    private PatraWeatherModel testModelMockito;

    @Before
    public void setUp() throws Exception {
        BufferedReader inputStreamReader=mock(BufferedReader.class);
        when(inputStreamReader.readLine()).thenReturn("RU").thenReturn("Razvilka").thenReturn("55.5917").thenReturn("37.7408");

        testModelMockito=new PatraWeatherModel(inputStreamReader.readLine(),inputStreamReader.readLine(),inputStreamReader.readLine(),inputStreamReader.readLine());

        /**
         *      Citirea din fisier
         */
        ArrayList<String> list= new ArrayList<>();
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

        countryCode=cuv[4];
        cityName=cuv[1];
        lat=cuv[2];
        lon=cuv[3];
    }

    /**
     *      Testarea efectiva
     */
    @Test
    public void getLat() {
        assertEquals(testModelMockito.getLat(),this.lat);
    }

    @Test
    public void latProperty() {
        assertEquals(testModelMockito.latProperty().getValue(),this.lat);
    }

    @Test
    public void getLon() {
        assertEquals(testModelMockito.getLon(),this.lon);
    }

    @Test
    public void lonProperty() {
        assertEquals(testModelMockito.lonProperty().getValue(),this.lon);
    }

    @Test
    public void getCityName() {
        assertEquals(testModelMockito.getLon(),this.lon);
    }

    @Test
    public void getCityNameProperty() {
        assertEquals(testModelMockito.getCityNameProperty().getValue(),this.cityName);
    }

    @Test
    public void getCountryCode() {
        assertEquals(testModelMockito.getCountryCode(),this.countryCode);
    }

    @Test
    public void getCountryCodeProperty() { assertEquals(testModelMockito.getCountryCodeProperty().getValue(),this.countryCode); }
}