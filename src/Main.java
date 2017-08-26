import mmcorej.CMMCore;
import mmcorej.StrVector;

public class Main {

    // Coordinates read off manually
    public static final double upperRightCornerX = 73381.7;
    public static final double upperRightCornerY = 4556.3;
    public static final double lowerLeftCornerX = 0.0;
    public static final double lowerLeftCornerY = 29000.0;


    // Shutter speeds for Canon camers
    public static final String shutterSpeeds = "30;25;20;15;13;10;8;6;5;4;3.2;2.5;2;1.6;1.3;" +
            "1;0.8;0.6;0.5;0.4;0.3;1/4;1/5;1/6;1/8;1/10;1/13;1/15;1/20;1/25;1/30;1/40;1/50;" +
            "1/60;1/80;1/100;1/125;1/160;1/200;1/250;1/320;1/400;1/500;1/640;1/800;1/1000;" +
            "1/1250;1/1600;1/2000;1/2500;1/3200;1/4000;";

    // Loads, initializes and tests the stage and camera using the MicroManager Core API
    // Warning: this script does not set limit switches. That should be done before the script is run, in Switchboard.
    // The process is described in the Reference.
    public static void main(String[] args) {
        // Controls whether the camera and stage, or just the stage, is tested
        boolean testCamera = false;
        // Controls whether a motion to the upper limit switches, or cal
        // is performed using the serial port interface
        boolean doCal = false;
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
            if (testCamera) {
                core.setCameraDevice("Camera");
                core.setProperty("Camera", "ShutterSpeeds", shutterSpeeds);
                core.setExposure(5);
            }

            // Everything is initialized! Now the stage and camera can be controlled.

            // Home the stage
            if (doCal) home(core, "StagePort", "XYStage");

            // Play around with the stage!
            core.setXYPosition(lowerLeftCornerX, lowerLeftCornerY);
            System.out.println(core.getXYStagePosition());
            core.waitForDevice("XYStage");
            core.setXYPosition(lowerLeftCornerX, upperRightCornerY);
            core.waitForDevice("XYStage");
            System.out.println(core.getXYStagePosition());
            core.setXYPosition(upperRightCornerX, upperRightCornerY);
            core.waitForDevice("XYStage");
            System.out.println(core.getXYStagePosition());
            core.setXYPosition(upperRightCornerX, lowerLeftCornerY);
            core.waitForDevice("XYStage");
            System.out.println(core.getXYStagePosition());
            core.setXYPosition(lowerLeftCornerX, lowerLeftCornerY);
            core.waitForDevice("XYStage");
            System.out.println(core.getXYStagePosition());

            // Play around with the camera
            if (testCamera) {
                core.snapImage();

                // Save the images
                byte[] imageAsBytaArray = (byte[]) core.getImage();
            }

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
        System.out.println("Properties of device " + deviceLabel);
        StrVector properties = core.getDevicePropertyNames(deviceLabel);
        for (int i=0; i<properties.size(); i++) {
            String prop = properties.get(i);
            String val = core.getProperty(deviceLabel, prop);
            System.out.println("Name: " + prop + ", value: " + val);
        }
    }
}

