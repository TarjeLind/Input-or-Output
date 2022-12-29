package ru.netology;

import org.w3c.dom.*;
import org.xml.sax.SAXException;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static boolean loadBasket;
    public static String loadFromFileName;
    public static String loadFromFileFormat;
    public static boolean saveBasket;
    public static String saveToFileName;
    public static String saveToFileFormat;
    public static boolean saveLog;
    public static String saveLogFileName;

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        Scanner scanner = new Scanner(System.in);
        String[] products = {"Молоко", "Соль", "Помидоры", "Оливки", "Вино", "Мясо", "Кофе"};
        int[] prices = {100, 50, 300,300,500,500,400};
        Basket basket = new Basket(products, prices);
        ClientLog clientLog = new ClientLog();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File("shop.xml"));
        Node config = document.getDocumentElement();

        readConfigFromXml(config);

        File fileLoad = new File(loadFromFileName);
        File fileSave = new File(saveToFileName);
        File fileLog = new File(saveLogFileName);

        //File fileCsv = new File("log.csv");
        ///File fileJson = new File("basket.json");

        if (loadBasket) {
            if (fileLoad.exists()) {
                if (loadFromFileFormat.equals("json")) {
                    basket = Basket.loadFromJsonFile(fileLoad);
                } else if (loadFromFileFormat.equals("txt")) {
                    basket = Basket.loadFromTxtFile(fileLoad);
                }
                System.out.println("Корзина с покупками восстановлена из файла");
                basket.printCart();
            } else {
                System.out.println("Сохраненной корзины с покупками нет");
            }
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

            if (saveLog) {
                clientLog.log(productNum, amount);
            }
        }
        if (saveBasket) {
            if (saveToFileFormat.equals("json")) {
                basket.saveJson(fileSave, basket);
            } else if (saveToFileFormat.equals("txt")) {
                basket.saveTxt(fileSave);
            }
        }
        if (saveLog) {
            clientLog.exportCSV(fileLog);
        }
        basket.printCart();
    }

    public static void readConfigFromXml(Node config) {
        NodeList nodeList = config.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (Node.ELEMENT_NODE == node.getNodeType()) {
                Element element = (Element) node;

                if (element.getParentNode().getNodeName().equals("load")) {
                    if (element.getNodeName().equals("enabled")) {
                        loadBasket = element.getTextContent().equals("true");
                    } else if (element.getNodeName().equals("fileName")) {
                        loadFromFileName = element.getTextContent();
                    } else if (element.getNodeName().equals("format")) {
                        loadFromFileFormat = element.getTextContent();
                    }
                } else if (element.getParentNode().getNodeName().equals("save")) {
                    if (element.getNodeName().equals("enabled")) {
                        saveBasket = element.getTextContent().equals("true");
                    } else if (element.getNodeName().equals("fileName")) {
                        saveToFileName = element.getTextContent();
                    } else if (element.getNodeName().equals("format")) {
                        saveToFileFormat = element.getTextContent();
                    }
                } else if (element.getParentNode().getNodeName().equals("log")) {
                    if (element.getNodeName().equals("enabled")) {
                        saveLog = element.getTextContent().equals("true");
                    } else if (element.getNodeName().equals("fileName")) {
                        saveLogFileName = element.getTextContent();
                    }
                }
                readConfigFromXml(node);
            }
        }
    }
}
