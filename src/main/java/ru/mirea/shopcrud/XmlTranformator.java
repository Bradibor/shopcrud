package ru.mirea.shopcrud;

import lombok.val;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.nio.file.Paths;
import java.util.Optional;

public class XmlTranformator {
    InputStream styleFrom = new FileInputStream(new File("C:\\Users\\bradi\\IdeaProjects\\shopcrud\\src\\main\\resources\\tranformFrom.xsl"));
    InputStream styleTo = new FileInputStream(new File("C:\\Users\\bradi\\IdeaProjects\\shopcrud\\src\\main\\resources\\transformTo.xsl"));

    public XmlTranformator() throws FileNotFoundException {
    }

    public File convertFrom(File file) {
        try {
            StreamSource styleSource = new StreamSource(styleFrom);
            StreamSource dataSource = new StreamSource(new FileInputStream(file));
            Transformer transformer = TransformerFactory.newInstance().newTransformer(styleSource);
            File temp = new File("C:\\Users\\bradi\\IdeaProjects\\shopcrud\\src\\main\\resources\\converted.xml");
            OutputStream out = new FileOutputStream(temp);
            Result result = new StreamResult(out);
            InputStream is = new FileInputStream(temp);
            transformer.transform(dataSource, result);
            return temp;
        } catch (TransformerException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public File convertTo(File file) {
        try {
            StreamSource styleSource = new StreamSource(styleTo);
            StreamSource dataSource = new StreamSource(new FileInputStream(new File("C:\\Users\\bradi\\IdeaProjects\\shopcrud\\src\\main\\resources\\converted.xml")));
            Transformer transformer = TransformerFactory.newInstance().newTransformer(styleSource);
            OutputStream out = new FileOutputStream(file);
            Result result = new StreamResult(out);
            InputStream is = new FileInputStream(file);
            transformer.transform(dataSource, result);
            return file;
        } catch (TransformerException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws FileNotFoundException {
        XmlTranformator tranformator = new XmlTranformator();
        File result = tranformator.convertFrom(new File("C:\\Users\\bradi\\IdeaProjects\\shopcrud\\src\\main\\resources\\shop2"));
        System.out.println(result);
        tranformator.convertTo(new File("C:\\Users\\bradi\\IdeaProjects\\shopcrud\\src\\main\\resources\\shop22.xml"));
    }
}
