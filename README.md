# 🧠 Sumativa Semana 8 - Desarrollo Orientado a Objetos II

---

## 👤 Autor del proyecto
- **Nombre completo:**  Catalina Zapata
- **Carrera:** Analista Programador
- **Nombre del Proyecto:** SpeedFast Rama Semana8
---
## 📘 Descripción general del proyecto
Sistema de gestión para la empresa SpeedFast, desarrollado en Java con Swing y MySQL (JDBC).
Permite gestionar **clientes, repartidores, pedidos y entregas** con operaciones CRUD
(crear, listar, actualizar y eliminar) y conserva la simulación de reparto concurrente
(un hilo por repartidor) de la semana anterior.

---
## 🧱 Estructura general del proyecto

```plaintext
Empresa_Reparto_SpeedFast/
│
├── src/
│   │
│   ├── app/
│   │   └── Main.java                            # Punto de entrada y ejecución principal del sistema
│   │
│   ├── model/
│   │   ├── Cliente.java                         # Representa los clientes registrados en el sistema
│   │   ├── Pedido.java                          # Representa los pedidos y sus datos
│   │   ├── Repartidor.java                      # Representa los repartidores y su ejecución concurrente
│   │   ├── Entrega.java                         # Representa las entregas realizadas
│   │   ├── Estado.java                          # Define los estados posibles de un pedido
│   │   └── TipoPedido.java                      # Define los tipos de pedidos disponibles
│   │
│   ├── dao/
│   │   ├── ConexionDB.java                      # Gestiona la conexión con la base de datos MySQL
│   │   ├── Transaccion.java                     # Gestiona las transacciones de la base de datos
│   │   ├── ClienteDAO.java                      # Gestiona los registros de clientes
│   │   ├── RepartidorDAO.java                   # Gestiona los registros de repartidores
│   │   ├── PedidoDAO.java                       # Gestiona los registros de pedidos
│   │   └── EntregaDAO.java                      # Gestiona los registros de entregas
│   │
│   ├── controller/
│   │   ├── ClienteController.java               # Controla el flujo de clientes
│   │   ├── RepartidorController.java            # Controla el flujo de repartidores
│   │   ├── PedidoController.java                # Controla el flujo de pedidos y entregas
│   │   └── EntregaController.java               # Controla el flujo de entregas
│   │
│   ├── data/
│   │   └── ZonaDeCarga.java                     # Gestiona la cola compartida de pedidos
│   │
│   ├── util/
│   │   └── Validaciones.java                    # Contiene reglas de validación reutilizables
│   │
│   └── view/
│       ├── VentanaPrincipal.java                # Interfaz principal del sistema
│       ├── VentanaCrud.java                     # Ventana base para operaciones CRUD
│       ├── VentanaClientes.java                 # Interfaz para gestionar clientes
│       ├── VentanaRepartidores.java             # Interfaz para gestionar repartidores
│       ├── VentanaPedidos.java                  # Interfaz para gestionar pedidos
│       ├── VentanaEntregas.java                 # Interfaz para gestionar entregas
│       ├── VentanaResultadoEntrega.java         # Muestra los resultados de las entregas
│       └── ItemCombo.java                       # Representa elementos de listas desplegables
│
└── README.md                                    # Documentación general del proyecto
```

---
## ⚙️ Instrucciones para compilar y ejecutar `Main`
1. Abrir el proyecto en IntelliJ IDEA.
2. Esperar a que IntelliJ cargue las dependencias del proyecto.
3. Navegar hasta la clase `Main` ubicada en el paquete `app`.
4. Ejecutar el método `main()` presionando el botón **Run** (▶).
5. Verificar los resultados en la consola de ejecución.

---

**Repositorio GitHub:** https: https://github.com/CatalinaZapata/Empresa_Reparto_SpeedFast2.git |
**Fecha de entrega:** 05/10/2026

---

© Duoc UC | Escuela de Informática y Telecomunicaciones | Sumativa Semana 8