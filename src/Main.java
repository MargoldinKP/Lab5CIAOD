import java.io.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    public static void main(String[]args) {
        File f1 = null;
        Scanner in = new Scanner(System.in);
        int variant;
        while (true) {
            System.out.println("\n1. Открыть текстовый файл ");
            System.out.println("2. Вывести содержимое текстового файла");
            System.out.println("3. Вывести символы алфавита с указанием их частоты появления с сортировкой по частоте; ");
            System.out.println("4. Сгенерировать коды для символов алфавита входного файла");
            System.out.println("5. Сжать содержимое текстового файла с помощью кодов фиксированной длины с сохранением данных в файл ");
            System.out.println("6. Сжать содержимое текстового файла с помощью кодов Хаффмана с сохранением данных в файл ");
            System.out.println("7. Сравнить размеры файлов исходного текстового файла и двух зашифрованных ");
            System.out.print("Выберите пункт меню: ");
            try {
                variant = Integer.parseInt(in.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Неверный выбор");
                continue;
            }
            switch (variant) {
                case 1:
                    System.out.println("Введите путь к файлу: ");
                    String f = in.nextLine();
                    f1 = new File(f);
                    if (f1.exists() && !f1.isDirectory()) {
                        System.out.println("Файл успешно открыт: " + f1.getName());
                    } else {
                        System.out.println("Файл не найден или это каталог. Попробуй еще раз ");
                        f1 = null;
                    }
                    break;
                case 2:
                    if (f1 != null && f1.isFile()) {
                        try (BufferedReader reader = new BufferedReader(new FileReader(f1))) {
                            String line;
                            System.out.println("Содержимое файла " + f1.getName() + ":");
                            while ((line = reader.readLine()) != null) {
                                System.out.println(line);
                            }
                        } catch (FileNotFoundException e) {
                            System.out.println("Файл не найден: " + e.getMessage());
                        } catch (IOException e) {
                            System.out.println("Ошибка при чтении файла: " + e.getMessage());
                        }
                    } else {
                        System.out.println("Сначала откройте текстовый файл (Кейс 1)");
                    }
                    break;
                case 3:
                    if (f1 == null || f1.isDirectory()) {
                        System.out.println("Сначала необходимо выбрать текстовый файл");
                        break;
                    }
                    try {
                        String content = new String(Files.readAllBytes(Paths.get(f1.getPath())));
                        Map<Character, Integer> freqMap = getFrequencyMap(content);
                        List<Map.Entry<Character, Integer>> sortedList = sortFrequencyMap(freqMap);
                        printFrequencyList(sortedList);
                    } catch (IOException e) {
                        System.out.println("Ошибка при чтении файла: " + e.getMessage());
                    }
                    break;
                case 4:
                    int var;
                    boolean q = true;
                    while (q) {
                        System.out.println("\n1. С кодами фиксированной длины для каждого символа алфавита ");
                        System.out.println("2. С кодами Хаффмана для каждого символа алфавита;");
                        System.out.println("3. Назад ");

                        System.out.print("Выберите пункт меню: ");
                        try {
                            var = Integer.parseInt(in.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Неверный выбор");
                            continue;
                        }
                        switch (var) {
                            case 1:
                                if (f1 != null && f1.isFile()) {
                                    try {
                                        // Считываем все содержимое файла в одну строку
                                        String inf = new String(Files.readAllBytes(Paths.get(f1.getPath())));
                                        Map<Character, String> codes = generateLengthCodes(inf);// коды фиксированной длины
                                        // Выводим коды для каждого символа
                                        for (Map.Entry<Character, String> entry : codes.entrySet()) {
                                            System.out.println(entry.getKey() + ": " + entry.getValue());
                                        }
                                    } catch (IOException e) {
                                        System.out.println("Ошибка при чтении файла: " + e.getMessage());
                                    }
                                } else {
                                    System.out.println("Файл не выбран или указанный путь не является файлом ");
                                }
                                break;
                            case 2:
                                if (f1 == null || f1.isDirectory()) {
                                    System.out.println("Сначала необходимо выбрать текстовый файл");
                                    break;
                                }
                                try {
                                    String inf = new String(Files.readAllBytes(Paths.get(f1.getPath())));
                                    Map<Character, String> codes = generateHuffmanCodes(inf);
                                    for (Map.Entry<Character, String> entry : codes.entrySet()) {
                                        System.out.println(entry.getKey() + ": " + entry.getValue());
                                    }
                                } catch (IOException e) {
                                    System.out.println("Ошибка при чтении файла: " + e.getMessage());
                                }
                                break;

                            case 3:
                                System.out.println("Назад");
                                q = false;
                                break;
                        }
                    }
                    break;
                case 5:
                    if (f1 != null && f1.isFile()) {
                        try {
                            String inf = new String(Files.readAllBytes(Paths.get(f1.getPath())));
                            Map<Character, String> codes = generateLengthCodes(inf);
                            String encodedContent = encodeInf(codes, inf);
                            saveToFile(encodedContent, "encoded_file.bin");
                            System.out.println("Файл успешно закодирован и сохранен под именем 'encoded_file.bin'");
                            System.out.println("Закодированный текст (фиксированная длина):");
                            print(encodedContent);
                        } catch (IOException e) {
                            System.out.println("Ошибка при чтении файла: " + e.getMessage());
                        }
                    } else {
                        System.out.println("Файл не выбран или указанный путь не является файлом.");
                    }
                    break;
                case 6:
                    if (f1 != null && f1.isFile()) {
                        try {
                            // Считываем содержимое исходного файла
                            String inf = new String(Files.readAllBytes(Paths.get(f1.getPath())));
                            Map<Character, String> codes = generateHuffmanCodes(inf);// Генерируем коды Хаффмана
                            // Кодируем текст с использованием сгенерированных кодов
                            String encodedContent = encodeInfWithHuffmanCodes(codes, inf);
                            // Сохраняем закодированный текст в файл
                            saveToFile(encodedContent, "encoded_file_huffman.bin"); // Также необходимо сохранить таблицу кодов
                            System.out.println("Файл успешно закодирован под именем 'encoded_file_huffman.bin'");
                            System.out.println("Закодированный текст (код Хаффмана):");
                            print(encodedContent);
                        } catch (IOException e) {
                            System.out.println("Ошибка при чтении файла: " + e.getMessage());
                        }
                    } else {
                        System.out.println("Файл не выбран или указанный путь не является файлом ");
                    }
                    break;
                case 7:
                        File encodedFixedLengthFile = new File("encoded_file.bin"); // Путь к файлу с фиксированной длиной
                        File encodedHuffmanFile = new File("encoded_file_huffman.bin"); // Путь к файлу с кодами Хаффмана

                        if(f1.exists() && encodedFixedLengthFile.exists() && encodedHuffmanFile.exists()) {
                            long originalFileSize = f1.length();
                            long fixedFileSize = encodedFixedLengthFile.length();
                            long huffmanFileSize = encodedHuffmanFile.length();

                            // Выведите размеры файлов
                            System.out.println("Размер исходного файла: " + originalFileSize + " байт");
                            System.out.println("Размер файла с фиксированной длиной кода: " + fixedFileSize + " байт");
                            System.out.println("Размер файла с кодом Хаффмана: " + huffmanFileSize + " байт");

                            System.out.println("Разница между размером исходного файла с файлом фикс. длины "+
                                    (originalFileSize-fixedFileSize)+" байт");
                            System.out.println("Разница между размером исходного файла с файлом с кодом Хаффмана "+
                                    (originalFileSize-huffmanFileSize)+" байт");
                        } else {
                            // Если один из файлов не найден, сообщите об этом
                            System.out.println("Один из файлов не существует. Попробуйте снова ");
                        }
                    break;

            }
        }
    }

    private static Map<Character, Integer> getFrequencyMap(String content) {
        Map<Character, Integer> freqMap = new HashMap<>();
        for (char ch : content.toCharArray()) {
            int z=freqMap.getOrDefault(ch, 0); //если мап содержит счетчик вернет его частоту, если его нет то 0
            freqMap.put(ch, z + 1); //символу сопоставляется его частота появления
        }
        return freqMap;
    }

    private static List<Map.Entry<Character, Integer>> sortFrequencyMap(Map<Character, Integer> freqMap) {
        //в конструкторе передаем все сеты мап-пары ключ-значения
        List<Map.Entry<Character, Integer>> sortedList = new ArrayList<>(freqMap.entrySet());
        sortedList.sort(Map.Entry.<Character, Integer>comparingByValue().reversed()); //компаратор сортирует по частоте
        return sortedList;
    }

    private static void printFrequencyList(List<Map.Entry<Character, Integer>> list) {
        for (Map.Entry<Character, Integer> entry : list) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }



        public static Map<Character, String> generateLengthCodes(String content) {
            Set<Character> charset = new HashSet<>();
            for (char c : content.toCharArray()) {
                charset.add(c);
            }
//вычисляется мин. кол-во бит и округляется до ближ. большего
            int bitsPerChar = (int) Math.ceil(Math.log(charset.size()) / Math.log(2));
            Map<Character, String> codes = new HashMap<>();
            int counter = 0;

            for (char c : charset) {
                String binaryString = Integer.toBinaryString(counter); //получаем двоичное значение
                //форматируем строку через битсПерЧар, если бинари стринг меньше то метод добавит пробелы
                binaryString = String.format("%" + bitsPerChar + "s", binaryString).replace(' ', '0');//реплейс нужен для замены пробелов нулями
                codes.put(c, binaryString); //добавляем в мап символ и значение
                counter++;
            }
            return codes;
        }

        public static String encodeInf(Map<Character, String> codes, String content) {
            StringBuilder encoded = new StringBuilder();
            for (char c : content.toCharArray()) {
                encoded.append(codes.get(c)); //строим закодированную строку
            }
            return encoded.toString();
        }

    public static void saveToFile(String encodedInf, String outputPath)
            throws IOException {
        BitSet bitSet = new BitSet(encodedInf.length()); //создаем битсет и храним каждый символ из энкодидсонтент
        int bitCounter = 0;
        for (char c : encodedInf.toCharArray()) { //по умолч. все биты уст. на фолс
            if (c == '1') {
                bitSet.set(bitCounter);//уст. соответствующий бит на тру
            }
            bitCounter++; //отслеживаем  позицию бита
        }
        try (FileOutputStream outputStream = new FileOutputStream(outputPath)) { //записываем в файл по пути оутпутПат
            outputStream.write(bitSet.toByteArray());
        }
    }



    private static Map<Character, String> generateHuffmanCodes(String inf) {
        // Подсчитываем частоты символов
        Map<Character, Integer> freqMap = new HashMap<>();
        for (char c : inf.toCharArray()) {
            freqMap.put(c, freqMap.getOrDefault(c, 0) + 1);
        }

        // cоздаем приоритетную очередь и добавляем туда листья для каждого символа
        PriorityQueue<HuffmanNode> queue = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> entry : freqMap.entrySet()) {
            queue.add(new HuffmanNode(entry.getKey(), entry.getValue()));
        }

        // cтроим дерево Хаффмана
        while (queue.size() > 1) {
            HuffmanNode left = queue.poll(); //извлечение двух узлов с наименьшей частотой
            HuffmanNode right = queue.poll();//наименьший приоретет
            //создание род. дерева
            HuffmanNode parent = new HuffmanNode('X', left.frequency + right.frequency);
            parent.left = left;
            parent.right = right;
            queue.add(parent); //добавляем родительский узел обратно в очередь
        }

        HuffmanNode node = queue.poll();// Генерируем коды для символов
        Map<Character, String> codes = new HashMap<>();
        generateCodes(node, "", codes);

        return codes;
    }

    private static void generateCodes(HuffmanNode node, String code, Map<Character, String> codes) {
        if (node != null) {
            if (node.left == null && node.right == null) {
                codes.put(node.character, code); //если файл листовый то добавляем его бинарный путь в кодс
            }
            generateCodes(node.left, code + "0", codes);
            generateCodes(node.right, code + "1", codes);
        }
    }


    private static String encodeInfWithHuffmanCodes(Map<Character, String> huffmanCodes, String content) {
        StringBuilder encoded = new StringBuilder();
        for (char c : content.toCharArray()) {
            encoded.append(huffmanCodes.get(c)); //также строим строку
        }
        return encoded.toString();
    }

    private static void print(String encodedContent) {
        final int max = 88; // Максимальная длина текста для вывода
        if (encodedContent.length() > max) {
            System.out.println(encodedContent.substring(0, max) + "...");
        } else {
            System.out.println(encodedContent);
        }
    }

}
class HuffmanNode implements Comparable<HuffmanNode> {
    char character;
    int frequency;
    HuffmanNode left, right;

    HuffmanNode(char character, int frequency) {
        this.character = character;
        this.frequency = frequency;
    }

    // Сравнение для приоритетной очереди основано на частоте появления символа.
    @Override
    public int compareTo(HuffmanNode other) {
        return Integer.compare(this.frequency, other.frequency);
    }
}
