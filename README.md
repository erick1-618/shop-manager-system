# Shop Manager System

**Shop Manager System** is a Java application designed to help manage a store, providing features such as product management, inventory control, and sales processing. The project contais a suport for Postgres 16 connection, providing a scalable solution for a large amount of data.

**Shop Manager System** it's a improved and complete rewrite of [Simple Item Manager](https://github.com/erick1-618/simple-item-manager)

## Features

- Product registration
- Sales and products tracking
- Sales processing
- Sales per product tracking
- Stock control

### Upcomming Features

- UI for better experience

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
This will compile the source files and ask your credenctials to connect to postgres database, it will also ask the name of the DB, for create a new one. Make sure there's not a databse with the given name.  

For running the application in Linux OS:

```
./run.sh
```

Type 0 to see the possible commands. 

These are the steps for manual installation and execution:

1. First, you need to configure **config.prop** in **/resources**. Create a file with this name, and follow the **config-example** template.
2. Then, you must compile the classes. In CMD, on the root directory of the repository, execute: `for /R src %i in (*.java) do javac -cp "lib/*" -d bin %i`
3. For execution: `java -cp "bin;lib/*" br.com.erick.sms.vision.Application`

## License

This project is under GLPv3 License
