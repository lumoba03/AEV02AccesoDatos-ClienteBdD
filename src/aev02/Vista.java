package aev02;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class Vista {

	JFrame frmBaseDeDades;
	JTable table;
	JTextField usuario_Tf;
	JTextField contrasenya_Tf;
	JTextField consulta_Tf;
	JButton btnReabrirCon;
	JButton btnCerrarCon;
	JButton btnLogin;
	JComboBox comboBox;
	JButton btnExecute;
	JScrollPane scrollPane;
	JScrollPane scrollPane_1;
	JTable table_1;

	/**
	 * Create the application.
	 */
	public Vista() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize() {
		frmBaseDeDades = new JFrame();
		frmBaseDeDades.setTitle("Base de Dades (Client)");
		frmBaseDeDades.setBounds(100, 100, 785, 511);
		frmBaseDeDades.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmBaseDeDades.getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("Usuari:");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNewLabel.setBounds(44, 10, 47, 19);
		frmBaseDeDades.getContentPane().add(lblNewLabel);

		JLabel lblContrasea = new JLabel("Clau:");
		lblContrasea.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblContrasea.setBounds(57, 39, 34, 19);
		frmBaseDeDades.getContentPane().add(lblContrasea);

		usuario_Tf = new JTextField();
		usuario_Tf.setBounds(101, 12, 96, 19);
		frmBaseDeDades.getContentPane().add(usuario_Tf);
		usuario_Tf.setColumns(10);

		contrasenya_Tf = new JTextField();
		contrasenya_Tf.setBounds(101, 41, 96, 19);
		frmBaseDeDades.getContentPane().add(contrasenya_Tf);
		contrasenya_Tf.setColumns(10);

		btnLogin = new JButton("Iniciar Sessió");

		btnLogin.setBounds(207, 10, 111, 48);
		frmBaseDeDades.getContentPane().add(btnLogin);

		consulta_Tf = new JTextField();
		consulta_Tf.setBounds(328, 12, 433, 19);
		frmBaseDeDades.getContentPane().add(consulta_Tf);
		consulta_Tf.setColumns(10);

		btnExecute = new JButton("Executar");

		btnExecute.setBounds(676, 40, 85, 21);
		frmBaseDeDades.getContentPane().add(btnExecute);

		comboBox = new JComboBox();
		comboBox.setBounds(555, 40, 111, 21);
		frmBaseDeDades.getContentPane().add(comboBox);

		btnCerrarCon = new JButton("Tancar con.");

		btnCerrarCon.setBounds(328, 40, 96, 21);
		frmBaseDeDades.getContentPane().add(btnCerrarCon);

		btnReabrirCon = new JButton("Reobrir con.");
		btnReabrirCon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnReabrirCon.setBounds(434, 40, 111, 21);
		frmBaseDeDades.getContentPane().add(btnReabrirCon);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 68, 741, 220);
		frmBaseDeDades.getContentPane().add(scrollPane);

		table = new JTable();
		scrollPane.setViewportView(table);

		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 298, 741, 166);
		frmBaseDeDades.getContentPane().add(scrollPane_1);

		table_1 = new JTable();
		scrollPane_1.setViewportView(table_1);

		this.frmBaseDeDades.setVisible(true);
	}
}
