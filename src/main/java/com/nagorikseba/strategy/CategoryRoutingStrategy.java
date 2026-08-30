package com.nagorikseba.strategy;

import com.nagorikseba.entity.Complaint;
import com.nagorikseba.entity.Department;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryRoutingStrategy implements ComplaintRoutingStrategy {
    @Override
    public Department route(Complaint complaint, List<Department> availableDepartments) {
        throw new UnsupportedOperationException("Category routing is not implemented yet");
    }
}
