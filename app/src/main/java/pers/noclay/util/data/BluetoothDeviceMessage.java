package pers.noclay.util.data;

/**
 * Created by 82661 on 2016/9/24.
 */
public class BluetoothDeviceMessage {
    private String name;
    private String address;
    private boolean isPaired;

    public BluetoothDeviceMessage(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isPaired() {
        return isPaired;
    }

    public void setPaired(boolean paired) {
        isPaired = paired;
    }
}
