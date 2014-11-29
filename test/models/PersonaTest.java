package models;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;
import static play.test.Helpers.testServer;

import org.junit.Test;

import play.db.jpa.JPA;

public class PersonaTest {

	@Test
	public void saving() {
		running(testServer(9000, fakeApplication(inMemoryDatabase("test"))), new Runnable() {
			public void run() {

				JPA.withTransaction(new play.libs.F.Callback0() {
					public void invoke() {

						Persona p = new Persona();
						p.setDni(77);
						p.setNombre("bruce");

						Persona saved = p.saveOrUpdate();

						JPA.em().getTransaction().setRollbackOnly();
						assertThat(p.getDni()).isEqualTo(77);
					}
				});

				JPA.withTransaction(new play.libs.F.Callback0() {
					public void invoke() {

						Persona p = Persona.findById(77);

						assertThat(p).isNull();
					}
				});
			}
		});
	}

	@Test
	public void savingAsync() {
		final Boolean rollbaclk = false;

		running(testServer(9000, fakeApplication(inMemoryDatabase("test"))), new Runnable() {
			public void run() {

				JPA.withTransaction(new play.libs.F.Callback0() {
					public void invoke() {

						Persona p = new Persona();
						p.setDni(77);
						p.setNombre("bruce");

						// Persona saved = p.saveOrUpdateAsync().get();
						p.saveOrUpdateAsync().get();

						if (rollbaclk) {
							JPA.em().getTransaction().setRollbackOnly();
						}

						assertThat(p.getDni()).isEqualTo(77);
					}
				});

				JPA.withTransaction(new play.libs.F.Callback0() {
					public void invoke() {

						Persona p = Persona.findById(77);

						if (rollbaclk) {
							assertThat(p).isNull();
						}
						else
						{
							assertThat(p.getDni()).isEqualTo(77);
						}
					}
				});
			}
		});

	}
}
