package json;

import java.util.List;

public record PageExtractor(int currentPage, int totalPages, List<FoodExtractor> foods) {
}
