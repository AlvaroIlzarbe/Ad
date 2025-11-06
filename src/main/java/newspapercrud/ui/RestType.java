package newspapercrud.ui;


import newspapercrud.domain.model.TypeDTO;
import newspapercrud.domain.service.TypeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class RestType {
    private final TypeService typeService;

    public RestType(TypeService typeService) {
        this.typeService = typeService;
    }

    @CrossOrigin(origins = "http://127.0.0.1:5500")
    @GetMapping("/types")
    public List<TypeDTO> getAll() {
        return typeService.getAll();
    }
}

