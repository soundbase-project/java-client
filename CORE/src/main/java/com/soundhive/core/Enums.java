package com.soundhive.core;

public class Enums {

    public enum License{
        ALL_RIGHTS_RESERVED("All right reserved"),
        CC("Creative Common");

        final private String label;

        License(String label) {
            this.label = label;
        }

        public String toString() {
            return label;
        }
    }

    public enum Visibility{
        PUBLIC("public"),
        FOLLOWERS("followers");

        final private String label;

        Visibility(String label) {
            this.label = label;
        }

        public String toString() {
            return label;
        }
    }

    public enum Timespan {
        //YEAR("year"),
        MONTH("month"),
        WEEK("week"),
        DAY("day"),
        HOUR("hour");

        final private String label;

        Timespan(String label) {
            this.label = label;
        }

        public String toString() {
            return label;
        }
    }

    public enum Scope {
        TRACKS("tracks"),
        USER("users");
        final private String label;

        Scope(String label) {
            this.label = label;
        }

        public String toString() {
            return label;
        }
    }
}
