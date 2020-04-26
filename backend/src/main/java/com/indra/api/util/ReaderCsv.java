package com.indra.api.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class ReaderCsv {

	public static List<String> lerArquivo(MultipartFile file) {
		List<String> linhas = new ArrayList<>();
		try {
			InputStream is = file.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_16));
			br.readLine();
			while (br.ready()) {
				linhas.add(br.readLine());
			}
			br.close();
			is.close();
			return linhas;
		} catch (IOException e) {
			System.out.println("Erro ao ler arquivo. " + e.getLocalizedMessage());
		}
		return null;
	}
}