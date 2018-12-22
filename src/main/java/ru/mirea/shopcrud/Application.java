package ru.mirea.shopcrud;

import ru.mirea.shopcrud.domain.Shop;

import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class Application {
    public static void main(String[] args) throws FileNotFoundException {
//        todo: добавить редактирование продуктов
        XmlRepository xmlRepository = new XmlRepository();
        Scanner scanner = new Scanner(System.in);
        System.out.println("enter working dir: ");
        String workDir = scanner.next();
        System.out.println("enter filename to load: ");
        String fileName = scanner.next();
        List<Shop> shops = xmlRepository.load(Paths.get(workDir, fileName));
        shops.forEach(System.out::println);
        System.out.println("enter +/- to add/delete shop, x to exit: ");
        String input = scanner.next();
        while (!input.equals("x")) {
            switch (input) {
                case "+": add(scanner, shops); break;
                case "-": delete(scanner, shops); break;
            }
            shops.forEach(System.out::println);
            System.out.println("enter '+'/'-' to add/delete product, 'x' to exit: ");
            input = scanner.next();
        }
        System.out.println("save changes? y/n");
        input = scanner.next();
        if(input.equals("y"))
        xmlRepository.save(shops, Paths.get(workDir, fileName));
    }

    private static void add(Scanner scanner, List<Shop> shops) {
        try {
            System.out.println("enter id/city/street/house/building");
            String input = scanner.next();
            String[] inputArray = input.split("/");
            Shop shop = new Shop();
            shop.setId(Long.valueOf(inputArray[0]));
            Shop.Address address = new Shop.Address();
            shop.setAddress(address);
            address.setCity(inputArray[1]);
            address.setStreet(inputArray[2]);
            address.setHouse(inputArray[3]);
            address.setBuilding(inputArray[4]);
            shops.add(shop);
        } catch (Exception ignored) {}

    }

    private static void delete(Scanner scanner, List<Shop> shops) {
        System.out.println("enter id to delete: ");
        String id = scanner.next();
        for(int i = 0; i < shops.size(); i ++) {
            if(Long.parseLong(id) == shops.get(i).getId()) shops.remove(i);
        }
    }
}
