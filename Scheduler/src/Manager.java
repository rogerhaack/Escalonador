import java.util.List;

public class Manager {

	public void ManagerScheduler(List<Process> posprocesses, Integer quantum) {
		boolean PRIORIDADE = true;
		int prioridade = posprocesses.get(0).getPriority();
		float retorno = 0;

		for (Process processe : posprocesses) {			
			if (processe.getPriority() != prioridade) {
				PRIORIDADE = false;
			}			
		}

		if (PRIORIDADE == true && quantum == 0) {
			NP np = new NP();
			retorno = np.CalculaNP(posprocesses);
			ExibeTempoMedio(retorno, "Não Preemptivo");
			
		} else if (PRIORIDADE == false && quantum == 0) {
			PP pp = new PP();
			retorno = pp.CalculaPP(posprocesses);
			ExibeTempoMedio(retorno, "Preemptivo por Prioridade");
			
		} else {
			PPT ppt = new PPT();
			retorno = ppt.CalculaPPT(posprocesses, quantum);
			ExibeTempoMedio(retorno, "Preemptivo por Prioridade + Tempo");
		}
	}
	
	public void ExibeTempoMedio(float TempoMedio,String TipoAlgoritmo) {
		SISOPInterface.outputTextArea.setText("Algoritmo Usado: " + TipoAlgoritmo);
		SISOPInterface.outputTextArea.append("\n");
		SISOPInterface.outputTextArea.append("Tempo Médio Espera: " + TempoMedio);
		SISOPInterface.outputTextArea.append("\n");
	}
}
