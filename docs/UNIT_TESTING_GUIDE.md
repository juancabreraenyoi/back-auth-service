# Guía Completa de Tests Unitarios en Spring Boot

Esta guía te enseñará cómo escribir tests unitarios efectivos para tu aplicación Spring Boot siguiendo Clean Architecture.

## 📋 Tabla de Contenidos

1. [Introducción a Tests Unitarios](#introducción-a-tests-unitarios)
2. [Configuración del Proyecto](#configuración-del-proyecto)
3. [Anatomía de un Test Unitario](#anatomía-de-un-test-unitario)
4. [Estrategia de Testing](#estrategia-de-testing)
5. [Testing de Casos de Uso](#testing-de-casos-de-uso)
6. [Testing de Controladores](#testing-de-controladores)
7. [Testing de Adaptadores](#testing-de-adaptadores)
8. [Mocking y Stubbing](#mocking-y-stubbing)
9. [Casos de Prueba Comunes](#casos-de-prueba-comunes)
10. [Mejores Prácticas](#mejores-prácticas)
11. [Ejercicios Prácticos](#ejercicios-prácticos)

---

## Introducción a Tests Unitarios

### ¿Qué es un Test Unitario?

Un test unitario es una prueba automatizada que verifica el comportamiento de una **unidad** de código (generalmente un método o clase) de forma **aislada**.

### ¿Por qué son importantes?

- ✅ **Detectan bugs temprano** antes de llegar a producción
- ✅ **Documentan el comportamiento** esperado del código
- ✅ **Facilitan refactoring** con confianza
- ✅ **Mejoran el diseño** del código (código testeable = código bien diseñado)
- ✅ **Reducen costos** de mantenimiento

### Pirámide de Testing

```
        /\
       /  \      E2E Tests (pocos, lentos, costosos)
      /____\
     /      \    Integration Tests (algunos, moderados)
    /________\
   /          \  Unit Tests (muchos, rápidos, baratos)
  /__________\
```

---

## Configuración del Proyecto

### Dependencias Necesarias

Ya están incluidas en `build.gradle`:

```gradle
dependencies {
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    testImplementation 'org.mockito:mockito-core'
    testImplementation 'org.mockito:mockito-junit-jupiter'
    testImplementation 'org.junit.jupiter:junit-jupiter'
}
```

### Estructura de Directorios

```
src/
├── main/
│   └── java/
│       └── com/arka/
│           ├── RegisterUserUseCase.java
│           └── ...
└── test/
    └── java/
        └── com/arka/
            ├── RegisterUserUseCaseTest.java  ← Tests unitarios
            └── ...
```

---

## Anatomía de un Test Unitario

### Estructura AAA (Arrange-Act-Assert)

```java
@Test
void shouldDoSomething() {
    // ARRANGE (Preparar): Configurar el escenario
    var input = "test data";
    var expected = "expected result";
    
    // ACT (Actuar): Ejecutar la acción
    var actual = systemUnderTest.method(input);
    
    // ASSERT (Afirmar): Verificar el resultado
    assertEquals(expected, actual);
}
```

### Nomenclatura de Tests

**Patrón:** `should[ExpectedBehavior]When[StateUnderTest]`

Ejemplos:
- `shouldRegisterUserWhenValidDataProvided()`
- `shouldThrowExceptionWhenEmailAlreadyExists()`
- `shouldReturnUserWhenEmailIsValid()`

---

## Estrategia de Testing

### ¿Qué testear en Clean Architecture?

#### 1. **Casos de Uso (Use Cases)** - PRIORIDAD ALTA ⭐⭐⭐
- Contienen la lógica de negocio
- Son independientes de frameworks
- Fáciles de testear

#### 2. **Entidades de Dominio** - PRIORIDAD ALTA ⭐⭐⭐
- Reglas de negocio core
- Validaciones

#### 3. **Controladores** - PRIORIDAD MEDIA ⭐⭐
- Validación de requests
- Mapeo de DTOs

#### 4. **Adaptadores** - PRIORIDAD MEDIA ⭐⭐
- Integración con servicios externos
- Mapeo de entidades

---

## Testing de Casos de Uso

### Ejemplo 1: RegisterUserUseCase

#### Casos de Prueba a Considerar

1. ✅ **Happy Path**: Usuario se registra exitosamente
2. ❌ **Email ya existe**: Debe lanzar excepción
3. ❌ **Password inválido**: Debe lanzar excepción
4. ✅ **Rol por defecto**: Usuario debe tener rol USER
5. ✅ **Password encriptado**: Password debe estar hasheado

#### Implementación del Test

```java
package com.arka;

import com.arka.entities.User;
import com.arka.entities.enums.Role;
import com.arka.entities.request.Register;
import com.arka.exceptions.EmailAlreadyExistsException;
import com.arka.gateway.JwtGateway;
import com.arka.gateway.SecurityGateway;
import com.arka.gateway.UserGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RegisterUserUseCase Tests")
class RegisterUserUseCaseTest {

    @Mock
    private UserGateway userGateway;

    @Mock
    private SecurityGateway securityGateway;

    @Mock
    private JwtGateway jwtGateway;

    @InjectMocks
    private RegisterUserUseCase registerUserUseCase;

    private Register validRegister;
    private User expectedUser;

    @BeforeEach
    void setUp() {
        // Arrange: Preparar datos de prueba
        validRegister = Register.builder()
                .email("test@example.com")
                .password("Password123")
                .build();

        expectedUser = User.builder()
                .id(1L)
                .email("test@example.com")
                .password("hashedPassword")
                .role(Role.USER)
                .build();
    }

    @Test
    @DisplayName("Debe registrar usuario exitosamente con datos válidos")
    void shouldRegisterUserSuccessfullyWhenValidDataProvided() {
        // Arrange
        when(userGateway.existsByEmail(validRegister.getEmail())).thenReturn(false);
        when(securityGateway.encodePassword(validRegister.getPassword())).thenReturn("hashedPassword");
        when(userGateway.save(any(User.class))).thenReturn(expectedUser);
        when(jwtGateway.generateToken(anyString())).thenReturn("jwt-token");

        // Act
        var result = registerUserUseCase.execute(validRegister);

        // Assert
        assertNotNull(result);
        assertEquals("jwt-token", result.getAccessToken());
        assertEquals("test@example.com", result.getUserResponse().getEmail());
        assertEquals(Role.USER, result.getUserResponse().getRole());

        // Verify interactions
        verify(userGateway, times(1)).existsByEmail(validRegister.getEmail());
        verify(securityGateway, times(1)).encodePassword(validRegister.getPassword());
        verify(userGateway, times(1)).save(any(User.class));
        verify(jwtGateway, times(1)).generateToken(validRegister.getEmail());
    }

    @Test
    @DisplayName("Debe lanzar EmailAlreadyExistsException cuando el email ya existe")
    void shouldThrowEmailAlreadyExistsExceptionWhenEmailExists() {
        // Arrange
        when(userGateway.existsByEmail(validRegister.getEmail())).thenReturn(true);

        // Act & Assert
        assertThrows(EmailAlreadyExistsException.class, () -> {
            registerUserUseCase.execute(validRegister);
        });

        // Verify que no se llamaron otros métodos
        verify(userGateway, times(1)).existsByEmail(validRegister.getEmail());
        verify(securityGateway, never()).encodePassword(anyString());
        verify(userGateway, never()).save(any(User.class));
        verify(jwtGateway, never()).generateToken(anyString());
    }

    @Test
    @DisplayName("Debe encriptar la contraseña antes de guardar")
    void shouldEncryptPasswordBeforeSaving() {
        // Arrange
        when(userGateway.existsByEmail(anyString())).thenReturn(false);
        when(securityGateway.encodePassword(validRegister.getPassword())).thenReturn("hashedPassword");
        when(userGateway.save(any(User.class))).thenReturn(expectedUser);
        when(jwtGateway.generateToken(anyString())).thenReturn("jwt-token");

        // Act
        registerUserUseCase.execute(validRegister);

        // Assert
        verify(securityGateway, times(1)).encodePassword("Password123");
        verify(userGateway, times(1)).save(argThat(user -> 
            user.getPassword().equals("hashedPassword")
        ));
    }

    @Test
    @DisplayName("Debe asignar rol USER por defecto")
    void shouldAssignUserRoleByDefault() {
        // Arrange
        when(userGateway.existsByEmail(anyString())).thenReturn(false);
        when(securityGateway.encodePassword(anyString())).thenReturn("hashedPassword");
        when(userGateway.save(any(User.class))).thenReturn(expectedUser);
        when(jwtGateway.generateToken(anyString())).thenReturn("jwt-token");

        // Act
        registerUserUseCase.execute(validRegister);

        // Assert
        verify(userGateway, times(1)).save(argThat(user -> 
            user.getRole() == Role.USER
        ));
    }
}
```

---

## Testing de Controladores

### Ejemplo: AuthController

```java
package com.arka.controllers;

import com.arka.RegisterUserUseCase;
import com.arka.entities.Auth;
import com.arka.entities.UserResponse;
import com.arka.entities.enums.Role;
import com.arka.entities.request.Register;
import com.arka.request.RegisterRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@DisplayName("AuthController Tests")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RegisterUserUseCase registerUserUseCase;

    private RegisterRequest validRequest;
    private Auth expectedAuth;

    @BeforeEach
    void setUp() {
        validRequest = new RegisterRequest();
        validRequest.setEmail("test@example.com");
        validRequest.setPassword("Password123");

        var userResponse = UserResponse.builder()
                .id(1L)
                .email("test@example.com")
                .role(Role.USER)
                .build();

        expectedAuth = Auth.builder()
                .accessToken("jwt-token")
                .userResponse(userResponse)
                .build();
    }

    @Test
    @DisplayName("POST /auth/register debe retornar 201 con datos válidos")
    void shouldReturn201WhenRegisterWithValidData() throws Exception {
        // Arrange
        when(registerUserUseCase.execute(any(Register.class))).thenReturn(expectedAuth);

        // Act & Assert
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accessToken").value("jwt-token"))
                .andExpect(jsonPath("$.user.email").value("test@example.com"))
                .andExpect(jsonPath("$.user.role").value("USER"));
    }

    @Test
    @DisplayName("POST /auth/register debe retornar 400 con email inválido")
    void shouldReturn400WhenRegisterWithInvalidEmail() throws Exception {
        // Arrange
        validRequest.setEmail("invalid-email");

        // Act & Assert
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /auth/register debe retornar 400 con password vacío")
    void shouldReturn400WhenRegisterWithEmptyPassword() throws Exception {
        // Arrange
        validRequest.setPassword("");

        // Act & Assert
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isBadRequest());
    }
}
```

---

## Mocking y Stubbing

### ¿Qué es Mocking?

**Mocking** es crear objetos "falsos" que simulan el comportamiento de objetos reales.

### Tipos de Test Doubles

1. **Mock**: Objeto falso que verifica interacciones
2. **Stub**: Objeto falso que retorna valores predefinidos
3. **Spy**: Objeto real con algunos métodos mockeados
4. **Fake**: Implementación simplificada

### Mockito Cheat Sheet

```java
// Crear mock
@Mock
private UserGateway userGateway;

// Inyectar mocks
@InjectMocks
private RegisterUserUseCase useCase;

// Stubbing (definir comportamiento)
when(userGateway.findByEmail("test@example.com"))
    .thenReturn(Optional.of(user));

// Verificar llamadas
verify(userGateway, times(1)).save(any(User.class));
verify(userGateway, never()).delete(any());

// Capturar argumentos
ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
verify(userGateway).save(captor.capture());
User savedUser = captor.getValue();

// Lanzar excepciones
when(userGateway.save(any())).thenThrow(new RuntimeException("Error"));

// Verificar con matchers
verify(userGateway).save(argThat(user -> 
    user.getEmail().equals("test@example.com")
));
```

---

## Casos de Prueba Comunes

### 1. Happy Path (Camino Feliz)

```java
@Test
void shouldExecuteSuccessfullyWithValidInput() {
    // Arrange
    var validInput = createValidInput();
    var expectedOutput = createExpectedOutput();
    when(dependency.method(any())).thenReturn(expectedOutput);
    
    // Act
    var result = useCase.execute(validInput);
    
    // Assert
    assertEquals(expectedOutput, result);
}
```

### 2. Validación de Entrada

```java
@Test
void shouldThrowExceptionWhenInputIsNull() {
    assertThrows(IllegalArgumentException.class, () -> {
        useCase.execute(null);
    });
}

@Test
void shouldThrowExceptionWhenEmailIsInvalid() {
    var invalidInput = createInputWithInvalidEmail();
    
    assertThrows(ValidationException.class, () -> {
        useCase.execute(invalidInput);
    });
}
```

### 3. Manejo de Errores

```java
@Test
void shouldHandleExceptionFromDependency() {
    // Arrange
    when(dependency.method(any())).thenThrow(new RuntimeException("Error"));
    
    // Act & Assert
    assertThrows(ServiceException.class, () -> {
        useCase.execute(validInput);
    });
}
```

### 4. Verificación de Interacciones

```java
@Test
void shouldCallDependenciesInCorrectOrder() {
    // Arrange
    InOrder inOrder = inOrder(dependency1, dependency2);
    
    // Act
    useCase.execute(validInput);
    
    // Assert
    inOrder.verify(dependency1).method1();
    inOrder.verify(dependency2).method2();
}
```

### 5. Casos Límite (Edge Cases)

```java
@Test
void shouldHandleEmptyList() {
    when(repository.findAll()).thenReturn(Collections.emptyList());
    
    var result = useCase.execute();
    
    assertTrue(result.isEmpty());
}

@Test
void shouldHandleMaximumValue() {
    var maxInput = Integer.MAX_VALUE;
    
    assertDoesNotThrow(() -> {
        useCase.execute(maxInput);
    });
}
```

---

## Mejores Prácticas

### ✅ DO (Hacer)

1. **Un concepto por test**
   ```java
   @Test
   void shouldValidateEmail() { /* solo valida email */ }
   
   @Test
   void shouldValidatePassword() { /* solo valida password */ }
   ```

2. **Tests independientes**
   - Cada test debe poder ejecutarse solo
   - No depender del orden de ejecución

3. **Nombres descriptivos**
   ```java
   @Test
   @DisplayName("Debe lanzar EmailAlreadyExistsException cuando el email ya existe")
   void shouldThrowEmailAlreadyExistsExceptionWhenEmailExists() { }
   ```

4. **Usar @BeforeEach para setup común**
   ```java
   @BeforeEach
   void setUp() {
       validInput = createValidInput();
   }
   ```

5. **Verificar comportamiento, no implementación**
   ```java
   // ✅ Bueno
   verify(userGateway).save(any(User.class));
   
   // ❌ Malo (demasiado específico)
   verify(userGateway).save(argThat(user -> 
       user.getId() == 1L && 
       user.getEmail().equals("test@example.com") &&
       user.getPassword().length() > 8
   ));
   ```

### ❌ DON'T (No Hacer)

1. **Tests que dependen de otros tests**
2. **Tests con lógica compleja**
3. **Tests que testean frameworks**
4. **Tests lentos** (usar mocks en lugar de BD real)
5. **Tests frágiles** (que fallan por cambios menores)

---

## Ejercicios Prácticos

### Ejercicio 1: LoginUserUseCase

**Objetivo:** Escribir tests para `LoginUserUseCase`

**Casos a testear:**
1. Login exitoso con credenciales válidas
2. Excepción cuando el usuario no existe
3. Excepción cuando la contraseña es incorrecta
4. Verificar que se genera un token JWT
5. Verificar que se valida la contraseña con SecurityGateway

**Plantilla:**

```java
@ExtendWith(MockitoExtension.class)
class LoginUserUseCaseTest {

    @Mock
    private UserGateway userGateway;

    @Mock
    private SecurityGateway securityGateway;

    @Mock
    private JwtGateway jwtGateway;

    @InjectMocks
    private LoginUserUseCase loginUserUseCase;

    @Test
    void shouldLoginSuccessfullyWithValidCredentials() {
        // TODO: Implementar
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // TODO: Implementar
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsIncorrect() {
        // TODO: Implementar
    }
}
```

### Ejercicio 2: ForgotPasswordUseCase

**Objetivo:** Escribir tests para `ForgotPasswordUseCase`

**Casos a testear:**
1. Envío exitoso de email de recuperación
2. Excepción cuando el usuario no existe
3. Verificar que se genera un token de reset
4. Verificar que se guarda el token en la BD
5. Verificar que se envía el email con EmailGateway

### Ejercicio 3: AssignRoleUseCase

**Objetivo:** Escribir tests para `AssignRoleUseCase`

**Casos a testear:**
1. Asignación exitosa de rol ADMIN
2. Asignación exitosa de rol USER
3. Excepción cuando el usuario no existe
4. Verificar que se actualiza el usuario en la BD

---

## Cobertura de Código

### ¿Qué es la cobertura?

Porcentaje de código ejecutado por los tests.

### Configurar JaCoCo (Java Code Coverage)

Agregar en `build.gradle`:

```gradle
plugins {
    id 'jacoco'
}

jacoco {
    toolVersion = "0.8.11"
}

test {
    finalizedBy jacocoTestReport
}

jacocoTestReport {
    dependsOn test
    reports {
        xml.required = true
        html.required = true
    }
}

jacocoTestCoverageVerification {
    violationRules {
        rule {
            limit {
                minimum = 0.80 // 80% de cobertura mínima
            }
        }
    }
}
```

### Generar reporte de cobertura

```bash
./gradlew test jacocoTestReport
```

El reporte HTML estará en: `build/reports/jacoco/test/html/index.html`

---

## Comandos Útiles

```bash
# Ejecutar todos los tests
./gradlew test

# Ejecutar tests de un módulo específico
./gradlew :usecase:test

# Ejecutar un test específico
./gradlew test --tests RegisterUserUseCaseTest

# Ejecutar tests con reporte detallado
./gradlew test --info

# Ejecutar tests en modo continuo
./gradlew test --continuous
```

---

## Recursos Adicionales

- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [Spring Boot Testing](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.testing)
- [Test Driven Development (TDD)](https://martinfowler.com/bliki/TestDrivenDevelopment.html)

---

## Resumen

1. **Tests unitarios** son esenciales para código de calidad
2. **Estructura AAA**: Arrange, Act, Assert
3. **Mockito** para crear mocks y stubs
4. **Testear casos de uso** es la prioridad más alta
5. **Un concepto por test** para mantener simplicidad
6. **Nombres descriptivos** para documentar comportamiento
7. **Cobertura de 80%+** es un buen objetivo

---

¡Ahora estás listo para escribir tests unitarios de calidad! 🚀
