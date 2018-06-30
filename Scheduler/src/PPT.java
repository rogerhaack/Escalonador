import java.util.ArrayList;
import java.util.List;

public class PPT {

	public float CalculaPPT(List<Process> posprocesses, Integer quantum) {
		Integer QUANTUM = quantum;
		int tempoAtual = 0, totalProcessos = 0, TempoEsperaTotal = 0;
		float TempoMedioEspera = 0;
		List<Process> ProcessosInicial, ProcessosExecucao, ProcessosPausados, ProcessosFinalizados;
		int[][] BurstProcess;

		ProcessosInicial = new ArrayList<Process>();
		ProcessosExecucao = new ArrayList<Process>();
		ProcessosPausados = new ArrayList<Process>();
		ProcessosFinalizados = new ArrayList<Process>();

		tempoAtual = posprocesses.get(0).getInsertionTime();
		totalProcessos = posprocesses.size();
		ProcessosInicial.addAll(posprocesses);
		BurstProcess = new int[totalProcessos][1];

		for (int i = 0; i < posprocesses.size(); i++) {
			BurstProcess[i][0] = posprocesses.get(i).getTotalTime();
		}

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
						} else if (pro.getPriority() < ProcessosExecucao.get(0).getPriority() && QUANTUM == 0) {
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

			if (ProcessosExecucao.size() > 0) {
				if (ProcessosPausados.size() > 0) {
					if (QUANTUM == 0) {
						if (VerificarPrioridade(ProcessosPausados)) {
							ProcessosExecucao.add(ProcessosPausados.get(0));
							ProcessosPausados.remove(0);
						} else {
							Process temp = ProcessosExecucao.get(0);
							ProcessosExecucao.remove(temp);
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

						QUANTUM = quantum;
					}
				}
			} else if (ProcessosExecucao.size() == 0) {
				if (ProcessosPausados.size() > 0) {

					if (VerificarPrioridade(ProcessosPausados)) {
						ProcessosExecucao.add(ProcessosPausados.get(0));
						ProcessosPausados.remove(0);
					} else {
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

			QUANTUM--;
			tempoAtual++;
		}

		TempoMedioEspera = TempoEsperaTotal / totalProcessos;

		return TempoMedioEspera;

	}

	public boolean VerificarPrioridade(List<Process> pausados) {
		boolean PRIORIDADE = true;
		int prioridade = pausados.get(0).getPriority();
		float retorno = 0;

		for (Process processe : pausados) {
			if (processe.getPriority() != prioridade) {
				PRIORIDADE = false;
			}
		}

		return PRIORIDADE;
	}
}
