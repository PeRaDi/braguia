package pt.uminho.braguia.pins.domain;

public enum PinMediaType {
    IMAGE,
    RECORD,
    VIDEO;

    static PinMediaType of(String type) {
        switch (type) {
            case "I":
                return IMAGE;
            case "R":
                return RECORD;
            case "V":
                return VIDEO;
            default:
                throw new RuntimeException(String.format("Invalid media type: %s", type));
        }
    }
}
