package os;

import java.util.Comparator;
import java.util.Vector;

public class osSystem {
	int cpuNUM;
	mainstore mainstore = null;
	Vector<PCB> Qstorage = new Vector<>();
	Vector<PCB> Qready = new Vector<>();
	Vector<PCB> Qhanged = new Vector<>();
	Vector<CPU> Qcpu = new Vector<>();
	Vector<Integer> Qcomplete = new Vector<>();

	public osSystem(int cpu) {
		mainstore = new mainstore();
		cpuNUM = cpu;
		for (int i = 0; i < cpuNUM; i++) {
			Qcpu.add(new CPU(i));
		}
		Qcomplete.add(Integer.valueOf("-1"));
	}

	public void running() {

		boolean removed = false;

		for (int i = 0; i < cpuNUM; i++) {
			Qcpu.get(i).running = null;
		}

		for (int i = 0; i < Qready.size();) {
			Qready.get(i).status = STATUS.Ready;
			if (Qready.get(i).Runtime == 0) {
				mainstore.restore(Qready.get(i));
				Qcomplete.add(Qready.get(i).PID);
				Qready.remove(i);
				removed = true;
			} else {
				i++;
			}
		}

		if (removed == true) {
			Qstorage.sort(new Comparator<PCB>() {// 后备队列的排序
				@Override
				public int compare(PCB p1, PCB p2) {
					// TODO Auto-generated method stub
					if (p1.Priority > p2.Priority) {
						return -1;
					} else if (p1.Priority == p2.Priority) {
						return 0;
					} else {
						return 1;
					}
				}
			});

			for (int i = 0; i < Qstorage.size();) {// 后备入就绪
				boolean flag = mainstore.AddPCB(Qstorage.get(i));
				if (flag == true) {
					Qready.add(Qstorage.get(i));
					Qstorage.remove(i);
				} else {
					i++;
				}
				removed = false;
			}


		}

		Qready.sort(new Comparator<PCB>() {// 就绪队列的排序
			@Override
			public int compare(PCB p1, PCB p2) {
				// TODO Auto-generated method stub
				if (p1.Priority > p2.Priority) {
					return -1;
				} else if (p1.Priority == p2.Priority) {
					return 0;
				} else {
					return 1;
				}
			}
		});
		
		int j = 0;
		for (int i = 0; j < cpuNUM && i < Qready.size(); i++) {
			PCB temp = Qready.get(i);
			if (temp.state == STATE.Separate) {
				Qcpu.get(j).run(temp);
				j++;
			} else {
				boolean flag = true;
				for (int t : temp.Former) {
					if (!Qcomplete.contains(t)) {
						flag = false;
						break;
					}
				}
				if (flag) {
					Qcpu.get(j).run(temp);
					j++;
				}
			}
		}

		/*
		 * int i = 0; for (int j = 0; i < Qcpu.size() && j < Qcpu.size(); j++) { PCB
		 * temp = Qready.get(j); if (temp.state == STATE.Separate || (temp.state ==
		 * STATE.Synchro && Qcomplete.contains(temp.Former))) { Qcpu.get(i).run(temp);
		 * i++; } }
		 */
	}

	boolean addPCB(PCB pcb) {
		boolean flag = mainstore.AddPCB(pcb);
		if (pcb.state == STATE.Synchro) {
			for (int i = 0; i < pcb.Former.size(); i++) {
				if (!Qcomplete.contains(pcb.Former.get(i))) {
					for (int j = 0; j < Qready.size(); j++) {
						if (pcb.Former.get(i) == Qready.get(j).PID) {
							if (Qready.get(j).state == STATE.Separate) {
								Qready.get(j).state = STATE.Synchro;
								Qready.get(j).Former = new Vector<>();
								Qready.get(j).Former.addElement(Integer.valueOf("-1"));
							}
						}
					}
					for (int j = 0; j < Qstorage.size(); j++) {
						if (pcb.Former.get(i) == Qstorage.get(j).PID) {
							if (Qstorage.get(j).state == STATE.Separate) {
								Qstorage.get(j).state = STATE.Synchro;
								Qstorage.get(j).Former = new Vector<>();
								Qstorage.get(j).Former.addElement(Integer.valueOf("-1"));
							}
						}
					}
					for (int j = 0; j < Qhanged.size(); j++) {
						if (pcb.Former.get(i) == Qhanged.get(j).PID) {
							if (Qhanged.get(j).state == STATE.Separate) {
								Qhanged.get(j).state = STATE.Synchro;
								Qhanged.get(j).Former = new Vector<>();
								Qhanged.get(j).Former.addElement(Integer.valueOf("-1"));
							}
						}
					}
				}
			}
		}
		if (flag == false) {
			Qstorage.add(pcb);
			return false;
		} else {
			Qready.add(pcb);
			return true;
		}
	}

	Vector<PCB> gettingCPU() {
		Vector<PCB> re = new Vector<>();
		for (int i = 0; i < cpuNUM; i++) {
			if (Qcpu.get(i).running != null) {
				re.add(Qcpu.get(i).running);
			}
		}
		return re;
	}

	Vector<unit> gTables() {
		return mainstore.unitTable;
	}

	boolean HangINQready(int pid) {
		for (int i = 0; i < Qready.size(); i++) {
			if (Qready.get(i).PID == pid) {
				if (Qready.get(i).status == STATUS.Ready) {
					PCB pcb = Qready.get(i);
					Qready.remove(i);
					mainstore.restore(pcb);
					pcb.status = STATUS.Hanging;
					Qhanged.add(pcb);
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}

	boolean unhang(int pid) {
		for (int i = 0; i < Qhanged.size(); i++) {
			if (Qhanged.get(i).PID == pid) {
				PCB pcb = Qhanged.get(i);
				Qhanged.remove(i);
				addPCB(pcb);
				return true;
			}
		}
		return false;
	}

}
