package org.example.storage;

import org.example.model.GovService;
import org.example.model.Institution;
import org.example.model.User;

import java.util.ArrayList;
import java.util.List;

public class DataStore {
    private static DataStore instance;

    private final InstitutionStorage institutionStorage;
    private final ServiceStorage serviceStorage;
    private final UserStorage userStorage;

    private final List<Institution> institutions;
    private final List<GovService> services;
    private final List<User> users;

    private DataStore() {
        institutionStorage = new InstitutionStorage("data/institutions.csv");
        serviceStorage = new ServiceStorage("data/services.csv");
        userStorage = new UserStorage("data/users.csv");

        institutions = institutionStorage.loadAll();
        services = serviceStorage.loadAll();
        users = userStorage.loadAll();

        seedIfEmpty();
    }

    public static DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    private void seedIfEmpty() {
        if (users.isEmpty()) {
            users.add(new User(1, "admin", "admin123", "ADMIN"));
            userStorage.saveAll(users);
        }

        if (institutions.isEmpty()) {
            institutions.add(new Institution(1, "ЦНАП м. Ужгород", "ЦНАП",
                    "пл. Поштова, 3, Ужгород", "+380312123456",
                    "cnap@uzh.gov.ua", "https://rada-uzhgorod.gov.ua",
                    "09:00-18:00", "Надання адміністративних послуг мешканцям міста."));

            institutions.add(new Institution(2, "Ужгородська міська рада", "Міська рада",
                    "пл. Поштова, 3, Ужгород", "+380312111111",
                    "office@rada-uzhgorod.gov.ua", "https://rada-uzhgorod.gov.ua",
                    "08:30-17:30", "Орган місцевого самоврядування міста Ужгород."));

            institutions.add(new Institution(3, "Пенсійний фонд", "Пенсійний фонд",
                    "вул. Загорська, 30, Ужгород", "+380312654321",
                    "info@pfu.gov.ua", "https://www.pfu.gov.ua",
                    "08:00-17:00", "Обслуговування громадян з питань пенсійного забезпечення."));

            institutionStorage.saveAll(institutions);
        }

        if (services.isEmpty()) {
            services.add(new GovService(1, 1, "Реєстрація місця проживання",
                    "Послуга з реєстрації місця проживання громадянина.",
                    "Паспорт, ІПН, заява",
                    "1-3 дні", "Безкоштовно",
                    "Подання здійснюється особисто."));

            services.add(new GovService(2, 1, "Видача довідки про склад сім’ї",
                    "Отримання довідки про склад сім’ї.",
                    "Паспорт, заява",
                    "1 день", "Безкоштовно",
                    "Можлива попередня електронна черга."));

            services.add(new GovService(3, 3, "Призначення пенсії за віком",
                    "Оформлення пенсійних виплат за віком.",
                    "Паспорт, трудова книжка, ІПН",
                    "до 10 днів", "Безкоштовно",
                    "Потрібна повна перевірка документів."));

            services.add(new GovService(4, 2, "Подання електронного звернення",
                    "Подання звернення до міської ради.",
                    "ПІБ, текст звернення, контакти",
                    "до 30 днів", "Безкоштовно",
                    "Можна подати онлайн."));

            serviceStorage.saveAll(services);
        }
    }

    public List<Institution> getInstitutions() {
        return institutions;
    }

    public List<GovService> getServices() {
        return services;
    }

    public List<User> getUsers() {
        return users;
    }

    public void saveInstitutions() {
        institutionStorage.saveAll(institutions);
    }

    public void saveServices() {
        serviceStorage.saveAll(services);
    }

    public void saveUsers() {
        userStorage.saveAll(users);
    }

    public int nextInstitutionId() {
        int max = 0;
        for (Institution i : institutions) {
            if (i.getId() > max) max = i.getId();
        }
        return max + 1;
    }

    public int nextServiceId() {
        int max = 0;
        for (GovService s : services) {
            if (s.getId() > max) max = s.getId();
        }
        return max + 1;
    }

    public Institution findInstitutionById(int id) {
        for (Institution i : institutions) {
            if (i.getId() == id) return i;
        }
        return null;
    }

    public User authenticate(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    public List<GovService> getServicesByInstitutionId(int institutionId) {
        List<GovService> result = new ArrayList<>();
        for (GovService service : services) {
            if (service.getInstitutionId() == institutionId) {
                result.add(service);
            }
        }
        return result;
    }

    public void deleteInstitution(int institutionId) {
        institutions.removeIf(i -> i.getId() == institutionId);
        services.removeIf(s -> s.getInstitutionId() == institutionId);
        saveInstitutions();
        saveServices();
    }

    public void deleteService(int serviceId) {
        services.removeIf(s -> s.getId() == serviceId);
        saveServices();
    }
}