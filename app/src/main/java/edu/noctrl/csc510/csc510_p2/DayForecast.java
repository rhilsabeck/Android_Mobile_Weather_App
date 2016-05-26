package edu.noctrl.csc510.csc510_p2;
import java.util.Date;
/**
 * Created by Ryan on 4/28/2015.
 */
public class DayForecast {
    public static enum DayPeriod{AM,PM}
    public Date day;
    public String icon;
    public double precipitation;
    public ForecastPeriod amForecast;
    public ForecastPeriod pmForecast;
}
