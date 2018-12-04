package ru.mirea.shopcrud.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
public class Shop {
    private Address address;
    private List<Product> products;
    private List<Supplier> suppliers;

    @Data
    static public class Address {
        private String city;
        private String street;
        private String house;
    }

    @Data
    static public class Product {
        private Long id;
        private Supplier supplier;
        private Price price;
        private Dimensions dimensions;
        private Long weight;
        private String description;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static public class Price {
        private String priceRUR;
        private String priceUSD;
    }

    @Data
    static public class Supplier {
        private Long id;
        private String name;
        private Country country;
    }

    @Data
    static public class Country {
        private String name;
        private String code;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static public class Dimensions {
        private Long x;
        private Long y;
        private Long z;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.join(" ", address.getCity(), address.getStreet(), address.getHouse()));
        sb.append("\nProducts: ");
        products.forEach(product -> {
            sb.append("\n"+String.join(" ", product.id+"", product.description,
                    product.getPrice().getPriceRUR()+"Р./"+product.getPrice().getPriceUSD()+"$",
                    String.join("x", product.dimensions.x+"", product.dimensions.y+"", product.dimensions.z+"")));
        });
        return sb.toString();
    }
}
