package ru.mirea.shopcrud;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import ru.mirea.shopcrud.domain.Shop;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Handler extends DefaultHandler {
    private Shop shop;
    private Shop.Address address;
    private List<Shop.Product> products;
    private Shop.Product product;
    private List<Shop.Supplier> suppliers;
    private Shop.Supplier supplier;
    private Shop.Dimensions dimensions;
    private Shop.Price price;
    private Shop.Country country;

    private boolean		city;
    private boolean		street;
    private boolean		house;
    private boolean		priceRUR;
    private boolean		priceUSD;
    private boolean		weight;
    private boolean		description;

    private boolean		nameFull;
    private boolean		code;
    private boolean		countryName;

    @Override
    public void startDocument() throws SAXException {
        this.shop = new Shop();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (qName) {
            case "address": {
                this.address = new Shop.Address();
                break;
            }
            case "city": {
                this.city = true;
                break;
            }
            case "street": {
                this.street = true;
                break;
            }
            case "house": {
                this.house = true;
                break;
            }
            case "products": {
                this.products = new LinkedList<>();
                break;
            }
            case "product": {
                this.product = new Shop.Product();
                this.product.setId(Long.valueOf(attributes.getValue("id")));
                break;
            }
            case "price": {
                this.price = new Shop.Price();
                break;
            }
            case "priceRUR": {
                this.priceRUR = true;
                break;
            }
            case "priceUSD": {
                this.priceUSD = true;
                break;
            }
            case "dimensions": {
                this.dimensions = new Shop.Dimensions();
                dimensions.setX(Long.valueOf(attributes.getValue("x")));
                dimensions.setY(Long.valueOf(attributes.getValue("y")));
                dimensions.setZ(Long.valueOf(attributes.getValue("z")));
                this.product.setDimensions(dimensions);
                break;
            }
            case "weight": {
                this.weight = true;
                break;
            }
            case "description": {
                this.description = true;
                break;
            }
            case "suppliers": {
                this.suppliers = new LinkedList<Shop.Supplier>();
                break;
            }
            case "supplier": {
                this.supplier = new Shop.Supplier();
                this.supplier.setId(Long.valueOf(attributes.getValue("id")));
                break;
            }
            case "name_full": {
                this.nameFull = true;
                break;
            }
            case "country": {
                this.country = new Shop.Country();
                break;
            }
            case "code": {
                this.code = true;
                break;
            }
            case "name": {
                this.countryName = true;
                break;
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        switch (qName) {
            case "address": {
                this.shop.setAddress(address);
                break;
            }
            case "city": {
                break;
            }
            case "street": {
                break;
            }
            case "house": {
                break;
            }
            case "products": {
                this.shop.setProducts(this.products);
                break;
            }
            case "product": {
                this.products.add(this.product);
                break;
            }
            case "price": {
                this.product.setPrice(this.price);
                break;
            }
            case "priceRUR": {
                break;
            }
            case "priceUSD": {
                break;
            }
            case "dimensions": {
                break;
            }
            case "weight": {
                break;
            }
            case "description": {
                break;
            }
            case "suppliers": {
                this.shop.setSuppliers(this.suppliers);
                break;
            }
            case "supplier": {
                this.suppliers.add(this.supplier);
                break;
            }
            case "name_full": {
                break;
            }
            case "country": {
                this.supplier.setCountry(this.country);
                break;
            }
            case "code": {
                break;
            }
            case "name": {
                break;
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        if(this.street) {
            this.address.setStreet(new String(ch, start, length));
            this.street = false;
        } else if (this.city) {
            this.address.setCity(new String(ch, start, length));
            this.city = false;
        } else if (this.house) {
            this.address.setHouse(new String(ch, start, length));
            this.house = false;
        } else if (this.priceRUR) {
            this.price.setPriceRUR(new String(ch, start, length));
            this.priceRUR = false;
        } else if (this.priceUSD) {
            this.price.setPriceUSD(new String(ch, start, length));
            this.priceUSD = false;
        } else if (this.weight) {
            this.product.setWeight(Long.valueOf(new String(ch, start, length)));
            this.weight = false;
        } else if (this.description) {
            this.product.setDescription(new String(ch, start, length));
            this.description = false;
        } else if (this.nameFull) {
            this.supplier.setName(new String(ch, start, length));
            this.nameFull = false;
        } else if (this.code) {
            this.country.setCode(new String(ch, start, length));
            this.code = false;
        } else if (this.countryName) {
            this.country.setName(new String(ch, start, length));
            this.countryName = false;
        }
    }

    public Shop getShop() {
        products.forEach(product ->
            product.setSupplier(
                suppliers.stream()
                        .filter(s->s.getId().equals(product.getId()))
                        .findAny().orElse(null))
        );
        return shop;
    }
}
