package main;

import entities.Usuario;
import entities.CredencialAcceso;
import service.UsuarioService;
import exceptions.ValidationException;
import exceptions.DatabaseException;
import exceptions.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class AppMenu {

    private static final Scanner scanner = new Scanner(System.in);
    private static final UsuarioService usuarioService = new UsuarioService();

    public static void main(String[] args) {
        int opcion;

        try {
            do {
                mostrarMenu();
                opcion = leerEntero("Seleccione una opcion: ");

                switch (opcion) {
                    case 1:
                        altaUsuario();
                        break;
                    case 2:
                        listarUsuarios();
                        break;
                    case 3:
                        bajaUsuario();
                        break;
                    case 4:
                        buscarUsuarioPorEmail();
                        break;
                    case 5:
                        buscarUsuarioPorUsername();
                        break;
                    case 6:
                        verUsuarioPorId();
                        break;
                    case 7:
                        actualizarUsuario();
                        break;
                    case 0:
                        System.out.println("Saliendo de la aplicacion...");
                        break;
                    default:
                        System.out.println("Opcion invalida, intente nuevamente.");
                }

                System.out.println();
            } while (opcion != 0);
        } finally {
            // Cerramos el scanner al salir de la aplicación
            scanner.close();
        }
    }

    private static void mostrarMenu() {
        System.out.println("=====================================");
        System.out.println("      MENU TPI PROGRAMACION II       ");
        System.out.println("=====================================");
        System.out.println("1) Alta de usuario");
        System.out.println("2) Listar usuarios");
        System.out.println("3) Baja logica de usuario");
        System.out.println("4) Buscar usuario por email");
        System.out.println("5) Buscar usuario por username");
        System.out.println("6) Ver usuario por ID");
        System.out.println("7) Actualizar usuario");
        System.out.println("0) Salir");
        System.out.println("=====================================");
    }

    // =====================
    // Opciones de menú
    // =====================

    private static void altaUsuario() {
        System.out.println("--- Alta de usuario ---");

        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Password (simple, sin hash para prueba): ");
        String password = scanner.nextLine();

        // Armamos la credencial
        CredencialAcceso cred = new CredencialAcceso();
        
        // En un sistema real, aquí NO se guardaría el texto plano,
        // sino el hash de la contraseña
        cred.setHashPassword(password);
        cred.setSalt("N/A");
        cred.setUltimoCambio(LocalDateTime.now());
        cred.setRequiereReset(false);
        cred.setEliminado(false);

        // Armamos el usuario
        Usuario u = new Usuario();
        u.setUsername(username);
        u.setEmail(email);
        u.setActivo(true);
        u.setFechaRegistro(LocalDateTime.now());
        u.setEliminado(false);
        u.setCredencialAcceso(cred);

        try {
            usuarioService.insertar(u);
            System.out.println("Usuario creado correctamente con ID: " + u.getId());
        } catch (ValidationException | DatabaseException e) {
            System.out.println("Error al crear usuario: " + e.getMessage());
        }
    }

    private static void listarUsuarios() {
        System.out.println("--- Listado de usuarios ---");

        List<Usuario> usuarios = usuarioService.getAll();
        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios cargados.");
            return;
        }
        
        // Nota: el listado NO incluye usuarios eliminados lógicamente (eliminado = 1).
        // Esto se controla desde el UsuarioDao con la condición WHERE eliminado = 0.
        for (Usuario u : usuarios) {
            System.out.println("ID: " + u.getId()
                    + " | Username: " + u.getUsername()
                    + " | Email: " + u.getEmail()
                    + " | Activo: " + u.isActivo()
                    + " | Eliminado: " + u.isEliminado());
        }
    }

    private static void bajaUsuario() {
        System.out.println("--- Baja lógica de usuario ---");

        long id = leerLong("Ingrese el ID del usuario a eliminar: ");

        Usuario existente;

        try {
            existente = usuarioService.getById(id);

            if (existente == null) {
                System.out.println("No existe un usuario con ese ID.");
                return;
            }

        } catch (EntityNotFoundException e) {
            System.out.println("No existe un usuario con ese ID.");
            return;
        } catch (DatabaseException e) {
            System.out.println("Error de base de datos al buscar el usuario: " + e.getMessage());
            return;
        }

        // Si llegamos hasta acá, el usuario existe
        try {
            usuarioService.eliminar(id);
            System.out.println("Usuario eliminado (baja lógica) correctamente.");
        } catch (DatabaseException e) {
            System.out.println("Error de base de datos al eliminar usuario: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error inesperado al eliminar usuario: " + e.getMessage());
        }
    }

    
    private static void verUsuarioPorId() {
        System.out.println("--- Ver usuario por ID ---");

        long id = leerLong("Ingrese el ID del usuario: ");

        try {
            Usuario u = usuarioService.getById(id);

            // Si getById devuelve null
            if (u == null) {
                System.out.println("No existe un usuario con ese ID.");
                return;
            }

            // Si llega acá, el usuario existe
            System.out.println("Usuario encontrado:");
            System.out.println("ID: " + u.getId()
                    + " | Username: " + u.getUsername()
                    + " | Email: " + u.getEmail()
                    + " | Activo: " + u.isActivo()
                    + " | Eliminado: " + u.isEliminado());

        } catch (EntityNotFoundException e) {
            // Por si el DAO lanza esta excepción cuando no encuentra nada
            System.out.println("No existe un usuario con ese ID.");
        } catch (DatabaseException e) {
            System.out.println("Error de base de datos al buscar por ID: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error inesperado al buscar usuario: " + e.getMessage());
        }
    }
    
    private static void actualizarUsuario() {
        System.out.println("--- Actualizar usuario ---");

        long id = leerLong("Ingrese el ID del usuario a actualizar: ");

        Usuario u;

        try {
            u = usuarioService.getById(id);

            if (u == null) {
                System.out.println("No existe un usuario con ese ID.");
                return;
            }

        } catch (EntityNotFoundException e) {
            System.out.println("No existe un usuario con ese ID.");
            return;
        } catch (DatabaseException e) {
            System.out.println("Error de base de datos al buscar el usuario: " + e.getMessage());
            return;
        }

        // Si llegamos hasta acá, tenemos un usuario válido
        System.out.println("Usuario actual:");
        System.out.println("ID: " + u.getId()
                + " | Username: " + u.getUsername()
                + " | Email: " + u.getEmail());

        System.out.println("Deje el campo vacío y presione ENTER para mantener el valor actual.");

        System.out.print("Nuevo username (" + u.getUsername() + "): ");
        String nuevoUsername = scanner.nextLine().trim();
        if (!nuevoUsername.isBlank()) {
            u.setUsername(nuevoUsername.toUpperCase());
        }

        System.out.print("Nuevo email (" + u.getEmail() + "): ");
        String nuevoEmail = scanner.nextLine().trim();
        if (!nuevoEmail.isBlank()) {
            u.setEmail(nuevoEmail.toUpperCase());
        }

        // Podrías agregar también cambio de "activo" si querés:
        // System.out.print("¿Activo? (S/N) [" + (u.isActivo() ? "S" : "N") + "]: ");
        // ...

        try {
            usuarioService.actualizar(u);
            System.out.println("Usuario actualizado correctamente.");
        } catch (ValidationException e) {
            System.out.println("Error de validación: " + e.getMessage());
        } catch (DatabaseException e) {
            System.out.println("Error de base de datos al actualizar: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error inesperado al actualizar: " + e.getMessage());
        }
    }
    
    private static void buscarUsuarioPorUsername() {
        System.out.println("--- Buscar usuario por username ---");
        System.out.print("Ingrese el username: ");
        String username = scanner.nextLine().trim().toUpperCase(); // mayúsculas

        try {
            Usuario u = usuarioService.buscarPorUsername(username);
            if (u != null) {
                System.out.println("Usuario encontrado:");
                System.out.println("ID: " + u.getId()
                        + " | Username: " + u.getUsername()
                        + " | Email: " + u.getEmail());
            } else {
                System.out.println("No existe un usuario con ese username.");
            }
        } catch (Exception e) {
            System.out.println("Error al buscar usuario: " + e.getMessage());
        }
    }
    
    private static void buscarUsuarioPorEmail() {
        System.out.println("--- Buscar usuario por email ---");

        System.out.print("Ingrese el email: ");
        String email = scanner.nextLine();

        try {
            Usuario u = usuarioService.buscarPorEmail(email);
            if (u != null) {
                System.out.println("Usuario encontrado: ");
                System.out.println("ID: " + u.getId()
                        + " | Username: " + u.getUsername()
                        + " | Email: " + u.getEmail());
            } else {
                System.out.println("No existe un usuario con ese email.");
            }
        } catch (Exception e) {
            System.out.println("Error al buscar usuario: " + e.getMessage());
        }
    }


    // =====================
    // Helpers de lectura
    // =====================

    private static int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String linea = scanner.nextLine();
            try {
                return Integer.parseInt(linea.trim());
            } catch (NumberFormatException e) {
                System.out.println("Por favor ingrese un numero entero valido.");
            }
        }
    }

    private static long leerLong(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String linea = scanner.nextLine();
            try {
                return Long.parseLong(linea.trim());
            } catch (NumberFormatException e) {
                System.out.println("Por favor ingrese un numero entero valido.");
            }
        }
    }
}
