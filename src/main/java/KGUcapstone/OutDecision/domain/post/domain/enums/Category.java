package KGUcapstone.OutDecision.domain.post.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Category {
    FOOD("food"),
    LOVE("love"),
    FASHION("fashion"),
    HOBBY("hobby"),
    WORK("work"),
    TRAVEL("travel"),
    OTHER("other");

    private final String value;

    public static Category fromValue(String value) {
        for (Category category : Category.values()) {
            if (category.value.equalsIgnoreCase(value)) {
                return category;
            }
        }
        throw new IllegalArgumentException("No constant with value " + value + " found");
    }
}
