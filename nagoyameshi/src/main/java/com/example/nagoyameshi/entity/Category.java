package com.example.nagoyameshi.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "categories")
@Data
@ToString(exclude = "categoriesRestaurants")
public class Category {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id")
   private Integer id;

   @Column(name = "name")
   private String name;

   /* カスケード削除の有効
    * 親テーブルのレコードを削除したときに、子テーブルの関連するレコードも同時に削除する機能の事
    */
   @OneToMany(mappedBy = "category", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
   private List<CategoryRestaurant> categoriesRestaurants;
}