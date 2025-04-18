package br.com.erick.sms.vision;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import br.com.erick.sms.controller.Controller;
import br.com.erick.sms.utils.CommandInterpreter;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	private Controller ctrl;

	private JPanel contentPanel;

	private JTextField textField;
	
	private JButton lastPressed;
	
	private CommandInterpreter interpreter;

	public MainWindow() {

		this.ctrl = Controller.getInstance();
		this.interpreter = new CommandInterpreter(ctrl);
		
		this.setLayout(new BorderLayout());

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new GridLayout(1, 3));
		
		this.contentPanel = new JPanel();
		this.contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		
		JScrollPane scrollPanel = new JScrollPane(contentPanel);
		scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		this.textField = getTextField();
		
		JButton b = new JButton();

		JButton[] panelButtons = getPanelButtons();

		buttonsPanel.add(panelButtons[0]);
		buttonsPanel.add(panelButtons[1]);
		buttonsPanel.add(panelButtons[2]);
		
		this.add(buttonsPanel, BorderLayout.NORTH);
		this.add(scrollPanel, BorderLayout.CENTER);
		this.add(textField, BorderLayout.SOUTH);

		this.setTitle("Shop Manager System v1.0");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(new Dimension(800, 600));
		this.setVisible(true);
	}

	private JButton[] getPanelButtons() {
		JButton[] buttons = new JButton[3];

		buttons[0] = new JButton("Products");

		buttons[0].addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				lastPressed = buttons[0];
				List<String> products = ctrl.getAllProducts();
				contentPanel.removeAll();
				products.forEach(p -> contentPanel.add(new JLabel(p)));
				contentPanel.revalidate();
				contentPanel.repaint();
			}
		});

		buttons[1] = new JButton("Cart");

		buttons[1].addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				lastPressed = buttons[1];
				List<String> cart = ctrl.getCart();
				contentPanel.removeAll();
				cart.forEach(c -> contentPanel.add(new JLabel(c)));
				contentPanel.revalidate();
				contentPanel.repaint();
			}
		});

		buttons[2] = new JButton("Sales");

		buttons[2].addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				lastPressed = buttons[2];
				List<String> sales = ctrl.getAllSales();
				contentPanel.removeAll();
				sales.forEach(s -> {
					
					String id = s.trim().substring(0, 1);
					
					List<String> saleDet = ctrl.getSaleDet(id);
					
				    JPanel vendaPanel = new JPanel();
				    vendaPanel.setLayout(new BoxLayout(vendaPanel, BoxLayout.Y_AXIS));
				    vendaPanel.setAlignmentX(LEFT_ALIGNMENT);

				    JButton vendaButton = new JButton(s);

				    JPanel detalhesPanel = new JPanel();
				    detalhesPanel.setLayout(new BoxLayout(detalhesPanel, BoxLayout.Y_AXIS));
				    detalhesPanel.setBackground(new java.awt.Color(240, 240, 240));
				    detalhesPanel.setVisible(false);

				    for(String sd : saleDet) {
				    	detalhesPanel.add(new JLabel(sd));
				    }

				    vendaButton.addActionListener(ev -> {
				        detalhesPanel.setVisible(!detalhesPanel.isVisible());
				        vendaPanel.revalidate();
				        vendaPanel.repaint();
				    });

				    vendaPanel.add(vendaButton);
				    vendaPanel.add(detalhesPanel);
				    contentPanel.add(vendaPanel);
				});
				contentPanel.revalidate();
				contentPanel.repaint();
			}
		});

		return buttons;
	}

	private JTextField getTextField() {
		JTextField tf = new JTextField();
		tf.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String txt = tf.getText();
				try{
					interpreter.interpret(txt);
				}catch(RuntimeException ex) {
					JOptionPane.showMessageDialog(contentPanel, ex.getMessage());
				}
				tf.setText("");
				lastPressed.doClick();
			}
		});
		return tf;
	}
}
