package fr.hyriode.api.serialization;

import java.io.Closeable;
import java.io.DataInput;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by AstFaster
 * on 16/02/2023 at 21:12
 */
public interface ObjectDataInput extends DataInput, Closeable {

    UUID readUUID() throws IOException;

    @Deprecated
    String readUTF() throws IOException;

    String readString() throws IOException;

    byte[] readByteArray() throws IOException;

    boolean[] readBooleanArray() throws IOException;

    char[] readCharArray() throws IOException;

    short[] readShortArray() throws IOException;

    int[] readIntArray() throws IOException;

    long[] readLongArray() throws IOException;

    double[] readDoubleArray() throws IOException;

    float[] readFloatArray() throws IOException;

    @Deprecated
    String[] readUTFArray() throws IOException;

    String[] readStringArray() throws IOException;

}
