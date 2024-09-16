package models;

import java.util.ArrayList;
import java.util.stream.Collectors;

import models.dispatchers.BoardDispatcher;
import models.dispatchers.Dispatcher;
import models.events.TileEvent;
import models.observers.BoardObserver;
import models.observers.TileObserver;

class Board implements Dispatcher, TileObserver {
	private static Board board;
	public ArrayList<Tile> tiles = new ArrayList<Tile>();
	public ArrayList<Continent> continents = new ArrayList<Continent>();
	private ArrayList<BoardObserver> observers = new ArrayList<BoardObserver>();

	public void dispatch() {
		// Monta os TileEvents e notifica ouvintes
		TileEvent[] tileEvents = new TileEvent[this.tiles.size()];
		tileEvents = this.tiles.stream().map((Tile tile) -> new TileEvent(tile.owner.color, tile.troops, tile.name, tile.selected, tile.lastSelected))
				.collect(Collectors.toList()).toArray(tileEvents);
		for (BoardObserver observer : observers) {
			observer.trigger(tileEvents);
		}
	}
	
	public void trigger() {
		this.dispatch();
	}

	public void subscribe(Object boardObserver) {
		this.observers.add((BoardObserver) boardObserver);
	}

	private Board(Continent[] continents) {
		for (Continent continent : continents) {
			this.continents.add(continent);
			this.tiles.addAll(continent.tiles);
		}
	}
	
	public void reset() {
		this.tiles = new ArrayList<Tile>();
		this.continents = new ArrayList<Continent>();
		this.observers = new ArrayList<BoardObserver>();
		this.buildBoard();
	}

	public Tile findTile(String tileName) {
		for (Tile t : tiles) {
			if (t.name.equals(tileName)) {
				return t;
			}
		}
		return null;
	}

	public ArrayList<Tile> getContinentTiles(String name) {
		for (Continent c : continents) {
			if (c.name == name) {
				return c.tiles;
			}
		}
		return null;
	}

	public Continent getTileContinent(Tile tile) {
		for (Continent c : continents) {
			if (c.tiles.contains(tile)) {
				return c;
			}
		}
		return null;
	}

	public static Board getBoard() {
		if (Board.board == null) {
			Board.board = new Board(new Continent[] {});
			Board.board.buildBoard();
		} 
		return board;

	}
	
