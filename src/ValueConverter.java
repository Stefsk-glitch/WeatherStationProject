public class ValueConverter {

    public static double airPressure(short rawValue){
        double resultAirPressure = rawValue / 29.5299830714145;
        return resultAirPressure;
    }

    public static double insideTemperature(short rawValue){
        double resultInsideTemperature = ((rawValue / 10.0) - 32.0) / 1.8;
        return resultInsideTemperature;
    }

    public static double outsideTemperature(short rawValue){
        double resultOusideTemperature = ((rawValue / 10.0) - 32.0) / 1.8;
        return resultOusideTemperature;
    }

    public static double humidity(short rawValue){
        double humidityPercentage = rawValue;
        return humidityPercentage;
    }

    public static double windSpeed(short rawValue){
        double resultWindSpeed = rawValue * 1.609344;
        return resultWindSpeed;
    }

    public static double windDirection(short rawValue){
        double resultWindDirection = rawValue;
        return resultWindDirection;
    }

    public static double rainMeter(short rawValue){
        if (!(rawValue == 0)){
            double result = rawValue / 100;
            double resultrainMeter = result * 25.4;
            return resultrainMeter;
        }
        return 0.0;
    }

    public static double uvIndex(short rawValue){
        double resultuvLevel = rawValue / 10.0;
        return resultuvLevel;
    }

    public static double batteryLevel(short rawValue){
        double resultBatteryLevel = ((rawValue * 300) / 512.0) / 100.0;
        return resultBatteryLevel;
    }

    public static String sunRise(short rawValue){
        int length = String.valueOf(rawValue).length();
        if (length < 4){
            String word = "0" + String.valueOf(rawValue).substring(0,1);
            String word1 = String.valueOf(rawValue).substring(1,3);
            return word + ":" + word1;
        }
        else {
            String word = String.valueOf(rawValue).substring(0,2);
            String word1 = String.valueOf(rawValue).substring(2,4);
            return word + ":" + word1;
        }
    }

    public static String sunSet(short rawValue){
        int length = String.valueOf(rawValue).length();
        if (length < 4){
            String word = 0 + String.valueOf(rawValue).substring(0,1);
            String word1 = String.valueOf(rawValue).substring(1,3);
            return word + ":" + word1;
        }
        else {
            String word = String.valueOf(rawValue).substring(0,2);
            String word1 = String.valueOf(rawValue).substring(2,4);
            return word + ":" + word1;
        }

    }

    public static double windChill(double temperaturePara, short windspeedPara){
        // input fahrenheit
        double resultWindChill = 35.74 + 0.6215 * temperaturePara + (0.4275 * temperaturePara - 35.75) * Math.pow(windspeedPara, 0.16);
        return resultWindChill;
        // output fahrenheit
    }

    public static double heatIndex(short temperaturePara, short humidityPara){
        // input fahrenheit
        int temperature = temperaturePara;
        double humidity = humidityPara;
        double heatIndexResult;
        
        final double C1 = -42.379;
        final double C2 = 2.04901523;
        final double C3 = 10.14333127;
        final double C4 = -0.22475541;
        final double C5 = -.00683783;
        final double C6 = -5.481717E-2;
        final double C7 = 1.22874E-3;
        final double C8 = 8.5282E-4;
        final double C9 = -1.99E-6;

        int T = temperature;
        double R = humidity;
        double T2 = temperature * temperature;
        double R2 = humidity * humidity;

        heatIndexResult = C1 + (C2 * T) + (C3 * R) + (C4 * T * R) + (C5 * T2) + (C6 * R2) + (C7 * T2 * R) + (C8 * T * R2) + (C9 * T2 * R2);
        heatIndexResult = (heatIndexResult - 32.0) / 1.80;
        // return fahrenheit
        return heatIndexResult;
    }
}
