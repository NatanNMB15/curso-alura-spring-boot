package br.com.alura.forum.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.forum.controller.dto.DetalhesTopicoDTO;
import br.com.alura.forum.controller.dto.TopicoDTO;
import br.com.alura.forum.controller.dto.TopicoFormDTO;
import br.com.alura.forum.controller.dto.UpdateTopicoFormDTO;
import br.com.alura.forum.modelo.Curso;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;

@RestController
@RequestMapping("/topicos")
public class TopicosController {

	@Autowired
	private TopicoRepository topicoRepository;
	
	@Autowired
	private CursoRepository cursoRepository;
	
	@GetMapping
	public List<TopicoDTO> lista(String nomeCurso) {
		if (nomeCurso == null) {
			List<Topico> topicos = topicoRepository.findAll();
			return TopicoDTO.converter(topicos);
		} else {
			List<Topico> topicos = topicoRepository.findByCurso_Nome(nomeCurso);
			return TopicoDTO.converter(topicos);
		}
	}
	
	@PostMapping
	@Transactional
	public ResponseEntity<TopicoDTO> cadastrar(@RequestBody @Valid TopicoFormDTO topicoFormDTO, UriComponentsBuilder uriBuilder) {
		Curso curso = cursoRepository.findByNome(topicoFormDTO.getNomeCurso());
		
		Topico topico = topicoFormDTO.converter(curso);
		
		topicoRepository.save(topico);
		
		URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
		
		return ResponseEntity.created(uri).body(new TopicoDTO(topico));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<DetalhesTopicoDTO> detalhar(@PathVariable Long id) {
		Optional<Topico> topico = topicoRepository.findById(id);

		//Nova versão Spring Boot utilizar agora o metodo getReferenceById:
		//Topico topico = topicoRepository.getReferenceById(id);
		
		if(!topico.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok(new DetalhesTopicoDTO(topico.get()));
	}
	
	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<TopicoDTO> atualizar(@PathVariable Long id, @RequestBody @Valid UpdateTopicoFormDTO topicoFormDTO) {
		Optional<Topico> optionalTopico = topicoRepository.findById(id);
		
		if(!optionalTopico.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		
		Topico topico = optionalTopico.get();
		
		topico.atualizar(topicoFormDTO);
		
		return ResponseEntity.ok(new TopicoDTO(topico));
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> remover(@PathVariable Long id) {
		Optional<Topico> optionalTopico = topicoRepository.findById(id);
		
		if(!optionalTopico.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		
		topicoRepository.deleteById(id);
		
		return ResponseEntity.ok().build();
	}
}
