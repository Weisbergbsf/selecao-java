package com.indra.api.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.indra.api.model.Bandeira;
import com.indra.api.model.Estado;
import com.indra.api.model.Historico;
import com.indra.api.model.Municipio;
import com.indra.api.model.Produto;
import com.indra.api.model.Revendedora;
import com.indra.api.model.dto.BandeiraDto;
import com.indra.api.model.dto.HistoricoCsvDto;
import com.indra.api.model.dto.MunicipioDto;
import com.indra.api.model.enuns.RegiaoEnum;
import com.indra.api.service.exceptions.ObjectNotFoundException;
import com.indra.api.util.PageResultCriteria;

@Service
public class HistoricoServiceImpl implements HistoricoService {

	@Autowired
	private EntityManager manager;

	@Override
	public MunicipioDto mediaPrecoDoMunicipio(String municipio) {
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
		CriteriaQuery<MunicipioDto> query = criteriaBuilder.createQuery(MunicipioDto.class);

		Root<Historico> historico = query.from(Historico.class);

		Join<Historico, Revendedora> revendedoraJoin = historico.join("revendedora", JoinType.INNER);
		Join<Revendedora, Municipio> municipioJoin = revendedoraJoin.join("municipio", JoinType.INNER);

		MunicipioDto municipioDto = new MunicipioDto();
		try {
			query.multiselect(municipioJoin.get("nome"), criteriaBuilder.avg(historico.get("valorVenda")))
			.where(criteriaBuilder.equal(municipioJoin.get("nome"), municipio.toUpperCase()));
			municipioDto = manager.createQuery(query).getSingleResult();
		} catch (IllegalArgumentException e) {
			throw new ObjectNotFoundException("Nenhum registro encontrado com esse município: "+municipio);
		} catch (Exception e) {
			throw new ObjectNotFoundException("Nenhum registro encontrado com esse município: "+municipio);
		}

		return municipioDto;
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageResultCriteria<HistoricoCsvDto> obterTodosPorSiglaDaRegiao(String siglaRegiao, int currentPage, int pageSize) {
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
		CriteriaQuery<HistoricoCsvDto> query = criteriaBuilder.createQuery(HistoricoCsvDto.class);

		Root<Historico> historico = query.from(Historico.class);

		Join<Historico, Revendedora> revendedoraJoin = historico.join("revendedora", JoinType.INNER);
		Join<Historico, Produto> produtoJoin = historico.join("produto", JoinType.INNER);
		Join<Produto, Bandeira> bandeiraJoin = produtoJoin.join("bandeira", JoinType.INNER);
		Join<Revendedora, Municipio> municipioJoin = revendedoraJoin.join("municipio", JoinType.INNER);
		Join<Municipio, Estado> estadoJoin = municipioJoin.join("estado", JoinType.INNER);

		try {
			query.multiselect(revendedoraJoin.get("regiao"), estadoJoin.get("nome"), municipioJoin.get("nome"),
					revendedoraJoin.get("nome"), revendedoraJoin.get("cnpj"), produtoJoin.get("nome"),
					historico.get("dataColeta"), historico.get("valorVenda"), historico.get("valorCompra"),
					produtoJoin.get("unidadeMedida"), bandeiraJoin.get("nome"))
				.where(criteriaBuilder.equal(revendedoraJoin.get("regiao"),RegiaoEnum.toEnum(siglaRegiao.toUpperCase())))
				.groupBy(historico.get("dataColeta"), revendedoraJoin.get("cnpj"), produtoJoin.get("nome"),
							historico.get("valorVenda"), historico.get("valorCompra"));
		} catch (IllegalArgumentException e) {
			throw new ObjectNotFoundException("Nenhum registro encontrado com essa sigla: "+siglaRegiao);
		}
		catch (Exception e) {
			throw new ObjectNotFoundException("Nenhum registro encontrado com essa sigla: "+siglaRegiao);
		}
		
		
		PageResultCriteria<HistoricoCsvDto> page = pagination(currentPage, pageSize, criteriaBuilder, query, historico);
		return page;
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageResultCriteria<BandeiraDto> mediaPrecoCompraVendaPorBandeira(int currentPage, int pageSize) {
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
		CriteriaQuery<BandeiraDto> query = criteriaBuilder.createQuery(BandeiraDto.class);

		Root<Historico> historico = query.from(Historico.class);

		Join<Historico, Produto> produtoJoin = historico.join("produto", JoinType.INNER);
		Join<Produto, Bandeira> bandeiraJoin = produtoJoin.join("bandeira", JoinType.INNER);

		query.multiselect(bandeiraJoin.get("nome"), criteriaBuilder.avg(historico.get("valorVenda")), criteriaBuilder.avg(historico.get("valorCompra")))
				.groupBy(bandeiraJoin.get("nome"));

		PageResultCriteria<BandeiraDto> page = pagination(currentPage, pageSize, criteriaBuilder, query, historico);

		return page;
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageResultCriteria<MunicipioDto> mediaPrecoCompraVendaPorMunicipio(int currentPage, int pageSize) {
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
		CriteriaQuery<MunicipioDto> query = criteriaBuilder.createQuery(MunicipioDto.class);

		Root<Historico> historico = query.from(Historico.class);

		Join<Historico, Revendedora> revendedoraJoin = historico.join("revendedora", JoinType.INNER);
		Join<Revendedora, Municipio> municipioJoin = revendedoraJoin.join("municipio", JoinType.INNER);

		query.multiselect(municipioJoin.get("nome"), criteriaBuilder.avg(historico.get("valorVenda")),
				criteriaBuilder.avg(historico.get("valorCompra")))
				.groupBy(municipioJoin.get("nome"));
		PageResultCriteria<MunicipioDto> page = pagination(currentPage, pageSize, criteriaBuilder, query, historico);
		return page;
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageResultCriteria<HistoricoCsvDto> obterDadosPorRevendedora(int currentPage, int pageSize) {
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
		CriteriaQuery<HistoricoCsvDto> query = criteriaBuilder.createQuery(HistoricoCsvDto.class);

		Root<Historico> historico = query.from(Historico.class);

		Join<Historico, Revendedora> revendedoraJoin = historico.join("revendedora", JoinType.INNER);
		Join<Historico, Produto> produtoJoin = historico.join("produto", JoinType.INNER);
		Join<Produto, Bandeira> bandeiraJoin = produtoJoin.join("bandeira", JoinType.INNER);
		Join<Revendedora, Municipio> municipioJoin = revendedoraJoin.join("municipio", JoinType.INNER);
		Join<Municipio, Estado> estadoJoin = municipioJoin.join("estado", JoinType.INNER);

		query.multiselect(revendedoraJoin.get("regiao"), estadoJoin.get("nome"), municipioJoin.get("nome"),
				revendedoraJoin.get("nome"), revendedoraJoin.get("cnpj"), produtoJoin.get("nome"),
				historico.get("dataColeta"), historico.get("valorVenda"), historico.get("valorCompra"),
				produtoJoin.get("unidadeMedida"), bandeiraJoin.get("nome"))
			 .groupBy(revendedoraJoin.get("cnpj"), produtoJoin.get("nome"), historico.get("dataColeta"), 
					 historico.get("valorVenda"), historico.get("valorCompra"));

		PageResultCriteria<HistoricoCsvDto> page = pagination(currentPage, pageSize, criteriaBuilder, query, historico);
		return page;
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageResultCriteria<HistoricoCsvDto> obterDadosPorDataColeta(int currentPage, int pageSize) {

		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
		CriteriaQuery<HistoricoCsvDto> query = criteriaBuilder.createQuery(HistoricoCsvDto.class);

		Root<Historico> historico = query.from(Historico.class);
		Join<Historico, Revendedora> revendedoraJoin = historico.join("revendedora", JoinType.INNER);
		Join<Historico, Produto> produtoJoin = historico.join("produto", JoinType.INNER);
		Join<Produto, Bandeira> bandeiraJoin = produtoJoin.join("bandeira", JoinType.INNER);
		Join<Revendedora, Municipio> municipioJoin = revendedoraJoin.join("municipio", JoinType.INNER);
		Join<Municipio, Estado> estadoJoin = municipioJoin.join("estado", JoinType.INNER);

		query.multiselect(revendedoraJoin.get("regiao"), estadoJoin.get("nome"), municipioJoin.get("nome"),
				revendedoraJoin.get("nome"), revendedoraJoin.get("cnpj"), produtoJoin.get("nome"),
				historico.get("dataColeta"), historico.get("valorVenda"), historico.get("valorCompra"),
				produtoJoin.get("unidadeMedida"), bandeiraJoin.get("nome"))
			.groupBy(historico.get("dataColeta"), revendedoraJoin.get("cnpj"), produtoJoin.get("nome"), 
					historico.get("valorVenda"), historico.get("valorCompra"));

		PageResultCriteria<HistoricoCsvDto> page = pagination(currentPage, pageSize, criteriaBuilder, query, historico);
		return page;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private PageResultCriteria pagination(int currentPage, int pageSize, CriteriaBuilder criteriaBuilder,
			CriteriaQuery<?> cQuery, Root<?> root) {
		
		PageResultCriteria page = new PageResultCriteria<>();
		Integer totalCount = 0;
		Integer totalPage = 0;
		List list = new ArrayList<>();
		try {
			currentPage = currentPage == 0 ? 1 :  currentPage;
			int firstResult = (currentPage - 1) * pageSize;
			Query query = manager.createQuery(cQuery);
			query.setFirstResult(firstResult);
			query.setMaxResults(pageSize);
			list = query.getResultList();

			cQuery.multiselect(criteriaBuilder.count(root));
			totalCount = (manager.createQuery(cQuery).getResultList() == null) ? 0
					: manager.createQuery(cQuery).getResultList().size();

			if (totalCount % pageSize == 0) {
				totalPage = totalCount / pageSize;
			} else {
				totalPage = totalCount / pageSize + 1;
			}

			page.setCurrentPage(currentPage);
			page.setList(list);
			page.setPageSize(pageSize);
			page.setTotalCount(totalCount);
			page.setTotalPage(totalPage);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			manager.close();
		}
		return page;
	}

}
