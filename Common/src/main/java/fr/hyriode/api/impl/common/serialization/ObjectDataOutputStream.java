package fr.hyriode.api.impl.common.serialization;

import fr.hyriode.api.serialization.ObjectDataOutput;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static fr.hyriode.api.impl.common.serialization.DataSerializerImpl.NULL_ARRAY_LENGTH;

/**
 * Created by AstFaster
 * on 16/02/2023 at 21:43
 */
public class ObjectDataOutputStream implements ObjectDataOutput {

    private final ByteArrayOutputStream byteArrayOutputStream;
    private final DataOutputStream outputStream;

    public ObjectDataOutputStream() {
        this.byteArrayOutputStream = new ByteArrayOutputStream();
        this.outputStream = new DataOutputStream(this.byteArrayOutputStream);
    }

    @Override
    public void write(int b) throws IOException {
        this.outputStream.write(b);
    }

    @Override
    public void write(byte @NotNull [] b) throws IOException {
        this.outputStream.write(b);
    }

    @Override
    public void write(byte @NotNull [] b, int off, int len) throws IOException {
        this.outputStream.write(b, off, len);
    }

    @Override
    public void writeBoolean(boolean v) throws IOException {
        this.outputStream.writeBoolean(v);
    }

    @Override
    public void writeByte(int v) throws IOException {
        this.outputStream.writeByte(v);
    }

    @Override
    public void writeShort(int v) throws IOException {
        this.outputStream.writeShort(v);
    }

    @Override
    public void writeChar(int v) throws IOException {
        this.outputStream.writeChar(v);
    }

    @Override
    public void writeInt(int v) throws IOException {
        this.outputStream.writeInt(v);
    }

    @Override
    public void writeLong(long v) throws IOException {
        this.outputStream.writeLong(v);
    }

    @Override
    public void writeFloat(float v) throws IOException {
        this.outputStream.writeFloat(v);
    }

    @Override
    public void writeDouble(double v) throws IOException {
        this.outputStream.writeDouble(v);
    }

    @Override
    public void writeBytes(@NotNull String s) throws IOException {
        this.outputStream.writeBytes(s);
    }

    @Override
    public void writeChars(@NotNull String s) throws IOException {
        this.outputStream.writeChars(s);
    }

    @Override
    public void writeUUID(@NotNull UUID uuid) throws IOException {
        this.writeLong(uuid.getMostSignificantBits());
        this.writeLong(uuid.getLeastSignificantBits());
    }

    @Override
    public void writeUTF(@NotNull String string) throws IOException {
        this.writeString(string);
    }

    @Override
    public void writeString(String string) throws IOException {
        if (string == null) {
            this.writeInt(NULL_ARRAY_LENGTH);
            return;
        }

        final byte[] bytes = string.getBytes(StandardCharsets.UTF_8);

        this.writeInt(bytes.length);
        this.write(bytes);
    }

    @Override
    public void writeByteArray(byte[] values) throws IOException {
        if (values == null) {
            this.writeInt(NULL_ARRAY_LENGTH);
            return;
        }

        this.writeInt(values.length);
        this.write(values);
    }

    @Override
    public void writeBooleanArray(boolean[] values) throws IOException {
        if (values == null) {
            this.writeInt(NULL_ARRAY_LENGTH);
            return;
        }

        this.writeInt(values.length);

        for (boolean b : values) {
            this.writeBoolean(b);
        }
    }

    @Override
    public void writeCharArray(char[] values) throws IOException {
        if (values == null) {
            this.writeInt(NULL_ARRAY_LENGTH);
            return;
        }

        this.writeInt(values.length);

        for (char c : values) {
            this.writeChar(c);
        }
    }

    @Override
    public void writeShortArray(short[] values) throws IOException {
        if (values == null) {
            this.writeInt(NULL_ARRAY_LENGTH);
            return;
        }

        this.writeInt(values.length);

        for (short s : values) {
            this.writeShort(s);
        }
    }

    @Override
    public void writeIntArray(int[] values) throws IOException {
        if (values == null) {
            this.writeInt(NULL_ARRAY_LENGTH);
            return;
        }

        this.writeInt(values.length);

        for (int i : values) {
            this.writeInt(i);
        }
    }

    @Override
    public void writeLongArray(long[] values) throws IOException {
        if (values == null) {
            this.writeInt(NULL_ARRAY_LENGTH);
            return;
        }

        this.writeInt(values.length);

        for (long l : values) {
            this.writeLong(l);
        }
    }

    @Override
    public void writeDoubleArray(double[] values) throws IOException {
        if (values == null) {
            this.writeInt(NULL_ARRAY_LENGTH);
            return;
        }

        this.writeInt(values.length);

        for (double d : values) {
            this.writeDouble(d);
        }
    }

    @Override
    public void writeFloatArray(float[] values) throws IOException {
        if (values == null) {
            this.writeInt(NULL_ARRAY_LENGTH);
            return;
        }

        this.writeInt(values.length);

        for (float f : values) {
            this.writeFloat(f);
        }
    }

    @Override
    public void writeUTFArray(String[] values) throws IOException {
        this.writeStringArray(values);
    }

    @Override
    public void writeStringArray(String[] values) throws IOException {
        if (values == null) {
            this.writeInt(NULL_ARRAY_LENGTH);
            return;
        }

        this.writeInt(values.length);

        for (String s : values) {
            this.writeString(s);
        }
    }

    @Override
    public byte[] toByteArray() {
        return this.byteArrayOutputStream.toByteArray();
    }

    @Override
    public void close() throws IOException {
        this.outputStream.close();
    }

}
