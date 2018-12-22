package ru.mirea.shopcrud;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ru.mirea.shopcrud.domain.Shop;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class XmlRepository {
    public List<Shop> load(Path path) {
        try {
//            File inputFile = path.toFile();
//            SAXParserFactory factory = SAXParserFactory.newInstance();
//            SAXParser saxParser = factory.newSAXParser();
//            Handler userhandler = new Handler();
//            saxParser.parse(inputFile, userhandler);
//            return userhandler.getShop();
            DocumentBuilderFactory factory =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(path.toFile());
            Element root = doc.getDocumentElement();
            NodeList nodeList = root.getElementsByTagName("shop");
            List<Shop> shops = new ArrayList<>();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Shop shop = new Shop();
                Node shopNode = nodeList.item(i);
                String id = shopNode.getAttributes().getNamedItem("id").getNodeValue();
                shop.setId(Long.parseLong(id));
                Element shopEl = (Element) shopNode;
                String city = shopEl.getElementsByTagName("city").item(0).getTextContent();
                String street = shopEl.getElementsByTagName("street").item(0).getTextContent();
                String house = shopEl.getElementsByTagName("house").item(0).getTextContent();
                String building = shopEl.getElementsByTagName("building").item(0).getTextContent();
                shop.setAddress(new Shop.Address(city, street, house, building));
                NodeList productNodeList = shopEl.getElementsByTagName("product");
                List<Shop.Product> products = new ArrayList<>();
                for (int j = 0; j < productNodeList.getLength(); j++) {
                    Node productNode = productNodeList.item(j);
                    Element productEl = (Element) productNode;
                    Shop.Product product = new Shop.Product();
                    Optional.ofNullable(productNode.getAttributes())
                            .map(namedNodeMap -> namedNodeMap.getNamedItem("id"))
                            .map(Node::getNodeValue)
                            .map(Long::parseLong)
                            .ifPresent(product::setId);
                    Optional.ofNullable(productNode.getAttributes())
                            .map(namedNodeMap -> namedNodeMap.getNamedItem("supplier_id"))
                            .map(Node::getNodeValue)
                            .map(Long::parseLong)
                            .ifPresent(product::setSupplier_id);
                    String description = productEl.getElementsByTagName("description").item(0).getTextContent();
                    product.setDescription(description);
                    Node priceNode = productEl.getElementsByTagName("price").item(0);
                    Shop.Price price = new Shop.Price();
                    Optional.ofNullable(priceNode.getAttributes())
                            .map(namedNodeMap -> namedNodeMap.getNamedItem("currency"))
                            .map(Node::getNodeValue)
                            .ifPresent(price::setCurrency);
                    price.setPrice(priceNode.getTextContent());
                    Node quantityNode = productEl.getElementsByTagName("quantity").item(0);
                    Shop.Quantity quantity = new Shop.Quantity();
                    Optional.ofNullable(quantityNode.getAttributes())
                            .map(namedNodeMap -> namedNodeMap.getNamedItem("type"))
                            .map(Node::getNodeValue)
                            .ifPresent(quantity::setType);
                    quantity.setValue(Integer.parseInt(quantityNode.getTextContent()));
                    product.setPrice(price);
                    product.setQuantity(quantity);
                    products.add(product);
                }
                shop.setProducts(products);
                shops.add(shop);
            }
            return shops;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public void save(List<Shop> shops, Path path) throws FileNotFoundException {
        try {
            OutputStream outputStream = new FileOutputStream(path.toFile());
            final XMLStreamWriter out = XMLOutputFactory.newInstance().createXMLStreamWriter(
                    new OutputStreamWriter(outputStream, "utf-8"));
            out.writeStartDocument();
            out.writeStartElement("shops");
            for (int i = 0; i < shops.size(); i++) {
                Shop shop = shops.get(i);
                out.writeStartElement("shop");
                out.writeAttribute("id", String.valueOf(shop.getId()));
                Shop.Address address = shop.getAddress();
                if (address != null && (address.getCity() != null || address.getHouse() != null || address.getStreet() != null)) {
                    out.writeStartElement("address");
                    Optional.ofNullable(address.getCity()).ifPresent(s -> writeSimpleElement("city", s, out));
                    Optional.ofNullable(address.getStreet()).ifPresent(s -> writeSimpleElement("street", s, out));
                    Optional.ofNullable(address.getHouse()).ifPresent(s -> writeSimpleElement("house", s, out));
                    Optional.ofNullable(address.getBuilding()).ifPresent(s -> writeSimpleElement("building", s, out));
                    out.writeEndElement();
                }
                out.writeStartElement("products");
                List<Shop.Product> products = Optional.ofNullable(shop.getProducts()).orElse(Collections.emptyList());
                for (int j = 0; j < products.size(); j++) {
                    Shop.Product product = products.get(j);
                    out.writeStartElement("product");
                    out.writeAttribute("id", String.valueOf(product.getId()));
                    out.writeAttribute("supplier_id", String.valueOf(product.getSupplier_id()));

                    out.writeStartElement("price");
                    out.writeAttribute("currency", product.getPrice().getCurrency());
                    out.writeCharacters(product.getPrice().getPrice());
                    out.writeEndElement();
                    out.writeStartElement("quantity");
                    out.writeAttribute("type", product.getQuantity().getType());
                    out.writeCharacters(String.valueOf(product.getQuantity().getValue()));
                    out.writeEndElement();
                    writeSimpleElement("description", product.getDescription(), out);
                    out.writeEndElement();
                }
                out.writeEndElement();
                out.writeEndElement();
            }

            out.writeEndElement();
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

    public static void main(String[] args) throws FileNotFoundException {
        XmlRepository xmlRepository = new XmlRepository();
        List<Shop> shops = xmlRepository.load(Paths.get("C:\\Users\\bradi\\IdeaProjects\\shopcrud\\src\\main\\resources\\shop.xml"));
        xmlRepository.save(shops, Paths.get("C:\\Users\\bradi\\IdeaProjects\\shopcrud\\src\\main\\resources\\kek3.xml"));
    }
}
