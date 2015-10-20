package me.woder.trafficremote;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextField;


public class TrafficGui {
	JPanel container;
	private JTextField textField;
	
	public TrafficGui() {
        JFrame window = new JFrame("TrafficControl");
        window.setResizable(false);
        container = new JPanel();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);       
        
        TrafficContentPane trcf = new TrafficContentPane();
        
        textField = new JTextField();
        textField.setColumns(10);
        
        JButton btnSend = new JButton("Send");
        GroupLayout gl_container = new GroupLayout(container);
        gl_container.setHorizontalGroup(
        	gl_container.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_container.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(gl_container.createParallelGroup(Alignment.LEADING)
        				.addComponent(trcf, GroupLayout.DEFAULT_SIZE, 694, Short.MAX_VALUE)
        				.addGroup(gl_container.createSequentialGroup()
        					.addComponent(textField, GroupLayout.PREFERRED_SIZE, 436, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(btnSend)))
        			.addContainerGap())
        );
        gl_container.setVerticalGroup(
        	gl_container.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_container.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(trcf, GroupLayout.PREFERRED_SIZE, 411, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED, 85, Short.MAX_VALUE)
        			.addGroup(gl_container.createParallelGroup(Alignment.BASELINE)
        				.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(btnSend))
        			.addContainerGap())
        );
        container.setLayout(gl_container); 
        System.out.println(container.getPreferredSize());
        
        window.getContentPane().add(container);
        window.pack();
        window.setVisible(true);
        System.out.println(window.getPreferredSize());
    }
}
