package controllers;

import models.Persona;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.libs.F.Function;
import play.libs.F.Promise;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

public class Application extends Controller {

	public static Result index() {
		return ok(index.render("Your new application is ready."));
	}

	@Transactional
	public static Result savePerson() {
		try {
			Persona p = savingAPerson();
			// JPA.em().getTransaction().setRollbackOnly();
			return ok("Person saved:" + p.getDni());
		}

		catch (Exception e) {
			System.out.println("rollbacking transaction");
			JPA.em().getTransaction().setRollbackOnly();
			return internalServerError("error saving");
		}
	}

	@Transactional
	public static Result savePersonAsync() {
		try {

			// Promise<Persona> savingAPersonAsync = savingAPersonAsync();
			Promise<Boolean> savingAPersonAsync = savingTwoPersonsAsync();

			Promise<Result> savingPromise = savingAPersonAsync.map(new Function<Boolean, Result>() {

				@Override
				public Result apply(Boolean r) throws Throwable {
					System.out.println("Person was saved...");
					return ok("Person saved:" + r);

				}

			}).recover(new Function<Throwable, Result>() {

				@Override
				public Result apply(Throwable ex) throws Throwable {
					System.out.println("Error while saving Person");
					ex.printStackTrace();
					throw new RuntimeException("error promising...", ex);
				}
			});

			System.out.println("me voy");
			return async(savingPromise);
		}

		catch (Exception e) {
			System.out.println("rollbacking transaction");
			JPA.em().getTransaction().setRollbackOnly();
			return internalServerError("error saving");
		}
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
		return p.saveOrUpdateAsync().get();
	}

	@Transactional
	private static Promise<Boolean> savingTwoPersonsAsync() {
		Persona p = new Persona();
		p.setDni(66);
		p.setNombre("bruce");

		Promise<Persona> saveOrUpdateAsync = p.saveOrUpdateAsync();

		Persona p2 = new Persona();
		p2.setDni(88);
		p2.setNombre("steve");

		final Promise<Persona> saveOrUpdateAsync2 = p2.saveOrUpdateAsync();

		try {

			Promise<Boolean> savingPromise = saveOrUpdateAsync.flatMap(new Function<Persona, Promise<Boolean>>() {

				@Override
				public Promise<Boolean> apply(Persona p) throws Throwable {

					return saveOrUpdateAsync2.map(new Function<Persona, Boolean>() {
						public Boolean apply(Persona p2) {

							return true;

						}
					});
				}

			}).recover(new Function<Throwable, Boolean>() {

				@Override
				public Boolean apply(Throwable ex) throws Throwable {
					System.out.println("Error while saving Person");
					ex.printStackTrace();

					// return false;
					throw new RuntimeException("error promising...", ex);
				}
			});

			return savingPromise;

		}
		catch (Exception e) {
			JPA.em().getTransaction().setRollbackOnly();
			return new Promise<Boolean>(false);
		}

	}
}