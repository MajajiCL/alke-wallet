# 🧪 Informe de Pruebas Unitarias - Alke Wallet

Este documento detalla la estructura, cobertura, aserciones y resultados del plan de pruebas unitarias implementado para validar la lógica del negocio de la aplicación **Alke Wallet**. Las pruebas fueron escritas en **JUnit 5** utilizando aserciones como `assertEquals`, `assertNotNull`, `assertTrue` y `assertThrows`.

---

## 📊 1. Resumen General del Plan de Pruebas

El objetivo principal de las pruebas es certificar que todas las transacciones financieras (depósitos, retiros) y los cálculos de conversión monetaria se realicen bajo estrictas reglas de negocio y de forma totalmente determinista.

* **Framework de Pruebas:** JUnit Jupiter (JUnit 5.10.0).
* **Clases bajo Prueba (SUT):**
  * `WalletServiceImpl.java` (Servicio de Billetera).
  * `CurrencyConverterImpl.java` (Servicio de Conversión).
* **Total de Casos de Prueba:** 14 test cases.
* **Tasa de Aprobación Esperada:** 100%.

---

## 🔍 2. Detalle de Casos de Prueba

### 2.1. Suite `WalletServiceTest.java` (Servicio de Billetera)

Esta suite valida la integridad del saldo de la cuenta y la correcta emisión de transacciones.

| ID | Nombre de la Prueba | Descripción | Aserciones y Validaciones |
|----|----------------------|-------------|----------------------------|
| W1 | `testCreateAccount` | Valida que al registrar un usuario se le cree una cuenta activa con saldo 0.0 y divisa USD por defecto. | `assertNotNull(testAccount)`<br>`assertEquals(testUser, testAccount.getUser())`<br>`assertEquals(0.0, balance)` |
| W2 | `testCreateAccountCustomCurrency` | Valida que se pueda crear una cuenta especificando otra divisa (ej. CLP). | `assertEquals(Currency.CLP, clpAccount.getCurrency())`<br>`assertEquals(0.0, clpAccount.getBalance())` |
| W3 | `testDepositSuccess` | Incrementa el saldo mediante un depósito válido y verifica el nuevo saldo disponible. | Deposita `150.0`. Verifica que el saldo final es exactamente `150.0`. |
| W4 | `testDepositNegativeAmountThrowsException` | Valida que al intentar depositar un valor negativo se lance la excepción `IllegalArgumentException`. | `assertThrows(IllegalArgumentException.class)` al depositar `-50.0`. |
| W5 | `testDepositZeroAmountThrowsException` | Valida que al intentar depositar 0.0 se lance `IllegalArgumentException`. | `assertThrows(IllegalArgumentException.class)` al depositar `0.0`. |
| W6 | `testWithdrawSuccess` | Deposita fondos y luego efectúa un retiro menor al saldo disponible, validando el descuento exacto. | Deposita `200.0`, retira `80.0`. Verifica que el saldo resultante es exactamente `120.0`. |
| W7 | `testWithdrawInsufficientFundsThrowsException` | **Caso Crítico:** Valida que si se solicita un retiro mayor al saldo disponible se dispare la excepción controlada `InsufficientFundsException`. | Deposita `50.0`. Intenta retirar `100.0`. `assertThrows(InsufficientFundsException.class)`. |
| W8 | `testWithdrawNegativeAmountThrowsException` | Valida que retiros negativos sean rechazados lanzando `IllegalArgumentException`. | `assertThrows(IllegalArgumentException.class)` al retirar `-10.0`. |
| W9 | `testWithdrawZeroAmountThrowsException` | Valida que retiros de valor cero sean rechazados lanzando `IllegalArgumentException`. | `assertThrows(IllegalArgumentException.class)` al retirar `0.0`. |

---

### 2.2. Suite `CurrencyConverterTest.java` (Conversor de Moneda)

Esta suite valida los cálculos de conversión entre las tres monedas soportadas (`CLP`, `USD`, `EUR`) basándose en tasas preestablecidas.

| ID | Nombre de la Prueba | Descripción | Cálculo Matemático Evaluado |
|----|----------------------|-------------|------------------------------|
| C1 | `testConvertCLPtoUSD` | Convierte 1000 CLP a USD. | `1000 * 0.0011 / 1.0 = 1.1 USD`<br>`assertEquals(1.1, resultado)` |
| C2 | `testConvertUSDtoEUR` | Convierte 100 USD a EUR. | `100 * 1.0 / 1.08 = 92.59 EUR`<br>`assertEquals(100.0 / 1.08, resultado)` |
| C3 | `testConvertEURtoCLP` | Convierte 100 EUR a CLP. | `100 * 1.08 / 0.0011 = 98181.81 CLP`<br>`assertEquals(108.0 / 0.0011, resultado)` |
| C4 | `testConvertSameCurrency` | Convierte fondos a la misma divisa. | `250.0 USD -> 250.0 USD`. Debe dar exactamente el mismo valor original. |
| C5 | `testConvertNegativeAmountThrowsException` | Intenta convertir un valor negativo. | `assertThrows(IllegalArgumentException.class)` al convertir `-100.0`. |
| C6 | `testConvertNullCurrencyThrowsException` | Intenta convertir enviando valores nulos en los parámetros de tipo de moneda. | `assertThrows(IllegalArgumentException.class)` si el origen o el destino es `null`. |

---

## 🛡️ 3. Robustez y Manejo de Excepciones

Para asegurar la confiabilidad técnica exigida por **Alkemy Digital**, el sistema está dotado de un doble muro de validación:

1. **Validación a nivel de Modelo (`Account.java`)**:
   * Los métodos `deposit(double amount)` y `withdraw(double amount)` validan internamente las precondiciones básicas de la cuenta. Si se detecta un monto negativo o fondos insuficientes, se arroja la excepción correspondiente inmediatamente, protegiendo el estado de la entidad.
2. **Validación a nivel de Capa de Servicio (`WalletServiceImpl.java`)**:
   * Valida la existencia e integridad física de las referencias de cuentas y usuarios antes de intentar realizar cualquier mutación.
   * Encapsula los errores y propaga excepciones específicas como `InsufficientFundsException`. Esto permite que capas superiores (como la interfaz de consola en `Main.java` o una futura API REST) capturen el error de forma segura, informen al usuario sin tumbar la aplicación, y dejen constancia en los logs.
