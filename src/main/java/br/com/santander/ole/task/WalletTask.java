package br.com.santander.ole.task;

import br.com.santander.ole.dto.LinesDTO;

import org.springframework.stereotype.Component;
import com.altec.bsbr.fw.batch.Task;

@Component
public class WalletTask extends Task<LinesDTO,LinesDTO> {

	/**
	 * Task de processamento.
	 */
	public LinesDTO execute(LinesDTO dto) throws Exception {
		return dto;
	}
}
