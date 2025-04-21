# Shop Manager System

**Shop Manager System** is a Java application designed to help manage a store, providing features such as product management, inventory control, and sales processing. The project contais a suport for Postgres 16 connection, providing a scalable solution for a large amount of data.

**Shop Manager System** it's a improved and complete rewrite of [Simple Item Manager](https://github.com/erick1-618/simple-item-manager)

## Features

- Product registration
- Sales and products tracking
- Sales processing
- Sales per product tracking
- Stock control
- UI for see products, cart and sales  

## Prerequisites

- JRE 11 or higher
- Postgres SQL configured
- Unix-like system (Linux, macOS) for automated installation (for Windows, see the manual installation)

## Installation

Clone this repository:

```
git clone https://github.com/erick1-618/shop-manager-system.git
```

Change to the project directory:

```
cd shop-manager-system
```

Run the installation script:

```
./install.sh
```
This will compile the source files and ask your credentials to connect to postgres database, it will also ask the name of the DB, for create a new one. Make sure there's not a database with the given name.  

For running the application in Linux OS:

```
./run.sh
```

These are the steps for manual installation and execution:

1. First, you need to configure **config.prop** in **/resources**. Create a file with this name, and follow the **config-example** template.
2. Then, you must compile the classes. In CMD, on the root directory of the repository, execute: `for /R src %i in (*.java) do javac -cp "lib/*" -d bin %i`
3. For execution: `java -cp "bin;lib/*" br.com.erick.sms.vision.Application`

## How to use

In the JFrame, you will have a text field to entry the avaliable commands:

1. Create a new product with the given name, value and stock

	`` new product_name unitary_value stock_avaliable``
	
2. Add the product with the given name and quantity in the cart. You can also click in the product.

	``cart product_id quantity ``

3. Delete the product with the given id

	``del produt_id1 product_id2 ... product_idn``

4. Remove the product with the given id of the cart (the id in the cart section)

	``rem product_id1 product_id2 ... product_idn``

5. Close the cart and create the sale

	``close``

6. Clear the cart

	``clear``

7. Add product units

	``add product_id quantity``

8. See the avaliable commands
	
	``help``


## License

This project is under GLPv3 License
