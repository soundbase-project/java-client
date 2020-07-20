package com.soundhive.cli.stats;

import com.soundhive.core.Enums;
import picocli.CommandLine;

@CommandLine.Command(
        name = "stats",
        description = "Stats for currently connected user."
)
public class StatsCommand implements Runnable{

    @CommandLine.Option(names = {"-s", "--timespan"},
            paramLabel = "target",
            description = "Timespan unit : ${COMPLETION-CANDIDATES}",
            type = {Enums.Timespan.class})
    Enums.Timespan timespan = null;

    @CommandLine.Option(names = {"-n", "--number"},
            paramLabel = "target",
            description = "How much timespan unit to include",
            type = {Enums.Scope.class})
    int number;

    @Override
    public void run() {
        System.out.println(String.format("Your stats for the %d last %s.", number, timespan.toString()));
    }
}
