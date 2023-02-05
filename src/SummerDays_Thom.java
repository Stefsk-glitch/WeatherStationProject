import java.rmi.registry.LocateRegistry;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class SummerDays_Thom {
    private int year;
    private LocalDate begin;
    private LocalDate end;
    private int length;

    public SummerDays_Thom(int year) {

        this.year = year;

    }

    public void getSummerPeriod(ArrayList<Measurement> lijst) {

        int thisDay = lijst.get(0).getDateStamp().getDayOfMonth();
        double temp = 0;
        LocalDate conceptBegin = LocalDate.of(this.year,1,1);
        LocalDate conceptEnd = LocalDate.of(this.year,1,1);
        int conceptLenght = 0;


        for (Measurement measurement : lijst) {

            if (temp < measurement.outsideTemperature() && measurement.outsideTemperature() < 1800) {
                temp = measurement.outsideTemperature();
            }


            if (measurement.getDateStamp().getDayOfMonth() != thisDay) {

                if (temp >= 25 && conceptEnd.getDayOfMonth() != thisDay) {

                    conceptBegin = LocalDate.of(this.year, measurement.getDateStamp().getMonth(), measurement.getDateStamp().getDayOfMonth());
                    conceptEnd = LocalDate.of(this.year, measurement.getDateStamp().getMonth(), measurement.getDateStamp().getDayOfMonth());
                    conceptLenght = 1;

                } else if (temp >= 25 && conceptEnd.getDayOfMonth() == thisDay) {

                    conceptEnd = LocalDate.of(this.year, measurement.getDateStamp().getMonth(), measurement.getDateStamp().getDayOfMonth());
                    conceptLenght++;

                }

                if (conceptLenght > this.length) {

                    this.length = conceptLenght;
                    this.begin = conceptBegin;
                    this.end = conceptEnd;

                }

                temp = 0;

            }

            thisDay = measurement.getDateStamp().getDayOfMonth();

        }

    }

    public ArrayList<Measurement> getData() {

        LocalDate beginDate = LocalDate.of(this.year, 1, 1);
        LocalDate endDate = LocalDate.of(this.year, 12, 31);

        Period year = new Period(beginDate, endDate);

        return year.getMeasurements();

    }

    public String printAll() {

        if (yearIsValid(year) == true) {

            getSummerPeriod(getData());

            return "\n begin: " + this.begin.getDayOfMonth() + "-" + this.begin.getMonth() + "\n end: " + this.end.getDayOfMonth() + "-" + this.end.getMonth();


        } else {

            return "Not a valid year";

        }
    }


    public boolean yearIsValid(int year){

        if (year < 2017){

            return false;

        } else {

            return true;

        }

    }

}
