package de.oryfox.vinylish.record;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.FileOutputStream;
import java.util.List;

@RestController
@RequestMapping("record")
@AllArgsConstructor
public class RecordController {

    final RecordRepository recordRepository;

    @PostMapping
    public Record addRecord(@RequestBody Record record) {
        return recordRepository.save(record);
    }

    @GetMapping
    public ResponseEntity<Object> listRecord(@RequestParam(required = false) Long id) {
        if (id != null) {
            var opt = recordRepository.findById(id);
            if (opt.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            return ResponseEntity.ok(opt.get());
        } else {
            return ResponseEntity.ok(recordRepository.findAll());
        }
    }

    @GetMapping("colors")
    public List<String> listColors() {
        return recordRepository.getAllColors();
    }
}
