package pt.uminho.braguia.models;

import java.util.List;

public class Trail {

    private int id;
    private String trail_img;
    private List<RelTrail> rel_trail;
    private List<Edge> edges;
    private String trail_name;
    private String trail_desc;
    private int trail_duration;
    private String trail_difficulty;

    public class RelTrail {
        private int id;
        private String value;
        private String attrib;
        private int trail;
    }

    public class Edge {
        private int id;
        private Pin edge_start;
        private Pin edge_end;
        private String edge_transport;
        private int edge_duration;
        private String edge_desc;
        private int edge_trail;

        public class Pin {
            private int id;
            private List<RelPin> rel_pin;
            private List<Media> media;
            private String pin_name;
            private String pin_desc;
            private double pin_lat;
            private double pin_lng;
            private double pin_alt;

            public class RelPin {
                private int id;
                private String value;
                private String attrib;
                private int pin;
            }

            public class Media {
                private int id;
                private String media_file;
                private String media_type;
                private int media_pin;
            }
        }
    }
}