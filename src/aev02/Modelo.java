package aev02;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.*;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.*;
import java.io.*;
import java.security.*;
import java.math.*;

/*Aquí van els metodes que conecten amb el controlador*/
public class Modelo {

	public Connection conexion = null;

	/**
	 * Obri connexió amb la base de dades.
	 *
	 * Extrau la informació per a accedir a la base de dades des del fitxer
	 * "datosConexion.xml".
	 * 
	 * @return La connexió del DriveManager.
	 */
	public Connection abrirConexion() {

		String url = null, username = null, pass = null;
		Boolean funciona = true;

		/* Dades de acces desde fitxer XML */

		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document document = dBuilder.parse(new File("datosConexion.xml"));

			NodeList nodeList = document.getElementsByTagName("conexion");

			Node node = nodeList.item(0);

			if (node.getNodeType() == Node.ELEMENT_NODE) {

				Element eElement = (Element) node;

				url = eElement.getElementsByTagName("url").item(0).getTextContent();
				username = eElement.getElementsByTagName("usuario").item(0).getTextContent();
				pass = eElement.getElementsByTagName("contrasenya").item(0).getTextContent();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		/* Conexio a la base de dades */

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conexion = DriverManager.getConnection(url, username, pass);

		} catch (Exception e) {
			System.out.println(e);
			JOptionPane.showMessageDialog(null, "Conexió fallida.");
			funciona = false;
		}

		if (funciona) {
			JOptionPane.showMessageDialog(null, "Conexió exitosa.");
		}
		return conexion;

	}

	/**
	 * Tanca la conexió.
	 */
	public void cerrarConexion() {
		if (conexion != null) {
			try {
				conexion.close();
				JOptionPane.showMessageDialog(null, "Conexió tancada amb éxit.");
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "Error al tancar conexió.");
			}
		}
	}

	/**
	 * Cerca en la base de dades si existeix una combinació de l'usuari i
	 * contrasenya que ha introduït l'usuari. La contrasenya introduïda l'encripta a
	 * MD5 i la compara amb la que hi ha en la base de dades.
	 * 
	 * @param usuario
	 * @param contrasenya
	 * @return True o False, bool.
	 */
	public boolean login(String usuario, String contrasenya) {
		Statement stmt;

		String user = "";
		String pass = "";

		try {
			stmt = conexion.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM users");
			while (rs.next()) {
				user = rs.getString(2);
				pass = rs.getString(3);
				if (user.equals(usuario) && pass.equals(encrypt(contrasenya))) {
					JOptionPane.showMessageDialog(null, "Sessió iniciada.");
					return true;
				}
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JOptionPane.showMessageDialog(null, "Inici de sessio fallida.");
		return false;

	}

	/**
	 * Encripta text a MD5.
	 * 
	 * @param password
	 * @return String amb el text en MD5.
	 */
	public String encrypt(String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(password.getBytes());
			BigInteger number = new BigInteger(1, messageDigest);
			String hashtext = number.toString(16);

			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext;
			}
			return hashtext;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Càrrega un array amb totes les taules que existeixen en la base de dades
	 * "books".
	 * 
	 * @return Array amb taules.
	 * @throws SQLException
	 */
	public List<String> listaTablas() throws SQLException {
		Statement stmt;
		List<String> tablas = new ArrayList<String>();

		stmt = conexion.createStatement();
		ResultSet rs = stmt.executeQuery("SHOW TABLES FROM books");
		while (rs.next()) {
			tablas.add(rs.getString(1));
		}
		rs.close();
		stmt.close();
		return tablas;
	}

	/**
	 * A través d'una consulta que introdueix l'usuari genera i retorna un model de
	 * la base de dades, pot ser carregat en una taula de Window Builder.
	 * 
	 * @param consulta
	 * @return Model de la base de dades.
	 * @throws SQLException
	 */
	public DefaultTableModel rellenarTabla(String consulta) throws SQLException {
		DefaultTableModel tabla = new DefaultTableModel();
		Statement stmt = conexion.createStatement();
		ResultSet rs = stmt.executeQuery(consulta);
		ResultSetMetaData metadata = rs.getMetaData();

		int columns = metadata.getColumnCount();

		String[] tags = new String[columns];
		for (int i = 0; i < columns; i++) {
			tags[i] = metadata.getColumnLabel(i + 1);
		}

		tabla.setColumnIdentifiers(tags);

		String[] datos = new String[columns];
		while (rs.next()) {
			for (int j = 0; j < columns; j++) {
				datos[j] = rs.getString(j + 1);
			}
			tabla.addRow(datos);
		}

		rs.close();
		stmt.close();

		return tabla;
	}

	/**
	 * Executa una consulta mitjançant un String.
	 * 
	 * @param consulta
	 */
	public void ejecutarConsulta(String consulta) {

		Statement stmt;
		try {
			stmt = conexion.createStatement();
			PreparedStatement ps = conexion.prepareStatement(consulta);
			int upd = ps.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al executar.");
			e.printStackTrace();
		}

	}
}
