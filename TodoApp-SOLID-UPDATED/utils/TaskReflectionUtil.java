package todoapp.utils;

import todoapp.annotations.TaskProperty;
import todoapp.models.Task;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class TaskReflectionUtil {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH'h'mm");

    // Mapa para traduzir os nomes dos campos
    private static final Map<String, String> FIELD_TRANSLATIONS = new HashMap<>();

    static {
        FIELD_TRANSLATIONS.put("serialVersionUID", "ID de Versão Serial");
        FIELD_TRANSLATIONS.put("id", "Identificador");
        FIELD_TRANSLATIONS.put("title", "Título");
        FIELD_TRANSLATIONS.put("description", "Descrição");
        FIELD_TRANSLATIONS.put("creationDate", "Data de Criação");
        FIELD_TRANSLATIONS.put("completed", "Concluída");
        FIELD_TRANSLATIONS.put("priority", "Prioridade");
    }


    public static void displayTaskProperties(Task task) {
        System.out.println("Propriedades da Tarefa:");
        Field[] fields = task.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);

            String fieldName = field.getName();


            if (fieldName.equals("id") || fieldName.equals("completed") || fieldName.equals("serialVersionUID")) {
                continue;
            }

            TaskProperty annotation = field.getAnnotation(TaskProperty.class);
            try {

                String desc = (annotation != null && !annotation.description().isEmpty())
                        ? annotation.description()
                        : "sem descrição"; // Caso a anotação esteja vazia ou não exista


                String translatedFieldName = FIELD_TRANSLATIONS.getOrDefault(fieldName, fieldName);

                Object val = field.get(task);

                // Formata LocalDateTime
                if (val instanceof LocalDateTime) {
                    val = ((LocalDateTime) val).format(FORMATTER);
                }

                // Formata o valor boolean para "Sim" ou "Não"
                if (val instanceof Boolean) {
                    val = (Boolean) val ? "Sim" : "Não";
                }

                System.out.printf("%s = %s (%s)%n", translatedFieldName, val, desc);
            } catch (IllegalAccessException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }

    public static boolean validateRequiredFields(Task task) {
        for (Field field : task.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            TaskProperty annotation = field.getAnnotation(TaskProperty.class);
            if (annotation != null && annotation.required()) {
                try {
                    Object val = field.get(task);
                    if (val == null) return false;
                } catch (IllegalAccessException e) {
                    return false;
                }
            }
        }
        return true;
    }
}