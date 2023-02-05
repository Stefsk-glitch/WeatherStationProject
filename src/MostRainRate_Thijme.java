import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.text.DateFormatSymbols;

public class MostRainRate_Thijme {

    private int year;
    private Period p = new Period();

    public MostRainRate_Thijme(int year) {
        this.year = year;
    }

    public int getYear() {
        return year;
    }

    public static String printAll(ArrayList<Double> lijst) {

        double highest = 0;

        for (Double aDouble : lijst) {
            if (highest < aDouble){
                highest = aDouble;
            }
        }
        int result = lijst.indexOf(highest);
        return getMonth(result);

    }


    public static String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month-1];
    }
}
