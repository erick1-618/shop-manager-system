package br.com.erick.sms;

import br.com.erick.sms.controller.Controller;

public class AddExamples {
	public static void main(String[] args) {
		Controller c = Controller.getInstance();
		
		c.addExamples();
	}
}
