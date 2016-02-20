package com.gmail.tracebachi.DelayedCommands;

/**
 * Created by Trace Bachi (tracebachi@gmail.com, BigBossZee) on 2/20/16.
 */
public interface DelayUtil
{
    static boolean isDelay(String input)
    {
        return input.startsWith("(") && input.endsWith(")");
    }

    static Integer parseDelay(String input)
    {
        try
        {
            String substring = input.substring(1, input.length() - 1);
            return substring.length() == 0 ? null : Integer.parseInt(substring);
        }
        catch(NumberFormatException e)
        {
            return null;
        }
    }
}
