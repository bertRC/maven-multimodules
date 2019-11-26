package ru.itpark.web.repository;

import ru.itpark.web.model.AutoModel;

import java.util.List;
import java.util.Optional;

public interface AutoRepository {
    List<AutoModel> getAll();

    Optional<AutoModel> getById(int id);

    String save(AutoModel model);

    String removeById(int id);

    List<AutoModel> search(String text);
}
