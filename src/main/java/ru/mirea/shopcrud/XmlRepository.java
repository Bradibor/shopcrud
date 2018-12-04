package ru.mirea.shopcrud;

import ru.mirea.shopcrud.domain.Shop;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.*;
import java.nio.file.Path;
import java.util.Optional;

public class XmlRepository {
    public Shop load(Path path) {
        try {
            File inputFile = path.toFile();
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            Handler userhandler = new Handler();
            saxParser.parse(inputFile, userhandler);
            return userhandler.getShop();
        } catch (Exception e) {
            e.printStackTrace();
            return new Shop();
        }
    }

    public void save(Shop shop, Path path) throws FileNotFoundException {
        try {
            OutputStream outputStream = new FileOutputStream(path.toFile());
            final XMLStreamWriter out = XMLOutputFactory.newInstance().createXMLStreamWriter(
                    new OutputStreamWriter(outputStream, "utf-8"));
            out.writeStartDocument();

            out.writeStartElement("shop");
            Shop.Address address = shop.getAddress();
            if (address != null && (address.getCity() != null || address.getHouse() != null || address.getStreet() != null)) {
                out.writeStartElement("address");
                Optional.ofNullable(address.getCity()).ifPresent(s -> writeSimpleElement("city", s, out));
                Optional.ofNullable(address.getStreet()).ifPresent(s -> writeSimpleElement("street", s, out));
                Optional.ofNullable(address.getHouse()).ifPresent(s -> writeSimpleElement("house", s, out));
                out.writeEndElement();
            }

            if (!shop.getProducts().isEmpty()) {
                out.writeStartElement("products");
                shop.getProducts().forEach(product -> {
                    try {
                        out.writeStartElement("product");
                        out.writeAttribute("id", String.valueOf(product.getId()));
                        if (product.getSupplier() != null) {
                            out.writeAttribute("supplier_id", String.valueOf(product.getSupplier().getId()));
                        }
                        Shop.Price price = product.getPrice();
                        if (price != null && (price.getPriceRUR() != null || price.getPriceUSD() != null)) {
                            out.writeStartElement("price");
                            Optional.ofNullable(price.getPriceRUR()).ifPresent(s -> writeSimpleElement("priceRUR", s, out));
                            Optional.ofNullable(price.getPriceUSD()).ifPresent(s -> writeSimpleElement("priceUSD", s, out));
                            out.writeEndElement();
                        }

                        Shop.Dimensions dimensions = product.getDimensions();
                        if (dimensions.getX() != null || dimensions.getY() != null || dimensions.getZ() != null)
                            out.writeStartElement("dimensions");
                        if (dimensions.getX() != null) out.writeAttribute("x", String.valueOf(dimensions.getX()));
                        if (dimensions.getY() != null) out.writeAttribute("y", String.valueOf(dimensions.getY()));
                        if (dimensions.getZ() != null) out.writeAttribute("z", String.valueOf(dimensions.getZ()));
                        out.writeEndElement();
                        Optional.ofNullable(product.getWeight()).ifPresent(s -> writeSimpleElement("weight", String.valueOf(s), out));
                        Optional.ofNullable(product.getDescription()).ifPresent(s -> writeSimpleElement("description", s, out));
                        out.writeEndElement();
                    } catch (XMLStreamException e) {
                        e.printStackTrace();
                    }
                });
                out.writeEndElement();
            }

            if (!shop.getSuppliers().isEmpty()) {
                out.writeStartElement("suppliers");
                shop.getSuppliers().forEach(supplier -> {
                    try {
                        out.writeStartElement("supplier");
                        out.writeAttribute("id", String.valueOf(supplier.getId()));
                        if (supplier.getName() != null) writeSimpleElement("name_full", supplier.getName(), out);
                        Shop.Country country = supplier.getCountry();
                        if (country != null && (country.getCode() != null || country.getName() != null)) {
                            out.writeStartElement("country");
                            Optional.ofNullable(country.getCode()).ifPresent(s -> writeSimpleElement("code", s, out));
                            Optional.ofNullable(country.getName()).ifPresent(s -> writeSimpleElement("name", s, out));
                            out.writeEndElement();
                        }
                        out.writeEndElement();
                    } catch (XMLStreamException e) {
                        e.printStackTrace();
                    }
                });
                out.writeEndElement();

            }


            out.writeEndElement();

            out.writeEndDocument();

            out.close();
        } catch (XMLStreamException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void writeSimpleElement(String name, String content, XMLStreamWriter out) {
        try {
            out.writeStartElement(name);
            out.writeCharacters(content);
            out.writeEndElement();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }
}
