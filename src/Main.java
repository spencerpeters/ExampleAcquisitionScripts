import mmcorej.CMMCore;
import mmcorej.StrVector;

public class Main {

    public static void main(String[] args) {
        CMMCore core = new CMMCore();
        String info = core.getVersionInfo();
        System.out.println(info);

        try {
            loadMarzhauserStage(core);
//            loadCanonCamera(core);

            core.initializeAllDevices();

            printProperties(core, "XYStage");
//            printProperties(core, "Camera");

            core.setXYStageDevice("XYStage");
//            core.setCameraDevice("Camera");

            core.setRelativeXYPosition(1000, 1000);

//            core.snapImage();
//            core.getImage();   // this returns an array, figure out the type


        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
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

    public static void printProperties(mmcorej.CMMCore core, String deviceLabel) throws Exception {
        StrVector properties = core.getDevicePropertyNames(deviceLabel);
        for (int i=0; i<properties.size(); i++) {
            String prop = properties.get(i);
            String val = core.getProperty(deviceLabel, prop);
            System.out.println("Name: " + prop + ", value: " + val);
        }
    }
}

