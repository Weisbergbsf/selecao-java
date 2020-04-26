package com.indra.api.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.indra.api.model.dto.BandeiraDto;
import com.indra.api.model.dto.HistoricoCsvDto;
import com.indra.api.model.dto.MunicipioDto;
import com.indra.api.service.HistoricoService;
import com.indra.api.util.PageResultCriteria;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/historicos")
public class HistoricoResource {

	@Autowired
	private HistoricoService historicoService;

	@ApiOperation(value = "Média de preço de combustível com base no nome do município")
	@GetMapping("/media-municipio")
	public ResponseEntity<?> mediaPrecoDoMunicipio(
			@RequestParam(value = "municipio", required = true) String municipio) {

		MunicipioDto media = historicoService.mediaPrecoDoMunicipio(municipio);

		return ResponseEntity.ok().body(media);
	}

	@ApiOperation(value = "Todas as informações importadas por sigla da região")
	@GetMapping("/todos-por-sigla")
	public ResponseEntity<?> obterTodosPorSiglaDaRegiao(
			@RequestParam(value = "siglaRegiao", required = true) String siglaRegiao,
			@RequestParam(value = "currentPage", defaultValue = "1") int currentPage,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

		PageResultCriteria<HistoricoCsvDto> resultado = null;
		try {
			resultado = historicoService.obterTodosPorSiglaDaRegiao(siglaRegiao, currentPage, pageSize);

		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}

		return ResponseEntity.ok().body(resultado);
	}

	@ApiOperation(value = "Valor médio do valor da compra e do valor da venda por bandeira")
	@GetMapping("/media-por-bandeira")
	public ResponseEntity<?> mediaPrecoCompraVendaPorBandeira(
			@RequestParam(value = "currentPage", defaultValue = "1") int currentPage,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

		PageResultCriteria<BandeiraDto> media = historicoService.mediaPrecoCompraVendaPorBandeira(currentPage,
				pageSize);
		return ResponseEntity.ok().body(media);
	}

	@ApiOperation(value = "Média de preço de combustível com base no nome do município")
	@GetMapping("/media-por-municipio")
	public ResponseEntity<?> mediaPrecoCompraVendaPorMunicipio(
			@RequestParam(value = "currentPage", defaultValue = "1") int currentPage,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

		PageResultCriteria<MunicipioDto> media = historicoService.mediaPrecoCompraVendaPorMunicipio(currentPage,pageSize);

		return ResponseEntity.ok().body(media);
	}

	@ApiOperation(value = "Dados agrupados por distribuidora/revendedora")
	@GetMapping("/informacoes-por-revendedora")
	public ResponseEntity<?> obterDadosPorRevendedora(
			@RequestParam(value = "currentPage", defaultValue = "1") int currentPage,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

		PageResultCriteria<HistoricoCsvDto> resultado = historicoService.obterDadosPorRevendedora(currentPage,
				pageSize);

		return ResponseEntity.ok().body(resultado);
	}

	@ApiOperation(value = "Dados agrupados pela data da coleta")
	@GetMapping("/informacoes-por-data-coleta")
	public ResponseEntity<?> obterDadosPorDataColeta(
			@RequestParam(value = "currentPage", defaultValue = "1") int currentPage,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

		PageResultCriteria<HistoricoCsvDto> resultado = historicoService.obterDadosPorDataColeta(currentPage, pageSize);

		return ResponseEntity.ok().body(resultado);
	}

}
