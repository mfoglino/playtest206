package crediusados;

import static org.fest.assertions.Assertions.assertThat;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.running;
import static play.test.Helpers.testServer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.codehaus.jackson.JsonNode;
import org.junit.Test;

import play.libs.WS;
import play.libs.F.Promise;
import play.libs.WS.Response;
import play.test.FakeApplication;
import play.test.Helpers;
import play.test.TestServer;

public class ApiCrediusadosTest {
	
	TestServer server =testServer(9000);
	
	@Test
	public void aSuccessfulCall()
	{
		running(server, new Runnable() {
			public void run() {
				
				CrediUsadosRequest crediUsadosRequest = new CrediUsadosRequest();
				crediUsadosRequest.setName("Susan Calvin");
				crediUsadosRequest.setPhone("1234566");
				crediUsadosRequest.setEmail("adummymail@dummy.com");		
				crediUsadosRequest.setDownPayment(1200.0);
				crediUsadosRequest.setTermMonths(12);
						
				Promise<JsonNode> quotation = ApiCrediUsados.getQuotation(crediUsadosRequest);
			
				System.out.println("RESP: "+ quotation.get().asText());

				
				
				//assertThat(r.getStatus()).isEqualTo(OK);
			}
		});
	}
	
	public static TestServer testServer(int port) {
		return new TestServer(port, fakeApplication());
	}
	
	public static FakeApplication fakeApplication() {
		return new FakeApplication(new File("."), Helpers.class.getClassLoader(), new HashMap(), new ArrayList());
	}
}
