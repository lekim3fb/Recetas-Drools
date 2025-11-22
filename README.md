# Sistema de Recomendación de Recetas de Cocina - Drools

## Descripción General

Este es un **Sistema Basado en el Conocimiento (SBC)** implementado en **Drools** que proporciona recomendaciones personalizadas de recetas de cocina basadas en las preferencias del usuario, restricciones dietéticas, ingredientes disponibles y nivel de habilidad culinaria.

El sistema implementa la conceptualización completa del dominio siguiendo metodología de Ingeniería del Conocimiento.

---

## Arquitectura del Sistema

### 1. Entidades Java (Declarativas)

El conocimiento declarativo se representa mediante clases Java que modelan:

#### **Ingrediente.java**
- Almacena información sobre ingredientes
- Propiedades: nombre, categoría, alergias, valores nutricionales (proteína, carbohidratos, grasas, calorías)
- Base de datos de 17 ingredientes disponibles

#### **Receta.java**
- Modela recetas con ingredientes, tiempo, dificultad y compatibilidades dietéticas
- Incluye métodos para calcular disponibilidad de ingredientes
- Almacena puntuaciones y razones de recomendación

#### **IngredienteReceta.java**
- Relación entre Ingrediente y Receta con cantidad y unidad

#### **Usuario.java**
- Perfil del usuario con ingredientes disponibles, preferencias y restricciones
- **Incluye validación de datos** que verifica:
  - Nombre no vacío
  - Tiempo mínimo de 5 minutos
  - Nivel de habilidad válido (principiante, intermedio, avanzado)

#### **Recomendacion.java**
- Encapsula el resultado de la evaluación de una receta
- Estados: recomendada, alternativa, descartada
- Puntuación y motivos justificados

#### **EvaluacionReceta.java**
- Puente entre Drools y DMN
- Evalúa la calidad de recetas usando decisiones complejas

---

## Módulos Drools (Conocimiento Procedimental y Heurístico)

El sistema está modularizado en **7 archivos DRL** según la metodología, cada uno con responsabilidades específicas:

### **01_basedatos.drl** - Base de Datos
- Inicializa 17 ingredientes con propiedades nutricionales
- Crea 4 recetas de ejemplo (pasta, pollo, omeleta, salmón)
- **Salience: 1000-999** (máxima prioridad)

**Recetas incluidas:**
1. Pasta a la Tomate (20 min, fácil, italiana)
2. Pechuga de Pollo a la Plancha (15 min, fácil, española)
3. Omeleta Clásica (10 min, fácil, francesa)
4. Salmón al Horno (25 min, medio, nórdica)

### **02_validacion.drl** - Validación de Datos
- **Salience: 500-470**
- Valida datos del usuario
- Emite advertencias sobre ingredientes/restricciones/habilidad
- Comprueba integridad de los datos introducidos

**Reglas de validación:**
- Nombre de usuario no vacío
- Tiempo disponible razonable
- Nivel de habilidad correcto
- Detección de restricciones

### **03_filtrado.drl** - Filtrado por Restricciones
- **Salience: 400-395**
- **Query:** `ObtenerRecetasDescartadas`
- Descarta recetas incompatibles

**Criterios de descarte:**
- Contiene ingredientes a los que es alérgico
- Tiempo de preparación > tiempo disponible
- Incompatibilidad con dieta (vegana, vegetariana, etc.)
- Penalización por dificultad excesiva para el nivel

### **04_puntuacion.drl** - Cálculo de Puntuaciones
- **Salience: 350-330**
- **Query:** `ObtenerRecetasRecomendadas` (ordenadas por puntuación)
- Calcula puntuación base y bonificaciones

**Sistema de puntuación:**
- Base ≥70% ingredientes: 80 puntos + (% - 70) × 0.5
- Base 50-70% ingredientes: 60 puntos + (% - 50) × 0.4
- Bonificación preferencia gastronómica: +15
- Bonificación compatibilidad dietética: +20
- Bonificación rapidez (<15 min): +10
- Bonificación nivel apropiado: +5

### **05_explicacion.drl** - Explicación de Recomendaciones
- **Salience: 200-185**
- **Query:** `ObtenerRecomendacionesConExplicacion`
- Genera justificaciones detalladas
- Identifica ingredientes faltantes
- Sugiere alternativas

