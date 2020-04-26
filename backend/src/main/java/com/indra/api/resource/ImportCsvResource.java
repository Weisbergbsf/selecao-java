package com.indra.api.resource;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.indra.api.service.DBService;
import com.indra.api.service.exceptions.DataIntegrityException;
import com.indra.api.service.exceptions.FileException;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/csv")
public class ImportCsvResource {

	@Autowired
	private DBService dbService;

	@ApiOperation(value = "Importação de csv - Obs: O arquivo 2019-1_CA.csv tem quase 500.000 registros. Para que a importação não demore muito, "
			+ "foi definido um limite default de 100 registros, caso queira todos os registros, informe o limite desesejado.")
	@PostMapping
	public ResponseEntity<?> salvarDados(@RequestParam("file") MultipartFile file, @RequestParam(value = "limite", defaultValue = "100") int limite) {

		try {
			dbService.instantiateDatabase(file, limite);
		} catch (ParseException e) {
			throw new FileException("Error ao ler arquivo");
		} catch (Exception e) {
			throw new DataIntegrityException("Requisição incorreta");
		}
		return ResponseEntity.ok().body("Banco de dados populado com sucesso!");

	}
}
