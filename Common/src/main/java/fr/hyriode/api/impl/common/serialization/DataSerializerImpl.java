package fr.hyriode.api.impl.common.serialization;

import fr.hyriode.api.serialization.DataSerializable;
import fr.hyriode.api.serialization.DataSerializer;

import java.io.IOException;

/**
 * Created by AstFaster
 * on 15/02/2023 at 15:52
 */
public class DataSerializerImpl implements DataSerializer {

    public static final int NULL_ARRAY_LENGTH = -1;

    @Override
    public <T extends DataSerializable> byte[] serialize(T object) {
        try (final ObjectDataOutputStream output = new ObjectDataOutputStream()) {
            object.write(output);

            return output.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <T extends DataSerializable> T deserialize(T object, byte[] bytes)  {
        try (final ObjectDataInputStream input = new ObjectDataInputStream(bytes)) {
            object.read(input);

            return object;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
