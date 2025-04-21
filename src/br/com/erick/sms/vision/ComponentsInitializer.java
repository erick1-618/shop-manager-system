package br.com.erick.sms.vision;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class ComponentsInitializer {
	
	private ComponentsInitializer() {
	}
	
	public static JButton[] getPanelButtons(MainWindow m) {
		JButton[] buttons = new JButton[3];

		buttons[0] = new JButton("Products");

		buttons[0].addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				m.lastPressed = buttons[0];
				List<String> products = m.ctrl.getAllProducts();
				m.contentPanel.removeAll();
				products.forEach(p -> {
					JButton b = new JButton(p);

					b.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							if (!b.getText().substring(b.getText().length() - 12).trim().equals("0")) {
								m.interpreter.interpret("cart " + p.substring(0, 10).trim() + " 1");
								m.lastPressed.doClick();
							}
						}
					});

					b.setFont(new Font("Monospaced", Font.PLAIN, 14));
					b.setBackground(Color.lightGray);
					m.contentPanel.add(b);
				});
				m.contentPanel.revalidate();
				m.contentPanel.repaint();
			}
		});

		buttons[1] = new JButton("Cart");

		buttons[1].addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				m.lastPressed = buttons[1];
				List<String> cart = m.ctrl.getCart();
				m.contentPanel.removeAll();

				cart.forEach(c -> {
					JLabel label = new JLabel(c);
					label.setFont(new Font("Monospaced", Font.PLAIN, 14));
					m.contentPanel.add(label);
				});
				m.contentPanel.revalidate();
				m.contentPanel.repaint();
			}
		});

		buttons[2] = new JButton("Sales");

		buttons[2].addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				m.lastPressed = buttons[2];
				List<String> sales = m.ctrl.getAllSales();
				m.contentPanel.removeAll();
				sales.forEach(s -> {

					String id = s.trim().substring(0, 1);

					List<String> saleDet = null;

					if (!id.substring(0, 1).equals("T")) {
						saleDet = m.ctrl.getSaleDet(id);
					}

					JPanel vendaPanel = new JPanel();
					vendaPanel.setLayout(new BoxLayout(vendaPanel, BoxLayout.Y_AXIS));
					vendaPanel.setAlignmentX(0.0f);

					JButton vendaButton = new JButton(s);
					vendaButton.setBackground(Color.lightGray);
					vendaButton.setHorizontalAlignment(SwingConstants.LEFT);
					vendaButton.setFont(new Font("Monospaced", Font.PLAIN, 14));
					vendaButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, vendaButton.getPreferredSize().height));

					JPanel detalhesPanel = new JPanel();
					detalhesPanel.setLayout(new BoxLayout(detalhesPanel, BoxLayout.Y_AXIS));
					detalhesPanel.setBackground(new java.awt.Color(240, 240, 240));
					detalhesPanel.setVisible(false);

					if (saleDet != null) {
						for (String sd : saleDet) {
							JLabel l = new JLabel(sd);
							l.setFont(new Font("Monospaced", Font.PLAIN, 14));
							detalhesPanel.add(l);
						}
					}

					vendaButton.addActionListener(ev -> {
						detalhesPanel.setVisible(!detalhesPanel.isVisible());
						vendaPanel.revalidate();
						vendaPanel.repaint();
					});

					vendaPanel.add(vendaButton);
					vendaPanel.add(detalhesPanel);
					m.contentPanel.add(vendaPanel);
				});
				m.contentPanel.revalidate();
				m.contentPanel.repaint();
			}
		});

		return buttons;
	}
	
	public static JTextField getTextField(MainWindow m) {
		JTextField tf = new JTextField();
		tf.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String txt = tf.getText();
				try {
					m.interpreter.interpret(txt);
				} catch (RuntimeException ex) {
					JOptionPane.showMessageDialog(m.contentPanel, ex.getMessage());
				}
				tf.setText("");

				if (m.lastPressed != null)
					m.lastPressed.doClick();
			}
		});
		return tf;
	}
}
