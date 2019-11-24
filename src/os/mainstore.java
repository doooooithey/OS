package os;

import java.util.Vector;

public class mainstore {
	int storage;
	Vector<unit> unitTable = new Vector<>();

	public mainstore() {
		storage = 500;
		unitTable.add(new unit(500, 0));
	}

	public boolean AddPCB(PCB pcb) {
		for (int i = 0; i < unitTable.size(); i++) {
			if (unitTable.get(i).size > pcb.Resources) {
				pcb.Address = unitTable.get(i).start;
				unitTable.get(i).start = unitTable.get(i).start + pcb.Resources;
				unitTable.get(i).size = unitTable.get(i).size - pcb.Resources;
				return true;
			} else if (unitTable.get(i).size == pcb.Resources) {
				pcb.Address = unitTable.get(i).start;
				unitTable.remove(i);
				return true;
			}
		}
		return false;

	}

	public void restore(PCB pcb) {// delete pcb
		int i = 0;
		for (i = 0; i < unitTable.size() && unitTable.get(i).start < pcb.Address; i++)
			;

		if (i == 0 || unitTable.get(i - 1).start + unitTable.get(i - 1).size < pcb.Address) {
			if (i == unitTable.size() || unitTable.get(i).start > pcb.Address + pcb.Resources) {
				unitTable.add(i, new unit(pcb.Resources, pcb.Address));
			} else {
				unitTable.get(i).start = pcb.Address;
				unitTable.get(i).size += pcb.Resources;
			}
		} else {
			if (i == unitTable.size() || unitTable.get(i).start > pcb.Address + pcb.Resources) {
				unitTable.get(i - 1).size = unitTable.get(i - 1).size + pcb.Resources;
			} else {
				unitTable.get(i - 1).size = unitTable.get(i - 1).size + pcb.Resources + unitTable.get(i).size;
				unitTable.remove(i);
			}
		}

	}
}

class unit {// pcb unit
	int size;
	int start;

	public unit(int size, int start) {
		this.size = size;
		this.start = start;
	}
}