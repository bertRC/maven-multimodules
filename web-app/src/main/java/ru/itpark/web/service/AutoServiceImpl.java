package ru.itpark.web.service;

import lombok.RequiredArgsConstructor;
import lombok.val;
import ru.itpark.web.exception.NotFoundException;
import ru.itpark.web.model.AutoModel;
import ru.itpark.web.repository.AutoRepository;

import javax.servlet.http.Part;
import java.util.List;

@RequiredArgsConstructor
public class AutoServiceImpl implements AutoService {
    private final AutoRepository repository;
    private final FileService fileService;

    @Override
    public List<AutoModel> getAll() {
        return repository.getAll();
    }

    @Override
    public AutoModel getById(int id) {
        return repository.getById(id).orElseThrow(() -> new NotFoundException(String.format("Object with id %d not found", id)));
    }

    @Override
    public void save(AutoModel model, Part part) {
        if (part != null) {
            val image = fileService.writeFile(part);
            model.setImageUrl(image);
        }
        String previousImageUrl = repository.save(model);
        if (previousImageUrl != null && !previousImageUrl.isEmpty()) {
            fileService.removeFile(previousImageUrl);
        }
    }

    @Override
    public boolean removeById(int id) {
        return fileService.removeFile(repository.removeById(id));
    }

    @Override
    public List<AutoModel> search(String text) {
        return repository.search(text);
    }
}
