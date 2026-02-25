## Car Hut

Car Hut is an offline car showroom management system. To run this application on your local setup:

- Clone this repository
- Run the following to compile:
  ```
  javac -d out -sourcepath src src/edu/iutcs/cr/*.java src/edu/iutcs/cr/persons/*.java src/edu/iutcs/cr/system/*.java src/edu/iutcs/cr/vehicles/*.java src/edu/iutcs/cr/util/*.java
  ```
- Run `java -cp .\out edu.iutcs.cr.Main` to start the program

---

## Refactoring Log

Each section below shows the **original code**, the **problem** it had, the **new code**, and the **OOP principle or design pattern** applied.

---

### 1. `InputReader` — New Singleton Utility
**New file:** `src/edu/iutcs/cr/util/InputReader.java`

#### Problem
Every method that needed console input created its own `new Scanner(System.in)`. This caused two runtime-breaking bugs:

- **Resource leak** — most Scanners were never closed.
- **Closing `System.in` permanently** — `Hatchback.setCompact()` called `scanner.close()`. Closing a `Scanner` that wraps `System.in` shuts down the underlying `InputStream` for the entire JVM. Every subsequent read in any class would then throw `java.util.NoSuchElementException`. `MainMenu.showAndSelectOperation()` had the same bug.

**Example of the problematic pattern (repeated across ~10 methods):**
```java
// Hatchback.java — original
public void setCompact() {
    Scanner scanner = new Scanner(System.in);   // creates a new Scanner every call
    System.out.print("Is the hatchback compact? (true/false): ");
    this.isCompact = scanner.nextBoolean();
    scanner.close();  // ← BUG: closes System.in permanently
}
```

#### Fix — `InputReader` Singleton
```java
// src/edu/iutcs/cr/util/InputReader.java — new file
public class InputReader {

    private static InputReader instance;
    private final Scanner scanner;

    private InputReader() {
        scanner = new Scanner(System.in);   // created exactly once, never closed
    }

    public static InputReader getInstance() {
        if (instance == null) {
            instance = new InputReader();
        }
        return instance;
    }

    public String nextLine()   { return scanner.nextLine(); }
    public int    nextInt()    { int v = scanner.nextInt();    scanner.nextLine(); return v; }
    public double nextDouble() { double v = scanner.nextDouble(); scanner.nextLine(); return v; }
    public boolean nextBoolean(){ boolean v = scanner.nextBoolean(); scanner.nextLine(); return v; }
    public String next()       { return scanner.next(); }
}
```

> Note: `nextInt()`, `nextDouble()`, and `nextBoolean()` consume the trailing newline via `scanner.nextLine()` so subsequent `nextLine()` calls are never poisoned by an empty string left in the buffer.

**Pattern:** Singleton | **Principle:** Resource Management

---

### 2. `VehicleFactory` — New Factory Class
**New file:** `src/edu/iutcs/cr/vehicles/VehicleFactory.java`

#### Problem
`SystemFlowRunner.addCar()` both *selected* and *constructed* every vehicle type in a hardcoded `if-else` chain. Adding a new vehicle subclass meant editing `SystemFlowRunner` — a class that has nothing to do with vehicle construction. This violates the **Open/Closed Principle**.

```java
// SystemFlowRunner.java — original addCar() method
Vehicle newItem = null;

if (vehicleType == 1) {
    System.out.println("\n\nCreate new bus");
    newItem = new Bus();
} else if (vehicleType == 2) {
    System.out.println("\n\nCreate new car");
    newItem = new Car();
} else if (vehicleType == 3) {
    System.out.println("\n\nCreate new hatchback");
    newItem = new Hatchback();
} else if (vehicleType == 4) {
    System.out.println("\n\nCreate new sedan");
    newItem = new Sedan();
} else {
    System.out.println("\n\nCreate new SUV");
    newItem = new SUV();
}
```

