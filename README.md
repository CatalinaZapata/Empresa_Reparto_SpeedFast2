# 🧠 Formativa Semana 7 - Desarrollo Orientado a Objetos II

---

## 👤 Autor del proyecto
- **Nombre completo:**  Catalina Zapata
- **Carrera:** Analista Programador
- **Nombre del Proyecto:** SpeedFast Rama Semana7
---
## 📘 Descripción general del proyecto
Este proyecto corresponde a la evaluación de la Semana 7 de Desarrollo Orientado a Objetos II, desarrollada en Java para la empresa SpeedFast.
El sistema simula la gestión de pedidos mediante múltiples repartidores que trabajan en paralelo, permitiendo gestionar el proceso de retiro y entrega de los pedidos.
Se implementan los conceptos de programación orientada a objetos, utilizando `Runnable`, `Thread` y mecanismos de sincronización para controlar el acceso a la zona de carga compartida.
Cada repartidor retira un pedido pendiente desde la zona de carga, actualiza su estado a `EN_REPARTO`, simula el proceso de entrega y finalmente cambia su estado a `ENTREGADO`.
Además, el sistema incorpora persistencia de datos mediante una base de datos MySQL, utilizando JDBC y clases DAO para gestionar pedidos, repartidores y entregas.
Las entregas realizadas se registran en la tabla `entrega`, almacenando información como el pedido, repartidor, fecha y hora de la entrega.
El sistema cuenta con una interfaz gráfica desarrollada en Swing que permite registrar y listar pedidos, registrar repartidores e iniciar el proceso de entrega. Una vez finalizado el proceso, se muestra una ventana informando que los pedidos pendientes fueron repartidos y entregados correctamente.
El objetivo es simular un sistema de distribución de pedidos que permita gestionar de forma concurrente el trabajo de los repartidores y mantener un registro de las entregas realizadas.

---
## 🧱 Estructura general del proyecto

```plaintext
Empresa_Reparto_SpeedFast/
│
├── database/
│   └── speedfast_db.sql
│
├── src/
│   ├── app/
│   │   └── Main.java                         # Punto de entrada y ejecución del sistema.
│   │
│   ├── controller/
│   │   └── PedidoController.java             # Controla el flujo de pedidos y las entregas.
│   │
│   ├── dao/
│   │   ├── ConexionDB.java                   # Gestiona la conexión con la base de datos MySQL.
│   │   ├── EntregaDAO.java                   # Gestiona los registros de entregas.
│   │   ├── PedidoDAO.java                    # Gestiona los registros de pedidos.
│   │   └── RepartidorDAO.java                # Gestiona los registros de repartidores.
│   │
│   ├── data/
│   │   └── ZonaDeCarga.java                  # Recurso compartido para gestionar los pedidos.
│   │
│   ├── model/
│   │   ├── Entrega.java                      # Representa las entregas realizadas.
│   │   ├── Estado.java                       # Enum que representa los estados de los pedidos.
│   │   ├── Pedido.java                       # Representa los pedidos del sistema.
│   │   ├── Repartidor.java                   # Representa a los repartidores y su ejecución concurrente.
│   │   └── TipoPedido.java                   # Enum que representa los tipos de pedido.
│   │
│   └── view/
│       ├── VentanaListaPedidos.java          # Interfaz para listar los pedidos.
│       ├── VentanaPrincipal.java             # Ventana principal del sistema.
│       ├── VentanaRegistroPedido.java        # Interfaz para registrar pedidos.
│       ├── VentanaRegistroRepartidor.java    # Interfaz para registrar repartidores.
│       └── VentanaResultadoEntrega.java      # Muestra el resultado del proceso de entrega.
│
└── README.md                                 # Descripción e instrucciones del proyecto.
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
**Fecha de entrega:** 28/09/2026

---

© Duoc UC | Escuela de Informática y Telecomunicaciones | Formativa Semana 7