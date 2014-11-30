package controllers;

import static org.fest.assertions.Assertions.assertThat;
import static play.mvc.Http.Status.INTERNAL_SERVER_ERROR;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;
import static play.test.Helpers.testServer;
import models.Persona;

import org.junit.Test;

import play.db.jpa.JPA;
import play.libs.WS;

public class ApplicationsTest {

	@Test
	public void whenErrorOnSavingThenRollbacksTransaction()
	{
		running(testServer(9000, fakeApplication(inMemoryDatabase("test"))), new Runnable() {
			public void run() {
				WS.Response response = WS.url("http://localhost:9000/savePersonAsync").get().get();

				// Asserts
				assertThat(response.getStatus()).isEqualTo(INTERNAL_SERVER_ERROR);


				JPA.withTransaction(new play.libs.F.Callback0() {
					public void invoke() {
						Persona p = Persona.findById(66);

						assertThat(p).isNull();
					}
				});
			}
		});

	}
}
