package com.app.divyansh.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.app.divyansh.model.ResponsePayload;

@Service
public class NumberService {
    private final int WINDOW_SIZE = 10;
    private final LinkedHashSet<Integer> window = new LinkedHashSet<>();

    private final Map<String, String> endpointMap = Map.of(
        "p", "http://20.244.56.144/evaluation-service/primes",
        "f", "http://20.244.56.144/evaluation-service/fibo",
        "e", "http://20.244.56.144/evaluation-service/even",
        "r", "http://20.244.56.144/evaluation-service/rand"
    );

    private final RestTemplate restTemplate = new RestTemplate();

    public ResponsePayload getNumbers(String type) {
        List<Integer> prevState = new ArrayList<>(window);

        if (!endpointMap.containsKey(type)) {
            throw new IllegalArgumentException("Invalid type: " + type);
        }

        List<Integer> fetched = fetchNumbers(endpointMap.get(type));
        List<Integer> uniqueFetched = new ArrayList<>();
        for (Integer n : fetched) {
            if (!window.contains(n)) uniqueFetched.add(n);
        }

        for (Integer n : uniqueFetched) {
            if (window.size() >= WINDOW_SIZE) {
                Iterator<Integer> it = window.iterator();
                if (it.hasNext()) window.remove(it.next());
            }
            window.add(n);
        }

        double avg = window.stream().mapToDouble(Integer::doubleValue).average().orElse(0.0);

        return new ResponsePayload(prevState, new ArrayList<>(window), uniqueFetched, Math.round(avg * 100.0) / 100.0);
    }

    private List<Integer> fetchNumbers(String url) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            Callable<List<Integer>> task = () -> {
                Map<?, ?> response = restTemplate.getForObject(url, Map.class);
                return (List<Integer>) response.get("numbers");
            };
            Future<List<Integer>> future = executor.submit(task);
            return future.get(500, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            return Collections.emptyList();
        } finally {
            executor.shutdown();
        }
    }
}