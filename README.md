# 🧠 Sumativa Semana 5 - Desarrollo Orientado a Objetos II

---

## 👤 Autor del proyecto
- **Nombre completo:**  Catalina Zapata
- **Carrera:** Analista Programador
- **Nombre del Proyecto:** Empresa_Reparto_SpeedFast RamaSemana5
---
## 📘 Descripción general del proyecto
Este proyecto corresponde a la evaluación de la Semana 5 de Desarrollo Orientado a Objetos II, desarrollada en Java para la empresa SpeedFast.  
El sistema simula la gestión concurrente de pedidos mediante múltiples repartidores que trabajan en paralelo.  
Se implementan `Runnable`, `Thread` y mecanismos de sincronización para controlar el acceso seguro a la zona de carga compartida.   
Cada repartidor retira un pedido, lo cambia a `EN_REPARTO`, simula su entrega y finalmente lo marca como `ENTREGADO`.  
El objetivo es evitar condiciones de carrera y asegurar que cada pedido sea procesado por un único repartidor.

---
## 🧱 Estructura general del proyecto

```plaintext
📁 Empresa_Reparto_SpeedFast/
│
├── 📁 src/
│   │
│   ├── 📁 app/
│   │   └── 📄 Main.java                    # Punto de entrada y ejecución del sistema.
│   │
│   ├── 📁 data/
│   │   └── 📄 ZonaDeCarga.java             # Recurso compartido para gestionar los pedidos.
│   │
│   └── 📁 model/
│       ├── 📄 Estado.java                  # Enum que representa los estados de los pedidos.
│       ├── 📄 Pedido.java                  # Representa los pedidos del sistema.
│       └── 📄 Repartidor.java              # Representa a los repartidores y su ejecución concurrente.
│
└── 📄 README.md                             # Descripción e instrucciones del proyecto.
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
**Fecha de entrega:** 14/09/2026

---

© Duoc UC | Escuela de Informática y Telecomunicaciones | Sumativa Semana 5