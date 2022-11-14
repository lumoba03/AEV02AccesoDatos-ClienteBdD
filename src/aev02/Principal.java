package aev02;

public class Principal {
	public static void main(String[] args) {
		Vista vista = new Vista();
		Modelo model = new Modelo();
		Controlador controlador = new Controlador(vista, model);
	}
}
