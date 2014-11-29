package crediusados;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrediUsadosRequest {

	private String name; 
	private String phone;
	private String email;
	private String zipCode;
	private Double downPayment;
	private Integer termMonths;
	private String carId;
	private Double carPrice;
	private String brand; 
	private String model;
	private Integer year;
	private String version;	
}

