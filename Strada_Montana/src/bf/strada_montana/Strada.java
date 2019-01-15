package bf.strada_montana;

import java.util.EnumMap;
import java.util.Vector;

public class Strada {
	
	EnumMap<Corsie, Vector<Veicolo>> code = new EnumMap<>(Corsie.class);
	EnumMap<Corsie, Boolean> corsie = new EnumMap<>(Corsie.class);

	public Strada() {
		//inizializzazione code vuote
		for(Corsie i: Corsie.values()){
	        code.put(i, new Vector<Veicolo>());
		}
		//inizializzazione corsie a libero
		for(Corsie i: Corsie.values()){
	        corsie.put(i, false);
		}
	}

	//funzione per l'aggiunta di un mezzo
	public void richiesta(Veicolo v) {
		//stampa a video delle informazioni del mezzo e della corsia;
		//aggiunta alla coda del mezzo
		System.out.println("arrivo mezzo(" + v.getTipo() + ") nella corsia " + v.getVerso());
		code.get(v.getVerso()).add(v);
		
		controllo(v);
	}

	//funzione per la gestione di ogni mezzo
	private synchronized void controllo(Veicolo v) {
		boolean passato = false;
	
//		Vector<Veicolo> codaA = code.get(v.getVerso());
//		Vector<Veicolo> codaB = code.get(Corsie.values()[(v.getVerso().ordinal() + 1) % 2]);
//		boolean corsiaA = corsie.get(v.getVerso());
//		boolean corsiaB = corsie.get(Corsie.values()[(v.getVerso().ordinal() + 1) % 2]);
		
		Vector<Veicolo> codaA;
		Vector<Veicolo> codaB;
		boolean corsiaA;		
		boolean corsiaB;
		
		//identificazione della coda nel verso di percorrenza, della coda nel verso opposto,
		//della corsia nel verso percorso, della corsia nel verso opposto
		if(v.getVerso() == Corsie.Destra) {
			codaA = code.get(Corsie.Destra);
			codaB = code.get(Corsie.Sinistra);
			corsiaA = corsie.get(Corsie.Destra);
			corsiaB = corsie.get(Corsie.Sinistra);
		} else {
			codaA = code.get(Corsie.Sinistra);
			codaB = code.get(Corsie.Destra);
			corsiaA = corsie.get(Corsie.Sinistra);
			corsiaB = corsie.get(Corsie.Destra);
		}

		
		//finché il veicolo non è passato viene controllato se è il suo turno, 
		//se lo è simula l'attraversamento e termina attraverso la funzione "passaggio",
		//se non lo è si rimette in attesa
		while(!passato) {
			if(permesso(v, codaA, codaB, corsiaA, corsiaB)) {
				passaggio(v.getVerso());
				passato = true;
			} else {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	//funzione per controllare se un mezzo può partire o no
	private boolean permesso(Veicolo v, Vector<Veicolo> codaA, Vector<Veicolo> codaB, boolean corsiaA, boolean corsiaB) {
		//controllo se nella fila opposta è libera
		if(codaB.size() == 0) {
			return true;
		} else {
			//controllo se la corsia è e se il veicolo è il primo della fila
			if(!corsiaA && codaA.indexOf(v) == 0) {
				switch(v.getTipo()) {
				case Auto:
					//se il veicolo è un auto e nella corsia opposta c'è un'altra auto il veicolo parte;
					//se il veicolo è un auto e nella corsia opposta c'è un camione e la corsia opposta è libera il veicolo parte;
					if( (codaB.get(0).getTipo() == TipiVeicolo.Auto) || 
						(codaB.get(0).getTipo() == TipiVeicolo.Camion && !corsiaB)) {
						return true;
					}
					break;
				case Camion:
					//se il veicolo è un camion e nella corsia opposta c'è un'altro camion
					//e la corsia opposta è libera il veicolo parte;
					if(codaB.get(0).getTipo() == TipiVeicolo.Camion && !corsiaB) {
						return true;
					}
					break;
				case Spazzaneve:
					//se il veicolo è uno spazzaneve e la corsia opposta è libera il veicolo parte;
					if(!corsiaB) {
						return true;
					}
					break;
				}
			}
		}
		return false;
	}

	//funzione per la simulazione del passaggio del veicolo
	private void passaggio(Corsie verso) {
		//stampa informazioni del veicolo partito
		System.out.println("attraversamento avviato(" + code.get(verso).get(0).getTipo() + ") nella corsia " + verso);
		
		//impostazione della corsia ad occupato
		corsie.put(verso, true);
		
		//attesa di 1000ms (1s) per simulare il passaggio del mezzo
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//stampa informazioni del veicolo che ha terminato l'attraversamento
		System.out.println("attraversamento completato(" + code.get(verso).get(0).getTipo() + ") nella corsia " + verso);
		
		//rimozione del mezzo dalla coda
		code.get(verso).remove(0);
		
		//impostazione della corsia a libero
		corsie.put(verso, false);
		
		//notifica di risveglio per gli altri veicoli in attesa
		try {
			notifyAll();
		} catch(IllegalMonitorStateException e) {

		}
	}
}
