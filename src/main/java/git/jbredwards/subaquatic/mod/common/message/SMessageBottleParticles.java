package git.jbredwards.subaquatic.mod.common.message;

import git.jbredwards.fluidlogged_api.api.network.IClientMessageHandler;
import git.jbredwards.fluidlogged_api.api.network.message.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public final class SMessageBottleParticles extends AbstractMessage
{
    public BlockPos pos;
    public int rgb;

    public SMessageBottleParticles() {}
    public SMessageBottleParticles(@Nonnull BlockPos posIn, int rgbIn) {
        pos = posIn;
        rgb = rgbIn;
        isValid = true;
    }

    @Override
    public void read(@Nonnull PacketBuffer buf) {
        pos = buf.readBlockPos();
        rgb = buf.readVarInt();
    }

    @Override
    public void write(@Nonnull PacketBuffer buf) {
        buf.writeBlockPos(pos);
        buf.writeVarInt(rgb);
    }

    public enum Handler implements IClientMessageHandler<SMessageBottleParticles>
    {
        INSTANCE;

        @SideOnly(Side.CLIENT)
        @Override
        public void handleMessage(@Nonnull SMessageBottleParticles message) {
            final ParticleManager manager = Minecraft.getMinecraft().effectRenderer;
            for(int i = 0; i < 5; i++) {
                final Particle particle = manager.particleTypes.get(EnumParticleTypes.WATER_SPLASH.getParticleID()).createParticle(-1, Minecraft.getMinecraft().world,
                        message.pos.getX() + Math.random(), message.pos.getY() + 1, message.pos.getZ() + Math.random(), 0, 0, 0);

                if(particle != null) {
                    if(message.rgb != -1) particle.setRBGColorF((message.rgb >> 16 & 255) / 255f, (message.rgb >> 8 & 255) / 255f, (message.rgb & 255) / 255f);
                    manager.addEffect(particle);
                }
            }
        }
    }
}