**Explicaciones generadas:**
- Porcentaje de ingredientes disponibles
- Ingredientes específicos faltantes
- Compatibilidad con restricciones
- Razones de bonificación aplicadas

### **06_informe.drl** - Informe Final
- **Salience: 100-45**
- Presenta resultados de forma clara y estructurada
- Lista recetas: recomendadas, alternativas, descartadas
- Muestra resumen final del análisis

**Formato del informe:**
```
[RECOMENDADA] Nombre Receta
├─ Puntuación: XX.X
├─ Tiempo: XX minutos
├─ Dificultad: nivel
├─ Región: región
├─ Motivo: razón
└─ Razones adicionales:
   • razón 1
   • razón 2
```

### **07_decision_dmn.drl** - Decisiones DMN
- **Salience: 365-360**
- Integra evaluaciones basadas en DMN (Decision Model and Notation)
- Clasifica recetas como: óptima, buena, aceptable, rechazar
- **Query:** `ObtenerEvaluacionesRecetas`

**Criterios DMN:**
- Óptima: ≥85% ingredientes + ≤15 min + dificultad fácil/medio (+30)
- Buena: 70-85% ingredientes + ≤30 min (+15)
- Aceptable: 50-70% ingredientes (+0)
- Rechazar: <50% ingredientes

---

## Archivo DMN (EvaluacionRecetas.dmn)

Define una tabla de decisión que clasifica recetas según:
- **Porcentaje de ingredientes disponibles**
- **Tiempo de preparación**
- **Nivel de dificultad**

La tabla de decisión tiene **4 reglas** que producen estados: óptima, buena, aceptable, rechazar.

---

## Flujo de Procesamiento

```
1. Usuario introduce datos
            ↓
2. Inicializar Base de Datos (01_basedatos)
            ↓
3. Validar Datos del Usuario (02_validacion)
            ↓
4. Filtrar Recetas Incompatibles (03_filtrado)
            ↓
5. Calcular Puntuaciones (04_puntuacion)
            ↓
6. Aplicar Decisiones DMN (07_decision_dmn)
            ↓
7. Generar Explicaciones (05_explicacion)
            ↓
8. Presentar Informe Final (06_informe)
            ↓
9. Mostrar Evidencias del Proceso
```

---

## Ejemplo de Uso

### Datos del Usuario
```
Nombre: Juan García
Ingredientes disponibles: 
  - pasta, tomate, cebolla, ajo, aceite de oliva, sal, huevo, pechuga de pollo
Preferencias gastronómicas: italiana, española
Restricciones dietéticas: vegetariano
Tiempo disponible: 30 minutos
Nivel de habilidad: intermedio
```

### Proceso
1. **Validación:** ✓ Datos válidos
2. **Filtrado:** 
   - ✗ Pechuga de Pollo descartada (no es vegetariana)
   - ✓ Pasta, Omeleta, Salmón pasan filtros
3. **Puntuación:**
   - Pasta a la Tomate: 100% ingredientes → base 80 → italiana +15 → vegetariana +20 → rapidez +10 = **125**
   - Omeleta: 100% ingredientes → base 80 → rapidez +10 → nivel +5 = **95**
   - Salmón: 85.7% ingredientes → base 83 → nivel +5 = **88**
4. **DMN:**
   - Pasta: 100% + 20 min → BUENA (+15)
   - Salmón: 85.7% + 25 min → BUENA (+15)
5. **Ranking Final:**
   1. Pasta a la Tomate: **140**
   2. Omeleta: **95**
   3. Salmón: **103**

---

## Queries Disponibles

El sistema proporciona 4 queries para consultar información:

### 1. ObtenerRecetasDescartadas
```drl
query "ObtenerRecetasDescartadas"
    $recomendacion: Recomendacion($estado: estado == "descartada")
end
```
Retorna todas las recetas descartadas con sus motivos.

### 2. ObtenerRecetasRecomendadas
```drl
query "ObtenerRecetasRecomendadas"
    $recomendacion: Recomendacion($estado: estado == "recomendada" || estado == "alternativa")
    order by $recomendacion.puntuacion desc
end
```
Retorna recetas ordenadas por puntuación descendente.

