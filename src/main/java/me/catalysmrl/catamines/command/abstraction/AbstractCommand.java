package me.catalysmrl.catamines.command.abstraction;

import me.catalysmrl.catamines.CataMines;
import me.catalysmrl.catamines.command.utils.ArgumentException;
import me.catalysmrl.catamines.command.utils.CommandContext;
import me.catalysmrl.catamines.command.utils.CommandException;

import org.bukkit.command.CommandSender;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * Simple abstract command
 */
public abstract class AbstractCommand implements Command {

    private final String name;
    private final String permission;
    private final Predicate<Integer> argumentCheck;
    private final boolean onlyPlayers;

    public AbstractCommand(String name, String permission, Predicate<Integer> argumentCheck, boolean onlyPlayers) {
        this.name = name;
        this.permission = permission;
        this.argumentCheck = argumentCheck;
        this.onlyPlayers = onlyPlayers;
    }

    @Override
    public abstract void execute(CataMines plugin, CommandSender sender, CommandContext context) throws CommandException;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Optional<String> getPermission() {
        return Optional.ofNullable(permission);
    }

    @Override
    public Predicate<Integer> checkArgLength() {
        return argumentCheck;
    }

    @Override
    public boolean onlyPlayers() {
        return onlyPlayers;
    }

    @Override
    public boolean isAuthorized(CommandSender sender) {
        return permission == null || sender.hasPermission(permission);
    }

    protected void assertArgLength(CommandContext ctx) throws ArgumentException.Usage {
        if (!checkArgLength().test(ctx.remaining())) throw new ArgumentException.Usage();
    }
}