#### Fix — `VehicleFactory`
```java
// src/edu/iutcs/cr/vehicles/VehicleFactory.java — new file
public class VehicleFactory {

    public static final int BUS = 1, CAR = 2, HATCHBACK = 3, SEDAN = 4, SUV = 5;
    public static final int MIN_TYPE = BUS, MAX_TYPE = SUV;

    private VehicleFactory() {}   // utility class — no instances

    public static Vehicle create(int type) {
        return switch (type) {
            case BUS       -> new Bus();
            case CAR       -> new Car();
            case HATCHBACK -> new Hatchback();
            case SEDAN     -> new Sedan();
            case SUV       -> new SUV();
            default        -> throw new IllegalArgumentException("Unknown vehicle type: " + type);
        };
    }

    public static String getTypeName(int type) {
        return switch (type) {
            case BUS       -> "Bus";
            case CAR       -> "Car";
            case HATCHBACK -> "Hatchback";
            case SEDAN     -> "Sedan";
            case SUV       -> "SUV";
            default        -> "Unknown";
        };
    }
}
```

`SystemFlowRunner.addCar()` is reduced to:
```java
// SystemFlowRunner.java — after refactoring
System.out.println("\n\nCreate new " + VehicleFactory.getTypeName(vehicleType));
Vehicle newItem = VehicleFactory.create(vehicleType);
database.getVehicles().add(newItem);
```

Adding a new vehicle type now only requires a change to `VehicleFactory`.

**Pattern:** Factory Method | **Principle:** Open/Closed Principle

---

### 3. `Person.java` — Setter Naming & SRP

#### Problem
`setName()`, `setId()`, and `setEmail()` were named like setters but actually performed console I/O — they prompted the user and then stored the result. This dual responsibility violates **SRP** and makes the class untestable without a live console. Each method also opened its own `Scanner`.

```java
// Person.java — original
public void setName() {
    Scanner scanner = new Scanner(System.in);  // new Scanner every call

    while (this.name == null || this.name.isBlank()) {
        System.out.print("Enter name: ");
        this.name = scanner.nextLine();

        if (name == null || name.isBlank()) {
            System.out.println("Name is mandatory!");
        }
    }
}

// ... setId() and setEmail() follow the exact same pattern
```

And the full constructor called these:
```java
public Person() {
    setName();   // console I/O hidden inside a "setter"
    setId();
    setEmail();
}
```

#### Fix
```java
// Person.java — after refactoring

// Full constructor: delegates to private read-helpers
public Person() {
    readName();
    readId();
    readEmail();
}

// Value-based public setters (no console I/O — follow JavaBeans convention)
public void setName(String name)   { this.name  = name;  }
public void setId(String id)       { this.id    = id;    }
public void setEmail(String email) { this.email = email; }

// Private console-reading helpers (implementation detail of the constructor)
private void readName() {
    InputReader reader = InputReader.getInstance();
    while (this.name == null || this.name.isBlank()) {
        System.out.print("Enter name: ");
        this.name = reader.nextLine();
        if (name.isBlank()) System.out.println("Name is mandatory!");
    }
}

private void readId() { /* same pattern */ }
private void readEmail() { /* same pattern */ }
```

**Principle:** Single Responsibility Principle, Encapsulation

---

### 4. `Buyer.java` — Same Setter Naming Fix

#### Problem
```java
// Buyer.java — original
public void setPaymentMethod() {
    Scanner scanner = new Scanner(System.in);
    System.out.print("Enter new payment method:");  // notice: no space after colon
    this.paymentMethod = scanner.nextLine();
}
```

#### Fix
```java
// Buyer.java — after refactoring

/** Value-based setter — no console I/O */
public void setPaymentMethod(String paymentMethod) {
    this.paymentMethod = paymentMethod;
}

/** Private console-reading helper called by constructor */
private void readPaymentMethod() {
    System.out.print("Enter payment method: ");
    this.paymentMethod = InputReader.getInstance().nextLine();
}
```

**Principle:** Single Responsibility Principle, Encapsulation

---

### 5. `Vehicle.java` — Setter Naming & SRP

#### Problem
Same pattern as `Person` — six no-arg methods named `setX()` that each opened a `new Scanner` and prompted the console:

```java
// Vehicle.java — original (one of six identical patterns)
public void setMake() {
    Scanner scanner = new Scanner(System.in);

    while (this.make == null || this.make.isBlank()) {
        System.out.print("Enter make: ");
        this.make = scanner.nextLine();

        if (make == null || make.isBlank()) {
            System.out.println("Make is mandatory!");
        }
    }
}
```

