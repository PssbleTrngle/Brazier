package com.possible_triangle.brazier.network;

import java.io.*;
import java.util.Optional;

public class ByteConfigSerializer<T> {

    public byte[] serialize(T config) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            ObjectOutputStream out;
            out = new ObjectOutputStream(bos);
            out.writeObject(config);
            out.flush();
            return bos.toByteArray();
        } catch (IOException ex) {
            return new byte[0];
        }
    }

    public Optional<T> deserialize(byte[] from) {
        ByteArrayInputStream bis = new ByteArrayInputStream(from);
        try (ObjectInputStream in = new ObjectInputStream(bis)) {
            return Optional.ofNullable((T) in.readObject());
        } catch (IOException | ClassNotFoundException ex) {
            return Optional.empty();
        }
    }

}
