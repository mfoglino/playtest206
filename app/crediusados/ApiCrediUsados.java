package crediusados;

import org.codehaus.jackson.JsonNode;

import play.libs.Json;
import play.libs.WS;
import play.libs.F.Function;
import play.libs.F.Promise;
import play.libs.WS.Response;
import play.libs.WS.WSRequestHolder;
import play.mvc.Result;
import play.mvc.Http.HeaderNames;

import com.typesafe.config.ConfigFactory;

public class ApiCrediUsados {

	private static final String API_CREDIUSADOS = ConfigFactory.load().getString("service.api_crediusados.url");

	public static Promise<JsonNode> getQuotation(CrediUsadosRequest requestParams) {
		Promise<Response> response = WS.url(API_CREDIUSADOS).setQueryParameter("key", "3pxNKFRX1ngZFgN1H2Mas6bnh1nzo5-201411051717502")
				.setHeader(HeaderNames.CONTENT_TYPE, "application/json").setTimeout(5000).post(Json.toJson(requestParams).toString());

		System.out.println("La resp:" +response.get().getBody());
		
		return response.map(new Function<Response, JsonNode>() {
			@Override
			public JsonNode apply(Response quotationResponse) throws Throwable {
				return quotationResponse.asJson();
			}
		});
	}
}
