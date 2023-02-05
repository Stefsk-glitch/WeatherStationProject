import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Hashtable;

public class Measurement {
    private RawMeasurement rawMeasurement;
    private ArrayList<Short> listWithAllShortValues = new ArrayList<>();
    private ArrayList<String> listWithAllStringValues = new ArrayList<>();
    private ArrayList<Double> listWithAllDoubleValues = new ArrayList<>();
    private ArrayList<LocalDateTime> listWithAllLocalDateTimeValues = new ArrayList<>();


    public Measurement(RawMeasurement rawMeasurement) {
        this.rawMeasurement = rawMeasurement;

        listWithAllStringValues.add(rawMeasurement.getStationId());
        listWithAllStringValues.add(sunRise());
        listWithAllStringValues.add(sunSet());

        listWithAllLocalDateTimeValues.add(rawMeasurement.getDateStamp());


        listWithAllDoubleValues.add(airPressure());
        listWithAllDoubleValues.add(insideTemperature());
        listWithAllDoubleValues.add(insideHumidity());
        listWithAllDoubleValues.add(outsideTemperature());
        listWithAllDoubleValues.add(windSpeed());
        listWithAllDoubleValues.add(avgWindSpeed());
        listWithAllDoubleValues.add(windDirection());
        listWithAllDoubleValues.add(outsideHumidity());
        listWithAllDoubleValues.add(rainMeter());
        listWithAllDoubleValues.add(uvIndex());
        listWithAllDoubleValues.add(batteryLevel());

        listWithAllShortValues.add(rawMeasurement.getSolarRad());
        listWithAllShortValues.add(rawMeasurement.getXmitBatt());
        listWithAllShortValues.add(rawMeasurement.getForeIcon());
    }

    public ArrayList<Short> getListWithAllShortValues() {
        return listWithAllShortValues;
    }

    public ArrayList<String> getListWithAllStringValues() {
        return listWithAllStringValues;
    }

    public ArrayList<Double> getListWithAllDoubleValues() {
        return listWithAllDoubleValues;
    }

    public ArrayList<LocalDateTime> getListWithAllLocalDateTimeValues() {
        return listWithAllLocalDateTimeValues;
    }

    public double airPressure() {
        return roundDouble(ValueConverter.airPressure(rawMeasurement.getBarometer()), 1);
    }

    public double insideTemperature() {
        return roundDouble(ValueConverter.insideTemperature(rawMeasurement.getInsideTemp()), 1);
    }

    public double outsideTemperature() {
        return roundDouble(ValueConverter.outsideTemperature(rawMeasurement.getOutsideTemp()), 1);
    }

    public double outsideHumidity() {
        return roundDouble(ValueConverter.humidity(rawMeasurement.getOutsideHum()), 1);
    }

    public double insideHumidity() {
        return roundDouble(ValueConverter.humidity(rawMeasurement.getInsideHum()), 1);
    }

    public double windSpeed() {
        return roundDouble(ValueConverter.windSpeed(rawMeasurement.getWindSpeed()), 1);
    }

    public double avgWindSpeed() {
        return roundDouble(ValueConverter.windSpeed(rawMeasurement.getAvgWindSpeed()), 1);
    }

    public double windDirection() {
        return roundDouble(ValueConverter.windDirection(rawMeasurement.getWindDir()), 1);
    }

    public double rainMeter() {
        short rainRate = rawMeasurement.getRainRate();
        return ValueConverter.rainMeter(rainRate);
    }

    public double rainMeterInMMPerHour() {
        return (ValueConverter.rainMeter(rawMeasurement.getRainRate())) / 60;
    }

    public double uvIndex() {
        return roundDouble(ValueConverter.uvIndex(rawMeasurement.getUVLevel()), 1);
    }

    public double batteryLevel() {
        return roundDouble(ValueConverter.batteryLevel(rawMeasurement.getBattLevel()), 1);
    }

    public String sunRise() {
        return ValueConverter.sunRise(rawMeasurement.getSunrise());
    }

    public String sunSet() {
        return ValueConverter.sunSet(rawMeasurement.getSunset());
    }

    public double windChill() {
        double fahrenheit = rawMeasurement.getOutsideTemp() / 10.0;
        return roundDouble(((ValueConverter.windChill(fahrenheit, rawMeasurement.getWindSpeed()) - 32.0) / 1.8), 1);
    }

    public double heatIndex() {
        //double fahrenheit = rawMeasurement.getOutsideTemp() / 10.0;
        return roundDouble((ValueConverter.heatIndex((short) (rawMeasurement.getOutsideTemp() / 10.0), (short) rawMeasurement.getOutsideHum())), 1);
    }

