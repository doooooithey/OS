package os;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class widget extends JFrame {
	int time = 0;
	int pcbNUM = 0;
	boolean Running = false;
	osSystem osSystem = null;
	RunningPro rp = new RunningPro(this);
	Object[] THready = { "PID", "优先级", "运行时间", "起始地址", "需要内存", "属性", "前驱" };
	Object[] THstorage = { "PID", "优先级", "运行时间", "需要内存", "属性", "前驱" };
	Object[] THhanged = { "PID", "优先级", "运行时间", "需要内存", "属性", "前驱" };
	Object[] THrunning = { "PID", "优先级", "运行时间", "所在CPU" };
	Object[] THmainstore = { "起始地址", "大小" };

	JPanel mainPanel = new JPanel();

	DefaultTableModel readyDTM = new DefaultTableModel(THready, 0) {
		public boolean isCellEditable(int row, int column) {
			return false;
		};
	};
	JTable readyTable = new JTable(readyDTM);
	DefaultTableModel hangedDTM = new DefaultTableModel(THhanged, 0) {
		public boolean isCellEditable(int row, int column) {
			return false;
		};
	};
	JTable hangedTable = new JTable(hangedDTM);
	DefaultTableModel storageDTM = new DefaultTableModel(THstorage, 0) {
		public boolean isCellEditable(int row, int column) {
			return false;
		};
	};
	JTable storageTable = new JTable(storageDTM);
	DefaultTableModel runningDTM = new DefaultTableModel(THrunning, 0) {
		public boolean isCellEditable(int row, int column) {
			return false;
		};
	};
	JTable runningTable = new JTable(runningDTM);
	DefaultTableModel memoryDTM = new DefaultTableModel(THmainstore, 0) {
		public boolean isCellEditable(int row, int column) {
			return false;
		};
	};
	JTable memoryTable = new JTable(memoryDTM);

	JButton Bstart = new JButton("开始");
	JButton Bpause = new JButton("暂停");
	JButton Bcreate = new JButton("添加进程");
	JButton Bsy = new JButton("添加同步进程");

	JLabel Jstorage = new JLabel("后备队列");
	JLabel Jready = new JLabel("就绪队列");
	JLabel Jhanged = new JLabel("挂起队列");
	JLabel Jmstore = new JLabel("未分分区表");
	JLabel Jrunning = new JLabel("运行队列");

	public widget() {
		osSystem = new osSystem(2);
		this.setTitle("Widget");
		this.setSize(1000, 800);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocation(50,50);
		this.add(mainPanel);

		mainPanel.setLayout(null);
		mainPanel.add(Jready);
		mainPanel.add(readyTable.getTableHeader());
		mainPanel.add(readyTable);
		mainPanel.add(Jhanged);
		mainPanel.add(hangedTable.getTableHeader());
		mainPanel.add(hangedTable);
		mainPanel.add(Jstorage);
		mainPanel.add(storageTable.getTableHeader());
		mainPanel.add(storageTable);
		mainPanel.add(Jrunning);
		mainPanel.add(runningTable.getTableHeader());
		mainPanel.add(runningTable);
		mainPanel.add(Jmstore);
		mainPanel.add(memoryTable.getTableHeader());
		mainPanel.add(memoryTable);
		mainPanel.add(Bstart);
		mainPanel.add(Bcreate);
		mainPanel.add(Bpause);
		mainPanel.add(Bsy);

		Jready.setSize(100, 20);
		Jready.setLocation(20, 20);
		Jready.setFont(new Font("微软雅黑", Font.PLAIN, 18));
		readyTable.getTableHeader().setSize(350, 20);
		readyTable.getTableHeader().setLocation(20, 40);
		readyTable.setSize(new Dimension(350, 450));
		readyTable.setLocation(20, 60);
		readyTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == e.BUTTON1 && e.getClickCount() == 2) {
					int index = readyTable.getSelectedRow();
					if (index != -1) {
						int pid = (int) readyTable.getValueAt(index, 0);
						if (osSystem.HangINQready(pid)) {
							UpdateTready();
							UpdateThanged();
							UpdateTmainstore();
						}
					}

				}
			}
		});

		Jhanged.setSize(100, 20);
		Jhanged.setLocation(400, 560);
		Jhanged.setFont(new Font("微软雅黑", Font.PLAIN, 18));
		hangedTable.getTableHeader().setSize(350, 20);
		hangedTable.getTableHeader().setLocation(400, 580);
		hangedTable.setSize(new Dimension(350, 100));
		hangedTable.setLocation(400, 600);
		hangedTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == e.BUTTON1 && e.getClickCount() == 2) {
					int index = hangedTable.getSelectedRow();
					if (index != -1) {
						int pid = (int) hangedTable.getValueAt(index, 0);
						if (osSystem.unhang(pid)) {
							UpdateTready();
							UpdateThanged();
							UpdateTmainstore();
							UpdateTstorage();
						}
					}
				}
			}
		});

		Jstorage.setSize(100, 20);
		Jstorage.setLocation(400, 360);
		Jstorage.setFont(new Font("微软雅黑", Font.PLAIN, 18));
		storageTable.getTableHeader().setSize(350, 20);
		storageTable.getTableHeader().setLocation(400, 380);
		storageTable.setSize(new Dimension(350, 100));
		storageTable.setLocation(400, 400);
		storageTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == e.BUTTON1 && e.getClickCount() == 2) {
					int index = storageTable.getSelectedRow();
					if (index != -1) {
						int pid = (int) storageTable.getValueAt(index, 0);
						if (osSystem.HangINQready(pid)) {
							UpdateThanged();
							UpdateTmainstore();
							UpdateTstorage();
						}
					}
				}
			}
		});

		Jrunning.setSize(100, 20);
		Jrunning.setLocation(20, 560);
		Jrunning.setFont(new Font("微软雅黑", Font.PLAIN, 18));
		runningTable.getTableHeader().setSize(350, 20);
		runningTable.getTableHeader().setLocation(20, 580);
		runningTable.setSize(new Dimension(350, 100));
		runningTable.setLocation(20, 600);

		Jmstore.setSize(100, 20);
		Jmstore.setLocation(400, 20);
		Jmstore.setFont(new Font("微软雅黑", Font.PLAIN, 18));
		memoryTable.getTableHeader().setSize(350, 20);
		memoryTable.getTableHeader().setLocation(400, 40);
		memoryTable.setSize(new Dimension(350, 200));
		memoryTable.setLocation(400, 60);

		Bstart.setSize(new Dimension(120, 30));
		Bstart.setLocation(800, 100);
		Bstart.setFont(new Font("微软雅黑", Font.PLAIN, 18));
		Bstart.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (Running == false) {
					Running = true;
					rp.Running = true;
					new Thread(rp).start();
				}
			}
		});
		Bpause.setSize(new Dimension(120, 30));
		Bpause.setLocation(800, 200);
		Bpause.setFont(new Font("微软雅黑", Font.PLAIN, 18));
		Bpause.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Running = false;
				rp.Running = false;
			}
		});
		Bcreate.setSize(new Dimension(120, 30));
		Bcreate.setLocation(800, 300);
		Bcreate.setFont(new Font("微软雅黑", Font.PLAIN, 18));
		Bcreate.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int runTime = (int) (Math.random() * 10 + 1);
				int priority = (int) (Math.random() * 10 + 1);
				int size = (int) (Math.random() * 50 + 50);
				PCB pcb = new PCB(pcbNUM, runTime, priority, size, STATUS.Ready, STATE.Separate);
				if (osSystem.addPCB(pcb) == true)
					UpdateTready();
				else
					UpdateTstorage();
				UpdateTmainstore();
				pcbNUM++;
			}
		});
		Bsy.setSize(new Dimension(120, 30));
		Bsy.setLocation(800, 400);
		Bsy.setFont(new Font("微软雅黑", Font.PLAIN, 18));
		Bsy.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int runTime = (int) (Math.random() * 10 + 1);
				int priority = (int) (Math.random() * 10 + 1);
				int size = (int) (Math.random() * 50 + 50);
				String input = JOptionPane.showInputDialog("输入前驱：");
				if (input == null)
					return;
				String[] strings = input.split(",");
				Vector<Integer> integers = new Vector<>();
				for (String string : strings) {
					int pre = Integer.valueOf(string);
					integers.add(pre);
				}
				PCB pcb = new PCB(pcbNUM, runTime, priority, size, STATUS.Ready, STATE.Synchro, integers);
				osSystem.addPCB(pcb);
				UpdateTready();
				UpdateTstorage();
				UpdateThanged();
				UpdateTmainstore();
				pcbNUM++;
			}
		});

		UpdateTmainstore();
		this.setVisible(true);
	}

	void UpdateTready() {
		// 更新就绪队列
		int readyRowNum = osSystem.Qready.size();
		Object[][] newReadyData = new Object[readyRowNum][7];
		for (int i = 0; i < readyRowNum; i++) {
			newReadyData[i][0] = osSystem.Qready.get(i).PID;
			newReadyData[i][1] = osSystem.Qready.get(i).Priority;
			newReadyData[i][2] = osSystem.Qready.get(i).Runtime;
			newReadyData[i][3] = osSystem.Qready.get(i).Address;
			newReadyData[i][4] = osSystem.Qready.get(i).Resources;
			if (osSystem.Qready.get(i).state == STATE.Synchro) {
				newReadyData[i][5] = "同步";
				String temp = "";
				for (int t : osSystem.Qready.get(i).Former) {
					String tString = String.valueOf(t);
					temp += tString + ",";
				}
				temp = temp.substring(0, temp.length() - 1);
				newReadyData[i][6] = temp;
			} else {
				newReadyData[i][5] = "独立";
				newReadyData[i][6] = "-";
			}
		}
		readyDTM.setDataVector(newReadyData, THready);
	}

	void UpdateTstorage() {
		int storageRowNum = osSystem.Qstorage.size();
		Object[][] newStorageData = new Object[storageRowNum][6];
		for (int i = 0; i < storageRowNum; i++) {
			newStorageData[i][0] = osSystem.Qstorage.get(i).PID;
			newStorageData[i][1] = osSystem.Qstorage.get(i).Priority;
			newStorageData[i][2] = osSystem.Qstorage.get(i).Runtime;
			newStorageData[i][3] = osSystem.Qstorage.get(i).Resources;
			if (osSystem.Qstorage.get(i).state == STATE.Synchro) {
				newStorageData[i][4] = "同步";
				String temp = "";
				for (int t : osSystem.Qstorage.get(i).Former) {
					String tString = String.valueOf(t);
					temp += tString + ",";
				}
				// temp = temp.substring(0, temp.length() - 2);
				newStorageData[i][5] = temp;
			} else {
				newStorageData[i][4] = "独立";
				newStorageData[i][5] = "-";
			}
		}
		storageDTM.setDataVector(newStorageData, THstorage);
	}

	void UpdateThanged() {
		// 更新挂起队列
		int hangedRowNum = osSystem.Qhanged.size();
		Object[][] newHangedData = new Object[hangedRowNum][6];
		for (int i = 0; i < hangedRowNum; i++) {
			newHangedData[i][0] = osSystem.Qhanged.get(i).PID;
			newHangedData[i][1] = osSystem.Qhanged.get(i).Priority;
			newHangedData[i][2] = osSystem.Qhanged.get(i).Runtime;
			newHangedData[i][3] = osSystem.Qhanged.get(i).Resources;
			if (osSystem.Qhanged.get(i).state == STATE.Synchro) {
				newHangedData[i][4] = "同步";
				String temp = "";
				for (int t : osSystem.Qhanged.get(i).Former) {
					String tString = String.valueOf(t);
					temp += tString + ",";
				}
				// temp = temp.substring(0, temp.length() - 2);
				newHangedData[i][5] = temp;
			} else {
				newHangedData[i][4] = "独立";
				newHangedData[i][5] = "-";
			}
		}
		hangedDTM.setDataVector(newHangedData, THhanged);
	}

	void UpdateRunning() {
		// 更新运行队列
		Vector<PCB> temp = osSystem.gettingCPU();
		int runningRowNum = temp.size();
		Object[][] newRunningData = new Object[runningRowNum][4];
		for (int i = 0; i < runningRowNum; i++) {
			newRunningData[i][0] = temp.get(i).PID;
			newRunningData[i][1] = temp.get(i).Priority;
			newRunningData[i][2] = temp.get(i).Runtime;
			newRunningData[i][3] = temp.get(i).runningCPU;
		}
		runningDTM.setDataVector(newRunningData, THrunning);
	}

	void UpdateTmainstore() {
		// 更新未分分区表
		Vector<unit> temp = osSystem.gTables();
		int memoryRowNum = temp.size();
		Object[][] newMemoryData = new Object[memoryRowNum][2];
		for (int i = 0; i < memoryRowNum; i++) {
			newMemoryData[i][0] = temp.get(i).start;
			newMemoryData[i][1] = temp.get(i).size;
		}
		memoryDTM.setDataVector(newMemoryData, THmainstore);
	}

	public static void main(String[] args) {
		new widget();
	}
}

class RunningPro implements Runnable {
	boolean Running = false;
	widget w = null;

	public RunningPro(widget widget) {
		// TODO Auto-generated constructor stub
		this.w = widget;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (Running) {
			w.osSystem.running();
			w.UpdateRunning();
			w.UpdateThanged();
			w.UpdateTmainstore();
			w.UpdateTready();
			w.UpdateTstorage();
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}