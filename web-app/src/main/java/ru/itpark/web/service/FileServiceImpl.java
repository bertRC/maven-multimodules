package ru.itpark.web.service;

import lombok.val;
import ru.itpark.web.exception.FileAccessException;

import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class FileServiceImpl implements FileService {
    private final String uploadPath;

    public FileServiceImpl(String uploadPath) {
        try {
            this.uploadPath = uploadPath;
            Files.createDirectories(Paths.get(uploadPath));
        } catch (IOException e) {
            throw new FileAccessException(e);
        }
    }

    @Override
    public void readFile(String id, OutputStream os) {
        try {
            val path = Paths.get(uploadPath).resolve(id);
            Files.copy(path, os);
        } catch (IOException e) {
            throw new FileAccessException(e);
        }
    }

    @Override
    public String writeFile(Part part) {
        try {
            val id = UUID.randomUUID().toString();
            part.write(Paths.get(uploadPath).resolve(id).toString());
            return id;
        } catch (IOException e) {
            throw new FileAccessException(e);
        }
    }

    @Override
    public void copyFileFromUrl(String url, String name) {
        try {
            InputStream in = new URL(url).openStream();
            Files.copy(in, Paths.get(uploadPath).resolve(name), REPLACE_EXISTING);
        } catch (IOException e) {
            throw new FileAccessException(e);
        }
    }

    @Override
    public boolean removeFile(String id) {
        try {
            val path = Paths.get(uploadPath).resolve(id);
            return Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new FileAccessException(e);
        }
    }
}
