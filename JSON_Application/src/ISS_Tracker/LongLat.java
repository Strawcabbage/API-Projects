package ISS_Tracker;

public class LongLat {

    double longitude;
    double latitude;

    public LongLat(double longitude2, double latitude2) {
        longitude = longitude2;
        latitude = latitude2;
    }

    public double calcDistance(LongLat ll) {
        return Math.acos(Math.sin(latitude / 57.29577951)*Math.sin(ll.latitude / 57.29577951)+Math.cos(latitude / 57.29577951)*Math.cos(ll.latitude / 57.29577951)*Math.cos(ll.longitude / 57.29577951-longitude / 57.29577951))*3963.0;
    }

    public static double calcVel(double timeInitial, double timeFinal, double distance) {
        return (distance * 1000) / (getTimeDifSecs(timeInitial, timeFinal) / 1000);
    }

    public static double getTimeDifSecs(double initialMillis, double finalMillis) {
        return finalMillis - initialMillis;
    }

}
