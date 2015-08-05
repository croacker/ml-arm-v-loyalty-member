package ru.peaksystems.varm.loyalty.data.dummy;

import ru.peaksystems.varm.loyalty.domain.DashboardNotification;

import java.util.Arrays;
import java.util.Collection;

public abstract class LoyaltyDataGenerator {

    static String randomFirstName() {
        String[] names = { "Алексей", "Валентин", "Андрей", "Денис", "Виталий",
                "Виктор", "Сергей", "Иван", "Илья", "Тимур", "Евгений", "Владимир",
                "Максим", "Антон", "Кирил", "Александр", "Игорь", "Павел", "Борис",
                "Валерий", "Василий", "Роман", "Степан", "Николай" };
        return names[(int) (Math.random() * names.length)];
    }

    static String randomLastName() {
        String[] names = { "Ковтун", "Палдин", "Рябов", "Филатов",
                "Веремейчик", "Артеменко", "Миронов", "Линьков", "Мурашов", "Ву",
                "Семичев", "Розов", "Забабурин", "Козлов", "Баранов", "Чернов",
                "Литовченко", "Цветков", "Лучин", "Федовров", "Васильев", "Горшенин",
                "Лушин", "Гук", "Куприн", "Новиков", "Шуклин", "Федоров",
                "Иванов", "Ивасишин", "Сидоров", "Бубнов", "Аксенов", "Мелкумов",
                "Окулов", "Тряпкин", "Виноградов", "Лебедев", "Осин", "Шагин",
                "Онопко", "Гусев", "Кривцов", "Мальцев", "Коноваленко", "Бабаев" };
        return names[(int) (Math.random() * names.length)];
    }

    public static String randomWord(int len, boolean capitalized) {
        String[] part = { "гир", "ма", "изо", "при", "ле", "ни", "ке", "мик",
                "ко", "прог", "ва", "ре", "ло", "гр", "есть", "жо", "эл", "тес",
                "ла", "ко", "но", "про", "так", "ну", "мы", "ноль", "да", "при",
                "соб", "рек", "мак", "вер", "ист", "для", "ва" };
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < len; i++) {
            String p = part[(int) (Math.random() * part.length)];
            if (i == 0 && capitalized) {
                p = Character.toUpperCase(p.charAt(0)) + p.substring(1);
            }
            sb.append(p);
        }
        return sb.toString();

    }

    public static String randomText(int words) {
        StringBuffer sb = new StringBuffer();
        int sentenceWordsLeft = 0;
        while (words-- > 0) {
            if (sb.length() > 0) {
                sb.append(' ');
            }
            if (sentenceWordsLeft == 0 && words > 0) {
                sentenceWordsLeft = (int) (Math.random() * 15);
                sb.append(randomWord(1 + (int) (Math.random() * 3), true));
            } else {
                sentenceWordsLeft--;
                sb.append(randomWord(1 + (int) (Math.random() * 3), false));
                if (words > 0 && sentenceWordsLeft > 2 && Math.random() < 0.2) {
                    sb.append(',');
                } else if (sentenceWordsLeft == 0 || words == 0) {
                    sb.append('.');
                }
            }
        }
        return sb.toString();
    }

    static String randomName() {
        int len = (int) (Math.random() * 4) + 1;
        return randomWord(len, true);
    }

    static String randomTitle(int words) {
        StringBuffer sb = new StringBuffer();
        int len = (int) (Math.random() * 4) + 1;
        sb.append(randomWord(len, true));
        while (--words > 0) {
            len = (int) (Math.random() * 4) + 1;
            sb.append(' ');
            sb.append(randomWord(len, false));
        }
        return sb.toString();
    }

    static String randomHTML(int words) {
        StringBuffer sb = new StringBuffer();
        while (words > 0) {
            sb.append("<h2>");
            int len = (int) (Math.random() * 4) + 1;
            sb.append(randomTitle(len));
            sb.append("</h2>");
            words -= len;
            int paragraphs = 1 + (int) (Math.random() * 3);
            while (paragraphs-- > 0 && words > 0) {
                sb.append("<p>");
                len = (int) (Math.random() * 40) + 3;
                sb.append(randomText(len));
                sb.append("</p>");
                words -= len;
            }
        }
        return sb.toString();
    }

    static Collection<DashboardNotification> randomNotifications() {
        DashboardNotification n1 = new DashboardNotification();
        n1.setId(1);
        n1.setFirstName(randomFirstName());
        n1.setLastName(randomLastName());
        n1.setAction("Новый отчет создан");
        n1.setPrettyTime("25 минут назад");
        n1.setContent(randomText(18));

        DashboardNotification n2 = new DashboardNotification();
        n2.setId(2);
        n2.setFirstName(randomFirstName());
        n2.setLastName(randomLastName());
        n2.setAction("Изменено задание");
        n2.setPrettyTime("2 дня назад");
        n2.setContent(randomText(10));

        return Arrays.asList(n1, n2);
    }

    public static int[] randomSparklineValues(int howMany, int min, int max) {
        int[] values = new int[howMany];

        for (int i = 0; i < howMany; i++) {
            values[i] = (int) (min + (Math.random() * (max - min)));
        }

        return values;
    }
}