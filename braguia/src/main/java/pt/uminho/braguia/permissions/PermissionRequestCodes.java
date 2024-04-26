package pt.uminho.braguia.permissions;

public enum PermissionRequestCodes {
    READ_CONTACTS_PERMISSION_REQUEST_CODE(101);

    private final int value;

    private PermissionRequestCodes(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
