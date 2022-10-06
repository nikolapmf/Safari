package com.example.demo.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.demo.repository.KategorijaRepository;
import com.example.demo.repository.KorisnikRepository;
import com.example.demo.repository.PredmetRepository;
import com.example.demo.repository.StanjeRepository;
import com.example.demo.repository.UlogaRepository;

import model.Kategorija;
import model.Korisnik;
import model.Predmet;
import model.Stanje;
import model.Uloga;


@Controller
@RequestMapping(value = "/kontroler")
public class Kontroler {
	
	@Autowired
	UlogaRepository ur;
	@Autowired
	KorisnikRepository kr;
	@Autowired
	PredmetRepository pr;
	@Autowired
	KategorijaRepository katrep;
	@Autowired
	StanjeRepository sr;
	

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setLenient(true);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf,true));
	}
	
	@RequestMapping(value = "/getUlogas", method = RequestMethod.GET)
	public String getUlogas(HttpServletRequest req) {
		List<Uloga> lista=ur.findAll();
		req.getSession().setAttribute("uloge", lista);
		
		return "registracija";
	}
	
	@RequestMapping(value = "/saveKorisnik", method = RequestMethod.POST)
	public String saveKorisnik(String ime, String prezime, String jmbg, String username, String password, String adresa, Integer uloga, HttpServletRequest req) {
		Korisnik k=new Korisnik();
		Uloga u=ur.findById(uloga).get();
		k.setAdresa(adresa);
		k.setIme(ime);
		k.setPrezime(prezime);
		k.setJmbg(jmbg);
		k.setUsername(username);
		k.setPassword(password);
		k.setUloga(u);
		kr.save(k);
		
		return "index";
	}
	
	@RequestMapping(value = "/getAukcije", method = RequestMethod.GET)
	public String getAukcije(HttpServletRequest req) {
		List<Predmet> lista=pr.findAll();
		req.getSession().setAttribute("predmeti", lista);
		
		return "PrikazAukcija";
	}
	
	@RequestMapping(value = "/checkInfo", method = RequestMethod.POST)
	public String checkInfo(String username, String password, HttpServletRequest req) {
		List<Predmet> lista=pr.findAll();
		req.getSession().setAttribute("predmeti", lista);
		Korisnik k=kr.findByUsername(username);
		String message="";
		if (k!=null) {
			if (!(k.getPassword().equals(password))) {
				message="Pogresna lozinka";
				req.getSession().setAttribute("poruka", message);
				return "index";
			}
			req.getSession().setAttribute("korisnik", k);
			return "regIndex";
		}
		else {
			message="Ne postoji korisnik sa tim username-om";
			req.getSession().setAttribute("poruka", message);
			return "index";
		}
	}
	@RequestMapping(value = "/getInfo", method = RequestMethod.GET)
	public String getInfo(HttpServletRequest req) {
		List<Stanje> stanja = sr.findAll();
		List<Kategorija> kategorije = katrep.findAll();
		req.getSession().setAttribute("stanja", stanja);
		req.getSession().setAttribute("kategorije", kategorije);
		return "dodajPredmet";
	}
	
	@RequestMapping(value = "/savePredmet", method = RequestMethod.POST)
	public String savePredmet(String naziv, String opis, Integer pocetnaCena, Date rokZavAuk, String slikaUrl, Integer stanje, Integer kategorija, HttpServletRequest req) {
		Predmet p = new Predmet();
		Stanje s = sr.findById(stanje).get();
		Kategorija k = katrep.findById(kategorija).get();
		Korisnik kor = (Korisnik)req.getSession().getAttribute("korisnik");
		p.setNaziv(naziv);
		p.setOpis(opis);
		p.setPocetnaCena(pocetnaCena);
		p.setRokZavAuk(rokZavAuk);
		p.setSlikaUrl(slikaUrl);
		p.setStanje(s);
		p.setKategorija(k);
		p.setKorisnik(kor);
		
		pr.save(p);
		req.getSession().setAttribute("prk", "Uspesno dodat predmet na aukciju!");
		return "dodajPredmet";
	}
}
