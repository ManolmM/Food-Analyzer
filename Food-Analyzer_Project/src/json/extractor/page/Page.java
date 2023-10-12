package json.extractor.page;

import json.extractor.food.name.FoodByName;

import java.util.List;

public record Page(int totalHits, int currentPage, int totalPages, List<FoodByName> foods) {
}