### 3. ObtenerRecomendacionesConExplicacion
```drl
query "ObtenerRecomendacionesConExplicacion"
    $recomendacion: Recomendacion(...)
end
```
Retorna recomendaciones con explicaciones detalladas.

### 4. ObtenerEvaluacionesRecetas (DMN)
```drl
query "ObtenerEvaluacionesRecetas"
    $evaluacion: EvaluacionReceta(...)
    order by $puntuacion desc
end
```
Retorna evaluaciones de calidad ordenadas.

---

## Modularización en KModule

El archivo `kmodule.xml` configura 6 módulos Drools con inclusiones jerárquicas:

```xml
BaseDatos
    ↓
Validacion (includes: BaseDatos)
    ↓
Filtrado (includes: Validacion)
    ↓
Puntuacion (includes: Filtrado)
    ↓
Explicacion (includes: Puntuacion)
    ↓
Informe (includes: Explicacion) [DEFAULT]
    ↓
Decision DMN (integrado en todas las sesiones)
```

Cada módulo hereda los hechos y reglas anteriores.

---

## Validación de Datos

La clase `Usuario` implementa el método `validarDatos()`:

```java
public boolean validarDatos() {
    // Valida:
    // 1. Nombre no vacío
    // 2. Tiempo ≥ 5 minutos
    // 3. Nivel válido: "principiante|intermedio|avanzado"
    
    // Retorna:
    // - true si es válido
    // - false si hay errores críticos
    
    // Genera mensajes de feedback en tipsvalidacion
}
```

---

## Fuentes del Conocimiento

### Documentales
- Tablas de composición de alimentos (propiedades nutricionales)
- Recetas populares de cocina (estructuras y tiempos)
- Guías de dietas especializadas

### Humanas
- Chefs profesionales (recetas y técnicas)
- Nutricionistas (restricciones dietéticas)
- Usuarios experimentados (preferencias)

### Académicas
- Material de Sistemas Basados en el Conocimiento
- Metodología CommonKADS
- Conceptos de Ingeniería del Conocimiento

---

## Estructura de Archivos

```
src/main/java/sbc/example/
├── Ingrediente.java
├── Receta.java
├── IngredienteReceta.java
├── Usuario.java
├── Recomendacion.java
├── EvaluacionReceta.java
└── Main.java

src/main/resources/sbc.example/
├── 01_basedatos.drl
├── 02_validacion.drl
├── 03_filtrado.drl
├── 04_puntuacion.drl
├── 05_explicacion.drl
├── 06_informe.drl
├── 07_decision_dmn.drl
└── EvaluacionRecetas.dmn

src/main/resources/META-INF/
└── kmodule.xml
```

---

## Ejecución del Sistema

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="sbc.example.Main"
```

El sistema mostrará:
1. Inicialización de base de datos
2. Validación de datos del usuario
3. Proceso de filtrado con motivos
4. Cálculo de puntuaciones con bonificaciones
5. Evaluaciones DMN
6. Explicaciones de recomendaciones
7. Informe final estructurado
8. Evidencias y justificaciones completas

---

## Evidencias Recabadas

El sistema registra y muestra:
- ✓ Datos del usuario validados
- ✓ Ingredientes disponibles vs. requeridos
- ✓ Motivos de descarte específicos
- ✓ Factores de bonificación aplicados
- ✓ Clasificaciones DMN
- ✓ Ranking final ordenado
- ✓ Justificaciones completas de cada recomendación

---

## Características Clave

✅ **Modularización:** 7 módulos DRL independientes  
✅ **Validación:** Verificación completa de datos de entrada  
✅ **Queries:** 4 queries diferentes para obtener información  
✅ **DMN:** Integración de tabla de decisiones  
✅ **Explicabilidad:** Justificaciones detalladas de recomendaciones  
✅ **Escalabilidad:** Fácil agregar nuevas recetas e ingredientes  
✅ **Ontología:** Relaciones claras entre entidades  

---

## Versión

**1.0** - Sistema inicial completo  
**Drools:** 8.35.0.Final  
**Java:** 11+  
**Maven:** 3.6+
