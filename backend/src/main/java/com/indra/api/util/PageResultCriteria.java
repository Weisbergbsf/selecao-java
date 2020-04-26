package com.indra.api.util;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PageResultCriteria<T> implements Serializable {

	private static final long serialVersionUID = 3265524976080127173L;

	private int totalCount;

	private int pageSize = 10;

	private int totalPage;

	private int currentPage = 1;

	private List<T> list;

}