package pt.uminho.braguia.trail.domain;

public enum MediaType {
    IMAGE,
    RECORD,
    VIDEO;

    static MediaType of(String type) {
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
