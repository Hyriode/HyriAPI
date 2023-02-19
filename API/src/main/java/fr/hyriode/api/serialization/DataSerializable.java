package fr.hyriode.api.serialization;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by AstFaster
 * on 15/02/2023 at 15:46.<br>
 *
 * Permits to your object to be serializable as a byte array.
 */
public interface DataSerializable {

    /**
     * Write the data to an output stream
     *
     * @param output The output used to write data
     * @throws IOException because of {@link DataOutput} operations
     */
    void write(ObjectDataOutput output) throws IOException;

    /**
     * Read the data from an input stream
     *
     * @param input The input used to read data
     * @throws IOException because of {@link DataInput} operations
     */
    void read(ObjectDataInput input) throws IOException;

}
