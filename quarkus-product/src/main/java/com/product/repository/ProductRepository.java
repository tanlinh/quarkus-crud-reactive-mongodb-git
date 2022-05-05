package com.product.repository;

import com.product.entity.Product;
import io.quarkus.mongodb.panache.PanacheMongoRepositoryBase;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProductRepository implements PanacheMongoRepositoryBase<Product, String> {
}
