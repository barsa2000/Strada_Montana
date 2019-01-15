package bf.strada_montana;

public class Veicolo extends Thread {
	private TipiVeicolo tipo;
	private Strada strada;
	private Corsie verso;
	
	@Override
	public void run() {
		strada.richiesta(this);
	}

	public Veicolo(TipiVeicolo tipo, Strada strada, Corsie verso) {
		this.tipo = tipo;
		this.strada = strada;
		this.verso = verso;
	}
	
	//funzione che ritorna il tipo di veicolo
	public TipiVeicolo getTipo() {
		return tipo;
	}
	
	//funzione che ritorna il verso di percorrenza
	public Corsie getVerso() {
		return verso;
	}
	
}
