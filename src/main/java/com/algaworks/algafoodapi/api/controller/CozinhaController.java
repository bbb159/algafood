package com.algaworks.algafoodapi.api.controller;

import java.util.List;

import com.algaworks.algafoodapi.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafoodapi.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafoodapi.domain.model.Cozinha;
import com.algaworks.algafoodapi.domain.repository.CozinhaRepository;
import com.algaworks.algafoodapi.domain.service.CadastroCozinhaService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/cozinhas")
public class CozinhaController {

	@Autowired
	private CadastroCozinhaService cadastroCozinhaService;

	@Autowired
	private CozinhaRepository cozinhaRepository;
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Cozinha> listar() {
		return cozinhaRepository.listar();
	}

	@GetMapping("/{cozinhaId}")
	public ResponseEntity<Cozinha> buscar(@PathVariable Long cozinhaId) {
		Cozinha cozinha = cozinhaRepository.buscar(cozinhaId);
		if (cozinha != null) {
			return ResponseEntity.ok(cozinha);
		}
		return ResponseEntity.notFound().build();
	}

	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public Cozinha adicionar(@RequestBody Cozinha cozinha) {
		return cadastroCozinhaService.salvar(cozinha);
	}

	@PutMapping("/{cozinhaId}")
	public ResponseEntity<Cozinha> atualizar(@PathVariable Long cozinhaId, @RequestBody Cozinha cozinha) {
		Cozinha cozinhaToUpdate = cozinhaRepository.buscar(cozinhaId);

		if (cozinhaToUpdate == null) {
			return ResponseEntity.notFound().build();
		}

//		cozinhaToUpdate.setNome(cozinha.getNome());
		BeanUtils.copyProperties(cozinha, cozinhaToUpdate, "id");
		cadastroCozinhaService.salvar(cozinhaToUpdate);
		return ResponseEntity.ok(cozinhaToUpdate);
	}

	@DeleteMapping("/{cozinhaId}")
	public ResponseEntity<Cozinha> excluir(@PathVariable Long cozinhaId) {
		try {
			cadastroCozinhaService.excluir(cozinhaId);
			return ResponseEntity.noContent().build();
		} catch (EntidadeEmUsoException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

	}
}
