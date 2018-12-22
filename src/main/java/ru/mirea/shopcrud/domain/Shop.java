package ru.mirea.shopcrud.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
public class Shop {
    private Long id;
    private Address address;
    private List<Product> products;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static public class Address {
        private String city;
        private String street;
        private String house;
        private String building;
    }

    @Data
    static public class Product {
        private Long id;
        private Long supplier_id;
        private Price price;
        private Quantity quantity;
        private String description;
    }

    @Data
    static public class Quantity {
        private String type;
        private Integer value;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static public class Price {
        private String currency;
        private String price;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.join(" ", address.getCity(), address.getStreet(), address.getHouse()));
        sb.append("\nProducts: ");
        if(products != null) {
            products.forEach(product -> {
                sb.append("\n" + String.join(" ", product.id + "", product.description,
                        product.getPrice().getPrice() + product.getPrice().getCurrency()));
            });
        }
        return sb.toString();
    }
}
