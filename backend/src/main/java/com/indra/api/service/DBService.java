package com.indra.api.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.indra.api.model.Bandeira;
import com.indra.api.model.Estado;
import com.indra.api.model.Historico;
import com.indra.api.model.Municipio;
import com.indra.api.model.Produto;
import com.indra.api.model.Revendedora;
import com.indra.api.model.dto.HistoricoCsvDto;
import com.indra.api.model.enuns.RegiaoEnum;
import com.indra.api.repository.BandeiraRepository;
import com.indra.api.repository.EstadoRepository;
import com.indra.api.repository.HistoricoRepository;
import com.indra.api.repository.MunicipioRepository;
import com.indra.api.repository.ProdutoRepository;
import com.indra.api.repository.RevendedoraRepository;
import com.indra.api.util.ReaderCsv;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DBService {

	@Autowired
	private BandeiraRepository bandeiraRepository;
	@Autowired
	private ProdutoRepository produtoRepository;
	@Autowired
	private EstadoRepository estadoRepository;
	@Autowired
	private MunicipioRepository municipioRepository;
	@Autowired
	private RevendedoraRepository revendedoraRepository;
	@Autowired
	private HistoricoRepository historicoRepository;

	private List<HistoricoCsvDto> historicosDto = new ArrayList<>();

	public void instantiateDatabase(MultipartFile file, int limite) throws ParseException {

		List<String> linhas = ReaderCsv.lerArquivo(file);
		historicosDto = criarColunas(linhas);

		salvarHistorico(limite);

	}

	private void salvarHistorico(int limite) {

		log.info("Processando historico para Salvar");

		Set<Historico> historicos = new HashSet<>();

		Set<Produto> produtos = salvarProduto();
		Set<Revendedora> revendedoras = salvarRevendedora();

		historicosDto.stream().limit(limite).forEach(obj -> {
			Produto produtoObj = produtos.stream().filter(produto -> obj.getNomeProduto().equals(produto.getNome()))
					.findAny().orElse(null);
			Revendedora revendedoraObj = revendedoras.stream()
					.filter(revendedora -> obj.getNomeRevendedora().equals(revendedora.getNome())).findAny()
					.orElse(null);
			historicos.add(new Historico(null, obj.getDataColeta(), obj.getValorVenda(), obj.getValorCompra(),
					produtoObj, revendedoraObj));
		});

		AtomicLong codigo = new AtomicLong();
		historicos.stream().forEach(municipio -> {
			municipio.setCodigo(codigo.incrementAndGet());
		});
		historicoRepository.saveAll(historicos);

		log.info("Historico Salvo com sucesso");
	}

	private Set<Revendedora> salvarRevendedora() {

		log.info("Processando Revendedora para Salvar");

		Set<Revendedora> revendedoras = new HashSet<>();

		Set<Municipio> municipios = salvarMunicipio();

		historicosDto.stream().forEach(obj -> {
			Municipio municipioObj = municipios.stream()
					.filter(municipio -> obj.getMunicipio().equals(municipio.getNome())).findAny().orElse(null);
			revendedoras.add(new Revendedora(null, obj.getNomeRevendedora(), obj.getCnpjRevendedora(), municipioObj,
					RegiaoEnum.toEnum(obj.getRegiao())));
		});
		AtomicLong codigo = new AtomicLong();
		revendedoras.stream().forEach(municipio -> {
			municipio.setCodigo(codigo.incrementAndGet());
		});
		revendedoraRepository.saveAll(revendedoras);

		log.info("Revendedoras salva com sucesso");

		return revendedoras;

	}

	private Set<Estado> salvarEstado() {

		log.info("Processando Estado para Salvar");

		Set<Estado> estados = new HashSet<>();

		AtomicLong codigo = new AtomicLong();
		historicosDto.stream().forEach(obj -> {
			estados.add(new Estado(null, obj.getSiglaEstado()));
		});
		estados.stream().forEach(bandeira -> {
			bandeira.setCodigo(codigo.incrementAndGet());
		});
		estadoRepository.saveAll(estados);

		log.info("Estados salvo com sucesso");

		return estados;
	}

	private Set<Municipio> salvarMunicipio() {

		log.info("Processando Municipio para Salvar");

		Set<Municipio> municipios = new HashSet<>();

		Set<Estado> estados = salvarEstado();

		historicosDto.stream().forEach(obj -> {
			Estado estadoObj = estados.stream().filter(estado -> obj.getSiglaEstado().equals(estado.getNome()))
					.findAny().orElse(null);
			municipios.add(new Municipio(null, obj.getMunicipio(), estadoObj));
		});
		AtomicLong codigo = new AtomicLong();
		municipios.stream().forEach(municipio -> {
			municipio.setCodigo(codigo.incrementAndGet());
		});
		municipioRepository.saveAll(municipios);

		log.info("Municipios salvo com sucesso");

		return municipios;

	}

	private Set<Produto> salvarProduto() {

		log.info("Processando Produto para Salvar");

		Set<Produto> produtos = new HashSet<>();

		Set<Bandeira> bandeiras = salvarBandeira();

		historicosDto.stream().forEach(obj -> {
			Bandeira bandeiraObj = bandeiras.stream().filter(bandeira -> obj.getBandeira().equals(bandeira.getNome()))
					.findAny().orElse(null);
			produtos.add(new Produto(null, obj.getNomeProduto(), obj.getUnidadeMedida(), bandeiraObj));
		});
		AtomicLong codigo = new AtomicLong();
		produtos.stream().forEach(prod -> {
			prod.setCodigo(codigo.incrementAndGet());
		});
		produtoRepository.saveAll(produtos);

		log.info("Produtos salvo com sucesso");

		return produtos;

	}

	private Set<Bandeira> salvarBandeira() {

		log.info("Processando Bandeira para Salvar");

		Set<Bandeira> bandeiras = new HashSet<>();

		historicosDto.stream().forEach(obj -> bandeiras.add(new Bandeira(null, obj.getBandeira())));

		AtomicLong codigo = new AtomicLong();
		bandeiras.stream().forEach(bandeira -> {
			bandeira.setCodigo(codigo.incrementAndGet());
		});

		bandeiraRepository.saveAll(bandeiras);

		log.info("Bandeiras salva com sucesso");

		return bandeiras;
	}

	private List<HistoricoCsvDto> criarColunas(List<String> linhas) throws ParseException {

		List<HistoricoCsvDto> historicos = new ArrayList<>();

		for (String linha : linhas) {
			String[] colunas = linha.split("\t");

			historicos.add(new HistoricoCsvDto(colunas[0], colunas[1], colunas[2], colunas[3], colunas[4], colunas[5],
					colunas[6], colunas[7], colunas[8], colunas[9], colunas[10]));
		}
		return historicos;
	}
}
