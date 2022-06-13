package fr.tartur.lib;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DataStorage<T> {

    private final File file;
    private final String fileName;

    private final Gson gson;
    private final T object;

    private String serialized;

    public DataStorage(T object, String parentDir, String fileName) {
        this.fileName = fileName + ".json";
        this.file = new File(parentDir, this.fileName);

        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    System.out.println("[DATA STORAGE] The \"" + this.fileName + "\" file has been successfully created !");
                }
            } catch (IOException e) {
                throw new RuntimeException("[DATA STORAGE] An I/O error has occurred : " + e);
            } catch (SecurityException e) {
                throw new RuntimeException("[DATA STORAGE] The \"" + this.fileName + "\" file could not be created due to missing rights : " + e);
            }
        }

        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .create();

        this.object = object;
    }

    public String serialize() {
        this.serialized = this.gson.toJson(this.object);
        return this.serialized;
    }

    public T deserialize(Class<T> classInstance) {
        return this.gson.fromJson(this.serialized, classInstance);
    }

    public void save() {
        try {
            FileWriter writer = new FileWriter(this.file);

            writer.write(this.serialized);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException("[DATA STORAGE] The \"" + this.fileName + "\" file could not be opened: " + e);
        }
    }

    public String getContentRaw() {
        return this.serialized;
    }

}
