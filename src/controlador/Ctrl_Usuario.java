/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import DAO.Crud_Auditoria;
import DAO.Crud_Usuario;
import Vistas.Dashboard;
import Vistas.FrmAuditoria;
import Vistas.FrmRol;
import Vistas.FrmUsuario;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import javax.swing.JOptionPane;
import modelo.Usuario;

public class Ctrl_Usuario implements ActionListener {

    static int obtenerIdRolCombo = 0;
    FrmUsuario vista;
    Crud_Usuario crud;

    public Ctrl_Usuario(FrmUsuario vista) {
        this.vista = vista;
        this.crud = new Crud_Usuario();
        Crud_Usuario.CargarTablaUsuarios();
        this.vista.btn_guardar.addActionListener(this);
        this.vista.btn_actualizar.addActionListener(this);
        this.vista.btn_eliminar.addActionListener(this);
        this.vista.btn_buscar.addActionListener(this);
        this.vista.btn_Roles.addActionListener(this);
        this.vista.btn_auditoria.addActionListener(this);
        
// Modificar el campo de contraseña para que solo muestre asteriscos
    

   

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btn_buscar) {
            //BUSCA EL CLIENTE SEGUN EL DNI
            if (FrmUsuario.txt_buscar_dniUsuario.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Escribe el DNI del usuario a buscar");

            } else {
                String usuarioBuscar = FrmUsuario.txt_buscar_dniUsuario.getText().trim();
                Connection cn = DAO.Conexion.conectar();
                String sql = "SELECT * FROM tb_usuario where DNI = '" + usuarioBuscar + "'";
                Statement st;
                try {
                    st = cn.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    if (rs.next()) {

                        FrmUsuario.txt_gestionar_nombre.setText(rs.getString("nombre")); //"
                        FrmUsuario.txt_gestionar_apellido.setText(rs.getString("apellido")); //"
                        FrmUsuario.txt_gestionar_dni.setText(rs.getString("dni")); //"
                        FrmUsuario.txt_gestionar_usuario.setText(rs.getString("usuario")); //"
                        FrmUsuario.txt_gestionar_password.setText(rs.getString("password")); //"
                        FrmUsuario.txt_gestionar_telefono.setText(rs.getString("telefono")); //"
                    } else {
                        JOptionPane.showMessageDialog(null, "¡Dni de usuario incorrecta o no encontrada!");
                    }
                    FrmUsuario.txt_buscar_dniUsuario.setText("");
                    cn.close();
                } catch (SQLException a) { // como el "e" está ocupado se coloca "a
                    System.out.println("¡Error al buscar usuario!, " + a);
                }
            }

        }
        if (e.getSource() == vista.btn_guardar) {
            Usuario usuario = new Usuario();
            Crud_Usuario controlUsuario = new Crud_Usuario();
            String rol = FrmUsuario.jComboBoxRol.getSelectedItem().toString().trim();

            // Obtener valores de los campos
            String nombre = FrmUsuario.txt_nombre.getText().trim();
            String apellido = FrmUsuario.txt_apellido.getText().trim();
            String dni = FrmUsuario.txt_dni.getText().trim();
            String usuarioText = FrmUsuario.txt_usuario.getText().trim();
            String password = FrmUsuario.txt_password.getText().trim();
            String telefono = FrmUsuario.txt_telefono.getText().trim();

            // Validar que los campos no estén vacíos
            if (nombre.isEmpty() || apellido.isEmpty() || dni.isEmpty() || usuarioText.isEmpty() || password.isEmpty() || telefono.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Completa todos los campos");
                //VALIDAR QUE LOS CAMPOS CUMPLA LAS CONDICIONES NUMÉRICAS Y LITERALES
            } else {
                boolean validacionNombre = esSoloLetras(nombre);
                boolean validacionApellido = esSoloLetras(apellido);
                boolean validacionDni = esNumerico(dni) && dni.length() == 8;
                boolean validacionTelefono = esNumerico(telefono) && telefono.length() == 9;

                if (!validacionNombre && !validacionApellido) {
                    JOptionPane.showMessageDialog(null, "Los campos Nombre y Apellido solo aceptan letras");
                } else if (!validacionNombre) {
                    JOptionPane.showMessageDialog(null, "El campo Nombre solo acepta letras");
                } else if (!validacionApellido) {
                    JOptionPane.showMessageDialog(null, "El campo Apellido solo acepta letras");
                } else if (!validacionDni && !validacionTelefono) {
                    JOptionPane.showMessageDialog(null, "El DNI debe tener 8 dígitos y el campo TELÉFONO debe tener 9 dígitos, ambos solo aceptan valores numéricos");
                } else if (!validacionDni) {
                    JOptionPane.showMessageDialog(null, "El campo DNI debe tener 8 dígitos y solo acepta valores numéricos");
                } else if (!validacionTelefono) {
                    JOptionPane.showMessageDialog(null, "El campo TELÉFONO debe tener 9 dígitos y solo acepta valores numéricos");
                } else {
                    // Validar que se haya seleccionado un rol válido
                    if (rol.equalsIgnoreCase("Seleccione ROL:")) {
                        JOptionPane.showMessageDialog(null, "Seleccione un rol");
                    } else {
                        // Validar si el usuario ya está registrado
                        if (!controlUsuario.existeUsuario(dni)) {
                            // Enviar datos del usuario
                            usuario.setNombre(nombre);
                            usuario.setApellido(apellido);

                            if (rol.equalsIgnoreCase("ADMIN")) {
                                usuario.setIdRol(1);
                            } else if (rol.equalsIgnoreCase("CAJERO")) {
                                usuario.setIdRol(2);
                            } else if (rol.equalsIgnoreCase("MESERO")) {
                                usuario.setIdRol(3);
                            }

                            this.IdRol();
                            usuario.setIdRol(obtenerIdRolCombo);

                            usuario.setDni(dni);
                            usuario.setUsuario(usuarioText);
                            usuario.setPassword(password);
                            usuario.setTelefono(telefono);
                            usuario.setEstado(1);

                            int confirmacion = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que quieres GUARDAR este USUARIO?", "Confirmar Actualización", JOptionPane.YES_NO_OPTION);
                            if (confirmacion == JOptionPane.YES_OPTION) {
                                if (controlUsuario.guardar(usuario)) {
                                    JOptionPane.showMessageDialog(null, "¡Usuario Registrado!");
                                    // Se enviará los datos a tb_auditoria
                                    registrarAccion("Logró agregar un(a) nuevo(a) usuario(a)");
                                    Crud_Usuario.CargarTablaUsuarios();
                                    Crud_Usuario.Limpiar();
                                } else {
                                    JOptionPane.showMessageDialog(null, "¡No se registró Usuario!");
                                }
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "El Usuario ya está registrado, ingrese otro.");
                        }
                    }
                }
            }
        }

