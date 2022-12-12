import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String[] products = {"Молоко", "Соль", "Помидоры", "Оливки", "Вино", "Мясо", "Кофе"};
        int[] prices = {100, 50, 300,300,500,500,400};
        Basket basket = new Basket(products, prices);
        File file = new File("basket.bin");

        if (file.exists()) {
            basket = Basket.loadFromBinFile(file);
            System.out.println("Корзина с покупками восстановлена из файла");
            basket.printCart();
        } else {
            System.out.println("Сохраненной корзины с покупками нет");
        }

        System.out.println("Список возможных товаров для покупки");
        for (int i = 0; i < products.length; i++) {
            System.out.println((i + 1) + ". " + products[i] + " - " + prices[i] + " руб/шт.");
        }

        while (true) {
            System.out.println("Выберите товар и количество или введите end");
            String input = scanner.nextLine();
            if (input.equals("end")) {
                break;
            }
            String[] parts = input.split(" ");
            int productNum = Integer.parseInt(parts[0]) - 1;
            int amount = Integer.parseInt(parts[1]);
            basket.addToCart(productNum, amount);
            basket.saveBin(file, basket);
        }
        basket.printCart();

    }
}