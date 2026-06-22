package com.alkemy.wallet;

import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.Currency;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.model.Transaction;
import com.alkemy.wallet.service.impl.CurrencyConverterImpl;
import com.alkemy.wallet.service.impl.WalletServiceImpl;
import com.alkemy.wallet.service.interfaces.CurrencyConverter;
import com.alkemy.wallet.service.interfaces.WalletService;
import com.alkemy.wallet.exception.InsufficientFundsException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Main application class presenting the interactive console wallet simulation.
 */
public class Main {

    private static final WalletService walletService = new WalletServiceImpl();
    private static final CurrencyConverter currencyConverter = new CurrencyConverterImpl();
    private static final List<Account> accounts = new ArrayList<>();
    private static Account activeAccount = null;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        // Seed some initial data to make testing easier
        seedInitialData();

        printHeader();

        while (!exit) {
            printMenu();
            System.out.print("Seleccione una opción: ");
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    registerNewUser(scanner);
                    break;
                case "2":
                    selectActiveAccount(scanner);
                    break;
                case "3":
                    showBalance();
                    break;
                case "4":
                    performDeposit(scanner);
                    break;
                case "5":
                    performWithdrawal(scanner);
                    break;
                case "6":
                    performCurrencyConversion(scanner);
                    break;
                case "7":
                    showTransactionHistory();
                    break;
                case "8":
                    exit = true;
                    System.out.println("\n¡Gracias por utilizar Alke Wallet! Hasta pronto.");
                    break;
                default:
                    System.out.println("\n[ERROR] Opción no válida. Por favor, intente de nuevo.");
            }
            if (!exit) {
                System.out.print("\nPresione Enter para continuar...");
                scanner.nextLine();
            }
        }
        scanner.close();
    }

    private static void seedInitialData() {
        User defaultUser = new User(1, "Constanza Gomez", "constanza@alkemy.com", "pass123");
        // Create a default account in USD with 500.0 balance
        WalletServiceImpl serviceImpl = (WalletServiceImpl) walletService;
        Account acc = serviceImpl.createAccount(defaultUser, Currency.USD);
        serviceImpl.deposit(acc, 500.0);
        accounts.add(acc);
        activeAccount = acc;
    }

    private static void printHeader() {
        System.out.println("==================================================");
        System.out.println("          🌟 BIENVENIDO A ALKE WALLET 🌟          ");
        System.out.println("    La solución digital de Alkemy Digital fintech ");
        System.out.println("==================================================");
        if (activeAccount != null) {
            System.out.printf("Cuenta activa: %s (%s) | Saldo: %.2f %s%n",
                    activeAccount.getUser().getName(),
                    activeAccount.getCurrency(),
                    activeAccount.getBalance(),
                    activeAccount.getCurrency().getSymbol());
        }
        System.out.println("==================================================");
    }

    private static void printMenu() {
        System.out.println("\n--- MENÚ DE OPERACIONES ---");
        System.out.println("1. Registrar nuevo usuario y crear cuenta");
        System.out.println("2. Cambiar/Seleccionar cuenta activa");
        System.out.println("3. Consultar saldo actual");
        System.out.println("4. Depositar dinero");
        System.out.println("5. Retirar dinero");
        System.out.println("6. Convertir saldo a otra moneda");
        System.out.println("7. Ver historial de transacciones");
        System.out.println("8. Salir");
        System.out.println("---------------------------");
    }

    private static void registerNewUser(Scanner scanner) {
        System.out.println("\n--- REGISTRO DE NUEVO USUARIO ---");
        System.out.print("Ingrese nombre completo: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("[ERROR] El nombre no puede estar vacío.");
            return;
        }

        System.out.print("Ingrese correo electrónico: ");
        String email = scanner.nextLine().trim();
        if (email.isEmpty() || !email.contains("@")) {
            System.out.println("[ERROR] Correo electrónico inválido.");
            return;
        }

        System.out.print("Ingrese contraseña: ");
        String password = scanner.nextLine().trim();
        if (password.length() < 4) {
            System.out.println("[ERROR] La contraseña debe tener al menos 4 caracteres.");
            return;
        }

        System.out.println("Seleccione tipo de moneda para su cuenta:");
        System.out.println("1. CLP$ (Peso Chileno)");
        System.out.println("2. US$ (Dólar Americano)");
        System.out.println("3. € (Euro)");
        System.out.print("Selección: ");
        String currencyChoice = scanner.nextLine().trim();

        Currency currency;
        switch (currencyChoice) {
            case "1":
                currency = Currency.CLP;
                break;
            case "2":
                currency = Currency.USD;
                break;
            case "3":
                currency = Currency.EUR;
                break;
            default:
                System.out.println("[WARNING] Selección inválida. Se asignará USD por defecto.");
                currency = Currency.USD;
        }

        int newUserId = accounts.size() + 1;
        User user = new User(newUserId, name, email, password);
        
        // Downcast to access the overloaded createAccount
        WalletServiceImpl serviceImpl = (WalletServiceImpl) walletService;
        Account newAccount = serviceImpl.createAccount(user, currency);
        
        accounts.add(newAccount);
        activeAccount = newAccount;

        System.out.println("\n[ÉXITO] ¡Usuario registrado con éxito!");
        System.out.printf("Titular: %s | Cuenta N°: %d | Moneda: %s | Saldo Inicial: %.2f %s%n",
                user.getName(), newAccount.getAccountId(), currency, newAccount.getBalance(), currency.getSymbol());
    }

    private static void selectActiveAccount(Scanner scanner) {
        if (accounts.isEmpty()) {
            System.out.println("\n[ERROR] No hay cuentas registradas en el sistema.");
            return;
        }

        System.out.println("\n--- SELECCIONAR CUENTA ACTIVA ---");
        for (int i = 0; i < accounts.size(); i++) {
            Account acc = accounts.get(i);
            System.out.printf("%d. %s - Cuenta N° %d (%s) | Saldo: %.2f %s%n",
                    (i + 1),
                    acc.getUser().getName(),
                    acc.getAccountId(),
                    acc.getCurrency(),
                    acc.getBalance(),
                    acc.getCurrency().getSymbol());
        }
        System.out.print("Seleccione el número de cuenta: ");
        String choiceStr = scanner.nextLine().trim();
        try {
            int choice = Integer.parseInt(choiceStr);
            if (choice > 0 && choice <= accounts.size()) {
                activeAccount = accounts.get(choice - 1);
                System.out.println("[ÉXITO] Cuenta activa cambiada a: " + activeAccount.getUser().getName());
            } else {
                System.out.println("[ERROR] Selección fuera de rango.");
            }
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Entrada inválida. Debe ingresar un número.");
        }
    }

    private static void showBalance() {
        if (activeAccount == null) {
            System.out.println("\n[ERROR] Debe registrar o seleccionar una cuenta activa primero.");
            return;
        }
        System.out.println("\n========================================");
        System.out.printf("       ESTADO DE CUENTA (N° %d)        %n", activeAccount.getAccountId());
        System.out.println("========================================");
        System.out.println("Titular: " + activeAccount.getUser().getName());
        System.out.println("Email: " + activeAccount.getUser().getEmail());
        System.out.printf("Saldo disponible: %.2f %s (%s)%n",
                walletService.getBalance(activeAccount),
                activeAccount.getCurrency().getSymbol(),
                activeAccount.getCurrency());
        System.out.println("========================================");
    }

    private static void performDeposit(Scanner scanner) {
        if (activeAccount == null) {
            System.out.println("\n[ERROR] Debe registrar o seleccionar una cuenta activa primero.");
            return;
        }

        System.out.print("\nIngrese el monto a depositar: ");
        String amountStr = scanner.nextLine().trim();
        try {
            double amount = Double.parseDouble(amountStr);
            walletService.deposit(activeAccount, amount);
            System.out.printf("[ÉXITO] Depósito realizado con éxito. Nuevo saldo: %.2f %s%n",
                    activeAccount.getBalance(), activeAccount.getCurrency().getSymbol());
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Formato de número inválido.");
        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    private static void performWithdrawal(Scanner scanner) {
        if (activeAccount == null) {
            System.out.println("\n[ERROR] Debe registrar o seleccionar una cuenta activa primero.");
            return;
        }

        System.out.print("\nIngrese el monto a retirar: ");
        String amountStr = scanner.nextLine().trim();
        try {
            double amount = Double.parseDouble(amountStr);
            walletService.withdraw(activeAccount, amount);
            System.out.printf("[ÉXITO] Retiro realizado con éxito. Nuevo saldo: %.2f %s%n",
                    activeAccount.getBalance(), activeAccount.getCurrency().getSymbol());
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Formato de número inválido.");
        } catch (InsufficientFundsException e) {
            System.out.println("\n[TRANSACCIÓN DENEGADA] " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    private static void performCurrencyConversion(Scanner scanner) {
        if (activeAccount == null) {
            System.out.println("\n[ERROR] Debe registrar o seleccionar una cuenta activa primero.");
            return;
        }

        System.out.println("\n--- CONVERSIÓN DE MONEDA ---");
        System.out.printf("Saldo actual en tu cuenta: %.2f %s%n",
                activeAccount.getBalance(), activeAccount.getCurrency().getSymbol());
        System.out.println("Seleccione la moneda a la que desea cotizar/convertir:");
        System.out.println("1. CLP$ (Peso Chileno)");
        System.out.println("2. US$ (Dólar Americano)");
        System.out.println("3. € (Euro)");
        System.out.print("Selección: ");
        String conversionChoice = scanner.nextLine().trim();

        Currency targetCurrency;
        switch (conversionChoice) {
            case "1":
                targetCurrency = Currency.CLP;
                break;
            case "2":
                targetCurrency = Currency.USD;
                break;
            case "3":
                targetCurrency = Currency.EUR;
                break;
            default:
                System.out.println("[ERROR] Selección de moneda no válida.");
                return;
        }

        System.out.print("¿Desea cotizar (1) una cantidad específica o (2) todo su saldo actual? (1/2): ");
        String mode = scanner.nextLine().trim();

        double amountToConvert;
        if ("2".equals(mode)) {
            amountToConvert = activeAccount.getBalance();
        } else {
            System.out.print("Ingrese la cantidad a cotizar: ");
            String specAmountStr = scanner.nextLine().trim();
            try {
                amountToConvert = Double.parseDouble(specAmountStr);
            } catch (NumberFormatException e) {
                System.out.println("[ERROR] Formato de número inválido.");
                return;
            }
        }

        try {
            double result = currencyConverter.convert(amountToConvert, activeAccount.getCurrency(), targetCurrency);
            System.out.println("\n--- RESULTADO DE LA COTIZACIÓN ---");
            System.out.printf("%.2f %s (%s) equivale a:%n",
                    amountToConvert, activeAccount.getCurrency().getSymbol(), activeAccount.getCurrency());
            System.out.printf("👉 %.2f %s (%s)%n",
                    result, targetCurrency.getSymbol(), targetCurrency);
            System.out.println("----------------------------------");
        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    private static void showTransactionHistory() {
        System.out.println("\n--- HISTORIAL DE TRANSACCIONES ---");
        WalletServiceImpl serviceImpl = (WalletServiceImpl) walletService;
        List<Transaction> txs;
        
        if (activeAccount != null) {
            System.out.printf("Transacciones para la cuenta activa (Titular: %s, ID: %d):%n",
                    activeAccount.getUser().getName(), activeAccount.getAccountId());
            txs = serviceImpl.getTransactionHistoryForAccount(activeAccount);
        } else {
            System.out.println("Todas las transacciones en el sistema:");
            txs = serviceImpl.getTransactionHistory();
        }

        if (txs.isEmpty()) {
            System.out.println("No se han registrado transacciones aún.");
        } else {
            for (Transaction tx : txs) {
                System.out.println(tx);
            }
        }
        System.out.println("----------------------------------");
    }
}