    public String toString() {
        String s = "\nMeasurements:"
                + "\nbarometer = \t" + airPressure()
                + "\ninsideTemp = \t" + insideTemperature()
                + "\noutsideHum = \t\t\t" + outsideHumidity()
                + "\ninsideHum = \t\t\t" + insideHumidity()
                + "\noutsideTemp = \t" + outsideTemperature()
                + "\nwindSpeed = \t" + windSpeed()
                + "\nwindDir = \t\t" + windDirection()
                + "\nrainRate = \t\t" + rainMeter()
                + "\nUVLevel = \t\t" + uvIndex()
                + "\nbattLevel = \t" + batteryLevel()
                + "\nsunrise = \t\t" + sunRise()
                + "\nheatindex = \t" + heatIndex()
                + "\nwindchill = \t" + windChill()
                + "\nsunset = \t\t" + sunSet();
        return s;
    }

    public boolean isValid(RawMeasurement rawMeasurementArray) {
        RawMeasurement list = rawMeasurementArray;

        // max gemeten waarde in nederland is 1050 wereldwijd 1084 als je deze waarde omrekent kom je op 1083 dus dat zou kunnen
        if (rawMeasurementArray.getBarometer() >= 32000) {
            return false;
        }
        if (rawMeasurementArray.getInsideTemp() >= 1100) {
            return false;
        }
        // als je deze waarde omrekent dan kom je rond de 43 graden uit dit zou kunnen want er is in nederland ooit 40.7 gemeten
        if (rawMeasurementArray.getOutsideTemp() >= 1100) {
            return false;
        }
        // als je dit omrekent kom je op 177 uit hoogst gemeten waarde in nederland is 175
        if (rawMeasurementArray.getWindSpeed() >= 110) {
            return false;
        }
        if (rawMeasurementArray.getAvgWindSpeed() >= 110) {
            return false;
        }
        // het is een waarde tussen de 1 en 360 deze kan dus niet hoger zijn de 360 of lager dan 1
        if (rawMeasurementArray.getWindDir() > 360 || rawMeasurementArray.getWindDir() < 0) {
            return false;
        }
        // water heeft een vochtiheid van 100 % er komen percentage terug dus hoger dan 90 lijkt me niet
        if (rawMeasurementArray.getOutsideHum() >= 90) {
            return false;
        }
        // in nederland is hoogste gemeten mm regen val in een uur 94 mm als je dit omrekent dan kom je op 95 dus kan echt niet hoger
        if (rawMeasurementArray.getRainRate() > 256) {
            return false;
        }
        // de grootste waarde index is in nederland 8 dus 85 / 10  = 8.5  hij zou dus echt niet hoger kunnen zijn dan 85
        if (rawMeasurementArray.getUVLevel() > 85) {
            return false;
        }
        return true;
    }

    public static double roundDouble(double valueToRound, int amountOfNumbersAfterTheDot) {

        double times = Math.pow(10, amountOfNumbersAfterTheDot);

        int number = (int) (valueToRound * times);

        double outputNumber = number / times;

        return outputNumber;
    }

    public static double lowestValue(ArrayList<Double> list) {

        Collections.sort(list);

        return list.get(0);
    }

    public static double highestValue(ArrayList<Double> list) {

        Collections.sort(list);

        return list.get(list.size() - 1);
    }

    public static double average(ArrayList<Double> list) {

        double sum = 0.0;

        for (double value : list) {
            sum += value;
        }

        return roundDouble((sum / list.size()), 1);
    }

    public static double median(ArrayList<Double> arrayList) {
        Collections.sort(arrayList);

        double middle;
        if (arrayList.size() % 2 == 0) {
            middle = (arrayList.get(arrayList.size() / 2) + arrayList.get((arrayList.size() / 2) - 1)) / 2;
        } else {
            middle = arrayList.get(arrayList.size() / 2);
        }
        return middle;
    }

    public static double standardDeviation(ArrayList<Double> list) {

        double sum = 0.0;
        double standardDeviation = 0.0;
        int length = list.size();

        for (double value : list) {
            sum += value;
        }

        double mean = sum / length;

        for (double value : list) {
            standardDeviation += Math.pow((value - mean), 2);
        }

        return roundDouble(Math.sqrt((standardDeviation / length)), 1);
    }

    public static ArrayList<Double> mode(ArrayList<Double> list) {
        ArrayList<Double> doubles = new ArrayList<>();
        int maxCounter = 0;

        for (double value : list) {
            int counter = 0;
            for (int i = 0; i < list.size(); i++) {
                if (value == list.get(i)) {
                    counter++;
                }
            }
            if (counter > maxCounter) {
                if (!doubles.contains(value)) {
                    maxCounter = counter;
                    doubles.clear();
                    doubles.add(value);
                }
            }
            if (counter == maxCounter) {
                if (!doubles.contains(value)) {
                    doubles.add(value);
                }
            }
        }
        return doubles;
    }

    public LocalDateTime getDateStamp() {
        return rawMeasurement.getDateStamp();
    }
}