#### Fix
```java
// Vehicle.java — after refactoring

// Value-based public setters
public void setRegistrationNumber(String r) { this.registrationNumber = r; }
public void setMake(String make)            { this.make  = make;  }
public void setModel(String model)          { this.model = model; }
public void setYear(String year)            { this.year  = year;  }
public void setPrice(double price)          { this.price = price; }

// Private console-reading helpers
private void readMake() {
    InputReader reader = InputReader.getInstance();
    while (this.make == null || this.make.isBlank()) {
        System.out.print("Enter make: ");
        this.make = reader.nextLine();
        if (make.isBlank()) System.out.println("Make is mandatory!");
    }
}
// readRegistrationNumber(), readModel(), readYear(), readPrice() follow same pattern
```

**Principle:** Single Responsibility Principle, Encapsulation

---

### 6. Vehicle Subclasses — Setter Naming, Bug Fixes

#### `Bus.java` and `Car.java`

```java
// Bus.java — original (Car.java had the identical issue)
int passengerCapacity;   // ← package-private field (encapsulation violation in Car)

public void setPassengerCapacity() {
    Scanner scanner = new Scanner(System.in);
    System.out.print("Enter new passenger capacity: ");
    this.passengerCapacity = scanner.nextInt();
}
```

```java
// Bus.java — after refactoring
private int passengerCapacity;   // properly private

public void setPassengerCapacity(int passengerCapacity) {  // value-based setter
    this.passengerCapacity = passengerCapacity;
}

private void readPassengerCapacity() {
    System.out.print("Enter passenger capacity: ");
    this.passengerCapacity = InputReader.getInstance().nextInt();
}
```

#### `Hatchback.java` — Two Bugs Fixed

**Bug 1:** `Hatchback` did not implement `Serializable` — the only vehicle subclass that didn't. This would throw `NotSerializableException` every time the inventory was saved to disk.

**Bug 2:** `scanner.close()` was called — same `System.in` destruction bug described in change #1.

```java
// Hatchback.java — original
public class Hatchback extends Vehicle {   // ← missing implements Serializable

    public void setCompact() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Is the hatchback compact? (true/false): ");
        this.isCompact = scanner.nextBoolean();
        scanner.close();    // ← BUG: closes System.in permanently
    }
}
```

```java
// Hatchback.java — after refactoring
public class Hatchback extends Vehicle implements Serializable {  // ← fixed

    public void setCompact(boolean compact) {   // value-based setter
        this.isCompact = compact;
    }

    private void readCompact() {               // console helper
        System.out.print("Is the hatchback compact? (true/false): ");
        this.isCompact = InputReader.getInstance().nextBoolean();
        // no scanner.close() — InputReader is never closed
    }
}
```

#### `Sedan.java` and `SUV.java` — Setter Naming

```java
// Sedan.java — original
public void setHasSunroof() {
    // Taking input within the setter, which might lead to unexpected behavior  ← even the original comment flags this
    Scanner scanner = new Scanner(System.in);
    System.out.print("Does the sedan have a sunroof? (true/false): ");
    this.hasSunroof = scanner.nextBoolean();
}
```

```java
// Sedan.java — after refactoring
public void setHasSunroof(boolean hasSunroof) {   // value-based setter
    this.hasSunroof = hasSunroof;
}

private void readHasSunroof() {
    System.out.print("Does the sedan have a sunroof? (true/false): ");
    this.hasSunroof = InputReader.getInstance().nextBoolean();
}
```

**Principle:** Encapsulation, SRP | **Bug Fix:** `Serializable`, `scanner.close()`

---

### 7. `DataStore.java` — DRY with Generic Helpers

#### Problem
The class had four pairs of nearly identical `save` / `load` methods. Each pair was ~10 lines that differed **only** in the filename and generic type — pure copy-paste duplication:

