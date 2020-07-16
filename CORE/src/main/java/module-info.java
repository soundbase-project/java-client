module CORE {
    requires transitive unirest.java;
    requires transitive javatuples;
    exports com.soundhive.core.response;
    exports com.soundhive.core.stats;
    exports com.soundhive.core.authentication;
    exports com.soundhive.core.conf;
    exports com.soundhive.core.generic;
    exports com.soundhive.core.tracks;
    exports com.soundhive.core.upload;
    exports com.soundhive.core;
    exports com.soundhive.core.samples;
}