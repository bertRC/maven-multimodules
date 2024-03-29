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
            val fileName = part.getSubmittedFileName();
            String image;
            if (fileName.isEmpty()) {
                image = repository.getImageUrl(model.getId()).orElse(null);
            } else {
                image = fileService.writeFile(part);
                if (model.getId() > 0) {
                    val previousImage = repository.getImageUrl(model.getId()).orElse(null);
                    fileService.removeFile(previousImage);
                }
            }
            model.setImageUrl(image);
        }
        repository.save(model);
    }

    @Override
    public void removeById(int id) {
        val imageUrl = repository.getImageUrl(id).orElse(null);
        repository.removeById(id);
        fileService.removeFile(imageUrl);
    }

    @Override
    public List<AutoModel> search(String text) {
        return repository.search(text);
    }
}
