/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.entity.datasync;

import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;

import javax.annotation.Nonnull;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * A generic data serializer class that makes registering data serializers far cleaner
 * @author jbred
 *
 */
public class DataSerializerGeneric<T> implements DataSerializer<T>
{
    @Nonnull protected final BiConsumer<PacketBuffer, T> writer;
    @Nonnull protected final Function<PacketBuffer, T> reader;
    @Nonnull protected final Function<T, T> copier;

    public DataSerializerGeneric(@Nonnull BiConsumer<PacketBuffer, T> writerIn, @Nonnull Function<PacketBuffer, T> readerIn) {
        this(writerIn, readerIn, value -> value);
    }

    public DataSerializerGeneric(@Nonnull BiConsumer<PacketBuffer, T> writerIn, @Nonnull Function<PacketBuffer, T> readerIn, @Nonnull Function<T, T> copierIn) {
        writer = writerIn;
        reader = readerIn;
        copier = copierIn;
    }

    @Override
    public void write(@Nonnull PacketBuffer buf, @Nonnull T value) { writer.accept(buf, value); }

    @Nonnull
    @Override
    public T read(@Nonnull PacketBuffer buf) { return reader.apply(buf); }

    @Nonnull
    @Override
    public DataParameter<T> createKey(int id) { return new DataParameter<>(id, this); }

    @Nonnull
    @Override
    public T copyValue(@Nonnull T value) { return copier.apply(value); }
}
