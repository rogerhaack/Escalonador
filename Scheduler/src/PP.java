import java.util.ArrayList;
import java.util.List;

public class PP {

	public float CalculaPP(List<Process> posprocesses) {
		int tempoTotal = 0, tempoAtual = 0, totalProcessos = 0;
		float TempoMedioEspera = 0;
		List<Process> ProcessosInicial, ProcessosExecucao, ProcessosPausados, ProcessosFinalizados;

		if (!posprocesses.isEmpty()) {

			tempoAtual = posprocesses.get(0).getInsertionTime();
			totalProcessos = posprocesses.size();

			ProcessosInicial = new ArrayList<Process>();
			ProcessosExecucao = new ArrayList<Process>();
			ProcessosPausados = new ArrayList<Process>();
			ProcessosFinalizados = new ArrayList<Process>();

			ProcessosInicial.addAll(posprocesses);

			int[][] BurstProcess = new int[totalProcessos][2];

			// Define o tempo total com base no Burst
			for (int i = 0; i < posprocesses.size(); i++) {
				tempoTotal += posprocesses.get(i).getTotalTime();

				BurstProcess[i][0] = posprocesses.get(i).getTotalTime();
				BurstProcess[i][1] = 0;
			}

			tempoTotal += tempoAtual;

			int TempoEsperaTotal = 0;

			while (ProcessosFinalizados.size() < totalProcessos) {
				
				if (ProcessosPausados.size() > 0) {
					TempoEsperaTotal += ProcessosPausados.size();
				}

				if (!ProcessosInicial.isEmpty()) {
					for (int i = 0; i < ProcessosInicial.size(); i++) {
						Process pro = ProcessosInicial.get(i);

						if (pro.getInsertionTime() == tempoAtual) {
							if (ProcessosExecucao.isEmpty()) {
								if (ProcessosPausados.isEmpty()) {									
									ProcessosExecucao.add(pro);
									ProcessosInicial.remove(pro);
								} else {									
									Process temp = pro;
									for (int j = 0; j < ProcessosPausados.size(); j++) {
										if (temp.getPriority() > ProcessosPausados.get(j).getPriority()) {
											ProcessosPausados.add(temp);
											temp = ProcessosPausados.get(j);
											ProcessosPausados.remove(ProcessosPausados.get(j));
										} else if (temp.getPriority() == ProcessosPausados.get(j).getPriority()) {
											if (temp.getInsertionTime() > ProcessosPausados.get(j).getInsertionTime()) {
												ProcessosPausados.add(temp);
												temp = ProcessosPausados.get(j);
												ProcessosPausados.remove(ProcessosPausados.get(j));
											}
										}
									}
									ProcessosExecucao.add(temp);
								}
							} else if (pro.getPriority() < ProcessosExecucao.get(0).getPriority()) {								
								ProcessosPausados.add(ProcessosExecucao.get(0));
								ProcessosExecucao.remove(ProcessosExecucao.get(0));
								ProcessosExecucao.add(pro);
								ProcessosInicial.remove(pro);
							} else {								
								ProcessosPausados.add(pro);
								ProcessosInicial.remove(pro);
							}
						}
					}
				}

				if (ProcessosPausados.size() > 0) {
					if (ProcessosExecucao.size() == 0) {						
						Process temp = ProcessosPausados.get(0);
						ProcessosPausados.remove(temp);
								
						
						for (int j = 0; j < ProcessosPausados.size(); j++) {
							if (temp.getPriority() > ProcessosPausados.get(j).getPriority()) {
								ProcessosPausados.add(temp);
								temp = ProcessosPausados.get(j);
								ProcessosPausados.remove(ProcessosPausados.get(j));
							} else if (temp.getPriority() == ProcessosPausados.get(j).getPriority()) {
								if (temp.getInsertionTime() > ProcessosPausados.get(j).getInsertionTime()) {									
									Process temporario = temp;
									temp = ProcessosPausados.get(j);
									ProcessosPausados.remove(ProcessosPausados.get(j));
									ProcessosPausados.add(temporario);
								}
							}
						}
						ProcessosExecucao.add(temp);
					}
				}

				if (ProcessosExecucao.size() > 0) {
					if (BurstProcess[ProcessosExecucao.get(0).getPid()][0] > 0) {
						BurstProcess[ProcessosExecucao.get(0).getPid()][0]--;
					}
					if (BurstProcess[ProcessosExecucao.get(0).getPid()][0] == 0) {						
						ProcessosFinalizados.add(ProcessosExecucao.get(0));
						ProcessosExecucao.remove(ProcessosExecucao.get(0));
					}
				}
				tempoAtual++;
			}

			TempoMedioEspera = TempoEsperaTotal / totalProcessos;
		

			return TempoMedioEspera;
		}
		return 0;

	}

}
