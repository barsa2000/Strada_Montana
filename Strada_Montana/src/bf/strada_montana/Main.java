package bf.strada_montana;

public class Main {

	public static void main(String[] args) {
		System.out.println("avvio main");
		Strada strada = new Strada();
		
		Veicolo v0 = new Veicolo(TipiVeicolo.Spazzaneve, strada, Corsie.Destra);
		v0.start();
		Veicolo v1 = new Veicolo(TipiVeicolo.Auto, strada, Corsie.Sinistra);
		v1.start();
		Veicolo v2 = new Veicolo(TipiVeicolo.Spazzaneve, strada, Corsie.Sinistra);
		v2.start();
		Veicolo v3 = new Veicolo(TipiVeicolo.Spazzaneve, strada, Corsie.Destra);
		v3.start();
		
		
		
		try {
			v0.join();
			v1.join();
			v2.join();
			v3.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("terminazione main");
	}

}