```java
// DataStore.java — original (this block repeated 4 times, changing only the type and filename)
public void saveInvoices(Set<Invoice> invoices) {
    try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("invoices.txt"))) {
        outputStream.writeObject(invoices);
    } catch (IOException e) {
        e.printStackTrace();
    }
}

public Set<Invoice> loadInvoices() {
    Set<Invoice> invoices = new HashSet<>();
    try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("invoices.txt"))) {
        invoices = (Set<Invoice>) inputStream.readObject();
    } catch (IOException | ClassNotFoundException e) {
        saveInvoices(invoices);
    }
    return invoices;
}

// saveBuyers() ... loadBuyers() — identical structure
// saveSellers() ... loadSellers() — identical structure
// saveVehicles() ... loadVehicles() — identical structure
```

#### Fix — Two Private Generic Helpers
```java
// DataStore.java — after refactoring

// File path constants (no more magic strings scattered across methods)
private static final String INVOICES_FILE = "invoices.txt";
private static final String BUYERS_FILE   = "buyers.txt";
private static final String SELLERS_FILE  = "sellers.txt";
private static final String VEHICLES_FILE = "cars.txt";

// All public methods are one-liners that delegate to the helpers
public void saveInvoices(Set<Invoice> invoices)  { saveData(invoices,  INVOICES_FILE); }
public void saveBuyers(Set<Buyer> buyers)         { saveData(buyers,    BUYERS_FILE);   }
public void saveSellers(Set<Seller> sellers)      { saveData(sellers,   SELLERS_FILE);  }
public void saveVehicles(Set<Vehicle> vehicles)   { saveData(vehicles,  VEHICLES_FILE); }

public Set<Invoice> loadInvoices()  { return loadData(INVOICES_FILE, new HashSet<>()); }
public Set<Buyer>   loadBuyers()    { return loadData(BUYERS_FILE,   new HashSet<>()); }
public Set<Seller>  loadSellers()   { return loadData(SELLERS_FILE,  new HashSet<>()); }
public Set<Vehicle> loadVehicles()  { return loadData(VEHICLES_FILE, new HashSet<>()); }

// Single save helper — all boilerplate lives here once
private void saveData(Object data, String fileName) {
    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
        out.writeObject(data);
    } catch (IOException e) {
        e.printStackTrace();
    }
}

// Single load helper — fallback to defaultValue if file is missing/corrupt
@SuppressWarnings("unchecked")
private <T> T loadData(String fileName, T defaultValue) {
    try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
        return (T) in.readObject();
    } catch (IOException | ClassNotFoundException e) {
        saveData(defaultValue, fileName);
        return defaultValue;
    }
}
```

Adding a new entity type now requires **one line** instead of twenty.

