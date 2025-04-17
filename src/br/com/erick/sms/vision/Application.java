package br.com.erick.sms.vision;

import java.util.List;
import java.util.Scanner;

import br.com.erick.sms.controller.Controller;

public class Application {

	private static Controller control;

	public static void main(String[] args) {
		control = Controller.getInstance();
		Scanner scan = new Scanner(System.in);
		String opt = "";

		fmsg();

		while (!opt.equals("12")) {
			opt = scan.nextLine();
			redirect(opt);
		}

		control.closeConn();

		System.out.println("Bye!");
	}

	private static void redirect(String option) {
		if (option == "6")
			return;
		switch (option) {
		case "0":
			System.out.print("\033[H\033[2J");
			commands();
			break;
		case "1":
			System.out.print("\033[H\033[2J");
			addNewProduct();
			break;
		case "2":
			System.out.print("\033[H\033[2J");
			addToCart();
			break;
		case "3":
			System.out.print("\033[H\033[2J");
			removeFromCart();
			break;
		case "4":
			System.out.print("\033[H\033[2J");
			cartList();
			break;
		case "5":
			System.out.print("\033[H\033[2J");
			control.closeCart();
			break;
		case "6":
			System.out.print("\033[H\033[2J");
			control.clearCart();
			break;
		case "7":
			System.out.print("\033[H\033[2J");
			prodList();
			break;
		case "8":
			System.out.print("\033[H\033[2J");
			salesList();
			break;
		case "9":
			System.out.print("\033[H\033[2J");
			saleDetails();
			break;
		case "10":
			System.out.print("\033[H\033[2J");
			addStock();
			break;
		case "11":
			System.out.print("\033[H\033[2J");
			removeProduct();
			break;
		default:
			return;
		}
	}

	private static void removeProduct() {
		Scanner s = new Scanner(System.in);
		prodList();
		System.out.println("Enter the product ID to remove");
		String id = s.nextLine();
		control.removeProduct(id);
	}

	private static void addStock() {
		prodList();
		Scanner s = new Scanner(System.in);
		System.out.println("Enter the product id to add");
		String id = s.nextLine();
		System.out.println("Enter the amount to add");
		String qt = s.nextLine();
		control.addStock(id, qt);
	}

	private static void removeFromCart() {
		cartList();
		Scanner s = new Scanner(System.in);
		System.out.println("Enter the product id to remove: ");
		String id = s.nextLine();

		control.removeFromCart(id);
	}

	private static void addNewProduct() {
		Scanner s = new Scanner(System.in);
		System.out.println("Enter the product name:");
		String name = s.nextLine();
		System.out.println("Enter the product value:");
		String value = s.nextLine();
		System.out.println("Enter quantity in stock");
		String stock = s.nextLine();
		control.addNewProduct(name, value, stock);
	}

	private static void addToCart() {
		prodList();
		Scanner s = new Scanner(System.in);
		System.out.println("Enter the product id");
		String id = s.nextLine();
		System.out.println("Enter the product quantity");
		String qt = s.nextLine();

		control.addToCart(id, qt);
	}

	private static void saleDetails() {
		salesList();
		System.out.println("Enter the sale ID: ");
		Scanner s = new Scanner(System.in);
		String id = s.nextLine();
		String sale = control.getSaleDet(id);
		System.out.println(sale);
	}

	private static void prodList() {
		List<String> prods = control.getAllProducts();
		System.out.println("---------------------- PRODUCTS LIST ----------------------------------------------------------------------------------------------------------\n");
		prods.forEach(p -> System.out.println(p + "\n"));
	}

	private static void salesList() {
		List<String> sales = control.getAllSales();
		System.out.println("------------------------- SALES LIST ------------------------------\n");
		sales.forEach(s -> System.out.println(s + "\n"));
	}

	private static void cartList() {
		List<String> cart = control.getCart();
		System.out.println("----------------------- ITENS IN THE CART ----------------------------------------------------------------------------------------------------\n");
		cart.forEach(c -> System.out.println(c + "\n"));
		System.out.println("===== TOTAL: R$ " + control.getCartTotal());
	}

	private static void fmsg() {
		System.out.println("\n>>>>>>>> SHOP MANAGER SYSTEM"
				+ "\n\nA simple solution for store products information and register sales!\n"
				+ "\nv0.2 by Erick Andrade\n" + "\n>> Type 0 for help\n");
	}

	private static void commands() {
		System.out.println(
				"\n[0] - Help	\n[1] - Add new product   \n[2] - Add product to the cart   \n[3] - Remove product of the cart"
						+ "\n[4] - See the cart    \n[5] - Close the cart            \n[6] - Clear the cart"
						+ "\n[7] - See all products 	\n[8] - See all sales	 \n[9] - See sale details"
						+ "\n[10] - Restock product	\n[11] - Remove product	\n[12] - Exit");
	}
}
