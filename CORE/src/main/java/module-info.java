module CORE {
    requires transitive unirest.java;
    exports com.soundhive.core.response;
    exports com.soundhive.core.stats;
    exports com.soundhive.core.authentication;
    exports com.soundhive.core.conf;
    exports com.soundhive.core.generic;
    exports com.soundhive.core.tracks;
    exports com.soundhive.core.upload;
}