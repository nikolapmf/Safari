package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the licitacija database table.
 * 
 */
@Entity
@Table(name="licitacija")
@NamedQuery(name="Licitacija.findAll", query="SELECT l FROM Licitacija l")
public class Licitacija implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idLicitacija;

	private String komentar;

	private int ocena;

	//bi-directional many-to-one association to Korisnik
	@ManyToOne
	private Korisnik korisnik;

	//bi-directional many-to-one association to Predmet
	@ManyToOne
	private Predmet predmet;

	public Licitacija() {
	}

	public int getIdLicitacija() {
		return this.idLicitacija;
	}

	public void setIdLicitacija(int idLicitacija) {
		this.idLicitacija = idLicitacija;
	}

	public String getKomentar() {
		return this.komentar;
	}

	public void setKomentar(String komentar) {
		this.komentar = komentar;
	}

	public int getOcena() {
		return this.ocena;
	}

	public void setOcena(int ocena) {
		this.ocena = ocena;
	}

	public Korisnik getKorisnik() {
		return this.korisnik;
	}

	public void setKorisnik(Korisnik korisnik) {
		this.korisnik = korisnik;
	}

	public Predmet getPredmet() {
		return this.predmet;
	}

	public void setPredmet(Predmet predmet) {
		this.predmet = predmet;
	}

}