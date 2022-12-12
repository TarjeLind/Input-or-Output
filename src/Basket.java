import java.io.*;

public class Basket implements Serializable {
    protected String[] products;
    protected int[] prices;
    protected int[] carts;

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
            sumProducts += carts[i] * prices[i];
            if (carts[i] > 0) {
                System.out.println(products[i] + " " + carts[i] + " шт. по " + prices[i] + " руб. - всего: " + (carts[i] * prices[i] + " руб."));
            }
        }
        System.out.println("Итого: " + sumProducts + " руб.");
    }
    public void saveBin(File file, Basket basket) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(basket);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    static Basket loadFromBinFile(File file) {
        Basket basket = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            basket = (Basket) ois.readObject();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return basket;
    }
}