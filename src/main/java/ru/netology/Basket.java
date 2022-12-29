package ru.netology;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

public class Basket implements Serializable {

    protected static String[] products;
    protected static int[] prices;
    protected static int[] carts;

    public Basket(String[] products, int[] prices) {
        this.products = products;
        this.prices = prices;
        carts = new int[products.length];
    }

    public void addToCart(int productNum, int amount) {
        carts[productNum] += amount;
    }

    public void printCart() {
        System.out.println("Ваша корзина: ");
        int sumProducts = 0;
        for (int i = 0; i < carts.length; i++) {
            if (carts[i] == 0) {
                continue;
            }
            System.out.println(products[i] + " " + carts[i] +
                    " шт. по " + prices[i] + " руб. - всего " +
                    (carts[i] * prices[i]) + " руб.");
            sumProducts += carts[i] * prices[i];
        }
        System.out.println("Итого - " + sumProducts + " руб");
    }


    public void saveTxt(File file) {
        try (FileWriter writer = new FileWriter(file, false)) {
            for (String product : products) {
                writer.write(product + " ");
            }
            writer.write("\n");
            for (int price : prices) {
                writer.write(price + " ");
            }
            writer.write("\n");
            for (int cart : carts) {
                writer.write(cart + " ");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static Basket loadFromTxtFile(File textFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(textFile))) {
            String[] products = reader.readLine().split(" ");

            String[] pricesStr = reader.readLine().split(" ");
            int[] prices = new int[pricesStr.length];
            for (int i = 0; i < pricesStr.length; i++) {
                prices[i] = Integer.parseInt(pricesStr[i]);
            }

            Basket basket = new Basket(products, prices);
            String[] cartStr = reader.readLine().split(" ");
            for (int i = 0; i < cartStr.length; i++) {
                basket.carts[i] = Integer.parseInt(cartStr[i]);
            }
            return basket;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void saveJson(File fileJson, Basket basket) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        try (FileWriter writer = new FileWriter(fileJson)) {
            writer.write(gson.toJson(basket));
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Basket loadFromJsonFile(File fileJson) {
        Basket basket;
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        try (FileReader reader = new FileReader(fileJson)) {
            basket = gson.fromJson(reader, Basket.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return basket;
    }
}
