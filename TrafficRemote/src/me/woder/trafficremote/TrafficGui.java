package me.woder.trafficremote;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;


public class TrafficGui {
	JPanel container;
	TrafficRemote traffic;
	private JTextField textField;
	private JTextPane textArea;
	public DefaultStyledDocument doc = new DefaultStyledDocument();
	public HashMap<String, AttributeSet> attributes;
	
	public TrafficGui(TrafficRemote traffice) {
		this.traffic = traffice;
		
        JFrame window = new JFrame("TrafficControl");
        window.setResizable(false);
        container = new JPanel();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   
        initilise(); //setup all the mess of variables
        
        TrafficContentPane trcf = new TrafficContentPane();
        JScrollPane scrollPane = new JScrollPane();
        textArea = new JTextPane(doc);
        DefaultCaret caret = (DefaultCaret)textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        scrollPane.setViewportView(textArea);
        textArea.setEditable(false);
        
        textField = new JTextField();
        textField.setColumns(10);
        
        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
            	/*traffic.tgui.insertText(textField.getText() + "\n", "green");
                textField.setText("");*/
            	traffic.chandle.processCommand(textField.getText());
            	textField.setText("");
            }
        });
        
        JButton btnSend = new JButton("Send");
        
        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {                   
            	traffic.chandle.processCommand(textField.getText());
            	textField.setText("");
            }
        });
        
        GroupLayout gl_container = new GroupLayout(container);
        gl_container.setHorizontalGroup(
        	gl_container.createParallelGroup(Alignment.LEADING)
        		.addGroup(Alignment.TRAILING, gl_container.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(gl_container.createParallelGroup(Alignment.TRAILING)
        				.addComponent(scrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 694, Short.MAX_VALUE)
        				.addComponent(trcf, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 694, Short.MAX_VALUE)
        				.addGroup(gl_container.createSequentialGroup()
        					.addComponent(textField, GroupLayout.DEFAULT_SIZE, 631, Short.MAX_VALUE)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(btnSend)))
        			.addContainerGap())
        );
        gl_container.setVerticalGroup(
        	gl_container.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_container.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(trcf, GroupLayout.PREFERRED_SIZE, 411, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(gl_container.createParallelGroup(Alignment.BASELINE)
        				.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(btnSend))
        			.addContainerGap())
        );
        container.setLayout(gl_container); 
        System.out.println(container.getPreferredSize());
       
        
        window.getContentPane().add(container, BorderLayout.SOUTH);
        window.pack();
        window.setVisible(true);
        System.out.println(window.getPreferredSize());
    }
	
	public void initilise(){
		final StyleContext cont = StyleContext.getDefaultStyleContext();
	    final AttributeSet black = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(0,0,0));
	    final AttributeSet blue = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(0,0,170));
	    final AttributeSet green = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(0,170,0));
	    final AttributeSet dark_aqua = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(0,170,170));
	    final AttributeSet dark_red = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(170,0,0));
	    final AttributeSet purple = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(170,0,170));
	    final AttributeSet orange = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(255,170,0));
	    final AttributeSet grey = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(170,170,170));
	    final AttributeSet dark_grey = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(85,85,85));
	    final AttributeSet indigo = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(85,85,255));
	    final AttributeSet bright_green = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(85,255,85));
	    final AttributeSet aqua = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(85,255,255));
	    final AttributeSet red = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(255,85,85));
	    final AttributeSet pink = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(255,85,255));
	    final AttributeSet yellow = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(255,255,85));
	    final AttributeSet white = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(255,255,255));
	    final AttributeSet reset = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.black);	    
	    
	    attributes = new HashMap<String, AttributeSet>();
        attributes.put("black", black);
        attributes.put("dark_blue", blue);
        attributes.put("dark_green", green);
        attributes.put("dark_aqua", dark_aqua);
        attributes.put("dark_red", dark_red);
        attributes.put("dark_purple", purple);
        attributes.put("gold", orange);
        attributes.put("gray", grey);
        attributes.put("dark_gray", dark_grey);
        attributes.put("blue", indigo);
        attributes.put("green", bright_green);
        attributes.put("aqua", aqua);
        attributes.put("red", red);
        attributes.put("light_purple", pink);
        attributes.put("yellow", yellow);
        attributes.put("white", black);
        attributes.put("reset", reset);
	}
	
	public void insertText(String text, String colour){
		AttributeSet attribute = attributes.get(colour);

        try{
            int len = doc.getLength();
            doc.insertString(len, text, attribute);              
        }catch (BadLocationException e){
            e.printStackTrace();
        }
		
	}

	public void tick() {

	}

}
