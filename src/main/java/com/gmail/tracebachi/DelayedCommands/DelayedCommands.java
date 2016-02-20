package com.gmail.tracebachi.DelayedCommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Trace Bachi (tracebachi@gmail.com, BigBossZee) on 2/20/16.
 */
public class DelayedCommands extends JavaPlugin
{
    private Map<String, CommandGroup> groupMap;

    @Override
    public void onLoad()
    {
        saveDefaultConfig();
    }

    @Override
    public void onEnable()
    {
        reloadConfig();
        parseCommandGroups();
    }

    @Override
    public void onDisable()
    {
        getServer().getScheduler().cancelTasks(this);

        groupMap.clear();
        groupMap = null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(!sender.hasPermission("DelayedCommands"))
        {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return true;
        }

        if(command.getLabel().equalsIgnoreCase("rundc"))
        {
            if(args.length == 0)
            {
                sender.sendMessage(ChatColor.RED + "/rundc <name>");
                return true;
            }

            String groupName = args[0];
            CommandGroup group = groupMap.get(groupName.toLowerCase());

            if(group == null)
            {
                sender.sendMessage(ChatColor.RED + "Invalid command group: " + groupName);
            }
            else
            {
                CommandsTask task = new CommandsTask(sender.getName(), group, this);

                sender.sendMessage(ChatColor.GREEN + "Running command group: " + groupName);
                task.runTaskTimer(this, 1, 1);
            }
        }
        else if(command.getLabel().equalsIgnoreCase("dcreload"))
        {
            getServer().getScheduler().cancelTasks(this);

            groupMap.clear();
            groupMap = null;

            reloadConfig();
            parseCommandGroups();

            sender.sendMessage(ChatColor.GREEN + "Command groups reloaded");
        }

        return true;
    }

    private void parseCommandGroups()
    {
        groupMap = new HashMap<>();

        for(String label : getConfig().getKeys(false))
        {
            List<String> stringList = getConfig().getStringList(label);

            if(isValidCommandList(label, stringList))
            {
                CommandGroup group = new CommandGroup(label, stringList);
                groupMap.put(label.toLowerCase(), group);
            }
        }
    }

    private boolean isValidCommandList(String label, List<String> commands)
    {
        boolean isValid = true;

        for(String command : commands)
        {
            if(DelayUtil.isDelay(command))
            {
                Integer result = DelayUtil.parseDelay(command);

                if(result == null)
                {
                    getLogger().severe("Error in command group {name: " + label + "}: " +
                        "\"" + command + "\" is not a valid delay.");

                    isValid = false;
                }
                else if(result <= 0)
                {
                    getLogger().severe("Error in command group {name: " + label + "}: " +
                        "\"" + command + "\" must be greater than 0.");

                    isValid = false;
                }
            }
        }

        return isValid;
    }
}
