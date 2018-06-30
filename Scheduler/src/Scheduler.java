
import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author roger haack
 */
public class Scheduler extends Thread {
	List<Process> processes = new ArrayList<Process>();
	List<Process> Posprocesses = new ArrayList<Process>();
	private Boolean running = true;
	private Integer quantum = 0;
	private Process runningProcess;
	private Integer nextPid = 0;
	private Integer currentTime = 0;

	public void addProcess(Process p) {
		processes.add(p);
		Posprocesses.add(p);
		updateCounter();
	}

	public Integer getCurrentTime() {
		return currentTime;
	}

	private void updateCounter() {
		SISOPInterface.labelProcessCount.setText("Processes Count: " + processes.size());
		SISOPInterface.labelCurrentTime.setText("Current Time: " + currentTime);
	}

	public void setQuantum(Integer quantum) {
		this.quantum = quantum;
	}

	public void stopSchedler() {
		running = false;
	}

	public Integer nextPid() {
		return nextPid++;
	}

	@Override
	public void run() {
		while (running) {
			try {
				if (runningProcess == null) {
					for (Process p : processes) {
						if (!p.isFinished()) {
							runningProcess = p;
							break;
						}
					}
				}

				if (runningProcess == null) {
					SISOPInterface.outputTextArea.setText("IDLE!");
					
//					List<Process> teste = new ArrayList<Process>();
//					
//					
//					Process p0 = new Process(0, 1, 10, 5);
//					Process p1 = new Process(1, 2, 10, 7);
//					Process p2 = new Process(2, 1, 10, 9);
//					Process p3 = new Process(3, 3, 10, 11);
//			       
//					teste.add(p0);
//					teste.add(p1);
//					teste.add(p2);
//					teste.add(p3);
					
					
					if(Posprocesses.size() > 0){
						Manager manager = new Manager();
						manager.ManagerScheduler(Posprocesses, quantum);
//						manager.ManagerScheduler(teste, 4);
					}
					
					
//					for (int i = 0; i < nextPid; i++) {
//						Process pro = Posprocesses.get(i);
//						System.out.println("PID: " + pro.getPid() + "\n" + "T: " + pro.getInsertionTime() + "\n" + "B: "
//								+ pro.getTotalTime() + "\n" + "Prioridade: " + pro.getPriority() + "\n");
//
//					}

				} else {
					SISOPInterface.outputTextArea.setText("RUNNING PROCESS PID = " + runningProcess.getPid());

					SISOPInterface.outputTextArea.append("\n");

					SISOPInterface.outputTextArea.append("INSERTION TIME = " + runningProcess.getInsertionTime());

					SISOPInterface.outputTextArea.append("\n");

					SISOPInterface.outputTextArea.append("REMAINING TIME = " + runningProcess.getRemainingTime());

					runningProcess.runProcess();
					if (runningProcess.isFinished()) {
						processes.remove(runningProcess);
						runningProcess = null;
					}
				}

				currentTime++;
				updateCounter();
				Thread.sleep(500);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
