# 🏢 Feature: Reportes General  y Casas pendientes de pago - Administración de Condominios

## 📝 Descripción
Este branch incluye la implementación completa de los módulos de **Reporte General (P7)** y **Casas Morosas (P8)** para el sistema de administración. Además de cubrir todos los requerimientos solicitados, se desarrolló una lógica de filtrado dinámico que mejora significativamente la visualización y análisis de los datos históricos.

## ✨ Funcionalidades Implementadas

### 📊 P7 | Reporte General
Un panel detallado que proporciona una visión financiera integral:
* **Tabla de Resumen (JTable):** Despliega el estado de las 30 casas, incluyendo:
  * Número de casa.
  * Nombre del propietario.
  * Estado del mes actual (Pagado / Pendiente).
  * Total pagado en el año.
* **Métricas de Recaudación:** Pie de página que contrasta el total recaudado del mes versus el total esperado (ej. Q45,000.00 o según modificaciones de cuota).
* **Módulo de Impresión:** Integración con **JasperReports** mediante un botón dedicado para imprimir un reporte exacto de la información renderizada en pantalla.
* **Dashboard Visual:** Gráficas comparativas que ilustran:
  * Pagado vs. Pendiente.
  * Recaudado en el año vs. Esperado.

### ⚠️ P8 | Casas pendientes de pago
Módulo optimizado para la gestión de cobros:
* **Filtro Automático:** Lista de casas que presentan saldo pendiente en el mes de consulta.
* **Gestión Rápida:** Muestra el número de casa, nombre del propietario y número de teléfono para facilitar el contacto directo.

### 🚀 Mejoras y Features Adicionales (Logros)
* **Motor de Filtrado de Tiempo:** Sistema de filtros para navegar entre meses y años (anteriores y futuros).
* **Límites de Fecha Inteligentes:** El rango de los filtros se ajusta dinámicamente, tomando como límite inferior el registro más antiguo y como límite superior el más reciente en la base de datos.
* **UI Reactiva:** Sincronización en tiempo real. Todas las gráficas, textos y tablas se actualizan instantáneamente al interactuar con los filtros de fecha.

## 🛠️ Tecnologías Utilizadas
* **Core:** Java con Maven
* **Frontend:** JavaFX, Scene Builder, CSS
* **Base de Datos:** SQL Server 2019 (Consultas estructuradas)
* **Reportes:** JasperReports
