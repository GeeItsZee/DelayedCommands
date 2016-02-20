package com.gmail.tracebachi.DelayedCommands;

import com.google.common.base.Preconditions;

import java.util.Collections;
import java.util.List;

/**
 * Created by Trace Bachi (tracebachi@gmail.com, BigBossZee) on 2/20/16.
 */
public class CommandGroup
{
    private String label;
    private List<String> commands;

    public CommandGroup(String label, List<String> commands)
    {
        Preconditions.checkNotNull(label);
        Preconditions.checkNotNull(commands);
        Preconditions.checkArgument(commands.size() > 0);

        this.label = label;
        this.commands = Collections.unmodifiableList(commands);
    }

    public String getLabel()
    {
        return label;
    }

    public List<String> getCommands()
    {
        return commands;
    }
}
