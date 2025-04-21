package br.com.erick.sms.vision;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
import javax.swing.SwingConstants;

import br.com.erick.sms.controller.Controller;
import br.com.erick.sms.utils.CommandInterpreter;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	protected Controller ctrl;

	protected JPanel contentPanel;

	protected JTextField textField;

	protected JButton lastPressed;

	protected CommandInterpreter interpreter;

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

		this.textField = ComponentsInitializer.getTextField(this);

		JButton b = new JButton();

		JButton[] panelButtons = ComponentsInitializer.getPanelButtons(this);

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
}
