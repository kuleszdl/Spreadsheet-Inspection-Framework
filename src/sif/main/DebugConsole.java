package sif.main;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class DebugConsole extends JFrame implements Runnable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextArea taText;

	public DebugConsole(){
		super("SIF Debug Console");
		setSize(500, 500);
		taText = new JTextArea();
		JScrollPane spRoot = new JScrollPane(taText);
		
		add(spRoot);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public void addText(String text){
		taText.append(text);
	}
	
	public void addStackTrace(Throwable e){
		taText.append(e.getMessage() + "\n");
		for (StackTraceElement st : e.getStackTrace()){
			taText.append(st.toString() + "\n");
		}
	}

	@Override
	public void run() {
		setVisible(true);
	}
}
