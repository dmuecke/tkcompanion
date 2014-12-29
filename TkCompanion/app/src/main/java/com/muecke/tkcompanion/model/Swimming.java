package com.muecke.tkcompanion.model;

import java.io.Serializable;

public class Swimming implements Serializable {
    public enum SwimStyle {
        FREE(0,"Freestyle","F"),
        BACKSTROKE(1,"Backstroke","R"),
        BREASTSTROKE(2,"Breaststroke","B"),
        BUTTERFLY(3,"Butterfly","S"),
        IM(4,"IM","L");

        private final String desc;
        private final String shortDesc;
        private int value;

        SwimStyle(int value, String desc, String shortDesc) {
            this.value = value;
            this.desc=desc;
            this.shortDesc = shortDesc;
        }

        public int getValue() {
            return value;
        }

        public String getDescription() {
            return desc;
        }
        public String getSortDesc() {
            return shortDesc;
        }

        public SwimStyle find(String s) {
            for (SwimStyle swimStyle : SwimStyle.values()) {
                if (s.equalsIgnoreCase(swimStyle.getSortDesc())) {
                    return swimStyle;
                }
            }

            return null;
        }
    }

    public enum Distance {
        SC25M(25,"25"),
        SC50M(50,"50"),
        SC75M(50,"75"),
        SC100M(100,"100"),
        SC125M(125,"125"),
        SC200M(200,"200"),
        SC400(400,"400"),
        SC800M(800,"800"),
        SC1500(1500,"1500"),
        SC4X50(50,"4x50");

        private String desc;
        private int value;

        Distance(int value,String desc) {

            this.value = value;
            this.desc = desc;
        }

        public int getValue() {
            return value;
        }

        public Distance find(String s) {
            for (Distance distance : Distance.values()) {
                if (s.equals(distance.getDesc())) {
                    return distance;
                }
            }
            return null;
        }

        public String getDesc() {
            return desc;
        }
    }

    public enum IntervalLength {
        SC25M(25,"25m"),
        SC50M(50,"50m"),
        SC75M(75,"75m"),
        SC100(100,"100m");

        private final String desc;
        private int value;

        IntervalLength(int value, String desc) {
            this.value = value;
            this.desc=desc;
        }

        public int getValue() {
            return value;
        }

        public String getDescription() {
            return desc;
        }
    }
}
