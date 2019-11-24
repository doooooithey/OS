package os;

public class CPU {
    PCB running=null;
    int num;
    public CPU(int n) {
    	num=n;
    }
    
    public void run(PCB pcb) {
    	running=pcb;
    	pcb.status=STATUS.Running;
    	pcb.Priority-=1;
    	pcb.Runtime-=1;
    	pcb.runningCPU=num;
    }
}
