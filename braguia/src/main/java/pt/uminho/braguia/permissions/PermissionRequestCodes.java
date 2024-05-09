package pt.uminho.braguia.permissions;

public enum PermissionRequestCodes {

    CALL_PHONE_PERMISSION_REQUEST_CODE(100),
    CALL_PRIVILEGED_PERMISSION_REQUEST_CODE(101),
    READ_CONTACTS_PERMISSION_REQUEST_CODE(102),
    ACCESS_FINE_LOCATION_PERMISSION_REQUEST_CODE(103),
    ACCESS_COARSE_LOCATION_PERMISSION_REQUEST_CODE(104);

    private final int value;

    private PermissionRequestCodes(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
