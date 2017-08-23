import mmcorej.CMMCore;
import mmcorej.StrVector;

public class Main {

    public static void main(String[] args) {
        CMMCore core = new CMMCore();
        String info = core.getVersionInfo();
        System.out.println(info);

        try {
            //core.loadDevice("XYStage", "Marzhauser", "EK-Mot 14 XY Stage");
            core.loadDevice("Camera", "DSLRRemoteCamera", "CanonDSLRCam");

            core.initializeAllDevices();

            core.setCameraDevice("Camera");

            System.out.println(core.getTimeoutMs());
            core.setTimeoutMs(20000);

            printProperties(core, "Camera");

//            core.setExposure(50);
            core.snapImage();
            core.getImage();
//
//            if (core.getBytesPerPixel() == 3) {
//
//            }

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

