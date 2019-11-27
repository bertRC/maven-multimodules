package ru.itpark.web.repository;

import ru.itpark.web.model.AutoModel;

import java.util.List;
import java.util.Optional;

public interface AutoRepository {
    List<AutoModel> getAll();

    Optional<AutoModel> getById(int id);

    void save(AutoModel model);

    Optional<String> getImageUrl(int id);

    void removeById(int id);

    List<AutoModel> search(String text);
}
