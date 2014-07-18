package sif.main;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				super.windowClosed(e);
				System.exit(-10);
			}
		});
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
