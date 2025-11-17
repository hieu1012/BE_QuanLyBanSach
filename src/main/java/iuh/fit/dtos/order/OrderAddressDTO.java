package iuh.fit.dtos.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderAddressDTO {
    private Integer id;
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String state;
    private String pincode;
    private String mobileNo;
    private String email;
}
