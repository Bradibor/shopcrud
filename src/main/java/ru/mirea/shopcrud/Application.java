package ru.mirea.shopcrud;

import ru.mirea.shopcrud.domain.Shop;

import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class Application {
    public static void main(String[] args) throws FileNotFoundException {
        XmlRepository xmlRepository = new XmlRepository();
        Scanner scanner = new Scanner(System.in);
        System.out.println("enter working dir: ");
        String workDir = scanner.next();
        System.out.println("enter filename to load: ");
        String fileName = scanner.next();
        Shop shop = xmlRepository.load(Paths.get(workDir, fileName));
        System.out.println(shop.toString());
        System.out.println("enter +/- to add/delete product, x to exit: ");
        String input = scanner.next();
        while (!input.equals("x")) {
            switch (input) {
                case "+": add(scanner, shop); break;
                case "-": delete(scanner, shop); break;
            }
            System.out.println(shop);
            System.out.println("enter '+'/'-' to add/delete product, 'x' to exit: ");
            input = scanner.next();
        }
        System.out.println("save changes? y/n");
        input = scanner.next();
        if(input.equals("y"))
//        String workDir = "C:\\Users\\bradi\\IdeaProjects\\shopcrud\\src\\main\\resources";
        xmlRepository.save(shop, Paths.get(workDir, fileName));
    }

    private static void add(Scanner scanner, Shop shop) {
        try {
            System.out.println("enter priceRur/priceUsd/x/y/z/weight/description/id");
            String input = scanner.next();
            String[] inputArray = input.split("/");
            Shop.Product product = new Shop.Product();
            product.setPrice(new Shop.Price(inputArray[0], inputArray[1]));
            product.setDimensions(new Shop.Dimensions(Long.parseLong(inputArray[2]),Long.parseLong(inputArray[3]),Long.parseLong(inputArray[4])));
            product.setWeight(Long.valueOf(inputArray[5]));
            product.setDescription(inputArray[6]);
            product.setId(Long.valueOf(inputArray[7]));
            shop.getProducts().add(product);
        } catch (Exception ignored) {}

    }

    private static void delete(Scanner scanner, Shop shop) {
        System.out.println("enter id to delete: ");
        String id = scanner.next();
        List<Shop.Product> productList = shop.getProducts();
        for(int i = 0; i < productList.size(); i ++) {
            if(Long.parseLong(id) == productList.get(i).getId()) productList.remove(i);
        }
    }
}
