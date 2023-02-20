package fr.hyriode.api.impl.common.serialization;

import fr.hyriode.api.serialization.ObjectDataInput;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static fr.hyriode.api.impl.common.serialization.DataSerializerImpl.NULL_ARRAY_LENGTH;

/**
 * Created by AstFaster
 * on 16/02/2023 at 21:38
 */
public class ObjectDataInputStream implements ObjectDataInput {

    private final DataInputStream inputStream;

    public ObjectDataInputStream(byte[] bytes) {
        this.inputStream = new DataInputStream(new ByteArrayInputStream(bytes));
    }

    @Override
    public void readFully(byte @NotNull [] b) throws IOException {
        this.inputStream.readFully(b);
    }

    @Override
    public void readFully(byte @NotNull [] b, int off, int len) throws IOException {
        this.inputStream.readFully(b, off, len);
    }

    @Override
    public int skipBytes(int n) throws IOException {
        return this.inputStream.skipBytes(n);
    }

    @Override
    public boolean readBoolean() throws IOException {
        return this.inputStream.readBoolean();
    }

    @Override
    public byte readByte() throws IOException {
        return this.inputStream.readByte();
    }

    @Override
    public int readUnsignedByte() throws IOException {
        return this.inputStream.readUnsignedByte();
    }

    @Override
    public short readShort() throws IOException {
        return this.inputStream.readShort();
    }

    @Override
    public int readUnsignedShort() throws IOException {
        return this.inputStream.readUnsignedShort();
    }

    @Override
    public char readChar() throws IOException {
        return this.inputStream.readChar();
    }

    @Override
    public int readInt() throws IOException {
        return this.inputStream.readInt();
    }

    @Override
    public long readLong() throws IOException {
        return this.inputStream.readLong();
    }

    @Override
    public float readFloat() throws IOException {
        return this.inputStream.readFloat();
    }

    @Override
    public double readDouble() throws IOException {
        return this.inputStream.readDouble();
    }

    @Deprecated
    @Override
    public String readLine() throws IOException {
        return this.inputStream.readLine();
    }

    @Override
    public UUID readUUID() throws IOException {
        final long mostSigBits = this.readLong();

        if (mostSigBits == NULL_ARRAY_LENGTH) {
            return null;
        }

        return new UUID(mostSigBits, this.readLong());
    }

    @Override
    public String readUTF() throws IOException {
        return this.inputStream.readUTF();
    }

    @Override
    public String readString() throws IOException {
        final int length = this.readInt();

        if (length == NULL_ARRAY_LENGTH) {
            return null;
        }

        final byte[] bytes = new byte[length];

        this.readFully(bytes);

        return new String(bytes, StandardCharsets.UTF_8);
    }

    @Override
    public byte[] readByteArray() throws IOException {
        final int length = this.readInt();

        if (length == NULL_ARRAY_LENGTH) {
            return null;
        }

        final byte[] bytes = new byte[length];

        this.readFully(bytes);

        return bytes;
    }

    @Override
    public boolean[] readBooleanArray() throws IOException {
        final int length = this.readInt();

        if (length == NULL_ARRAY_LENGTH) {
            return null;
        }

        if (length > 0) {
            final boolean[] booleans = new boolean[length];

            for (int i = 0; i < length; i++) {
                booleans[i] = this.readBoolean();
            }
            return booleans;
        }
        return new boolean[0];
    }

    @Override
    public char[] readCharArray() throws IOException {
        final int length = this.readInt();

        if (length == NULL_ARRAY_LENGTH) {
            return null;
        }

        if (length > 0) {
            final char[] chars = new char[length];

            for (int i = 0; i < length; i++) {
                chars[i] = this.readChar();
            }
            return chars;
        }
        return new char[0];
    }

    @Override
    public short[] readShortArray() throws IOException {
        final int length = this.readInt();

        if (length == NULL_ARRAY_LENGTH) {
            return null;
        }

        if (length > 0) {
            final short[] shorts = new short[length];

            for (int i = 0; i < length; i++) {
                shorts[i] = this.readShort();
            }
            return shorts;
        }
        return new short[0];
    }

    @Override
    public int[] readIntArray() throws IOException {
        final int length = this.readInt();

        if (length == NULL_ARRAY_LENGTH) {
            return null;
        }

        if (length > 0) {
            final int[] ints = new int[length];

            for (int i = 0; i < length; i++) {
                ints[i] = this.readInt();
            }
            return ints;
        }
        return new int[0];
    }

    @Override
    public long[] readLongArray() throws IOException {
        final int length = this.readInt();

        if (length == NULL_ARRAY_LENGTH) {
            return null;
        }

        if (length > 0) {
            final long[] longs = new long[length];

            for (int i = 0; i < length; i++) {
                longs[i] = this.readLong();
            }
            return longs;
        }
        return new long[0];
    }

    @Override
    public double[] readDoubleArray() throws IOException {
        final int length = this.readInt();

        if (length == NULL_ARRAY_LENGTH) {
            return null;
        }

        if (length > 0) {
            final double[] doubles = new double[length];

            for (int i = 0; i < length; i++) {
                doubles[i] = this.readDouble();
            }
            return doubles;
        }
        return new double[0];
    }

    @Override
    public float[] readFloatArray() throws IOException {
        final int length = this.readInt();

        if (length == NULL_ARRAY_LENGTH) {
            return null;
        }

        if (length > 0) {
            final float[] floats = new float[length];

            for (int i = 0; i < length; i++) {
                floats[i] = this.readFloat();
            }
            return floats;
        }
        return new float[0];
    }

    @Override
    public String[] readUTFArray() throws IOException {
        return this.readStringArray();
    }

    @Override
    public String[] readStringArray() throws IOException {
        final int length = this.readInt();

        if (length == NULL_ARRAY_LENGTH) {
            return null;
        }

        if (length > 0) {
            final String[] strings = new String[length];

            for (int i = 0; i < length; i++) {
                strings[i] = this.readString();
            }
            return strings;
        }
        return new String[0];
    }

    @Override
    public void close() throws IOException {
        this.inputStream.close();
    }

}
