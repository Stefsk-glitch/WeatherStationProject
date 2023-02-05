import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Display {
    private int width = 128;
    private int blocks;
    private int blockNumber;
    private int beginYear;
    private int beginMonth;
    private int beginDay;
    private int verticalBlockNumber;
    private LocalDate beginPeriod;
    private LocalDate endPeriod;

    private ArrayList<Measurement> measurements = new ArrayList<>();
    private TempDifference_Mert MertsOpdracht = new TempDifference_Mert();
    private GoodWeatherDays_Stef StefsOpdracht = new GoodWeatherDays_Stef();
    private MaxRain_Quinten QuintensOpdracht = new MaxRain_Quinten();

    Scanner reader = new Scanner(System.in);


    public Display(int blocks) {
        this.blocks = blocks;
        blockNumber = 1;
        verticalBlockNumber = 0;
        start();
    }

    public void start() {
        IO.init();
        ControlGUI.clrDMDisplay();

        buildVerticalSquare();
        buildSquare();
        buildBlocks();
        fillBlock(blockNumber);
        fillBlockVertical(verticalBlockNumber);

        boolean nextButtonPressed = false;
        boolean backButtonPressed = false;
        boolean redButtonPressed = false;
        boolean setVerTab1 = false;
        boolean setVerTab2 = false;
        boolean setVerTab3 = false;
        boolean setVerTab4 = false;
        boolean setVerTab5 = false;
        boolean setVerTab6 = false;
        boolean setTab1 = false;
        boolean setTab2 = false;
        boolean setTab3 = false;
        boolean setTab4 = false;
        boolean setTab5 = false;
        boolean setTab6 = false;
        boolean setTab7 = false;
        boolean setTab8 = false;
        boolean setTab9 = false;
        boolean setTab10 = false;
        boolean setTab11 = false;
        boolean setTab12 = false;



        while (true) {
            // If next button is pressed middle button
            if (IO.readShort(0x100) != 0 && !nextButtonPressed) {
                verticalBlockNumber = 0;
                blockNumber++;

                if (blockNumber > blocks) {
                    blockNumber = 1;
                }
                ControlGUI.clrDMDisplay();
                buildVerticalSquare();
                buildSquare();
                buildBlocks();
                fillBlock(blockNumber);
                nextButtonPressed = true;
            }

            if (IO.readShort(0x100) == 0) {
                nextButtonPressed = false;
            }

            // If back button is pressed most left button
            if (IO.readShort(0x90) != 0 && !backButtonPressed) {
                verticalBlockNumber = 0;
                blockNumber--;

                if (blockNumber < 1) {
                    blockNumber = blocks;
                }

                ControlGUI.clrDMDisplay();
                buildVerticalSquare();
                buildSquare();
                buildBlocks();
                fillBlock(blockNumber);
                backButtonPressed = true;
            }

            if (IO.readShort(0x90) == 0) {
                backButtonPressed = false;
            }

            // If red button is pressed
            if (IO.readShort(0x80) != 0 && !redButtonPressed) {
                verticalBlockNumber++;
                ControlGUI.clrDMDisplay();
                buildVerticalSquare();
                buildSquare();
                buildBlocks();
                if (verticalBlockNumber != 7) {
                    fillBlockVertical(verticalBlockNumber);
                }
                fillBlock(blockNumber);
                redButtonPressed = true;

            }

            if (IO.readShort(0x80) == 0) {
                redButtonPressed = false;
            }

            if (verticalBlockNumber == 7) {
                verticalBlockNumber = 0;

                if (setTab1) {
                    ArrayList<Measurement> weeklyValues = getWeeklyValues();
                    if (weeklyValues != null) {
                        ControlGUI.writeToClrDMDisplay("\nTemperatuur: " + weeklyValues.get(0).outsideTemperature());
                    } else {
                        ControlGUI.writeToClrDMDisplay("\nTemperatuur: error");
                    }
                }
                if (setTab2) {
                    ArrayList<Measurement> weeklyValues = getWeeklyValues();
                    if (weeklyValues != null) {
                        ControlGUI.writeToClrDMDisplay("\nAirp: " + weeklyValues.get(0).airPressure());
                    } else {
                        ControlGUI.writeToClrDMDisplay("\nAirp: error");
                    }
                }

                if (setTab4) {
                    ArrayList<Measurement> weeklyValues = getWeeklyValues();
                    if (weeklyValues != null) {
                        ControlGUI.writeToClrDMDisplay("\nWindspeed: " + weeklyValues.get(0).windSpeed());
                    } else {
                        ControlGUI.writeToClrDMDisplay("\nWindspeed: error");
                    }
                }

                if (setTab5) {
                    ArrayList<Measurement> weeklyValues = getWeeklyValues();
                    if (weeklyValues != null) {
                        ControlGUI.writeToClrDMDisplay("\nRainmeter: " + weeklyValues.get(0).rainMeter());
                    } else {
                        ControlGUI.writeToClrDMDisplay("\nRainmeter: error");
                    }
                }
                setVerTab1 = false;
                setVerTab2 = false;
                setVerTab3 = false;
                setVerTab4 = false;
                setVerTab5 = false;
                setVerTab6 = false;
            }


            if (verticalBlockNumber == 1 && !setVerTab1) {
                if (setTab1) {
                    ControlGUI.writeToClrDMDisplay("\nTemp min: " + Measurement.lowestValue(getMathfunctions(Entities.outsideTemp)));
                }
                if (setTab2) {
                    ControlGUI.writeToClrDMDisplay("\nAirp min: " + Measurement.lowestValue(getMathfunctions(Entities.airPressure)));
                }
                if (setTab3) {
                    ControlGUI.writeToClrDMDisplay("\nHum min: " + Measurement.lowestValue(getMathfunctions(Entities.humidity)));
                }
                if (setTab4) {
                    ControlGUI.writeToClrDMDisplay("\nWinds min: " + Measurement.lowestValue(getMathfunctions(Entities.windSpeed)));
                }
                if (setTab5) {
                    ControlGUI.writeToClrDMDisplay("\nRainm min: " + Measurement.lowestValue(getMathfunctions(Entities.rainMeter)));
                }

                setVerTab1 = true;
                setVerTab2 = false;
            }

            if (verticalBlockNumber == 2 && !setVerTab2) {
                if (setTab1) {
                    ControlGUI.writeToClrDMDisplay("\nTemp max: " + Period.round(Measurement.highestValue(getMathfunctions(Entities.outsideTemp)), 1));
                }
                if (setTab2) {
                    ControlGUI.writeToClrDMDisplay("\nAirp max: " + Period.round(Measurement.highestValue(getMathfunctions(Entities.airPressure)), 1));
                }
                if (setTab3) {
                    ControlGUI.writeToClrDMDisplay("\nHum max: " + Period.round(Measurement.highestValue(getMathfunctions(Entities.humidity)), 1));
                }
                if (setTab4) {
                    ControlGUI.writeToClrDMDisplay("\nWinds max: " + Period.round(Measurement.highestValue(getMathfunctions(Entities.windSpeed)), 1));
                }
                if (setTab5) {
                    ControlGUI.writeToClrDMDisplay("\nRainm max: " + Period.round(Measurement.highestValue(getMathfunctions(Entities.rainMeter)), 1));
                }

                setVerTab1 = false;
                setVerTab2 = true;
            }

            if (verticalBlockNumber == 3 && !setVerTab3) {
                if (setTab1) {
                    ControlGUI.writeToClrDMDisplay("\nTemp avg: " + Measurement.average(getMathfunctions(Entities.outsideTemp)));
                }
                if (setTab2) {
                    ControlGUI.writeToClrDMDisplay("\nAirp avg: " + Measurement.average(getMathfunctions(Entities.airPressure)));
                }
                if (setTab3) {
                    ControlGUI.writeToClrDMDisplay("\nHum avg: " + Measurement.average(getMathfunctions(Entities.humidity)));
                }
                if (setTab4) {
                    ControlGUI.writeToClrDMDisplay("\nWinds avg: " + Measurement.average(getMathfunctions(Entities.windSpeed)));
                }
                if (setTab5) {
                    ControlGUI.writeToClrDMDisplay("\nRainm avg: " + Measurement.average(getMathfunctions(Entities.rainMeter)));
                }

                setVerTab2 = false;
                setVerTab3 = true;
            }

            if (verticalBlockNumber == 4 && !setVerTab4) {
                if (setTab1) {
                    ControlGUI.writeToClrDMDisplay("\nTemp median: " + Measurement.median(getMathfunctions(Entities.outsideTemp)));
                }
                if (setTab2) {
                    ControlGUI.writeToClrDMDisplay("\nAirp median: " + Measurement.median(getMathfunctions(Entities.airPressure)));
                }
                if (setTab3) {
                    ControlGUI.writeToClrDMDisplay("\nHum median: " + Measurement.median(getMathfunctions(Entities.humidity)));
                }
                if (setTab4) {
                    ControlGUI.writeToClrDMDisplay("\nWinds median: " + Measurement.median(getMathfunctions(Entities.windSpeed)));
                }
                if (setTab5) {
                    ControlGUI.writeToClrDMDisplay("\nRainm median: " + Measurement.median(getMathfunctions(Entities.rainMeter)));
                }
                setVerTab3 = false;
                setVerTab4 = true;
            }

            if (verticalBlockNumber == 5 && !setVerTab5) {
                if (setTab1) {
                    ControlGUI.writeToClrDMDisplay("\nTemp mode: " + Measurement.mode(getMathfunctions(Entities.outsideTemp)));
                }
                if (setTab2) {
                    ControlGUI.writeToClrDMDisplay("\nAirp mode: " + Measurement.mode(getMathfunctions(Entities.airPressure)));
                }
                if (setTab3) {
                    ControlGUI.writeToClrDMDisplay("\nHum mode: " + Measurement.mode(getMathfunctions(Entities.humidity)));
                }
                if (setTab4) {
                    ControlGUI.writeToClrDMDisplay("\nWinds mode: " + Measurement.mode(getMathfunctions(Entities.windSpeed)));
                }
                if (setTab5) {
                    ControlGUI.writeToClrDMDisplay("\nRainm mode: " + Measurement.mode(getMathfunctions(Entities.rainMeter)));
                }
                setVerTab4 = false;
                setVerTab5 = true;
            }

            if (verticalBlockNumber == 6 && !setVerTab6) {
                if (setTab1) {
                    ControlGUI.writeToClrDMDisplay("\nOutsideT Stdd: " + Measurement.standardDeviation(getMathfunctions(Entities.outsideTemp)));
                }
                if (setTab2) {
                    ControlGUI.writeToClrDMDisplay("\nAirp Stdd: " + Measurement.standardDeviation(getMathfunctions(Entities.airPressure)));
                }
                if (setTab3) {
                    ControlGUI.writeToClrDMDisplay("\nHumidity Stdd: " + Measurement.standardDeviation(getMathfunctions(Entities.humidity)));
                }
                if (setTab4) {
                    ControlGUI.writeToClrDMDisplay("\nWindspeed Stdd: " + Measurement.standardDeviation(getMathfunctions(Entities.windSpeed)));
                }
                if (setTab5) {
                    ControlGUI.writeToClrDMDisplay("\nRainmeter Stdd: " + Measurement.standardDeviation(getMathfunctions(Entities.rainMeter)));
                }
                setVerTab5 = false;
                setVerTab6 = true;
            }

            if (verticalBlockNumber == 7) {
                setVerTab6 = false;
            }

            if (blockNumber == 1 && !setTab1) {

                ArrayList<Measurement> weeklyValues = getWeeklyValues();
                if (weeklyValues != null) {
                    ControlGUI.writeToClrDMDisplay("\nTemperatuur: " + weeklyValues.get(0).outsideTemperature());
                } else {
                    ControlGUI.writeToClrDMDisplay("\nTemperatuur: error");
                }

                setTab1 = true;
                setTab2 = false;
            }
            if (blockNumber == 2 && !setTab2) {
                ArrayList<Measurement> weeklyValues = getWeeklyValues();
                if (weeklyValues != null) {
                    ControlGUI.writeToClrDMDisplay("\nAirpressure: " + weeklyValues.get(0).airPressure());
                } else {
                    ControlGUI.writeToClrDMDisplay("\nAirpressure: error");
                }
                setTab1 = false;
                setTab2 = true;
            }
            if (blockNumber == 3 && !setTab3) {
                ArrayList<Measurement> weeklyValues = getWeeklyValues();
                if (weeklyValues != null) {
                    ControlGUI.writeToClrDMDisplay("\nHumidity: " + weeklyValues.get(0).outsideHumidity());
                } else {
                    ControlGUI.writeToClrDMDisplay("\nHumidity: error");
                }
                setTab2 = false;
                setTab3 = true;
            }
            if (blockNumber == 4 && !setTab4) {
                ArrayList<Measurement> weeklyValues = getWeeklyValues();
                if (weeklyValues != null) {
                    ControlGUI.writeToClrDMDisplay("\nWindspeed: " + weeklyValues.get(0).windSpeed());
                } else {
                    ControlGUI.writeToClrDMDisplay("\nWindspeed: error");
                }
                setTab3 = false;
                setTab4 = true;

            }
            if (blockNumber == 5 && !setTab5) {
                ArrayList<Measurement> weeklyValues = getWeeklyValues();
                if (weeklyValues != null) {
                    ControlGUI.writeToClrDMDisplay("\nRainmeter: " + weeklyValues.get(0).rainMeter());
                } else {
                    ControlGUI.writeToClrDMDisplay("\nRainmeter: error");
                }
                setTab4 = false;
                setTab5 = true;
            }
            if (blockNumber == 6 && !setTab6 && IO.readShort(0x80) == 0) {
                ControlGUI.writeToClrDMDisplay("\nMaand met de meeste\nregen in een jaar");

                setTab5 = false;
            }

            if (blockNumber == 6 && !setTab6 && IO.readShort(0x80) == 1) {
                int year = 0;
                String stringYear;
                boolean correctDate = false;

                while (!correctDate) {
                    System.out.println("Welk jaar: ");
                    stringYear = reader.nextLine();
                    if (stringYear.isEmpty()) {
                        System.out.println("Please enter a valid year (2016 - 2022)");
                    } else {
                        year = Integer.parseInt(stringYear);
                        if (year < 2016 || year > 2022) {
                            System.out.println("Please enter a year between 2016 - 2022");
                        } else {
                            correctDate = true;
                        }
                    }
                }

                MostRainRate_Thijme ThijmesOpdracht = new MostRainRate_Thijme(year);

                LocalDate thijmeBeginPeriode = LocalDate.of(year, 1, 1);
                LocalDate thijmeEindPeriode = LocalDate.of(year, 12, 31);
                Period thijmePeriode = new Period(thijmeBeginPeriode, thijmeEindPeriode);
                ArrayList<Double> totalRainList = thijmePeriode.getTotalRain();

                ControlGUI.clrDMDisplay();
                ControlGUI.writeToClrDMDisplay("\nMost rainmonth is");
                ControlGUI.writeToClrDMDisplay("\n" + ThijmesOpdracht.getYear() + " is " + MostRainRate_Thijme.printAll(totalRainList));

                buildSquare();
                buildBlocks();
                buildVerticalSquare();
                buildVerticalBlocks();
                fillBlock(6);
                verticalBlockNumber = 0;

                setTab5 = false;
                setTab6 = true;
            }


            if (blockNumber == 7 && !setTab7 && IO.readShort(0x80) == 0) {
                ControlGUI.writeToClrDMDisplay("\nGrootste verschil\nin temp:");

                setTab6 = false;
            }
            if (blockNumber == 7 && !setTab7 && IO.readShort(0x80) == 1) {
                dateBuilder(true);
                dateBuilder(false);

                Period mertPeriode = new Period(this.beginPeriod, this.endPeriod);

                ControlGUI.clrDMDisplay();

                ControlGUI.writeToClrDMDisplay("\nVerschil: " + MertsOpdracht.biggestDifference(mertPeriode));
                ControlGUI.writeToClrDMDisplay("\nDatum: " + MertsOpdracht.getDayOfBiggestDifference());

                buildSquare();
                buildBlocks();
                buildVerticalSquare();
                buildVerticalBlocks();
                fillBlock(7);
                verticalBlockNumber = 0;
                setTab6 = false;
                setTab7 = true;
            }


            if (blockNumber == 8 && !setTab8 && IO.readShort(0x80) == 0) {
                ControlGUI.writeToClrDMDisplay("\nLangste tijd zomer\nin een jaar");

                setTab7 = false;
            }

            if (blockNumber == 8 && !setTab8 && IO.readShort(0x80) == 1) {

                int year = 0;
                String stringYear;
                boolean correctDate = false;

                while (!correctDate) {
                    System.out.println("Year: ");
                    stringYear = reader.nextLine();
                    if (stringYear.isEmpty()) {
                        System.out.println("Please enter a valid year (2017 - 2022)");
                    } else {
                        year = Integer.parseInt(stringYear);
                        if (year < 2017 || year > 2022) {
                            System.out.println("Please enter a year between 2017 - 2022");
                        } else {
                            correctDate = true;
                        }
                    }
                }
                SummerDays_Thom thomsOpdracht = new SummerDays_Thom(year);
                ControlGUI.clrDMDisplay();
                ControlGUI.writeToClrDMDisplay(thomsOpdracht.printAll());

                buildSquare();
                buildBlocks();
                buildVerticalSquare();
                buildVerticalBlocks();
                fillBlock(8);
                verticalBlockNumber = 0;

                setTab7 = false;
                setTab8 = true;
            }

            if (blockNumber == 9 && !setTab9 && IO.readShort(0x80) == 0) {
                ControlGUI.writeToClrDMDisplay("\nGoede\nweerdagen:");

                setTab8 = false;
            }

            if (blockNumber == 9 && !setTab9 && IO.readShort(0x80) == 1) {
                dateBuilder(true);
                dateBuilder(false);

                Period stefPeriode = new Period(this.beginPeriod, this.endPeriod);
                ControlGUI.clrDMDisplay();
                ControlGUI.writeToClrDMDisplay("\nDagen: " + StefsOpdracht.goodWeatherDays(stefPeriode));

                buildSquare();
                buildBlocks();
                buildVerticalSquare();
                buildVerticalBlocks();
                fillBlock(9);
                verticalBlockNumber = 0;

                setTab8 = false;
                setTab9 = true;
            }

            if (blockNumber == 10 && !setTab8 && IO.readShort(0x80) == 0) {
                ControlGUI.writeToClrDMDisplay("\nAaneengesloten\nregen:");

                setTab9 = false;
                setTab10 = true;
            }

            if (blockNumber == 10 && !setTab10 && IO.readShort(0x80) == 1) {
                dateBuilder(true);
                dateBuilder(false);

                Period quintenPeriode = new Period(this.beginPeriod, this.endPeriod);

                ControlGUI.clrDMDisplay();

                ControlGUI.writeToClrDMDisplay("\nQuinten: " + QuintensOpdracht.maxRegenVal(quintenPeriode));

                buildSquare();
                buildBlocks();
                buildVerticalSquare();
                buildVerticalBlocks();
                fillBlock(10);
                verticalBlockNumber = 0;

                setTab9 = false;
                setTab10 = true;
            }

            if (blockNumber == 11 && !setTab11) {
                setTab10 = false;
                setTab11 = true;
            }
            if (blockNumber == 12 && !setTab12) {
                setTab11 = false;
                setTab12 = true;
            }
            if (blockNumber == 13) {
                setTab12 = false;
            }

        }
    }

    public void blockRedButton(){
        if (IO.readShort(0x80) == 1){
            IO.writeShort(0x80, 0);
        }
    }

    private void buildSquare() {
        ControlGUI.buildLines(width, 4, false, true);
        ControlGUI.buildLines(width, 0, false, true);
        ControlGUI.buildLines(0, 4, true, false);
        ControlGUI.buildLines(127, 4, true, false);
    }

    private void buildVerticalSquare() {
        ControlGUI.buildLines(120, 24, true, false);
        ControlGUI.buildLines(127, 24, true, false);
        buildVerticalBlocks();
    }

    private void buildVerticalBlocks() {
        int x;

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 28; j += 4) {
                x = 120;
                for (int k = 0; k < 8; k++) {
                    ControlGUI.setPixel(x, j);
                    x++;
                }
            }
        }
    }

    private void buildBlocks() {
        int amountOfPixelLeft = width - blocks - 2;
        int pixelForBlock = amountOfPixelLeft / blocks;
        int drawPoint = 1;

        for (int i = 0; i < blocks; i++) {
            drawPoint += pixelForBlock;
            ControlGUI.buildLines(drawPoint, 4, true, false);
            drawPoint++;
        }
    }

    private void fillBlock(int blockNumber) {
        int amountOfPixelLeft = width - blocks - 2;
        int pixelForBlock = amountOfPixelLeft / blocks;
        int drawPoint = 1;
        int otherBlockSizes = 0;

        if (blockNumber > 1) {
            otherBlockSizes = pixelForBlock * (blockNumber - 1);
        }

        for (int i = 1; i < 4; i++) {
            for (int j = (drawPoint * blockNumber) + otherBlockSizes; j < (drawPoint * blockNumber) + (pixelForBlock * blockNumber); j++) {
                ControlGUI.setPixel(j, i);
            }
        }
    }

    private void fillBlockVertical(int verticalBlockNumber) {
        int drawPoint;

        ArrayList<Integer> yValues = new ArrayList<>();
        Collections.addAll(yValues, 1, 5, 9, 13, 17, 21);

        if (verticalBlockNumber > 0) {
            for (int y = 1; y < 4; y++) {
                drawPoint = 121;
                for (int i = 0; i < 6; i++) {
                    ControlGUI.setPixel(drawPoint, yValues.get(verticalBlockNumber - 1) + (y - 1));
                    drawPoint++;
                }
            }
        } else {
            buildVerticalSquare();
        }
    }

    public ArrayList<Measurement> getWeeklyValues() {
        LocalDate einde = LocalDate.now();
        LocalDate begin = einde.minusDays(15);

        Period p = new Period(begin, einde);

        ArrayList<Measurement> weeklyValues = p.getMeasurements();
        if (weeklyValues.size() == 0) {
            return null;
        }
        return weeklyValues;
    }

    public ArrayList<Double> getMathfunctions(Entities unit) {
        measurements = getWeeklyValues();
        ArrayList<Double> values = new ArrayList<>();
        for (Measurement value : measurements) {
            values.add(valueGetter(unit, value));
        }
        return values;
    }

    public double valueGetter(Entities unit, Measurement result) {
        if (unit.equals(Entities.outsideTemp)) {
            return result.outsideTemperature();
        }
        if (unit.equals(Entities.insideTemp)) {
            return result.insideTemperature();
        }
        if (unit.equals(Entities.airPressure)) {
            return result.airPressure();
        }
        if (unit.equals(Entities.humidity)) {
            return result.outsideHumidity();
        }
        if (unit.equals(Entities.windSpeed)) {
            return result.windSpeed();
        }
        if (unit.equals(Entities.windDirection)) {
            return result.windDirection();
        }
        if (unit.equals(Entities.rainMeter)) {
            return result.rainMeter();
        }
        if (unit.equals(Entities.uvIndex)) {
            return result.uvIndex();
        }
        if (unit.equals(Entities.batteryLevel)) {
            return result.batteryLevel();
        }
        if (unit.equals(Entities.heatIndex)) {
            return result.heatIndex();
        }
        if (unit.equals(Entities.windChill)) {
            return result.windChill();
        }
        return 0.0;
    }

    public void dateBuilder(boolean isBeginPeriod) {
        ArrayList<Integer> monthList = new ArrayList<>();
        Collections.addAll(monthList, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31);
        int year;
        int month;
        int day;
        int endDay;

        if (isBeginPeriod) {
            System.out.println("Year:");
            beginYear = Integer.parseInt(reader.nextLine());
            while (beginYear < 2016 || beginYear > 2022) {
                System.out.println("Enter a valid year between 2016 - 2022");
                System.out.println("Year:");
                beginYear = Integer.parseInt(reader.nextLine());
            }
            System.out.println("Month:");
            beginMonth = Integer.parseInt(reader.nextLine());
            while (beginMonth < 1 || beginMonth > 12) {
                System.out.println("Enter a month between 1 - 12");
                System.out.println("Month:");
                beginMonth = Integer.parseInt(reader.nextLine());
            }
            System.out.println("Day:");
            beginDay = Integer.parseInt(reader.nextLine());
            endDay = monthList.get(beginMonth - 1);
            while (beginDay < 1 || beginDay > endDay) {
                System.out.println("Enter a day between 1 - " + endDay);
                System.out.println("Day:");
                beginDay = Integer.parseInt(reader.nextLine());
            }
            this.beginPeriod = LocalDate.of(beginYear, beginMonth, beginDay);
        } else {
            System.out.println("Year:");
            year = Integer.parseInt(reader.nextLine());
            while (year < 2016 || year < beginYear) {
                System.out.println("Enter a valid year above " + beginYear);
                System.out.println("Year:");
                year = Integer.parseInt(reader.nextLine());
            }
            System.out.println("Month:");
            month = Integer.parseInt(reader.nextLine());
            while (month < 1 || month < beginMonth) {
                System.out.println("Enter a valid month above " + beginMonth);
                System.out.println("Month:");
                month = Integer.parseInt(reader.nextLine());
            }
            System.out.println("Day:");
            day = Integer.parseInt(reader.nextLine());
            endDay = monthList.get(month - 1);
            while (day < 1 || day < beginDay || day > endDay) {
                System.out.println("Enter a valid day above " + beginDay + " and under " + endDay);
                System.out.println("Day:");
                day = Integer.parseInt(reader.nextLine());
            }

            this.endPeriod = LocalDate.of(year, month, day);
        }
    }
}
