# Shop Manager System

**Shop Manager System** is a Java application designed to help manage a store, providing features such as product management, inventory control, and sales processing. The project contais a suport for Postgres 16 connection, providing a scalable solution for a large amount of data.

**Shop Manager System** it's a improved and complete rewrite of [Simple Item Manager](https://github.com/erick1-618/simple-item-manager)

## Features

- Product registration
- Sales and products tracking
- Sales processing
- Sales per product tracking

### Upcomming Features

- UI for better experience
- Products quantity tracking
- Products exclusion

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

For running the application:

```
./run.sh
```

Type 0 to see the possible commands. 

## License

This project is under GLPv3 License
