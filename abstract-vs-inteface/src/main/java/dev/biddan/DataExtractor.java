package dev.biddan;

import java.util.List;

public interface DataExtractor<T> {
    List<T> extractData();
}