        if (e.getSource() == vista.btn_actualizar) {
            int idUsuario = Crud_Usuario.idUsuario; // obtiene el id desde el crud y ya se soluciona el error
            Usuario usuario = new Usuario();
            Crud_Usuario controlUsuario = new Crud_Usuario();
            String rol = FrmUsuario.jComboBoxRol.getSelectedItem().toString().trim();
            String telefono = FrmUsuario.txt_gestionar_telefono.getText().trim();
            String dni = FrmUsuario.txt_gestionar_dni.getText().trim();
            String nombre = FrmUsuario.txt_gestionar_nombre.getText().trim();
            String apellido = FrmUsuario.txt_gestionar_apellido.getText().trim();
            String usuarioText = FrmUsuario.txt_gestionar_usuario.getText().trim();
            String password = FrmUsuario.txt_gestionar_password.getText().trim();

            if (idUsuario == 0) {
                JOptionPane.showMessageDialog(null, "¡Seleccione en la tabla un Usuario!");
            } else {
                // Validar que los campos no estén vacíos
                if (nombre.isEmpty() || apellido.isEmpty() || dni.isEmpty() || usuarioText.isEmpty() || password.isEmpty() || telefono.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "¡Completa todos los campos!");
                } else {
                    boolean validacionNombre = esSoloLetras(nombre);
                    boolean validacionApellido = esSoloLetras(apellido);
                    boolean validacionDni = esNumerico(dni) && dni.length() == 8;
                    boolean validacionTelefono = esNumerico(telefono) && telefono.length() == 9;

                    if (!validacionNombre && !validacionApellido) {
                        JOptionPane.showMessageDialog(null, "Los campos Nombre y Apellido solo aceptan letras");
                    } else if (!validacionNombre) {
                        JOptionPane.showMessageDialog(null, "El campo Nombre solo acepta letras");
                    } else if (!validacionApellido) {
                        JOptionPane.showMessageDialog(null, "El campo Apellido solo acepta letras");
                    } else if (!validacionDni && !validacionTelefono) {
                        JOptionPane.showMessageDialog(null, "El DNI debe tener 8 dígitos y el campo TELÉFONO debe tener 9 dígitos, ambos solo aceptan valores numéricos");
                    } else if (!validacionDni) {
                        JOptionPane.showMessageDialog(null, "El campo DNI debe tener 8 dígitos y solo acepta valores numéricos");
                    } else if (!validacionTelefono) {
                        JOptionPane.showMessageDialog(null, "El campo TELÉFONO debe tener 9 dígitos y solo acepta valores numéricos");
                    } else {
                        // Validar que se haya seleccionado un rol válido
                        if (rol.equalsIgnoreCase("Seleccione ROL:")) {
                            JOptionPane.showMessageDialog(null, "Seleccione rol");
                        } else {
                            usuario.setNombre(nombre);
                            usuario.setApellido(apellido);
                            usuario.setDni(dni);
                            usuario.setUsuario(usuarioText);
                            usuario.setPassword(password);
                            usuario.setTelefono(telefono);

                            if (rol.equalsIgnoreCase("ADMIN")) {
                                usuario.setIdRol(1);
                            } else if (rol.equalsIgnoreCase("CAJERO")) {
                                usuario.setIdRol(2);
                            } else if (rol.equalsIgnoreCase("MESERO")) {
                                usuario.setIdRol(3);
                            }

                            this.IdRol2();
                            usuario.setIdRol(obtenerIdRolCombo);
                            usuario.setEstado(1);

                            int confirmacion = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que quieres ACTUALIZAR este USUARIO seleccionado?", "Confirmar Actualización", JOptionPane.YES_NO_OPTION);
                            if (confirmacion == JOptionPane.YES_OPTION) {
                                if (controlUsuario.actualizar(usuario, idUsuario)) {
                                    JOptionPane.showMessageDialog(null, "¡Actualización Exitosa!");
                                    // se enviará los datos a tb_aditoria
                                    registrarAccion("Logró actualizar el usuario: " + idUsuario);
                                    Crud_Usuario.Limpiar2();
                                    Crud_Usuario.CargarTablaUsuarios();
                                    idUsuario = 0;
                                } else {
                                    JOptionPane.showMessageDialog(null, "¡Error al Actualizar usuario desde el ctrl_Usuario en el método actualizar!");
                                }
                            }
                        }
                    }
                }
            }
        }

        if (e.getSource() == vista.btn_eliminar) {
            Crud_Usuario controlUsuario = new Crud_Usuario();
            int idUsuario = Crud_Usuario.idUsuario;
            if (idUsuario == 0) {
                JOptionPane.showMessageDialog(null, "¡Seleccione un usuario!");
            } else {
                int confirmacion = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que quieres eliminar este USUARIO seleccionado?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
                if (confirmacion == JOptionPane.YES_OPTION) {
                    if (controlUsuario.eliminar(idUsuario)) {
                        JOptionPane.showMessageDialog(null, "Usuario " + idUsuario + " eliminado correctamente!");
                        // Registrar la acción en la auditoría
                        registrarAccion("Logró eliminar un(a) usuario con id: " + idUsuario + "");
                        Crud_Usuario.CargarTablaUsuarios();
                        Crud_Usuario.Limpiar2();
                    } else {
                        JOptionPane.showMessageDialog(null, "¡Error al eliminar usuario desde el controlador!");
                        // Registrar la acción fallida en la auditoría
                        registrarAccion("Falló al intentar eliminar un(a) usuario con id: " + idUsuario + "");
                        Crud_Usuario.Limpiar2();
                    }
                }
            }
        }

        //esto es para el combo de la izquierda
        //PARA GESTIONAR BOTONES PARA EDITAR roles
        if (e.getSource() == vista.btn_Roles) {
            FrmRol p11 = new FrmRol();
            Dashboard.ShowPanel(p11);
            registrarAccion("Presionó el botón ROLES");
        }
        if (e.getSource() == vista.btn_auditoria) {

            FrmAuditoria p12 = new FrmAuditoria();
            Dashboard.ShowPanel(p12);
            registrarAccion("Presionó el botón AUDITORÍA");
            //actualizar la tabla en tiempo real
            Crud_Auditoria.CargarTablaAuditoria();
        }

    }
    //combo de la izquierda

    public static int IdRol() {
        String sql = "select * from tb_rol where rol = '" + FrmUsuario.jComboBoxRol.getSelectedItem() + "'"; // 
        Statement st;
        try {
            Connection cn = DAO.Conexion.conectar();
            st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                obtenerIdRolCombo = rs.getInt("idRol");
            }
        } catch (SQLException e) {
            System.out.println("Error al obener id rol");
        }
        return obtenerIdRolCombo;
    }

    //combo de roles de la derecha
    public static int IdRol2() {
        String sql = "select * from tb_rol where rol = '" + FrmUsuario.jComboBox_gestionarRol.getSelectedItem() + "'"; // 
        Statement st;
        try {
            Connection cn = DAO.Conexion.conectar();
            st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                obtenerIdRolCombo = rs.getInt("idRol");
            }
        } catch (SQLException e) {
            System.out.println("Error al obener id rol");
        }
        return obtenerIdRolCombo;
    }

// para la tabla auditpria
    // Métodoo para registrar laS acciones en la tabla de auditoría
    private void registrarAccion(String accion) {
        try {
            Connection con = DAO.Conexion.conectar();
            String sql = "INSERT INTO tb_auditoria (idUsuario, fecha_conexion, hora_conexion, accion_realizada, ip_computadora) "
                    + "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, Crud_Usuario.idUsuarioAuditoria); //
            ps.setTimestamp(2, new Timestamp(new Date().getTime())); // Fecha actual
            ps.setTimestamp(3, new Timestamp(new Date().getTime())); // Hora actual
            ps.setString(4, accion); // Acción realizada
            ps.setString(5, Crud_Usuario.obtenerDireccionIP()); // IP de la computadora
            ps.executeUpdate();
            con.close();
        } catch (SQLException e) {
            System.out.println("Error al registrar la acción en la auditoría: " + e);
        }
    }

    //comprueba que lso campos sean numéricos
    private boolean esNumerico(String str) {
        return str.chars().allMatch(Character::isDigit);
    }
    // Comprueba que los campos solo contengan letras

    private boolean esSoloLetras(String str) {
        return str.chars().allMatch(Character::isLetter);
    }
}