	public void buildBoard() {
		Tile africaDoSul = new Tile("Africa do Sul");
		Tile angola = new Tile("Angola");
		Tile argelia = new Tile("Argelia");
		Tile egito = new Tile("Egito");
		Tile nigeria = new Tile("Nigeria");
		Tile somalia = new Tile("Somalia");
		Tile alasca = new Tile("Alasca");
		Tile calgary = new Tile("Calgary");
		Tile california = new Tile("California");
		Tile groenlandia = new Tile("Groenlandia");
		Tile mexico = new Tile("Mexico");
		Tile novaYork = new Tile("Nova York");
		Tile quebec = new Tile("Quebec");
		Tile texas = new Tile("Texas");
		Tile vancouver = new Tile("Vancouver");
		Tile arabiaSaudita = new Tile("Arabia Saudita");
		Tile bangladesh = new Tile("Bangladesh");
		Tile cazaquistao = new Tile("Cazaquistao");
		Tile china = new Tile("China");
		Tile coreiaDoNorte = new Tile("Coreia do Norte");
		Tile coreiaDoSul = new Tile("Coreia do Sul");
		Tile estonia = new Tile("Estonia");
		Tile india = new Tile("India");
		Tile ira = new Tile("Ira");
		Tile iraque = new Tile("Iraque");
		Tile japao = new Tile("Japao");
		Tile jordania = new Tile("Jordania");
		Tile letonia = new Tile("Letonia");
		Tile mongolia = new Tile("Mongolia");
		Tile paquistao = new Tile("Paquistao");
		Tile russia = new Tile("Russia");
		Tile siberia = new Tile("Siberia");
		Tile siria = new Tile("Siria");
		Tile tailandia = new Tile("Tailandia");
		Tile turquia = new Tile("Turquia");
		Tile argentina = new Tile("Argentina");
		Tile brasil = new Tile("Brasil");
		Tile peru = new Tile("Peru");
		Tile venezuela = new Tile("Venezuela");
		Tile espanha = new Tile("Espanha");
		Tile franca = new Tile("Franca");
		Tile italia = new Tile("Italia");
		Tile polonia = new Tile("Polonia");
		Tile reinoUnido = new Tile("Reino Unido");
		Tile romenia = new Tile("Romenia");
		Tile suecia = new Tile("Suecia");
		Tile ucrania = new Tile("Ucrania");
		Tile australia = new Tile("Australia");
		Tile indonesia = new Tile("Indonesia");
		Tile novaZelandia = new Tile("Nova Zelandia");
		Tile perth = new Tile("Perth");

		
		arabiaSaudita.addNeighbour(iraque);
		angola.addNeighbour(africaDoSul);
		brasil.addNeighbour(argentina);
		brasil.addNeighbour(peru);
		brasil.addNeighbour(venezuela);
		peru.addNeighbour(argentina);
		peru.addNeighbour(venezuela);
		mexico.addNeighbour(venezuela);
		mexico.addNeighbour(texas);
		mexico.addNeighbour(california);
		texas.addNeighbour(california);
		texas.addNeighbour(novaYork);
		texas.addNeighbour(vancouver);
		texas.addNeighbour(quebec);
		california.addNeighbour(vancouver);
		novaYork.addNeighbour(quebec);
		vancouver.addNeighbour(alasca);
		vancouver.addNeighbour(quebec);
		vancouver.addNeighbour(calgary);
		calgary.addNeighbour(alasca);
		calgary.addNeighbour(groenlandia);
		quebec.addNeighbour(groenlandia);
		reinoUnido.addNeighbour(groenlandia);
		reinoUnido.addNeighbour(franca);
		espanha.addNeighbour(franca);
		franca.addNeighbour(italia);
		franca.addNeighbour(suecia);
		italia.addNeighbour(romenia);
		italia.addNeighbour(polonia);
		italia.addNeighbour(suecia);
		polonia.addNeighbour(romenia);
		polonia.addNeighbour(ucrania);
		romenia.addNeighbour(ucrania);
		siberia.addNeighbour(alasca);
		estonia.addNeighbour(suecia);
		estonia.addNeighbour(letonia);
		estonia.addNeighbour(russia);
		letonia.addNeighbour(polonia);
		letonia.addNeighbour(ucrania);
		letonia.addNeighbour(russia);
		letonia.addNeighbour(cazaquistao);
		letonia.addNeighbour(turquia);
		letonia.addNeighbour(suecia);
		russia.addNeighbour(cazaquistao);
		russia.addNeighbour(siberia);
		cazaquistao.addNeighbour(siberia);
		cazaquistao.addNeighbour(mongolia);
		cazaquistao.addNeighbour(china);
		cazaquistao.addNeighbour(turquia);
		cazaquistao.addNeighbour(japao);
		turquia.addNeighbour(ucrania);
		turquia.addNeighbour(china);
		turquia.addNeighbour(paquistao);
		turquia.addNeighbour(siria);
		china.addNeighbour(mongolia);
		china.addNeighbour(coreiaDoNorte);
		china.addNeighbour(coreiaDoSul);
		china.addNeighbour(paquistao);
		china.addNeighbour(turquia);
		china.addNeighbour(india);
		mongolia.addNeighbour(japao);
		japao.addNeighbour(coreiaDoNorte);
		coreiaDoNorte.addNeighbour(coreiaDoSul);
		coreiaDoSul.addNeighbour(india);
		coreiaDoSul.addNeighbour(bangladesh);
		coreiaDoSul.addNeighbour(tailandia);
		tailandia.addNeighbour(bangladesh);
		bangladesh.addNeighbour(india);
		paquistao.addNeighbour(india);
		paquistao.addNeighbour(siria);
		paquistao.addNeighbour(ira);
		ira.addNeighbour(iraque);
		ira.addNeighbour(siria);
		ira.addNeighbour(siria);
		ira.addNeighbour(jordania);
		ira.addNeighbour(arabiaSaudita);
		siria.addNeighbour(jordania);
		siria.addNeighbour(turquia);
		jordania.addNeighbour(arabiaSaudita);
		jordania.addNeighbour(iraque);
		argelia.addNeighbour(italia);
		argelia.addNeighbour(espanha);
		argelia.addNeighbour(egito);
		argelia.addNeighbour(nigeria);
		nigeria.addNeighbour(brasil);
		nigeria.addNeighbour(egito);
		nigeria.addNeighbour(somalia);
		nigeria.addNeighbour(angola);
		egito.addNeighbour(romenia);
		egito.addNeighbour(somalia);
		egito.addNeighbour(jordania);
		somalia.addNeighbour(arabiaSaudita);
		somalia.addNeighbour(angola);
		somalia.addNeighbour(africaDoSul);
		siria.addNeighbour(iraque);
		indonesia.addNeighbour(india);
		indonesia.addNeighbour(bangladesh);
		indonesia.addNeighbour(australia);
		indonesia.addNeighbour(novaZelandia);
		novaZelandia.addNeighbour(australia);
		australia.addNeighbour(perth);
		
		

		Continent americaDoSul = new Continent("America do Sul", new Tile[] {
				brasil, argentina, peru, venezuela }, 2);

		Continent americaDoNorte = new Continent("America do Norte", new Tile[] {
				alasca, calgary, groenlandia, vancouver, quebec, california, novaYork, texas,
				mexico }, 5);

		Continent africa = new Continent("Africa", new Tile[] {
				somalia, angola, egito, argelia, nigeria, africaDoSul }, 3);

		Continent europa = new Continent("Europa", new Tile[] {
				espanha, franca, italia, polonia, reinoUnido, romenia, suecia, ucrania }, 5);

		Continent asia = new Continent("Asia", new Tile[] {
				arabiaSaudita, bangladesh, cazaquistao, china, coreiaDoNorte, coreiaDoSul,
				estonia, letonia, india, ira, iraque, japao, jordania, mongolia, paquistao,
				russia, siberia, siria, tailandia, turquia }, 7);

		Continent oceania = new Continent("Oceania", new Tile[] {
				australia, indonesia, novaZelandia, perth }, 2);

		Continent[] continents = new Continent[] { americaDoSul, americaDoNorte, africa, europa,
			asia, oceania };
		
		for (Continent continent : continents) {
			this.continents.add(continent);
			this.tiles.addAll(continent.tiles);
		}
	}
	
	public String toString() {
		String result = "Board\n";
		result += String.join("", this.tiles.stream().map(t -> t.toString()).toArray(String[]::new));
		return result;
	}
}
