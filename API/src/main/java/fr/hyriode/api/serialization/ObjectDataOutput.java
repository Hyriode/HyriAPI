package fr.hyriode.api.serialization;

import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.DataOutput;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by AstFaster
 * on 16/02/2023 at 21:21
 */
public interface ObjectDataOutput extends DataOutput, Closeable {

    void writeUUID(@NotNull UUID uuid) throws IOException;

    @Deprecated
    void writeUTF(@NotNull String string) throws IOException;

    void writeString(String string) throws IOException;

    void writeByteArray(byte[] values) throws IOException;

    void writeBooleanArray(boolean[] values) throws IOException;

    void writeCharArray(char[] values) throws IOException;

    void writeShortArray(short[] values) throws IOException;

    void writeIntArray(int[] values) throws IOException;

    void writeLongArray(long[] values) throws IOException;

    void writeDoubleArray(double[] values) throws IOException;

    void writeFloatArray(float[] values) throws IOException;

    @Deprecated
    void writeUTFArray(String[] values) throws IOException;

    void writeStringArray(String[] values) throws IOException;

    byte[] toByteArray();

}
