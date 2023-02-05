import java.time.*;
import java.time.temporal.*;
import java.util.ArrayList;

/**
 * A class to contain a period of time
 *
 * @author Johan Talboom
 * @version 2.0
 */
public class Period
{
    private LocalDate beginPeriod;
    private LocalDate endPeriod;

    /**
     * default constructor, sets the period to today
     */
    public Period()
    {
        beginPeriod = LocalDate.now();
        endPeriod = LocalDate.now();
    }

    public Period(LocalDate beginPeriod, LocalDate endPeriod)
    {
        this.beginPeriod = beginPeriod;
        this.endPeriod = endPeriod;
    }

    public Period(LocalDate beginPeriod)
    {
        this.beginPeriod = beginPeriod;
        this.endPeriod = LocalDate.now();
    }

    public Period(int days)
    {
        this.beginPeriod = LocalDate.now().minus(java.time.Period.ofDays(days));
        this.endPeriod = LocalDate.now();
    }

    /**
     * Simple setter for start of period
     */
    public void setStart(int year, int month, int day)
    {
        beginPeriod = LocalDate.of(year, month, day);
    }

    /**
     * simple setter for end of period
     */
    public void setEnd(int year, int month, int day)
    {
        endPeriod = LocalDate.of(year, month, day);
    }

    /**
     * alternative setter for start of period
     *
     * @param beginPeriod
     */
    public void setStart(LocalDate beginPeriod)
    {
        this.beginPeriod = beginPeriod;
    }

    /**
     * alternative setter for end of period
     *
     * @param endPeriod
     */
    public void setEnd(LocalDate endPeriod)
    {
        this.endPeriod = endPeriod;
    }

    /**
     * calculates the number of days in the period
     */
    public long numberOfDays()
    {
        return ChronoUnit.DAYS.between(beginPeriod, endPeriod);
    }


    /**
     * gets all raw measurements of this period from the database
     *
     * @return a list of raw measurements
     */
    public ArrayList<RawMeasurement> getRawMeasurements()
    {
        return DatabaseConnection.getMeasurementsBetween(LocalDateTime.of(beginPeriod, LocalTime.of(0, 1)), LocalDateTime.of(endPeriod, LocalTime.of(23, 59)));
    }

    public RawMeasurement getRecentRawMeasurement()
    {
        return DatabaseConnection.getMostRecentMeasurement();
    }

    /**
     * Builds an ArrayList of measurements. This method also filters out any 'bad' measurements
     *
     * @return a filtered list of measurements
     */
    public ArrayList<Measurement> getMeasurements()
    {
        ArrayList<Measurement> measurements = new ArrayList<>();
        ArrayList<RawMeasurement> rawMeasurements = getRawMeasurements();
        for (RawMeasurement rawMeasurement : rawMeasurements)
        {
            Measurement measurement = new Measurement(rawMeasurement);
            if (measurement.isValid(rawMeasurement))
            {
                measurements.add(measurement);
            }
        }
        return measurements;
    }
    public double getAverageOutsideTemperature()
    {
        ArrayList<Measurement> measurements = getMeasurements();
        ArrayList<Double> listWithOutsideTemps = new ArrayList<>();

        for (Measurement measurement : measurements)
        {
            listWithOutsideTemps.add(measurement.outsideTemperature());
        }

        double average = Measurement.average(listWithOutsideTemps);

        //calculate average outside temperature and return it
        return average;
    }

    public static int GoodWeatherDays(Period period)
    {

        // Het is goed weer als:
        // Buiten temperatuur > 10 en < 25 (graden)
        // Gevallen neerslag < 5 (Als er geen regen is gevallen of bijna geen regen)
        // windsnelheid < 13 (km per uur)

        int goodWeatherDays = 0;
        ArrayList<Measurement> measurements = period.getMeasurements();
        boolean isFirstTime = true;
        LocalDate rememberedDate = null;
        int sumOfTemp = 0;
        int sumOfAir = 0;
        int sumOfRain = 0;
        int i = 0;

        for (Measurement measurement : measurements)
        {

            if (isFirstTime)
            {
                rememberedDate = measurement.getListWithAllLocalDateTimeValues().get(0).toLocalDate();
                isFirstTime = false;
            }

            LocalDate date = measurement.getListWithAllLocalDateTimeValues().get(0).toLocalDate();

            if (date.equals(rememberedDate))
            {
                sumOfTemp += measurement.outsideTemperature();
                sumOfAir += measurement.windSpeed();
                sumOfRain += measurement.rainMeter();
            } else
            {
                int averageOfTemp = sumOfTemp / (i - 1);
                int averageOfAir = sumOfAir / (i - 1);
                int averageOfRain = sumOfRain / (i - 1);

                rememberedDate = measurement.getListWithAllLocalDateTimeValues().get(0).toLocalDate();
                sumOfRain = 0;
                sumOfTemp = 0;
                sumOfAir = 0;
                sumOfTemp += measurement.outsideTemperature();
                sumOfAir += measurement.windSpeed();
                sumOfRain += measurement.rainMeter();
                i = 0;

                if (averageOfTemp > 10 && averageOfTemp < 25)
                {
                    if (averageOfRain < 5)
                    {
                        if (averageOfAir < 13)
                        {
                            goodWeatherDays++;
                        }
                    }
                }
            }
            i++;
        }
        return goodWeatherDays;
    }

    public static double round(double value, int places)
    {
        double roundoff = Math.pow(10, places);
        return Math.round(value * roundoff) / roundoff;
    }

    public ArrayList<Double> getTotalRain()
    {
        ArrayList<Measurement> measurements = getMeasurements();

        Month lastMonth = measurements.get(0).getDateStamp().getMonth();
        double sum = 0;
        ArrayList<Double> waardesVanMaande = new ArrayList<>();

        for (Measurement measurement : measurements)
        {
            sum += measurement.rainMeter();
            if (measurement.getDateStamp().getMonth() != lastMonth)
            {
                waardesVanMaande.add(sum);
                sum = 0;
            }
            lastMonth = measurement.getDateStamp().getMonth();
        }
        waardesVanMaande.add(sum);
        return waardesVanMaande;
    }

    public ArrayList<Double> getOutsideTemp()
    {
        ArrayList<Measurement> measurements = getMeasurements();
        ArrayList<Double> tempArray = new ArrayList<>();

        for (Measurement measurement : measurements)
        {
            tempArray.add(round((measurement.outsideTemperature()), 1));
        }

        return tempArray;

    }
}
