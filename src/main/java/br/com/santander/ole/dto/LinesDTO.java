package br.com.santander.ole.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LinesDTO {

	private List<String> lines;
	private String wallet;
	
	public LinesDTO() {
		super();
	}
	
}
