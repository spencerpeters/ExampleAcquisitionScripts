import mmcorej.CMMCore;
import mmcorej.StrVector;

public class Main {

    // Coordinates read off manually for the upper right corner of the slide
    public static final double upperRightCornerX = 73381.7;
    public static final double UpperRightCornerY = 4556.3;

    // Loads, initializes and tests the stage and camera using the MicroManager Core API
    public static void main(String[] args) {
        // Controls whether the camera and stage, or just the stage, is tested
        boolean testCamera = false;
        // Get a reference to the MicroManager Core object, which has all the core API device control methods
        CMMCore core = new CMMCore();

        // Print version info
        String info = core.getVersionInfo();
        System.out.println(info);

        // Try loading, initializing, and controlling the stage and camera
        try {
            loadMarzhauserStage(core);
            if (testCamera) loadCanonCamera(core);

            core.initializeAllDevices();

            printProperties(core, "XYStage");
            if (testCamera) printProperties(core, "Camera");

            core.setXYStageDevice("XYStage");
            if (testCamera) core.setCameraDevice("Camera");

            // TODO
            home(core, "StagePort", "XYStage");

//            setLimitSwitches(core, "StagePort", "XYStage");

            System.out.println(core.getYPosition());
            core.waitForDevice("XYStage");
            core.setXYPosition(0, 29050);
            core.waitForDevice("XYStage");

            System.out.println(core.getYPosition());

//            core.snapImage();
//            core.getImage();   // this returns an array, figure out the type SAVE!!!

        // Default exception handling (any exception handling logic can go here)
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void home(CMMCore core, String stageSerialPortLabel, String stageLabel) throws Exception {
        core.setSerialPortCommand(stageSerialPortLabel, "!cal", "\r");
        core.waitForDevice(stageLabel);
    }

    public static void setLimitSwitches(CMMCore core, String stageSerialPortLabel, String stageLabel) throws Exception{
        String lowerLimit = "0.0";
        String upperLimit = "29000.0";
        core.setSerialPortCommand(stageSerialPortLabel, "!lim y" + " " + lowerLimit + " " + upperLimit, "\r");
        // This is worthless, does absolutely 0
        // in fact, no, it appears to UNDO the setting of the limit switches in Switchboard, what the freaking hell
        core.waitForDevice(stageLabel);
    }

    public static void loadCanonCamera(CMMCore core) throws Exception {
        core.loadDevice("Camera", "DSLRRemoteCamera", "CanonDSLRCam");
    }

    public static void loadMarzhauserStage(CMMCore core) throws Exception {
        core.loadDevice("StagePort", "SerialManager", "COM4");
        core.setProperty("StagePort", "StopBits", "2");

        core.loadDevice("XYStage", "Marzhauser", "XYStage");
        core.setProperty("XYStage", "Port", "StagePort");
    }

    // Print out the names and values of all the properties of the device labeled deviceLabel
    public static void printProperties(mmcorej.CMMCore core, String deviceLabel) throws Exception {
        StrVector properties = core.getDevicePropertyNames(deviceLabel);
        for (int i=0; i<properties.size(); i++) {
            String prop = properties.get(i);
            String val = core.getProperty(deviceLabel, prop);
            System.out.println("Name: " + prop + ", value: " + val);
        }
    }
}

