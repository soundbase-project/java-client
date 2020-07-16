package com.soundhive.core;

public class GenericDeclarations {

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
}
