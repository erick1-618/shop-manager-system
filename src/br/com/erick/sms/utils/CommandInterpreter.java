package br.com.erick.sms.utils;

import java.security.InvalidParameterException;
import java.util.Arrays;

import br.com.erick.sms.controller.Controller;

public class CommandInterpreter {

	private static final String DCEM = "Invalid Command. Press Help button for commands avaliable";

	private Controller ctrl;

	public CommandInterpreter(Controller ctrl) {
		this.ctrl = ctrl;
	}

	public void interpret(String command) {
		String[] cmd = command.split(" ");
		String macro = cmd[0];
		String[] payload = Arrays.copyOfRange(cmd, 1, cmd.length);
		switch (macro) {
		case "new":
			newProduct(payload);
			break;
		case "del":
			deleteProduct(payload);
			break;
		case "cart":
			addToCart(payload);
			break;
		case "rem":
			removeFromCart(payload);
			break;
		case "close":
			closeCart(payload);
			break;
		case "add":
			addStock(payload);
			break;
		case "clear":
			clearCart(payload);
			break;
		default:
			throw new RuntimeException(DCEM);
		}
	}

	private void addStock(String[] payload) {
		try {
			if(payload.length != 2)
				throw new RuntimeException();
			
			ctrl.addStock(payload[0], payload[1]);
			
		}catch(RuntimeException e) {
			throw new RuntimeException(DCEM);
		}
	}

	private void closeCart(String[] payload) {
		try {
			if(payload.length != 0) 
				throw new RuntimeException();
			ctrl.closeCart();
		}catch(RuntimeException e) {
			if(e instanceof NullPointerException)
				throw new RuntimeException("The cart is empty");
			throw new RuntimeException(DCEM);
		}
	}

	private void addToCart(String[] payload) {
		try {
			if (payload.length != 2)
				throw new RuntimeException();
			long id = Long.parseLong(payload[0]);
			int qt = Integer.parseInt(payload[1]);
			ctrl.addToCart("" + id, "" + qt);
		} catch (Exception e) {
			if (e instanceof InvalidParameterException)
				throw new RuntimeException("No enough stock");
			throw new RuntimeException(DCEM);
		}
	}

	private void newProduct(String[] payload) {
		try {
			Integer stock = Integer.parseInt(payload[payload.length - 1]);
			Double unitvalue = Double.parseDouble(payload[payload.length - 2]);
			String assembledName = "";
			for (int i = 0; i < payload.length - 2; i++) {
				assembledName += payload[i] + " ";
			}
			assembledName = assembledName.trim();
			if (!assembledName.matches("^[A-Za-zÀ-ÿ][A-Za-zÀ-ÿ0-9 ]*$") || unitvalue <= 0 || assembledName.length() > 50
					|| stock < 0)
				throw new RuntimeException();
			ctrl.addNewProduct(assembledName, "" + unitvalue, "" + stock);
		} catch (Exception e) {
			throw new RuntimeException(DCEM);
		}
	}

	private void deleteProduct(String[] payload) {
		try {
			for (int i = 0; i < payload.length; i++) {
				ctrl.removeProduct(payload[i]);
			}
		} catch (Exception e) {
			if (e instanceof InvalidParameterException)
				throw new RuntimeException("There's a product with the given id in the cart");
			throw new RuntimeException(DCEM);
		}
	}

	private void removeFromCart(String[] payload) {
		try {
			for (int i = 0; i < payload.length; i++) {
				ctrl.removeFromCart(payload[i]);
			}
		} catch (Exception e) {
			throw new RuntimeException(DCEM);
		}
	}
	
	private void clearCart(String[] payload) {
		try {
			if(payload.length != 0) 
				throw new RuntimeException();
			ctrl.clearCart();
		}catch(RuntimeException e) {
			e.printStackTrace();
			throw new RuntimeException(DCEM);
		}
	}
}
