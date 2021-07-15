package fr.hyriode.hyriapi.tools.sign;

import fr.hyriode.hyriapi.tools.util.PacketUtil;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftSign;
import org.bukkit.entity.Player;

public class Sign {

    private String[] lines;

    private final SignCompleteCallback completeCallback;

    private final SignManager signManager;

    public Sign(SignManager signManager, SignCompleteCallback completeCallback) {
        this.signManager = signManager;
        this.completeCallback = completeCallback;
    }

    public Sign withLines(String... lines) {
        if (lines.length == 4) {
            this.lines = lines;
        } else {
            throw new IllegalArgumentException("Signs must have 4 lines !");
        }
        return this;
    }

    public void open(Player player) {
        final Location location = player.getLocation();
        final BlockPosition blockPosition = new BlockPosition(location.getBlockX(), 1, location.getBlockZ());
        final PacketPlayOutBlockChange blockChangePacket = new PacketPlayOutBlockChange(((CraftWorld) player.getWorld()).getHandle(), blockPosition);

        blockChangePacket.block = Blocks.STANDING_SIGN.getBlockData();

        PacketUtil.sendPacket(player, blockChangePacket);

        final IChatBaseComponent[] components = CraftSign.sanitizeLines(this.lines);
        final TileEntitySign sign = new TileEntitySign();

        sign.a(new BlockPosition(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ()));

        System.arraycopy(components, 0, sign.lines, 0, sign.lines.length);

        PacketUtil.sendPacket(player, sign.getUpdatePacket());

        final PacketPlayOutOpenSignEditor openSignEditorPacket = new PacketPlayOutOpenSignEditor(blockPosition);

        PacketUtil.sendPacket(player, openSignEditorPacket);

        this.signManager.addGUI(player.getUniqueId(), this);
    }

    protected SignCompleteCallback getCompleteCallback() {
        return this.completeCallback;
    }

}
