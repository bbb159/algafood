package com.algaworks.algafoodapi.api.controller;

import com.algaworks.algafoodapi.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafoodapi.domain.model.Restaurante;
import com.algaworks.algafoodapi.domain.repository.RestauranteRepository;
import com.algaworks.algafoodapi.domain.service.CadastroRestauranteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private CadastroRestauranteService cadastroRestauranteService;

    @GetMapping
    public List<Restaurante> listar() {
        return restauranteRepository.findAll();
    }

    @GetMapping("/{restauranteId}")
    public ResponseEntity<Restaurante> buscar(@PathVariable Long restauranteId) {
        Optional<Restaurante> restaurante = restauranteRepository.findById(restauranteId);
        if (restaurante.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(restaurante.get());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> adicionar(@RequestBody Restaurante restaurante) {
        try {
            restaurante = cadastroRestauranteService.salvar(restaurante);
            return ResponseEntity.status(HttpStatus.CREATED).body(restaurante);
        } catch (EntidadeNaoEncontradaException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PutMapping("/{restauranteId}")
    public ResponseEntity<?> atualizar(@PathVariable Long restauranteId, @RequestBody Restaurante restaurante) {
        try {
            Optional<Restaurante> restauranteToUpdate = restauranteRepository.findById(restauranteId);
            if (restauranteToUpdate.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            BeanUtils.copyProperties(restaurante, restauranteToUpdate.get(),
                    "id", "formasPagamento", "endereco", "dataCadastro", "produtos");
            cadastroRestauranteService.salvar(restauranteToUpdate.get());
            return ResponseEntity.ok(restauranteToUpdate.get());
        } catch (EntidadeNaoEncontradaException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{restauranteId}")
    public ResponseEntity<?> atualizarParcial(@PathVariable Long restauranteId, @RequestBody Map<String, Object> campos) {

        Optional<Restaurante> restauranteAtual = restauranteRepository.findById(restauranteId);

        if (restauranteAtual.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        merge(campos, restauranteAtual.get());

        return atualizar(restauranteId, restauranteAtual.get());
    }

    private void merge(@RequestBody Map<String, Object> camposOrigem, Restaurante restauranteDestino) {

        ObjectMapper objectMapper = new ObjectMapper();
        Restaurante restauranteOrigem = objectMapper.convertValue(camposOrigem, Restaurante.class);

        camposOrigem.forEach((attribute, value) -> {
            Field field = ReflectionUtils.findField(Restaurante.class, attribute);
            field.setAccessible(true);

            Object novoValor = ReflectionUtils.getField(field, restauranteOrigem);

            ReflectionUtils.setField(field, restauranteDestino, novoValor);
        });
    }


}
