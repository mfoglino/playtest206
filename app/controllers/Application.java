package controllers;

import java.util.concurrent.Callable;

import models.Persona;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.libs.Akka;
import play.libs.F;
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

	// @Transactional
	public static Result savePersonAsync() {
		try {

			// Promise<Persona> savingAPersonAsync = savingAPersonAsync();
			Promise<Boolean> savingAPersonAsync = savingTwoPersonsAsync();

			Promise<Result> savingPromise = savingAPersonAsync.map(new Function<Boolean, Result>() {

				@Override
				public Result apply(Boolean r) throws Throwable {
					System.out.println("Persons was saved...");
					return ok("Persons saved:" + r);

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
			// JPA.em().getTransaction().setRollbackOnly();
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

		return Akka.future(new Callable<Boolean>() {
			@Override
			public Boolean call() {
				try {
					return JPA.withTransaction(new F.Function0<Boolean>() {
						@Override
						public Boolean apply() throws Throwable {						
							
							try {
								Persona p = new Persona();
								p.setDni(66);
								p.setNombre("bruce");

								Persona p2 = new Persona();
								p2.setDni(88);
								p2.setNombre("steve");

								p.saveOrUpdate();
								p2.saveOrUpdate();

								// throw new RuntimeException("catcheate esta");
								return true;
							} catch (Throwable e) {
								JPA.em().getTransaction().setRollbackOnly();
								return false;
								// throw new RuntimeException("Error saving Personas...", e);
							}				
							
						}
					});
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				}

			}
		});
	}
}