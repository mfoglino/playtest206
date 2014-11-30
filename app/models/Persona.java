package models;

import java.util.concurrent.Callable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import lombok.Data;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import play.db.jpa.JPA;
import play.libs.Akka;
import play.libs.F.Promise;
import scala.util.control.Exception;

@Data
@Entity
@Table(name = "PERSONA")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@JsonSerialize(include = Inclusion.NON_NULL)
public class Persona implements Cloneable {

	@Column(name = "DNI", precision = 10, nullable = false, columnDefinition = "NUMBER(10)")
	@Id
	private int dni;

	@Column(name = "NOMBRE", length = 30, nullable = false)
	private String nombre;

//	@Column(name = "APELLIDO", length = 30, nullable = true)
//	private String apellido;

	public static Persona findById(int dni) {
		return JPA.em().find(Persona.class, dni);
	}

	public Persona saveOrUpdate() {
		return JPA.em().merge(this);
	}

	public Promise<Persona> saveOrUpdateAsync() {
		final Persona person = this;
		JPA.em().detach(person);

		final EntityManager em = JPA.em();
		
		return Akka.future(new Callable<Persona>() {
			@Override
			public Persona call() {
				try {					
					JPA.bindForCurrentThread(em);
					Persona merge = JPA.em().merge(person);
					
					
					//throw new RuntimeException("catcheate esta");
					return merge;
				}
				catch (Throwable e) {
					throw new RuntimeException("Error saving Persona:" + person.getDni(), e);
				}
			}
		});
	}
}
