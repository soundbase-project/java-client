package com.soundhive.gui.sample;


import com.soundhive.core.authentication.SessionHandler;
import com.soundhive.core.response.Response;
import com.soundhive.core.samples.Sample;
import com.soundhive.core.samples.SampleQueries;
import javafx.concurrent.Task;

import java.util.List;

public class SampleTask extends Task<Response<List<Sample>>> {

    final SessionHandler session;


    public SampleTask(final SessionHandler session){
        this.session = session;
    }

    @Override
    protected Response<List<Sample>> call() {
        return SampleQueries.queryUserSamples(this.session);
    }
}
