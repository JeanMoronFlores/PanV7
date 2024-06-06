/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import DAO.Crud_Usuario;
import Vistas.Dashboard;
import Vistas.Login;
import static Vistas.Login.txtPassword;
import static Vistas.Login.txtusuario;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import modelo.Usuario;

/**
 *
 * @author JEAN
 */
public class Ctrl_Login implements ActionListener {

    Login vista;

    public Ctrl_Login(Login vista) {
        this.vista = vista;
        // Inicializar la vista
        this.vista.setResizable(false);
        this.vista.setLocationRelativeTo(null);
        this.vista.setTitle("Login Panchi's Pizza");

        // Agregar el ActionListener A los botones
        //Crud_Dashboard.CargarTablaCategorias();
        this.vista.btnIngresar.addActionListener(
                this);

        this.vista.txtusuario.addActionListener(
                this);

        this.vista.txtPassword.addActionListener(
                this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnIngresar) {
            this.Login();
        }

    }

    private void Login() {
        if (!txtusuario.getText().isEmpty() && !txtPassword.getText().isEmpty()) {
            Crud_Usuario controlUsuario = new Crud_Usuario();
            Usuario usuario = new Usuario();
            usuario.setUsuario(txtusuario.getText().trim());
            usuario.setPassword(txtPassword.getText().trim());

            if (controlUsuario.loginUser(usuario)) {
                //JOptionPane.showMessageDialog(null, "ingreso correcto");

                // Crear una instancia de Dashboard y pasar el rol del usuario
//                Dashboard menu = new Dashboard();
                // Obtener el rol del usuario
                int idRol = controlUsuario.obtenerIdRolUsuario(usuario.getIdUsuario());
                // Crear una instancia de Dashboard y establecer el ID del rol
                Dashboard menu = Dashboard.getInstance();
                menu.setIdRol(idRol);
                menu.setVisible(true);
                vista.dispose();//antes era this.dispose();
//                menu.setVisible(true);
//                this.dispose();
            } else {
                JOptionPane.showMessageDialog(null, "ingrese las credenciales correctas");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Los campos están vacíos");
        }
    }

}