**Principle:** DRY (Don't Repeat Yourself)

---

### 8. `Invoice.java` — Remove I/O from Constructor (SRP)

#### Problem
The constructor silently triggered console I/O by calling `takePayment()`. Constructors should initialise state — they should not prompt users. This makes the class impossible to instantiate in unit tests and hides unexpected side effects from callers.

```java
// Invoice.java — original constructor
public Invoice(Buyer buyer, Seller seller, ShoppingCart shoppingCart) {
    this.buyer = buyer;
    this.seller = seller;
    this.shoppingCart = shoppingCart;
    takePayment();          // ← triggers Scanner.nextBoolean() as a constructor side effect
    markCarAsUnavailable();
    dateTime = LocalDateTime.now();
}

public void takePayment() {
    Scanner scanner = new Scanner(System.in);
    System.out.print("Is payment done (true/false): ");
    this.isPaid = scanner.nextBoolean();   // I/O inside an object constructor
}
```

#### Fix — `isPaid` as a Constructor Parameter
```java
// Invoice.java — after refactoring

// isPaid is now passed in by the caller (who already owns the I/O flow)
public Invoice(Buyer buyer, Seller seller, ShoppingCart shoppingCart, boolean isPaid) {
    this.buyer = buyer;
    this.seller = seller;
    this.shoppingCart = shoppingCart;
    this.isPaid = isPaid;           // plain assignment — no I/O
    markVehiclesUnavailable();
    this.dateTime = LocalDateTime.now();
}

// takePayment() method removed entirely
```

The caller (`SystemFlowRunner.createInvoice()`) now reads `isPaid` where all other I/O for that flow already lives:

```java
// SystemFlowRunner.createInvoice() — after refactoring
System.out.print("Is payment done (true/false): ");
boolean isPaid = reader.nextBoolean();                         // I/O in the flow layer

Invoice invoice = new Invoice(buyer, seller, cart, isPaid);   // pure construction
```

**Principle:** Single Responsibility Principle, Constructor purity

---

### 9. `MainMenu.java` — Scanner Closure Bug Fixed

#### Problem
`showAndSelectOperation()` called `scanner.close()` at the end. Because `MainMenu` is called in a loop, this destroyed `System.in` after the very first menu interaction — the second call to `mainMenu.showAndSelectOperation()` would throw `NoSuchElementException`.

```java
// MainMenu.java — original
public int showAndSelectOperation() {
    Scanner scanner = new Scanner(System.in);
    showMenu();
    int selectedOperation = -1;

    while(selectedOperation<1 || selectedOperation>9) {
        System.out.print("Enter your choice: ");
        selectedOperation = scanner.nextInt();

        if(selectedOperation<1 || selectedOperation>9) {
            System.out.print("Enter a valid operation: ");
        }
    }
    scanner.close();   // ← BUG: closes System.in permanently, crashes next iteration
    return selectedOperation;
}
```

#### Fix
```java
// MainMenu.java — after refactoring
public int showAndSelectOperation() {
    showMenu();
    InputReader reader = InputReader.getInstance();  // shared, never closed
    int selectedOperation = -1;

    while (selectedOperation < 1 || selectedOperation > 9) {
        System.out.print("Enter your choice: ");
        selectedOperation = reader.nextInt();

        if (selectedOperation < 1 || selectedOperation > 9) {
            System.out.println("Please enter a number between 1 and 9.");
        }
    }

    return selectedOperation;
}
```

**Pattern:** Singleton (via `InputReader`) | **Bug Fix:** Resource Management

---

### 10. `SystemDatabase.java` — Thread-Safe Singleton + Stream Lookups

#### Problem 1 — Unsafe Singleton
The singleton used a plain null-check with no synchronisation. Two threads calling `getInstance()` simultaneously could both observe `null` and each create a separate instance.

```java
// SystemDatabase.java — original singleton
private static SystemDatabase instance;   // not volatile

public static SystemDatabase getInstance() {
    if (isNull(instance)) {               // ← race condition: two threads can both enter here
        instance = new SystemDatabase();  // ← two instances created
    }
    return instance;
}
```

#### Fix — Double-Checked Locking + `volatile`
```java
// SystemDatabase.java — after refactoring
private static volatile SystemDatabase instance;   // volatile: write is immediately visible to all threads

public static SystemDatabase getInstance() {
    if (isNull(instance)) {
        synchronized (SystemDatabase.class) {       // only one thread enters the constructor
            if (isNull(instance)) {                 // re-check after acquiring the lock
                instance = new SystemDatabase();
            }
        }
    }
    return instance;
}
```

#### Problem 2 — Verbose for-loop Lookups
```java
// SystemDatabase.java — original findBuyerById
public Buyer findBuyerById(String id) {
    Buyer newBuyer = new Buyer(id);

    for (Buyer buyer : buyers) {
        if (buyer.equals(newBuyer)) {
            return buyer;
        }
    }
    return null;
}
```

#### Fix — Stream one-liners
```java
// SystemDatabase.java — after refactoring
public Buyer findBuyerById(String id) {
    Buyer key = new Buyer(id);
    return buyers.stream().filter(b -> b.equals(key)).findFirst().orElse(null);
}

public Seller findSellerById(String id) {
    Seller key = new Seller(id);
    return sellers.stream().filter(s -> s.equals(key)).findFirst().orElse(null);
}

public Vehicle findVehicleByRegistrationNumber(String registrationNumber) {
    Vehicle key = new Vehicle(registrationNumber);
    return vehicles.stream().filter(v -> v.equals(key)).findFirst().orElse(null);
}
```

**Pattern:** Singleton (thread-safe) | **Principle:** Concurrency correctness, Clean Code

---

### 11. `SystemFlowRunner.java` — Switch Expressions + Factory Delegation

#### Problem 1 — Long if-else Chain for Operation Dispatch
```java
// SystemFlowRunner.run() — original
if (selectedOperation == 1) {
    ...
} else if (selectedOperation == 2) {
    ...
} else if (selectedOperation == 3) {
    ...
} else if (selectedOperation == 4) {
    ...
} else if (selectedOperation == 5) {
    ...
} else if (selectedOperation == 6) {
    ...
} else if (selectedOperation == 7) {
    ...
} else if(selectedOperation==8) {  // inconsistent spacing
    ...
}
```

#### Fix — Switch Expression (arrow syntax)
```java
// SystemFlowRunner.run() — after refactoring
switch (op) {
    case 1 -> { System.out.println("\n\n\nAdd new seller");    database.getSellers().add(new Seller()); promptToViewMainMenu(); }
    case 2 -> { System.out.println("\n\n\nAdd new customer");  database.getBuyers().add(new Buyer());   promptToViewMainMenu(); }
    case 3 -> { System.out.println("\n\n\nAdd new vehicle");   addCar();                                promptToViewMainMenu(); }
    case 4 -> { System.out.println("\n\n\nInventory list");    database.showInventory();                promptToViewMainMenu(); }
    case 5 -> { System.out.println("\n\n\nSeller's list");     database.showSellerList();               promptToViewMainMenu(); }
    case 6 -> { System.out.println("\n\n\nCustomer's list");   database.showBuyerList();                promptToViewMainMenu(); }
    case 7 -> { System.out.println("\n\n\nCreate order");      createOrder(); }
    case 8 -> { System.out.println("\n\n\nInvoice list");      database.showInvoices();                 promptToViewMainMenu(); }
}
```

#### Problem 2 — addCar() built vehicles itself (see change #2 for full before)
```java
// SystemFlowRunner.addCar() — original (abbreviated)
if (vehicleType == 1) { newItem = new Bus(); }
else if (vehicleType == 2) { newItem = new Car(); }
// ...
```

#### Fix — Delegates to VehicleFactory
```java
// SystemFlowRunner.addCar() — after refactoring
for (int i = VehicleFactory.MIN_TYPE; i <= VehicleFactory.MAX_TYPE; i++) {
    System.out.println(i + ". " + VehicleFactory.getTypeName(i));
}
// ...
Vehicle newItem = VehicleFactory.create(vehicleType);
database.getVehicles().add(newItem);
```

**Pattern:** Factory Method | **Principle:** Open/Closed Principle, Clean Code

---

### Summary Table

| # | File(s) | Pattern / Principle | Problem fixed |
|---|---|---|---|
| 1 | `util/InputReader.java` *(new)* | **Singleton** | Eliminated ~15 `new Scanner(System.in)` instances; fixed `scanner.close()` crash |
| 2 | `vehicles/VehicleFactory.java` *(new)* | **Factory Method** | Removed vehicle construction `if-else` from `SystemFlowRunner` |
| 3 | `persons/Person.java` | **SRP**, Encapsulation | `setX()` renamed to `readX()` (private); proper `setX(value)` setters added |
| 4 | `persons/Buyer.java` | **SRP**, Encapsulation | Same as #3 for `setPaymentMethod()` |
| 5 | `vehicles/Vehicle.java` | **SRP**, Encapsulation | Same as #3 for all six field setters |
| 6 | `vehicles/Bus.java`, `Car.java`, `Hatchback.java`, `Sedan.java`, `SUV.java` | **SRP**, Encapsulation, Bug Fix | Setter renaming; `Hatchback` missing `Serializable` added; `scanner.close()` removed |
| 7 | `system/DataStore.java` | **DRY** | 4× boilerplate save/load pairs collapsed into 2 generic private helpers |
| 8 | `Invoice.java` | **SRP** | I/O removed from constructor; `isPaid` passed as parameter |
| 9 | `MainMenu.java` | Resource Management | `scanner.close()` removed — was crashing the app after first menu use |
| 10 | `system/SystemDatabase.java` | **Singleton** (thread-safe) | `volatile` + double-checked locking; lookup loops → Streams |
| 11 | `SystemFlowRunner.java` | **Factory Method**, Clean Code | `if-else` chains → `switch`; construction → `VehicleFactory` |
