import java.util.List;

public class NP {

	public float CalculaNP(List<Process> posprocesses) {

		if (!posprocesses.isEmpty()) {
			int tempoAtual = posprocesses.get(0).getInsertionTime();
			float tempoEspera = 0, TempoMedioEspera = 0;

			for (int i = 0; i < posprocesses.size(); i++) {
				Process pro = posprocesses.get(i);
				tempoAtual += pro.getTotalTime();

				tempoEspera += (tempoAtual - pro.getInsertionTime() - pro.getTotalTime());
			}

			TempoMedioEspera = tempoEspera / posprocesses.size();
			
			return TempoMedioEspera;
		}
		return 0;		
	}

}
