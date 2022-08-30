package br.com.alura.forum.controller.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import br.com.alura.forum.modelo.StatusTopico;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.modelo.Usuario;

public class DetalhesTopicoDTO extends TopicoDTO {

	private String nomeAutor;
	private StatusTopico status;
	private List<RespostaDTO> listRespostas;
	
	public DetalhesTopicoDTO(Topico topico) {
		super(topico);

		this.nomeAutor = Optional.ofNullable(topico.getAutor()).map(Usuario::getNome).orElse("");
		this.status = topico.getStatus();
		this.listRespostas = new ArrayList<>();
		this.listRespostas.addAll(topico.getRespostas().stream().map(RespostaDTO::new).collect(Collectors.toList()));
	}

	public String getNomeAutor() {
		return nomeAutor;
	}

	public StatusTopico getStatus() {
		return status;
	}

	public List<RespostaDTO> getListRespostas() {
		return listRespostas;
	}

	
}
