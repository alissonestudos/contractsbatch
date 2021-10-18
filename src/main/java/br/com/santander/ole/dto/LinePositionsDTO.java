package br.com.santander.ole.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LinePositionsDTO {
	
	private Integer codCovenantPosition;
	private Integer contractNumberPosition;
	private Integer entryNumberPosition;
	private Integer statusPosition;
	private Integer cnpjCpfPosition;
	private Integer trancheValuePosition;
	private Integer registrationPosition;
	
	public LinePositionsDTO() {
		super();
	}
	
}
