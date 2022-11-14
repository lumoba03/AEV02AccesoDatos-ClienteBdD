package aev02;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/*Aquí van els metodes que conecten amb l'interfície*/

public class Controlador {
	private Vista vista;
	private Modelo model;

	private Boolean login = false;

	public Controlador(Vista v, Modelo m) {
		this.vista = v;
		this.model = m;
		initEventHandlers();

	}

	/**
	 * Elimina totes les dades del comboBox per a carregar una llista en ell.
	 * 
	 * @throws SQLException
	 */
	public void cargarCombobox() throws SQLException {
		vista.comboBox.removeAllItems();
		for (int i = 0; i < model.listaTablas().size(); i++)
			vista.comboBox.addItem(model.listaTablas().get(i).toString());
	}

	public void initEventHandlers() {
		model.abrirConexion();
		vista.scrollPane.hide();
		vista.scrollPane_1.hide();

		vista.btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				login = model.login(vista.usuario_Tf.getText(), vista.contrasenya_Tf.getText());
				if (login) {
					vista.scrollPane.show();
					vista.scrollPane_1.show();
				}
			}
		});

		vista.btnReabrirCon.setEnabled(false);
		vista.btnCerrarCon.setEnabled(true);

		/*
		 * Aquest botó permet tancar la connexió amb la base de dades si has iniciat
		 * sessió.
		 */
		vista.btnCerrarCon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (login) {

					model.cerrarConexion();

					vista.btnReabrirCon.setEnabled(true);
					vista.btnCerrarCon.setEnabled(false);
				} else {
					JOptionPane.showMessageDialog(null, "Tens que iniciar sessió per a realizar aquesta acció.");
				}
			}
		});

		/*
		 * Aquest botó permet tornar a obrir la connexió amb la base de dades si has
		 * iniciat sessió.
		 */
		vista.btnReabrirCon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.abrirConexion();
				vista.btnReabrirCon.setEnabled(false);
				vista.btnCerrarCon.setEnabled(true);
			}
		});

		/* Càrrega la taula seleccionada des del comboBox a la taula del programa. */
		vista.comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String selected = vista.comboBox.getSelectedItem().toString();
				String consulta = "SELECT * FROM " + selected + ";";
				try {
					DefaultTableModel modelo = model.rellenarTabla(consulta);
					vista.table.setModel(modelo);

					modelo = model.rellenarTabla("SHOW COLUMNS FROM " + selected + ";");
					vista.table_1.setModel(modelo);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		try {
			cargarCombobox();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		/*
		 * Executa la consulta que realitza l'usuari en prémer el botó d'executar. Si no
		 * has iniciat sessió permet només realitzar Select, en cas contrari permet
		 * atacar la base de dades per a realitzar modificacions en aquesta. Després de
		 * fer l'execució mostra la taula en el programa.
		 */
		vista.btnExecute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String selected = vista.comboBox.getSelectedItem().toString();
				String consulta = vista.consulta_Tf.getText();

				/*
				 * Separe la consulta de l'usuari per paraules, normalment sempre és la primera
				 * paraula la que indica el tipus de consulta, así que es pren en una variable.
				 */
				String[] sp = consulta.split(" ");
				String tipoAccion = sp[0];
				String tabla = sp[1];
				/*
				 * Normalment és després d'un "From" quan s'especifica la taula a la qual es vol
				 * consultar o fer un canvi, així que mitjançant un *For busque aqueix "From" en
				 * la llista de paraules per a fer que guarde la següent paraula una vegada el
				 * trobe.
				 */
				for (int i = 0; i < sp.length; i++) {
					if (sp[i].equals("FROM") || sp[i].equals("from") || sp[i].equals("into") || sp[i].equals("INTO")) {
						tabla = sp[i + 1];
						break;
					}
				}

				if (tipoAccion.equals("SELECT")) {

					try {
						DefaultTableModel modelo = model.rellenarTabla(consulta);
						vista.table.setModel(modelo);
					} catch (SQLException ex) {
						JOptionPane.showMessageDialog(null, "Error al executar l'acció, comproba el camp de volta.");
						ex.printStackTrace();
					}
				} else if (login) {
					try {
						int reply = JOptionPane.showConfirmDialog(null,
								"Aquesta acció farà canvis en la base de dades.\n" + "Vols continuar?",
								"Confirmar acció", JOptionPane.YES_NO_OPTION);

						if (reply == JOptionPane.YES_OPTION) {

							model.ejecutarConsulta(consulta);
							DefaultTableModel modelo = model.rellenarTabla("SELECT * FROM " + tabla + ";");
							vista.table.setModel(modelo);
						}
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				} else {
					JOptionPane.showMessageDialog(null, "Tens que iniciar sessió per a realizar aquesta acció.");
				}
			}

		});

	}
}
