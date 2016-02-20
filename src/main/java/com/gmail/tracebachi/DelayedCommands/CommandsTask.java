package com.gmail.tracebachi.DelayedCommands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.logging.Logger;

import static com.gmail.tracebachi.DelayedCommands.DelayUtil.*;

/**
 * Created by Trace Bachi (tracebachi@gmail.com, BigBossZee) on 2/20/16.
 */
public class CommandsTask extends BukkitRunnable
{
    private int index = 0;
    private int delayCounter = 0;
    private String sender;
    private CommandGroup commandGroup;
    private DelayedCommands plugin;

    public CommandsTask(String sender, CommandGroup commandGroup, DelayedCommands plugin)
    {
        this.sender = sender;
        this.commandGroup = commandGroup;
        this.plugin = plugin;
    }

    @Override
    public void run()
    {
        if(delayCounter > 0)
        {
            delayCounter--;

            if(delayCounter <= 0)
            {
                index++;
            }

            return;
        }

        List<String> commands = commandGroup.getCommands();
        Logger logger = plugin.getLogger();

        if(index != commands.size())
        {
            String command = commands.get(index);

            if(isDelay(command))
            {
                Integer delay = parseDelay(command);

                if(delay != null && delay > 0)
                {
                    delayCounter = delay;
                }
                else
                {
                    logger.severe("Invalid delay in " +
                        "{group: " + commandGroup.getLabel() +
                        ", command: " + command + "}");
                }

                return;
            }

            if(sender.equals("CONSOLE"))
            {
                logger.info("Console ran /" + command);
                index++;

                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
            }
            else
            {
                Player player = Bukkit.getPlayer(sender);

                if(player != null)
                {
                    logger.info(player.getName() + " ran /" + command);
                    index++;

                    player.performCommand(command);
                }
                else
                {
                    logger.info(sender + " logged off while executing " +
                        "{group: " + commandGroup.getLabel() +
                        ", command: " + command + "}");
                    cancel();
                }
            }
        }
        else
        {
            logger.info("Completed executing command group: " +
                "{group: " + commandGroup.getLabel() + "}");
            cancel();
        }
    }
}
