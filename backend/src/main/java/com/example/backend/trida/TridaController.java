package com.example.backend.trida;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tridy")
@CrossOrigin(origins = "*")
public class TridaController {
    @Autowired
    private TridaRepository tridaRepository;

    @GetMapping("/{id}")
    public @ResponseBody ResponseEntity<TridaEntity> get(@PathVariable long id) {
        var t = tridaRepository.findById(id);
        if (t.isPresent()) {
            return new ResponseEntity<>(t.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/new")
    public @ResponseBody ResponseEntity<String> create(@RequestParam String name) {
        var trida = new TridaEntity();
        trida.setName(name);

        tridaRepository.save(trida);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/delete")
    public @ResponseBody ResponseEntity<String> delete(@RequestParam long id) {
        tridaRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/all")
    public @ResponseBody ResponseEntity<Iterable<TridaEntity>> getAll() {
        return new ResponseEntity<>(tridaRepository.findAll(), HttpStatus.OK);
    }

    @PostMapping("/update")
    public @ResponseBody ResponseEntity<String> update(@RequestParam long id,
            @RequestParam(required = false) String name) {
        var trida = tridaRepository.findById(id).get();
        trida.setName(name);
        tridaRepository.save(trida);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
