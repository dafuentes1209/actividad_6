package actividad_6;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class VentanaPrincipal extends JFrame {
    private JLabel lblNombre;
    private JLabel lblNumero;
    private JTextField txtNombre;
    private JTextField txtNumero;
    private JButton btnLeer;
    private JButton btnCrear;
    private JButton btnBorrar;
    private JButton btnActualizar;
    private JButton btnCerrar;

    public VentanaPrincipal() {
        setTitle("Gestor de Contactos");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        lblNombre = new JLabel("Nombre:");
        txtNombre = new JTextField(20);
        lblNumero = new JLabel("Número:");
        txtNumero = new JTextField(20);

        btnLeer = new JButton("Leer");
        btnCrear = new JButton("Crear");
        btnBorrar = new JButton("Borrar");
        btnActualizar = new JButton("Actualizar");
        btnCerrar = new JButton("Cerrar");

        add(lblNombre);
        add(txtNombre);
        add(lblNumero);
        add(txtNumero);
        add(btnLeer);
        add(btnCrear);
        add(btnBorrar);
        add(btnActualizar);
        add(btnCerrar);

        btnLeer.addActionListener(this::btnLeerActionPerformed);
        btnCrear.addActionListener(this::btnCrearActionPerformed);
        btnBorrar.addActionListener(this::btnBorrarActionPerformed);
        btnActualizar.addActionListener(this::btnActualizarActionPerformed);
        btnCerrar.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void btnCrearActionPerformed(ActionEvent evt) {
        try {
            String newName = txtNombre.getText();
            long newNumber = Long.parseLong(txtNumero.getText());

            File file = new File("C:\\Users\\David\\OneDrive\\Documentos\\amiguitos_files\\amiguitos_files.txt");

            if (!file.exists()) {
                file.createNewFile();
            }

            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            raf.seek(raf.length());
            raf.writeBytes(newName + "!" + newNumber);
            raf.writeBytes(System.lineSeparator());
            raf.close();

            System.out.println("Amigo creado.");
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void btnLeerActionPerformed(ActionEvent evt) {
        try {
            File file = new File("C:\\Users\\David\\OneDrive\\Documentos\\amiguitos_files\\amiguitos_files.txt");
            RandomAccessFile raf = new RandomAccessFile(file, "r");
            String line;
            System.out.println("Lista de amigos:");

            while ((line = raf.readLine()) != null) {
                System.out.println(line);
            }

            raf.close();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void btnBorrarActionPerformed(ActionEvent evt) {
        try {
            String nameToDelete = txtNombre.getText();
            File file = new File("C:\\Users\\David\\OneDrive\\Documentos\\amiguitos_files\\amiguitos_files.txt");

            if (!file.exists()) {
                file.createNewFile();
            }

            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            File tmpFile = new File("C:\\Users\\David\\OneDrive\\Documentos\\amiguitos_files\\tmp_amiguitos_files.txt");
            RandomAccessFile tmpRaf = new RandomAccessFile(tmpFile, "rw");

            String line;
            boolean found = false;

            while ((line = raf.readLine()) != null) {
                String[] parts = line.split("!");
                String name = parts[0];

                if (name.equals(nameToDelete)) {
                    found = true;
                    continue; // Skip adding this line to tmp file
                }

                tmpRaf.writeBytes(line + System.lineSeparator());
            }

            raf.close();
            tmpRaf.close();

            if (found) {
                // Replace original file with the temp file
                file.delete();
                tmpFile.renameTo(file);
                System.out.println("Amigo borrado.");
            } else {
                tmpFile.delete();
                System.out.println("El amigo no existe.");
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void btnActualizarActionPerformed(ActionEvent evt) {
        try {
            String oldName = txtNombre.getText();
            long newNumber = Long.parseLong(txtNumero.getText());
            File file = new File("C:\\Users\\David\\OneDrive\\Documentos\\amiguitos_files\\amiguitos_files.txt");

            if (!file.exists()) {
                file.createNewFile();
            }

            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            File tmpFile = new File("C:\\Users\\David\\OneDrive\\Documentos\\amiguitos_files\\tmp_amiguitos_files.txt");
            RandomAccessFile tmpRaf = new RandomAccessFile(tmpFile, "rw");

            String line;
            boolean found = false;

            while ((line = raf.readLine()) != null) {
                String[] parts = line.split("!");
                String name = parts[0];

                if (name.equals(oldName)) {
                    line = name + "!" + newNumber; // Update the number
                    found = true;
                }

                tmpRaf.writeBytes(line + System.lineSeparator());
            }

            raf.close();
            tmpRaf.close();

            if (found) {
                file.delete();
                tmpFile.renameTo(file);
                System.out.println("Amigo actualizado.");
            } else {
                tmpFile.delete();
                System.out.println("El amigo no existe.");
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new VentanaPrincipal();
    }
}
