package fr.hyriode.api.impl.common.world;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by AstFaster
 * on 08/07/2022 at 13:23
 */
public class WorldCompression {

    public byte[] zipWorld(File worldFolder) {
        try (final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); final ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream)) {
            final byte[] buffer = new byte[1024];
            final List<String> files = this.listFiles(worldFolder);

            for (String defaultFile : files) {
                final String file = defaultFile.replace(worldFolder.getName(), "world");
                final String[] splitted = file.replace("\\", "/").split("/");

                if (splitted.length > 1) {
                    final String parent = splitted[splitted.length - 2];

                    if (file.endsWith("uid.dat") || file.endsWith("session.lock") || parent.equals("data") || parent.equals("stats") || parent.equals("playerdata") || parent.equals("advancements")) {
                        continue;
                    }
                }

                final ZipEntry entry = new ZipEntry(file);

                zipOutputStream.putNextEntry(entry);

                final FileInputStream inputStream = new FileInputStream(defaultFile);

                int len;
                while ((len = inputStream.read(buffer)) > 0) {
                    zipOutputStream.write(buffer, 0, len);
                }

                inputStream.close();
            }

            zipOutputStream.closeEntry();

            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void unzipWorld(File worldFolder, byte[] bytes) {
        try (final ZipInputStream inputStream = new ZipInputStream(new ByteArrayInputStream(bytes)))  {
            final byte[] buffer = new byte[1024];

            ZipEntry entry = inputStream.getNextEntry();
            while (entry != null) {
                final File newFile = new File(entry.getName().replace("world", worldFolder.getName()).replace("\\", "/"));
                final File parent = newFile.getParentFile();

                if (parent != null && !parent.isDirectory() && !parent.mkdirs()) {
                    throw new IOException("Failed to create directory " + parent);
                }

                final FileOutputStream outputStream = new FileOutputStream(newFile);

                int len;
                while ((len = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, len);
                }
                outputStream.close();

                entry = inputStream.getNextEntry();
            }

            inputStream.closeEntry();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> listFiles(File folder) {
        final List<String> result = new ArrayList<>();

        for (final File f : Objects.requireNonNull(folder.listFiles())) {
            if (f.isDirectory()) {
                result.addAll(listFiles(f));
            }

            if (f.isFile()) {
                result.add(f.getPath());
            }
        }
        return result;
    }

}
