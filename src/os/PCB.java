package os;

import java.util.Vector;

enum STATUS {
	Ready, // 就绪
	Blocked, // 阻塞
	Running, // 运行
	Waiting, // 后备
	Hanging;// 挂起
}

enum STATE {
	Separate, // 独立
	Synchro;// 同步
}

public class PCB {
	int PID;
	int Runtime;
	int Priority;
	int Address;
	int Resources;
	Vector<Integer> Former;
	STATUS status;
	STATE state;
	int runningCPU;

	public PCB(int pid, int time, int priority, int resources, STATUS status1, STATE state1) {
		PID = pid;
		Runtime = time;
		Priority = priority;
		Resources = resources;
		status = status1;
		state = state1;
	}

	public PCB(int pid, int time, int priority, int resources, STATUS status1, STATE state1, Vector<Integer> former) {
		PID = pid;
		Runtime = time;
		Priority = priority;
		Resources = resources;
		status = status1;
		state = state1;
		Former = former;
	}
}
