package su.nexmedia.auth.command.admin;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.auth.NexAuth;
import su.nexmedia.auth.Perms;
import su.nexmedia.auth.Placeholders;
import su.nexmedia.auth.config.Lang;
import su.nexmedia.engine.api.command.AbstractCommand;
import su.nexmedia.engine.api.command.CommandResult;
import su.nexmedia.engine.utils.CollectionsUtil;

import java.util.List;

public class ResetSecretCommand extends AbstractCommand<NexAuth> {

    public ResetSecretCommand(@NotNull NexAuth plugin) {
        super(plugin, new String[]{"resetsecret"}, Perms.COMMAND_ADMIN);
        this.setDescription(plugin.getMessage(Lang.COMMAND_ADMIN_RESETSECRET_DESC));
        this.setUsage(plugin.getMessage(Lang.COMMAND_ADMIN_RESETSECRET_USAGE));
    }

    @Override
    @NotNull
    public List<String> getTab(@NotNull Player player, int arg, @NotNull String[] args) {
        if (arg == 1) {
            return CollectionsUtil.playerNames(player);
        }
        return super.getTab(player, arg, args);
    }

    @Override
    public void onExecute(@NotNull CommandSender sender, @NotNull CommandResult result) {
        if (result.length() != 2) {
            this.printUsage(sender);
            return;
        }

        this.plugin.getUserManager().getUserDataAsync(result.getArg(1)).thenAccept(user -> {
            if (user == null) {
                this.errorPlayer(sender);
                return;
            }

            user.getSecretKey().reset();
            plugin.getUserManager().saveUser(user);

            plugin.getMessage(Lang.COMMAND_ADMIN_RESETSECRET_DONE)
                .replace(Placeholders.PLAYER_NAME, user.getName())
                .send(sender);
        });
    }
}
