package controllers;

import models.Persona;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

public class Application extends Controller {

	public static Result index() {
		return ok(index.render("Your new application is ready."));
	}

	@Transactional
	public static Result savePerson() {

		Persona p = savingAPerson();

		
		//JPA.em().getTransaction().setRollbackOnly();
		return ok("Person saved:" + p.getDni());
	}

	@Transactional
	public static Result getPerson() {

		Persona p = Persona.findById(66);
		if (p == null)
			return notFound();

		return ok("Person:" + p.getDni() + " " + p.getNombre());
	}

	private static Persona savingAPerson() {
		Persona p = new Persona();
		p.setDni(66);
		p.setNombre("bruce");
		return p.saveOrUpdate();
	}
}