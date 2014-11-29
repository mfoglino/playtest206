package playtest206;

import org.junit.Test;

import controllers.SomeLogging_;
import play.Logger;

public class ATest {
	
	@Test
	public void test(){
		
		Logger.info("/////////  Mira como te imprimo esto INFO...");
		Logger.error("/////////  Mira como te mando un ERROR...");
		System.out.println("#### Hello play! #####");
		System.out.println("                                             ");
		SomeLogging_ s = new SomeLogging_();
		s.logueameEsta();
	}
}
