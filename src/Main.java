import mmcorej.CMMCore;
import mmcorej.StrVector;

public class Main {

    public static void main(String[] args) {
        CMMCore core = new CMMCore();
        String info = core.getVersionInfo();
        System.out.println(info);

        try {
            core.loadDevice("StagePort", "SerialManager", "COM4");
            core.setProperty("StagePort", "StopBits", "2");

            core.loadDevice("XYStage", "Marzhauser", "XYStage");
            core.setProperty("XYStage", "Port", "StagePort");
            printProperties(core, "XYStage");
            core.initializeAllDevices();

            core.setXYStageDevice("XYStage");

            core.setRelativeXYPosition(1000, 1000);
//            core.loadDevice("Camera", "DSLRRemoteCamera", "CanonDSLRCam");
//
//            core.initializeAllDevices();
//
//            core.setCameraDevice("Camera");
//
////            printProperties(core, "Camera");
//
////            core.setExposure(50);  // This line triggers an error because the camera's "Shutter Speeds" is not set
//            core.snapImage();
//            core.getImage();   // this returns an array, figure out the type


        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void printProperties(mmcorej.CMMCore core, String deviceLabel) throws Exception {
        StrVector properties = core.getDevicePropertyNames(deviceLabel);
        for (int i=0; i<properties.size(); i++) {
            String prop = properties.get(i);
            String val = core.getProperty(deviceLabel, prop);
            System.out.println("Name: " + prop + ", value: " + val);
        }
    }
}

