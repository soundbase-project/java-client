package com.soundhive.gui.sample;

import com.soundhive.core.authentication.SessionHandler;
import com.soundhive.core.response.Response;
import com.soundhive.core.samples.Sample;
import javafx.concurrent.Service;

import java.util.List;

public class SampleService extends Service<Response<List<Sample>>> {

    public final SessionHandler session;

    public SampleService(final SessionHandler session) {
        this.session = session;
    }

    @Override
    protected SampleTask createTask() {
        return new SampleTask(session);
    }
}
